package graphics;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.Scene;

public class TaskApplication extends Application {
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
        loader.setController(new TaskApplicationController());
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
