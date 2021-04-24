package graphics.controllers;

import graphics.Settings;
import graphics.controllers.Controller;
import javafx.event.ActionEvent;
import javafx.event.EventTarget;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import mytodos.Task;
import mytodos.TaskRegistry;

import java.util.HashMap;

public class ConfirmDeleteController extends Controller {
    HashMap<Task, Boolean> isSelected;
    DeleteController controller;

    @FXML
    private Button cancelButton;

    @FXML
    private Button deleteButton;

    ConfirmDeleteController(TaskRegistry taskRegistry, Settings settings, HashMap<Task, Boolean> isSelected, DeleteController controller) {
        super(taskRegistry, settings);
        this.isSelected = isSelected;
        this.controller = controller;
    }

    void closeStage(ActionEvent event) {
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }

    @FXML
    void onCancelButton(ActionEvent event) {
        closeStage(event);
    }

    @FXML
    void onDeleteButton(ActionEvent event) {
        for (Task task: isSelected.keySet()) {
            if(isSelected.get(task)){
                taskRegistry.removeTask(task);
            }
        }
        closeStage(event);

    }

    void initialize() {

    }
}