package graphics;

import enums.TaskPriority;
import enums.TaskStatus;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import mytodos.Task;
import mytodos.TaskRegistry;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;

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
    private ChoiceBox<TaskPriority> priorityChoiceBox;
    @FXML
    private ChoiceBox<TaskStatus> statusChoiceBox;
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
        ObservableList<TaskPriority> taskPriorities = FXCollections.observableArrayList(TaskPriority.values());
        priorityChoiceBox.setItems(taskPriorities);

        if (task != null) {
            descriptionTextField.setText(task.getDescription());
            categoryTextField.setText(task.getCategory());
            deadlineDatePicker.setValue(task.getDeadline());
            priorityChoiceBox.setValue(task.getPriority());
            statusChoiceBox.setValue(task.getStatus());
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

    Task createTaskFromFields() {
        String description = descriptionTextField.getText();
        String category = categoryTextField.getText();
        LocalDate deadline = deadlineDatePicker.getValue();
        TaskPriority priority = priorityChoiceBox.getValue();
        return new Task(description, category, deadline, priority);
    }

    void onAddTask(ActionEvent event) {
        taskRegistry.registerTask(createTaskFromFields());
        this.controller.updateTable();
        taskRegistry.writeTasksToFile();
        closeStage(event);
    }

    void onSaveTask(ActionEvent event) {
        int index = taskRegistry.getTasks().indexOf(this.task);
        taskRegistry.getTasks().remove(this.task);
        taskRegistry.getTasks().add(index, createTaskFromFields());
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

    }
}
