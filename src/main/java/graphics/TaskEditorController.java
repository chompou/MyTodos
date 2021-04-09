package graphics;

import enums.TaskPriority;
import enums.TaskStatus;
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

    @FXML
    void onAddTask(ActionEvent event) {
        String description = descriptionTextField.getText();
        String category = categoryTextField.getText();
        LocalDate deadline = deadlineDatePicker.getValue();
        TaskPriority priority = priorityChoiceBox.getValue();
        taskRegistry.registerTask(new Task(description, category, deadline, priority));
        this.controller.updateTable();
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }
    @FXML
    void onSaveTask(ActionEvent event) {

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

    }

    @FXML
    void onDeleteTask(ActionEvent event) {

    }
}
