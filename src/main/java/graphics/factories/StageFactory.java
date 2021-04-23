package graphics.factories;

import graphics.controllers.Controller;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class StageFactory {

    public static Stage createStage(String fxmlPath, Controller controller) throws IOException{
        FXMLLoader loader = new FXMLLoader(controller.getClass().getResource(fxmlPath));
        loader.setController(controller);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(loader.load()));
        return stage;
    }

}
