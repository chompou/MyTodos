import java.io.*;
import java.util.*;

import enums.TaskStatus;
import enums.TaskPriority;

/**
 * This class represents the list of tasks created by the user.
 * Its only field is the arraylist of tasks.
 */
public class TaskRegistry {
    final String RELATIVE_PATH = "tasks.todo";
    private ArrayList<Task> tasks;

    /**
     * This method creates a new tasks registry
     */
    public TaskRegistry() {
        this.tasks = new ArrayList<Task>();
    }

    /**
     * This method gets the arraylist of tasks.
     * @return the arraylist of tasks
     */
    public ArrayList<Task> getTasks() {
        return tasks;
    }

    /**
     * This method sets the arrayList of tasks.
     * @param tasks the new arraylist of tasks.
     */
    public void setTasks(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    /**
     * This method registers a task in the arraylist if it is not already registered.
     * @param task the tasks we are trying to register.
     * @return true if the task was not in the list and was successfully registered. False if the task was already in the list.
     */
    public boolean registerTask(Task task){
        if(tasks.contains(task)){
            return false;
        } else {
            this.tasks.add(task);
            return true;
        }
    }

    /**
     * This method tries to delete a task in the registry
     * @param task the task we are trying to delete.
     * @return true if the tasks was found and deleted, false if not
     */
    public boolean deleteTask(Task task){
        if(tasks.remove(task)){
            return true;
        } else {
            return false;
        }
    }

    /**
     * This method takes in a comparator and uses it to sort the task list.
     * @param comparator the comparator we are using to sort the task list with.
     * @return the sorted list
     */
    public ArrayList<Task> sortTasks(Comparator<Task> comparator){
        //not finished
        return tasks;
    }

    /**
     * This method filters the task arraylist according to three provided arguments
     * @param status a TaskStatus to filter statuses
     * @param search a search string to filter descriptions. the string will be matched as a case insensitive substring of any matching descriptions
     * @param category a string to filter categories. the string may be case insensitive, but must otherwise match.
     * @return the filtered tasklist
     */
    public ArrayList<Task> filterTasks(TaskStatus status, String search, String category){
        ArrayList<Task> filteredTasks = new ArrayList<>();
        for (Task task: tasks)
            if (status == null || task.getStatus() == status)
                if (search == null || task.getDescription().toLowerCase().contains(search.toLowerCase()))
                    if (category == null || task.getCategory().equalsIgnoreCase(category))
                        filteredTasks.add(task);

        return filteredTasks;
    }

    /**
     * This method deletes all the finished tasks.
     * @return true if they where deleted, false if not.
     */
    public boolean deleteFinishedTasks(){
        Iterator<Task> i = tasks.iterator();
        while(i.hasNext()){
            Task task = i.next();
            if (task.getStatus() == TaskStatus.COMPLETE){
                i.remove();
            }
        }
        return true;
    }

    /**
     * This method writes the current ArrayList of Task objects to a file specified by the RELATIVE_PATH variable.
     * @return true if ArrayList was successfully written to file, false if an IOException occurred.
     * @throws IOException
     */

    public boolean writeTasksToFile() throws IOException {
        File file = new File(this.RELATIVE_PATH);
        try (FileOutputStream fs = new FileOutputStream(file);
             ObjectOutputStream os = new ObjectOutputStream(fs)) {
            os.writeObject(this.tasks);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * This method loads an ArrayList object from a file specified by the RELATIVE_PATH variable.
     * @return Object that corresponds to the deserialized ArrayList, null if IOException occurred.
     * @throws IOException
     */

    public Object loadTasksFromFile() throws IOException {
        Object object = null;
        File file = new File(this.RELATIVE_PATH);
        try (FileInputStream fs = new FileInputStream(file);
             ObjectInputStream is = new ObjectInputStream(fs)) {
            object = is.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return object;
    }

    /**
     * This method check if the taskregistry is equal to another object.
     * @param o the object we are checking against
     * @return true if the objects are equal. False if they are not.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskRegistry that = (TaskRegistry) o;
        return Objects.equals(tasks, that.tasks);
    }

    /**
     * This method generates hashCode for the taskRegistry.
     * @return the hashcode for the object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(tasks);
    }

    /**
     * This method returns the tostring of the taskRegistry, which shows all the values for all the fields.
     * @return the tostring of the taskRegistry.
     */
    @Override
    public String toString() {
        return "TaskRegistry{" +
                "tasks=" + tasks +
                '}';
    }
}
