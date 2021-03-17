import java.util.ArrayList;
import java.util.Comparator;

public class TaskRegistry {
    private ArrayList<Task> taskList;

    /**
     * This method creates a new tasks registry
     */
    public TaskRegistry() {
        this.taskList = new ArrayList<Task>();
    }

    /**
     * This method registers a task in the arraylist if it is not already registered.
     * @param task the tasks we are trying to register.
     * @return true if the task was not in the list and was successfully registered. False if the task was already in the list.
     */
    public boolean registerTask(Task task){
        if(taskList.contains(task)){
            return true;
        } else {
            return false;
        }
    }

    /**
     * This method tries to delete a task in the registry
     * @param task the task we are trying to delete.
     * @return true if the tasks was found and deleted, false if not
     */
    public boolean deleteTask(Task task){
        if(taskList.remove(task)){
            return true;
        } else {
            return false;
        }
    }

    /**
     * This method takes in a comparator and uses it to sort the tasklist.
     * @param comparator the comparator we are using to sort the taskslist with.
     * @return the sorted list
     */
    public ArrayList<Task> sortTasks(Comparator<Task> comparator){
        //not finished
        return taskList;
    }

    /**
     * This method filters the taskslist
     * @return the filtered tasklist
     */
    public ArrayList<Task> filterTasks(){
        //not finished
        return taskList;
    }

    /**
     * This method deletes all the finished tasks.
     * @return true if they where deleted, false if not.
     */
    public boolean deleteFinishedTasks(){

        return true;
    }


}
