package graphics;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;

public class ConfirmDeleteController {

    @FXML
    private Button cancelButton;

    @FXML
    private Button deleteButton;

    void closeStage(ActionEvent event) {
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }

    @FXML
    void onCancelButton(ActionEvent event) {
        closeStage(event);
    }

    @FXML
    void onDeleteButton(ActionEvent event) {

    }

}