import enums.TaskPriority;
import enums.TaskStatus;
import mytodos.Task;
import mytodos.TaskRegistry;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;

import static org.junit.Assert.*;

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

        assertEquals(taskRegistry.getTasks(), taskRegistry.loadTasksFromFile());
    }

    @Test
    public void filterTasksTest() {
        ArrayList<Task> filteredTasks = taskRegistry.filterTasks(TaskStatus.COMPLETE, null, null);
        assertEquals(new ArrayList<Task>(), filteredTasks);

        filteredTasks = taskRegistry.filterTasks(null, "3", null);
        assertEquals(taskRegistry.getTasks().subList(2, 3), filteredTasks);

        filteredTasks = taskRegistry.filterTasks(null, null, "Tasks");
        assertEquals(taskRegistry.getTasks().subList(0, 2), filteredTasks);

        filteredTasks = taskRegistry.filterTasks(TaskStatus.TODO, "2", null);
        assertEquals(taskRegistry.getTasks().subList(1, 2), filteredTasks);

        filteredTasks = taskRegistry.filterTasks(TaskStatus.TODO, null, "Other");
        assertEquals(taskRegistry.getTasks().subList(2, 3), filteredTasks);

        filteredTasks = taskRegistry.filterTasks(null, "1", "other");
        assertEquals(new ArrayList<Task>(), filteredTasks);

        filteredTasks = taskRegistry.filterTasks(TaskStatus.TODO, "1", "tasks");
        assertEquals(taskRegistry.getTasks().subList(0, 1), filteredTasks);
    }
}
