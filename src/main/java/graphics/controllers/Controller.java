package graphics.controllers;

import graphics.Settings;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import mytodos.TaskRegistry;

/**
 * An abstract controller class used for implementing into dedicated controller classes
 */
public abstract class Controller {
    final TaskRegistry taskRegistry;
    final Settings settings;

    public Controller(TaskRegistry taskRegistry, Settings settings) {
        this.taskRegistry = taskRegistry;
        this.settings = settings;
    }

    abstract void initialize();

    public Settings getSettings() {
        return settings;
    }

    void closeStage(ActionEvent event) {
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }
}
