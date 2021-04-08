package graphics;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.Scene;
import mytodos.TaskRegistry;


public class TaskApplication extends Application {
    TaskRegistry taskRegistry;


    @Override
    public void start(Stage stage) throws Exception {
        taskRegistry = new TaskRegistry();

        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/mytodomain.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root, 600 ,400);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
