package graphics;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import mytodos.Task;
import mytodos.TaskRegistry;

public class TaskEditorController {
    boolean newTask;
    Task task;
    TaskRegistry taskRegistry;

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


    TaskEditorController(TaskRegistry taskRegistry) {
        this.task = new Task();
        this.taskRegistry = taskRegistry;
        this.newTask = true;

    }

    TaskEditorController(TaskRegistry taskRegistry, Task task) {
        this.task = task;
        this.taskRegistry = taskRegistry;
        this.newTask = false;

    }

    void closeStage(ActionEvent event) {
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }

    @FXML
    void initialize() {
        priorityChoiceBox.setItems(FXCollections.observableArrayList(Task.priorities));

        descriptionTextField.setText(task.getDescription());
        categoryTextField.setText(task.getCategory());
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
        } if (categoryTextField.getText() == null) {
            categoryTextField.setPromptText("Required Field");
            categoryTextField.setStyle("-fx-border-color: red; -fx-border-width: 2;");
            missingFields = true;
        }

        if (missingFields)
            return;

        task.setDescription(descriptionTextField.getText());
        task.setCategory(categoryTextField.getText());
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
