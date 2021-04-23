package graphics.controllers;

import graphics.Settings;
import mytodos.TaskRegistry;

public abstract class Controller {
    final TaskRegistry taskRegistry;
    final Settings settings;

    public Controller(TaskRegistry taskRegistry, Settings settings) {
        this.taskRegistry = taskRegistry;
        this.settings = settings;
    };

    abstract void initialize();


}
