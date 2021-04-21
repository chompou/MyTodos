package graphics;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.Scene;
import mytodos.TaskRegistry;


public class TaskApplication extends Application {
    TaskRegistry taskRegistry = new TaskRegistry();
    private static Stage primaryStage;

    private static void setPrimaryStage(Stage stage) {
        TaskApplication.primaryStage = stage;
    }

    static Stage getPrimaryStage() {
        return TaskApplication.primaryStage;
    }

    @Override
    public void start(Stage stage) throws Exception {
        setPrimaryStage(stage);
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/mytodomain.fxml"));
        loader.setController(new ApplicationController());
        Parent root = loader.load();
        Scene scene = new Scene(root, 600 ,400);
        scene.getStylesheets().add("DarkTheme.css");
        scene.getStylesheets().clear();
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
