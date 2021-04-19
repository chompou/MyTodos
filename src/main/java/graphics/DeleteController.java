package graphics;

import enums.TaskPriority;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import mytodos.TaskRegistry;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DeleteController {
    final private TaskRegistry taskRegistry;
    final private ApplicationController controller;

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


    public DeleteController(TaskRegistry taskRegistry, ApplicationController controller) {
        this.taskRegistry = taskRegistry;
        this.controller = controller;
    }

    @FXML
    void initialize() {
        List<String> mappedPriorities = Arrays.stream(TaskPriority.values()).map(TaskPriority::getValue).collect(Collectors.toList());
        ObservableList<String> taskPriorities = FXCollections.observableArrayList(mappedPriorities);
    }

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
