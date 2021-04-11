package graphics;

import enums.TaskPriority;
import enums.TaskStatus;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import mytodos.Task;
import mytodos.TaskRegistry;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TaskEditorController {
    final private Task task;
    final private TaskRegistry taskRegistry;
    final private ApplicationController controller;

    @FXML
    private TextField descriptionTextField;
    @FXML
    private TextField categoryTextField;
    @FXML
    private DatePicker deadlineDatePicker;
    @FXML
    private ChoiceBox<String> priorityChoiceBox;
    @FXML
    private ChoiceBox<String> statusChoiceBox;
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private Button addSaveTask;
    @FXML
    private Button deleteTask;
    @FXML
    private Button cancelChange;

    TaskEditorController(ApplicationController controller, TaskRegistry taskRegistry) {
        this.controller = controller;
        this.taskRegistry = taskRegistry;
        this.task = null;

    }

    TaskEditorController(ApplicationController controller, TaskRegistry taskRegistry, Task task) {
        this.controller = controller;
        this.taskRegistry = taskRegistry;
        this.task = task;
    }

    @FXML
    void initialize() {
        List<String> mappedPriorities = Arrays.stream(TaskPriority.values()).map(TaskPriority::getValue).collect(Collectors.toList());
        ObservableList<String> taskPriorities = FXCollections.observableArrayList(mappedPriorities);
        priorityChoiceBox.setItems(taskPriorities);

        if (task != null) {
            descriptionTextField.setText(task.getDescription());
            categoryTextField.setText(task.getCategory());
            deadlineDatePicker.setValue(task.getDeadline());
            priorityChoiceBox.setValue(task.getPriority().getValue());
            System.out.println(task.getStatus());
            statusChoiceBox.setValue(task.getStatus().getValue());
            startDatePicker.setValue(task.getStartDate());
            endDatePicker.setValue(task.getEndDate());
            addSaveTask.setText("Save");

        } else {
            descriptionTextField.setText(null);
            categoryTextField.setText(null);
            deadlineDatePicker.setValue(null);
            priorityChoiceBox.setValue(null);
            statusChoiceBox.setValue(null);
            startDatePicker.setValue(LocalDate.now());
            endDatePicker.setValue(null);
            deleteTask.setVisible(false);
        }
    }

    void closeStage(ActionEvent event) {
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }

    Task createTaskFromFields(boolean readSecondaryInputs) {
        String description = descriptionTextField.getText();
        String category = categoryTextField.getText();
        LocalDate deadline = deadlineDatePicker.getValue();
        TaskPriority priority = TaskPriority.byValue(priorityChoiceBox.getValue());
        Task newTask = new Task(description, category, deadline, priority);
        if (readSecondaryInputs) {
            newTask.setStatus(TaskStatus.byValue(statusChoiceBox.getValue()));
            newTask.setStartDate(startDatePicker.getValue());
            newTask.setEndDate(endDatePicker.getValue());
        }
        return newTask;
    }

    void onAddTask(ActionEvent event) {
        taskRegistry.registerTask(createTaskFromFields(false));
        this.controller.updateTable();
        taskRegistry.writeTasksToFile();
        closeStage(event);
    }

    void onSaveTask(ActionEvent event) {
        taskRegistry.updateTask(task, createTaskFromFields(true));
        this.controller.updateTable();
        taskRegistry.writeTasksToFile();
        closeStage(event);
    }

    @FXML
    void onAddSaveTask(ActionEvent event) {
        if (task != null){
            onSaveTask(event);
        } else {
            onAddTask(event);
        }

    }

    @FXML
    void onCancelChange(ActionEvent event) {
        closeStage(event);
    }

    @FXML
    void onDeleteTask(ActionEvent event) {
        taskRegistry.deleteTask(this.task);
        this.controller.updateTable();
        taskRegistry.writeTasksToFile();
        closeStage(event);
    }
}
