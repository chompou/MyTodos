package graphics;

import graphics.controllers.Controller;
import graphics.controllers.TaskApplicationController;
import graphics.factories.StageFactory;
import javafx.application.Application;
import javafx.stage.Stage;
import mytodos.TaskRegistry;

public class TaskApplication extends Application {
    private static Stage primaryStage;

    private static void setPrimaryStage(Stage stage) {
        TaskApplication.primaryStage = stage;
    }

    public static Stage getPrimaryStage() {
        return TaskApplication.primaryStage;
    }

    @Override
    public void start(Stage stage) throws Exception {
        setPrimaryStage(stage);
        TaskRegistry taskRegistry = new TaskRegistry();
        Settings settings = Settings.loadSettings();
        Controller controller = new TaskApplicationController(taskRegistry, settings);

        stage.setScene(StageFactory.createStage("/mytodomain.fxml", controller).getScene());
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
