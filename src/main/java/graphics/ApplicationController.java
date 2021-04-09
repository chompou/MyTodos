package graphics;

import enums.TaskPriority;
import enums.TaskStatus;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import mytodos.Task;
import mytodos.TaskRegistry;

import javax.imageio.IIOException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;

public class ApplicationController {
    private TaskRegistry taskRegistry;
    private FilteredList<Task> filteredTasks;

    @FXML
    private SplitPane splitPane;
    @FXML
    private BorderPane borderPane;
    @FXML
    private VBox vBox;

    @FXML
    private TableView<Task> taskTable;
    @FXML
    private TableColumn<Task, String> descriptionColumn;
    @FXML
    private TableColumn<Task, String> categoryColumn;
    @FXML
    private TableColumn<Task, LocalDate> deadlineColumn;
    @FXML
    private TableColumn<Task, TaskPriority> priorityColumn;

    @FXML
    private ToggleGroup toggleGroup1;
    @FXML
    private ToggleButton todoToggleButton;
    @FXML
    private ToggleButton inProgressToggleButton;
    @FXML
    private ToggleButton completedToggleButton;

    @FXML
    private Button createTaskButton;
    @FXML
    private Button deleteTasksButton;
    @FXML
    private Button settingsButton;

    @FXML
    private TextField searchTextField;
    @FXML
    private ChoiceBox<String> categoryFilterChoiceBox;

    @FXML
    void onTaskCreate(ActionEvent event)  {
        Object controller = new TaskEditorController(this, taskRegistry);
        openTaskEditor(controller);
    }

    @FXML
    void onDeleteTasks(ActionEvent event) {

    }

    @FXML
    void onSettings(ActionEvent event) {
    }

    void updateTable() {
        updateCategories();
        ObservableList<Task> observableTasks = FXCollections.observableList(taskRegistry.getTasks());
        filteredTasks = new FilteredList<>(observableTasks);
        taskTable.setItems(filteredTasks);
    }

    void updateCategories() {
        Set<String> categories = taskRegistry.getTasks().stream().map(Task::getCategory).collect(Collectors.toSet());
        ArrayList<String> categoryList = new ArrayList<>(categories);
        categoryList.add(0, "All Categories");
        categoryFilterChoiceBox.setItems(FXCollections.observableList(categoryList));
        categoryFilterChoiceBox.getSelectionModel().selectFirst();
    }

    void updateFilter() {
        TaskStatus taskStatus = null;
        String search = null;
        String category = null;

        Toggle selectedToggle = toggleGroup1.getSelectedToggle();
        if (selectedToggle != null) {
            int index = toggleGroup1.getToggles().indexOf(selectedToggle);
            taskStatus = TaskStatus.values()[2 - index];
        }

        if (!searchTextField.getText().equals("")) {
            search = searchTextField.getText();
        }

        if (categoryFilterChoiceBox.getValue() != null && !categoryFilterChoiceBox.getValue().equals("All Categories")) {
            category = categoryFilterChoiceBox.getValue();
        }

        filteredTasks.setPredicate(TaskRegistry.filterPredicate(taskStatus, search, category));
    }

    void editTask(Task task) {
        Object controller = new TaskEditorController(this, taskRegistry, task);
        openTaskEditor(controller);
    }

    void openTaskEditor(Object controller) {
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/mytodotask.fxml"));
        loader.setController(controller);
        try{
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void initialize() {
        taskRegistry = new TaskRegistry();
        taskRegistry.loadTasksFromFile();
        updateTable();

        taskTable.setRowFactory( tv -> {
            TableRow<Task> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty())) {
                    Task task = row.getItem();
                    editTask(task);
                }
            });
            return row;
        });

        toggleGroup1.selectedToggleProperty().addListener(button -> updateFilter());
        searchTextField.textProperty().addListener((obs, oldText, newText) -> updateFilter());
        categoryFilterChoiceBox.valueProperty().addListener(event -> updateFilter());

        TableColumn<Task, Void> checkboxColumn = new TableColumn<Task, Void>("Status");

        Callback<TableColumn<Task, Void>, TableCell<Task, Void>> cellFactory = new Callback<TableColumn<Task, Void>, TableCell<Task, Void>>() {
            @Override
            public TableCell<Task, Void> call(final TableColumn<Task, Void> taskVoidTableColumn) {
                final TableCell<Task, Void> cell = new TableCell<Task, Void>() {
                    CheckBox checkBox = new CheckBox();

                    {
                        checkBox.setAllowIndeterminate(true);
                        checkBox.setOnAction((ActionEvent event) -> {
                            TaskStatus status;
                            boolean indeterminate = checkBox.isIndeterminate();
                            boolean selected = checkBox.isSelected();
                            if (!indeterminate)
                                if (selected)
                                    status = TaskStatus.COMPLETE;
                                else
                                    status = TaskStatus.TODO;
                            else
                                status = TaskStatus.IN_PROGRESS;

                            Task task = getTableView().getItems().get(getIndex());

                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(checkBox);
                        }
                    }
                };
                cell.setAlignment(Pos.CENTER);
                return cell;
            }
        };

        checkboxColumn.setCellFactory(cellFactory);
        taskTable.getColumns().add(0, checkboxColumn);

        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        deadlineColumn.setCellValueFactory(new PropertyValueFactory<>("deadline"));
        priorityColumn.setCellValueFactory(new PropertyValueFactory<>("priority"));

        checkboxColumn.prefWidthProperty().bind(taskTable.widthProperty().multiply(0.09));
        descriptionColumn.prefWidthProperty().bind(taskTable.widthProperty().multiply(0.4));
        categoryColumn.prefWidthProperty().bind(taskTable.widthProperty().multiply(0.2));
        deadlineColumn.prefWidthProperty().bind(taskTable.widthProperty().multiply(0.18));
        priorityColumn.prefWidthProperty().bind(taskTable.widthProperty().multiply(0.12));
    }
}
