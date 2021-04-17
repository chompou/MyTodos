package graphics;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import mytodos.TaskRegistry;

public class SettingsController {

    private final ApplicationController controller;
    private final TaskRegistry taskRegistry;

    SettingsController(ApplicationController controller, TaskRegistry taskRegistry) {
        this.controller = controller;
        this.taskRegistry = taskRegistry;
    }

    @FXML
    private Button applyButton;

    @FXML
    private Button cancelButton;

    @FXML
    private ChoiceBox<?> darkModeChoiceBox;

    @FXML
    private ChoiceBox<?> textSizeChoiceBox;

    @FXML
    private ChoiceBox<?> colorBlindChoiceBox;

    @FXML
    void onApply(ActionEvent event) {

    }

    @FXML
    void onCancel(ActionEvent event) {

    }

}
