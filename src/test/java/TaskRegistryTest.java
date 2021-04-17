import enums.TaskPriority;
import enums.TaskStatus;
import mytodos.Task;
import mytodos.TaskRegistry;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.*;

import static org.junit.Assert.*;

import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TaskRegistryTest {

    private TaskRegistry taskRegistry;

    @Before
    public void setUp() {
        this.taskRegistry = new TaskRegistry();
        Task task = new Task("mytodos.Task #1", "Tasks", null, TaskPriority.LOW);
        taskRegistry.registerTask(task);
        task = new Task("mytodos.Task #2", "Tasks", null, TaskPriority.LOW);
        taskRegistry.registerTask(task);
        task = new Task("mytodos.Task #3", "Other", null, TaskPriority.LOW);
        taskRegistry.registerTask(task);
    }

    @After
    public void TearDown() {
        File file = new File("tasks.todo");
        file.delete();
    }


    @Test
    public void writeAndReadTasksTest() {
        assertTrue(taskRegistry.writeTasksToFile());

        ArrayList<Task> oldTasks = new ArrayList<Task>();
        oldTasks.addAll(taskRegistry.getTasks());
        taskRegistry.loadTasksFromFile();

        assertEquals(oldTasks, taskRegistry.getTasks());
    }

    @Test
    public void filterTasksTest() {
        Predicate<Task> predicate = TaskRegistry.filterPredicate(null, null, null);
        assertEquals(taskRegistry.getTasks(), filter(taskRegistry.getTasks(), predicate));

        predicate = TaskRegistry.filterPredicate(null, "3", null);
        assertEquals(taskRegistry.getTasks().subList(2, 3), filter(taskRegistry.getTasks(), predicate));

        predicate = TaskRegistry.filterPredicate(null, null, "Tasks");
        assertEquals(taskRegistry.getTasks().subList(0, 2), filter(taskRegistry.getTasks(), predicate));

        predicate = TaskRegistry.filterPredicate(TaskStatus.TODO, "2", null);
        assertEquals(taskRegistry.getTasks().subList(1, 2), filter(taskRegistry.getTasks(), predicate));

        predicate = TaskRegistry.filterPredicate(TaskStatus.TODO, null, "Other");
        assertEquals(taskRegistry.getTasks().subList(2, 3), filter(taskRegistry.getTasks(), predicate));

        predicate = TaskRegistry.filterPredicate(null, "1", "other");
        assertEquals(new ArrayList<Task>(), filter(taskRegistry.getTasks(), predicate));

        predicate = TaskRegistry.filterPredicate(TaskStatus.TODO, "1", "tasks");
        assertEquals(taskRegistry.getTasks().subList(0, 1), filter(taskRegistry.getTasks(), predicate));
    }

    static List<Task> filter(List<Task> tasks, Predicate<Task> predicate) {
        return tasks.stream().filter(predicate).collect(Collectors.toList());
    }

    @Test
    public void deleteTaskTest() {
        Task task1 = new Task("mytodos.Task #1", "Tasks", null, TaskPriority.LOW);
        taskRegistry.registerTask(task1);
        assertTrue(taskRegistry.deleteTask(task1));
    }
}
