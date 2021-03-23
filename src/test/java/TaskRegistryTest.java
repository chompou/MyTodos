import enums.TaskPriority;
import enums.TaskStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TaskRegistryTest {

    private TaskRegistry taskRegistry;

    @Before
    public void setUp() {
        this.taskRegistry = new TaskRegistry();
        Task task = new Task("Task #1", "Tasks", null, TaskPriority.LOW);
        taskRegistry.registerTask(task);
        task = new Task("Task #2", "Tasks", null, TaskPriority.LOW);
        taskRegistry.registerTask(task);
        task = new Task("Task #3", "Other", null, TaskPriority.LOW);
        taskRegistry.registerTask(task);
    }

    @After
    public void TearDown() {
        File file = new File(taskRegistry.RELATIVE_PATH);
        file.delete();
    }


    @Test
    public void writeAndReadTasksTest() {
        try {
            assertTrue(taskRegistry.writeTasksToFile());
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            assertEquals(this.taskRegistry.getTasks(), taskRegistry.loadTasksFromFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
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
