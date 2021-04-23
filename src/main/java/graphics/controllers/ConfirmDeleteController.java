package graphics.controllers;

import graphics.Settings;
import graphics.controllers.Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import mytodos.TaskRegistry;

public class ConfirmDeleteController extends Controller {

    @FXML
    private Button cancelButton;

    @FXML
    private Button deleteButton;

    ConfirmDeleteController(TaskRegistry taskRegistry, Settings settings) {
        super(taskRegistry, settings);
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

    }

    void initialize() {

    }
}