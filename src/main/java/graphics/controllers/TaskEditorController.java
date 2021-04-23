package graphics.controllers;

import graphics.Settings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import mytodos.Task;
import mytodos.TaskRegistry;

public class TaskEditorController extends Controller{
    boolean newTask;
    Task task;

    @FXML
    private TextField descriptionTextField;
    @FXML
    private ComboBox<String> categoryComboBox;
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


    TaskEditorController(TaskRegistry taskRegistry, Settings settings) {
        super(taskRegistry, settings);
        this.task = new Task();
        this.newTask = true;

    }

    TaskEditorController(TaskRegistry taskRegistry, Settings settings, Task task) {
        super(taskRegistry, settings);
        this.task = task;
        this.newTask = false;

    }

    void closeStage(ActionEvent event) {
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }

    @FXML
    void initialize() {
        categoryComboBox.setItems(taskRegistry.getCategories());
        priorityChoiceBox.setItems(Task.priorities);

        descriptionTextField.setText(task.getDescription());
        categoryComboBox.setValue(task.getCategory());
        deadlineDatePicker.setValue(task.getDeadline());
        priorityChoiceBox.setValue(Task.priorities.get(task.getPriority()));
        statusChoiceBox.setValue(Task.statuses.get(task.getStatus()));
        startDatePicker.setValue(task.getStartDate());
        endDatePicker.setValue(task.getEndDate());

    }

    @FXML
    void onAddSaveTask(ActionEvent event) {
        boolean missingFields = false;
        if (descriptionTextField.getText() == null) {
            descriptionTextField.setPromptText("Required Field");
            descriptionTextField.setStyle("-fx-border-color: red; -fx-border-width: 2;");
            missingFields = true;
        } if (categoryComboBox.getValue() == null) {
            categoryComboBox.setPromptText("Required Field");
            categoryComboBox.setStyle("-fx-border-color: red; -fx-border-width: 2;");
            missingFields = true;
        }

        if (missingFields)
            return;

        task.setDescription(descriptionTextField.getText());
        task.setCategory(categoryComboBox.getValue());
        task.setDeadline(deadlineDatePicker.getValue());
        task.setPriority(priorityChoiceBox.getValue());
        task.setStatus(statusChoiceBox.getValue());
        task.setStartDate(startDatePicker.getValue());
        task.setEndDate(endDatePicker.getValue());

        if (newTask)
            taskRegistry.registerTask(task);

        closeStage(event);
    }

    @FXML
    void onCancelChange(ActionEvent event) {
        closeStage(event);
    }

    @FXML
    void onDeleteTask(ActionEvent event) {
        taskRegistry.removeTask(task);
        closeStage(event);
    }





}
