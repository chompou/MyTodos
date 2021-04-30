package graphics.controllers;

import graphics.Settings;
import graphics.TaskApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import mytodos.TaskRegistry;

import java.util.Optional;

/**
 * A controller for handling setup and logic for the settings window
 */
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
    /**
     * Method for what happens when the apply button is pressed.
     * @param event
     */
    @FXML
    public void onApply(ActionEvent event) {

        // Checks what theme is currently selected, and selects the correct theme based on the input
        if (getDarkThemeChoice(darkModeChoiceBox)) {
            TaskApplication.getPrimaryStage().getScene().getStylesheets().add("DarkTheme.css");
            settings.setDarkTheme(true);
        } else {
            TaskApplication.getPrimaryStage().getScene().getStylesheets().clear();
            settings.setDarkTheme(false);
        }

        // Checks what text size is selected, and changes the text size in the stylesheet based on the input
        TaskApplication.getPrimaryStage().getScene().lookup(".root").setStyle("-fx-font-size:" + textSizeChoiceBox.getValue() + "px;");
        settings.setTextSize(textSizeChoiceBox.getValue());

        // Saves the current settings to settings-file
        settings.saveSettings();
        closeStage(event);
    }

    /**
     * Method for what happens when cancel button is clicked. Closes the window without saving any settings.
     * @param event
     */
    @FXML
    public void onCancel(ActionEvent event) {
        closeStage(event);
    }

    @FXML
    void initialize() {

        // Sets up the different choiceboxes
        setDarkModeChoiceBox();
        setTextSizeChoiceBox();

        // Checks for changes in text size choicebox and sends an alert
        textSizeChoiceBox.valueProperty().addListener( (v, oldVal, newVal) -> textSizeAlertBox());
    }

    // Setup for darkmode choicebox, Adds options and checks what the current theme is.
    public void setDarkModeChoiceBox() {
        darkModeChoiceBox.getItems().addAll("No", "Yes");
        if (settings.isDarkTheme()) {
            darkModeChoiceBox.setValue("Yes");
        } else darkModeChoiceBox.setValue("No");
    }
    // Adds options to the text size choicebox
    public void setTextSizeChoiceBox() {
        textSizeChoiceBox.getItems().addAll(8, 12, 14, 16, 22);
        textSizeChoiceBox.setValue(settings.getTextSize());
    }

    public boolean getDarkThemeChoice(ChoiceBox<String> choiceBox) {
        String theme = choiceBox.getValue();
        return theme.equals("Yes");
    }

    // Sets up alert box for text size
    public void textSizeAlertBox() {
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
    }
}