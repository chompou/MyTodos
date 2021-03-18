import enums.TaskPriority;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

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
        task = new Task("Task #3", "Tasks", null, TaskPriority.LOW);
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
}
