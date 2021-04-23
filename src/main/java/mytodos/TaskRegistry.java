package mytodos;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * This class represents the list of tasks created by the user.
 * It adapts functionality and implements listeners for the JavaFX ObservableList object.
 */

public class TaskRegistry {
    final static String RELATIVE_PATH = "tasks.todo";
    private ObservableList<Task> tasks;

    /**
     * Creates a new task registry with an empty ObservableList with the appropriate listeners
     */
    public TaskRegistry() {
        this.tasks = FXCollections.observableArrayList(task -> {
            return new Observable[] {
                    task.descriptionProperty(),
                    task.categoryProperty(),
                    task.deadlineProperty(),
                    task.priorityProperty(),
                    task.statusProperty(),
                    task.startDateProperty(),
                    task.endDateProperty()
            };
        });
        loadTasks();
        this.tasks.addListener((ListChangeListener<Task>) change -> saveTasks());
    }

    /**
     * @return The ObservableList of all tasks in the registry
     */
    public ObservableList<Task> getTasks() {
        return this.tasks;
    }

    /**
     * Retrieves a single task from registry by index
     * @param index The index of the element
     * @return The indexed task
     */
    public Task getTask(int index) {
        return this.tasks.get(index);
    }

    /**
     * Maps all current tasks in the registry into a set of unique category strings. Intended for ChoiceBox and ComboBox displays
     * @return An ObservableList with the mapped category strings.
     */
    public ObservableList<String> getCategories() {
        return FXCollections.observableArrayList(this.tasks.stream().map(Task::getCategory).collect(Collectors.toSet()));
    }

    /**
     * Registers a task in the current registry if it is not already registered
     * @param task The Task to register
     * @return true if the task was successfully registered, false if it was already registered
     */
    public boolean registerTask(Task task) {
        if (this.tasks.contains(task)) {
            return false;
        } else {
            this.tasks.add(task);
            return true;
        }
    }

    /**
     * Removes a single task from the registry by first matching object
     * @param task The Task object to be removed
     * @return true if task was successfully removed, false if it was not
     */
    public boolean removeTask(Task task) {
        return this.tasks.remove(task);
    }

    /**
     * Serializes and stores the current registry list to a file
     * @return true if list was successfully saved, false if it was not
     */
    public boolean saveTasks() {
        try  {
            FileOutputStream fileOutputStream = new FileOutputStream(TaskRegistry.RELATIVE_PATH);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(new ArrayList<>(this.getTasks()));
            objectOutputStream.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * Loads and deserializes arraylist from a file, then empties and refills the instantiated ObservableList
     * @return true if list was successfully loaded, false if it was not
     */
    public boolean loadTasks() {
        try {
            FileInputStream fileInputStream = new FileInputStream(TaskRegistry.RELATIVE_PATH);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            ArrayList<Task> loadedTasks = (ArrayList<Task>) objectInputStream.readObject();
            this.tasks.clear();
            this.tasks.addAll(loadedTasks);
        } catch (FileNotFoundException ex) {
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }


    /**
     * Creates a predicate for filtering a collection of tasks according to three provided arguments
     * @param status a TaskStatus to filter statuses
     * @param search a search string to filter descriptions. the string will be matched as a case insensitive substring of any matching descriptions
     * @param category a string to filter categories. the string may be case insensitive, but must otherwise match.
     * @return a predicate for filtering Task objects
     */
    public static Predicate<Task> filterPredicate(Integer status, String search, String category) {
        return task -> {
            if (status == null || task.getStatus() == status)
                if (search == null || task.getDescription().toLowerCase().contains(search.toLowerCase()))
                    if (category == null || task.getCategory().equalsIgnoreCase(category))
                        return true;
            return false;
        };
    }
}
