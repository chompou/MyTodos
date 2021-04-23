package graphics.factories;

import graphics.Settings;
import graphics.controllers.Controller;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class StageFactory {

    public static Stage createStage(String fxmlPath, Controller controller) throws IOException{
        Settings settings = controller.getSettings();
        FXMLLoader loader = new FXMLLoader(controller.getClass().getResource(fxmlPath));
        loader.setController(controller);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        Scene scene = new Scene(loader.load());

        if (settings.isDarkTheme())
            scene.getStylesheets().add("DarkTheme.css");

        scene.lookup(".root").setStyle("-fx-font-size:" + settings.getTextSize() + "px;");

        stage.setScene(scene);
        return stage;
    }

}
