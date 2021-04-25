package graphics.controllers;

import graphics.Settings;
import graphics.TaskApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import mytodos.TaskRegistry;

import java.util.Optional;

public class SettingsController extends Controller{

    public SettingsController(TaskRegistry taskRegistry, Settings settings) {
        super(taskRegistry, settings);
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
            settings.setDarkTheme(true);
        } else {
            TaskApplication.getPrimaryStage().getScene().getStylesheets().clear();
            settings.setDarkTheme(false);
        }

        TaskApplication.getPrimaryStage().getScene().lookup(".root").setStyle("-fx-font-size:" + textSizeChoiceBox.getValue() + "px;");
        settings.setTextSize(textSizeChoiceBox.getValue());

        settings.saveSettings();
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
        textSizeChoiceBox.valueProperty().addListener( (v, oldVal, newVal) -> alertBox().showAndWait());
    }

    void setDarkModeChoiceBox() {
        darkModeChoiceBox.getItems().addAll("No", "Yes");
        if (settings.isDarkTheme()) {
            darkModeChoiceBox.setValue("Yes");
        } else darkModeChoiceBox.setValue("No");
    }

    void setColorBlindChoiceBox() {
        colorBlindChoiceBox.getItems().addAll("Off", "Red-green", "Blue-yellow", "Complete");
        colorBlindChoiceBox.setValue("Off");
    }

    Alert alertBox() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Warning");
        alert.setHeaderText("Text size warning");
        alert.setContentText("Changing text size is an experimental feature. Changing it too much can result in some glitches or bugs. \n" +
                "By hitting cancel you will reset text size to default value.\n \n " +
                "Click OK to proceed");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() != ButtonType.OK) {
            TaskApplication.getPrimaryStage().getScene().lookup(".root").setStyle("-fx-font-size:" + 12 + "px;");
        }
        return alert;
    }


    void setTextSizeChoiceBox() {
        textSizeChoiceBox.getItems().addAll(8, 12, 14, 16, 22);
        textSizeChoiceBox.setValue(settings.getTextSize());
    }

    int getSizeChoice(ChoiceBox<Integer> choiceBox) {
        return choiceBox.getValue();
    }

    boolean getDarkThemeChoice(ChoiceBox<String> choiceBox) {
        String theme = choiceBox.getValue();
        return theme.equals("Yes");
    }
}