package graphics;

import enums.TaskPriority;
import enums.TaskStatus;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import mytodos.Task;
import mytodos.TaskRegistry;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;

public class TaskEditorController {
    private Task task;

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
    private Button delete;
    @FXML
    private Button cancelChange;

    TaskEditorController(TaskRegistry taskRegistry) {
        this.task = null;
        descriptionTextField.setText(null);
        categoryTextField.setText(null);
        deadlineDatePicker.setValue(null);
        priorityChoiceBox.setValue(null);
        statusChoiceBox.setValue(null);
        startDatePicker.setValue(LocalDate.now());
        endDatePicker.setValue(null);

    }


    TaskEditorController(TaskRegistry taskRegistry, Task task) {
        descriptionTextField.setText(task.getDescription());
        categoryTextField.setText(task.getCategory());
        deadlineDatePicker.setValue(task.getDeadline());
        priorityChoiceBox.setValue(task.getPriority());
        statusChoiceBox.setValue(task.getStatus());
        startDatePicker.setValue(task.getStartDate());
        endDatePicker.setValue(task.getEndDate());

    }

    @FXML
    void onAddTask(ActionEvent event) {

    }
    @FXML
    void onSaveTask(ActionEvent event) {

    }
    @FXML
    void onAddSaveTask(ActionEvent event) {


    }

    @FXML
    void onCancelChange(ActionEvent event) {

    }

    @FXML
    void onDeleteTask(ActionEvent event) {

    }
}
