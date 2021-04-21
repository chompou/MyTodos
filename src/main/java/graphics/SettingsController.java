package graphics;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import mytodos.Task;
import mytodos.TaskRegistry;

public class SettingsController {

    private final ApplicationController controller;
    private final TaskRegistry taskRegistry;

    SettingsController(ApplicationController controller, TaskRegistry taskRegistry) {
        this.controller = controller;
        this.taskRegistry = taskRegistry;
    }

    void closeStage(ActionEvent event) {
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }

    @FXML
    private Button applyButton;

    @FXML
    private Button cancelButton;

    @FXML
    private ChoiceBox<String> darkModeChoiceBox;

    @FXML
    private ChoiceBox<Integer> textSizeChoiceBox;

    @FXML
    private ChoiceBox<String> colorBlindChoiceBox;

    @FXML
    void onApply(ActionEvent event) {

        if (getDarkThemeChoice(darkModeChoiceBox)) {
            TaskApplication.getPrimaryStage().getScene().getStylesheets().add("DarkTheme.css");
        } else {
            TaskApplication.getPrimaryStage().getScene().getStylesheets().clear();
        }
        closeStage(event);
    }

    @FXML
    void onCancel(ActionEvent event) {
        closeStage(event);
    }

    @FXML
    void initialize() {
        setDarkModeChoiceBox();
        setColorBlindChoiceBox();
        setTextSizeChoiceBox();
    }

    void setDarkModeChoiceBox() {
        darkModeChoiceBox.getItems().addAll("No", "Yes");
        darkModeChoiceBox.setValue("No");
    }

    void setColorBlindChoiceBox() {
        colorBlindChoiceBox.getItems().addAll("Off", "Red-green", "Blue-yellow", "Complete");
        colorBlindChoiceBox.setValue("Off");
    }

    void setTextSizeChoiceBox() {
        textSizeChoiceBox.getItems().addAll(8, 12, 14, 16, 22);
        textSizeChoiceBox.setValue(12);
    }

    int getSizeChoice(ChoiceBox<Integer> choiceBox) {
        return choiceBox.getValue();
    }

    boolean getDarkThemeChoice(ChoiceBox<String> choiceBox) {
        String theme = choiceBox.getValue();
        return theme.equals("Yes");
    }
}
