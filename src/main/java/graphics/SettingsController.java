package graphics;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.BorderPane;
import mytodos.Task;
import mytodos.TaskRegistry;

public class SettingsController {

    private final TaskApplicationController controller;
    private final TaskRegistry taskRegistry;

    SettingsController(TaskApplicationController controller, TaskRegistry taskRegistry) {
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

        TaskApplication.getPrimaryStage().getScene().lookup(".root").setStyle("-fx-font-size:" + textSizeChoiceBox.getValue() + "px;");

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
        textSizeChoiceBox.getItems().addAll(8, 10, 11, 12, 14, 16, 18, 22, 26, 30);
        textSizeChoiceBox.setValue(12);
    }

    boolean getDarkThemeChoice(ChoiceBox<String> darkModeChoiceBox) {
        String theme = darkModeChoiceBox.getValue();
        return theme.equals("Yes");
    }
}
