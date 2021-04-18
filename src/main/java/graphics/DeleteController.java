package graphics;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class DeleteController {

    @FXML
    private TextField searchTextField;

    @FXML
    private Button UnselectAllButton;

    @FXML
    private Button SelectAllCompletedButton;

    @FXML
    private Button SelectAllButton;

    @FXML
    private TableView<?> taskTable;

    @FXML
    private TableColumn<?, ?> selectedColumn;

    @FXML
    private TableColumn<?, ?> descriptionColumn;

    @FXML
    private TableColumn<?, ?> categoryColumn;

    @FXML
    private TableColumn<?, ?> deadlineColumn;

    @FXML
    private TableColumn<?, ?> statusColumn;

    @FXML
    private Button DeleteByCategoriesButton;

    @FXML
    private Button CancelButton;

    @FXML
    private Button DeleteButton;

    @FXML
    void onCancelButton(ActionEvent event) {

    }

    @FXML
    void onDeleteButton(ActionEvent event) {

    }

    @FXML
    void onDeleteByCategoriesButton(ActionEvent event) {

    }

    @FXML
    void onSelectAllButton(ActionEvent event) {

    }

    @FXML
    void onSelectAllCompletedButton(ActionEvent event) {

    }

    @FXML

    void onUnselectAllButton(ActionEvent event) {

    }

}
