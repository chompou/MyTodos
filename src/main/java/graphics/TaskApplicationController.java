package graphics;

import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import mytodos.Task;
import mytodos.TaskRegistry;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.stream.Collectors;

public class TaskApplicationController {
    private TaskRegistry taskRegistry;
    private FilteredList<Task> filteredTasks;
    private SortedList<Task> sortedTasks;

    @FXML
    private TableView<Task> taskTable;
    @FXML
    private TableColumn<Task, Void> statusColumn;
    @FXML
    private TableColumn<Task, String> descriptionColumn;
    @FXML
    private TableColumn<Task, String> categoryColumn;
    @FXML
    private TableColumn<Task, LocalDate> deadlineColumn;
    @FXML
    private TableColumn<Task, String> priorityColumn;

    @FXML
    private ToggleGroup toggleGroup1;
    @FXML
    private ToggleButton todoToggleButton;
    @FXML
    private ToggleButton inProgressToggleButton;
    @FXML
    private ToggleButton completedToggleButton;

    @FXML
    private TextField searchTextField;
    @FXML
    private ChoiceBox<String> categoryFilterChoiceBox;

    @FXML
    void onTaskCreate(ActionEvent event) {
        openTaskEditorStage(new TaskEditorController(taskRegistry));
    }

    @FXML
    void onDeleteTasks(ActionEvent event) {
        DeleteController controller = new DeleteController(taskRegistry, this);
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/mytododelete.fxml"));
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
    void onSettings(ActionEvent event) {
        SettingsController controller = new SettingsController(this, taskRegistry);
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/mytodosettings.fxml"));
        loader.setController(controller);
        try {
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

    private void openTaskEditorStage(TaskEditorController controller) {
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/mytodotask.fxml"));
        loader.setController(controller);
        try {
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
        this.taskRegistry = new TaskRegistry();

        Callback<TableColumn<Task, Void>, TableCell<Task, Void>> statusCellFactory = new Callback<>() {
            @Override
            public TableCell<Task, Void> call(final TableColumn<Task, Void> taskVoidTableColumn) {
                final TableCell<Task, Void> cell = new TableCell<>() {
                    CheckBox checkBox = new CheckBox();

                    {
                        checkBox.setAllowIndeterminate(true);
                        checkBox.setOnAction((ActionEvent event) -> {
                            Task task = getTableView().getItems().get(getIndex());
                            boolean indeterminate = checkBox.isIndeterminate();
                            boolean selected = checkBox.isSelected();
                            if (!indeterminate && selected)
                                task.setStatus(2);
                            if (indeterminate && !selected)
                                task.setStatus(1);
                            if (!indeterminate && !selected)
                                task.setStatus(0);

                            if (task.getStatus() == 2)
                                task.setEndDate(LocalDate.now());
                            else
                                task.setEndDate(null);
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            Task task = getTableView().getItems().get(getIndex());
                            if (task.getStatus() == 2){
                                checkBox.setIndeterminate(false);
                                checkBox.setSelected(true);
                            } else if (task.getStatus() == 1) {
                                checkBox.setIndeterminate(true);
                                checkBox.setSelected(false);
                            } else {
                                checkBox.setIndeterminate(false);
                                checkBox.setSelected(false);
                            }
                            setGraphic(checkBox);
                        }
                    }
                };
                cell.setAlignment(Pos.CENTER);
                return cell;
            }
        };

        statusColumn.setCellFactory(statusCellFactory);
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
        categoryColumn.setCellValueFactory(cellData -> cellData.getValue().categoryProperty());
        deadlineColumn.setCellValueFactory(cellData -> cellData.getValue().deadlineProperty());
        priorityColumn.setCellValueFactory(cellData -> Bindings.stringValueAt(Task.priorities, cellData.getValue().priorityProperty()));

        priorityColumn.setComparator(Comparator.comparingInt(Task.priorities::indexOf));

        statusColumn.prefWidthProperty().bind(taskTable.widthProperty().multiply(0.09));
        descriptionColumn.prefWidthProperty().bind(taskTable.widthProperty().multiply(0.4));
        categoryColumn.prefWidthProperty().bind(taskTable.widthProperty().multiply(0.2));
        deadlineColumn.prefWidthProperty().bind(taskTable.widthProperty().multiply(0.18));
        priorityColumn.prefWidthProperty().bind(taskTable.widthProperty().multiply(0.12));

        filteredTasks = new FilteredList<>(this.taskRegistry.getTasks());
        sortedTasks = new SortedList<>(filteredTasks);
        sortedTasks.comparatorProperty().bind(taskTable.comparatorProperty());
        taskTable.setItems(sortedTasks);

        categoryFilterChoiceBox.setOnShowing(event -> categoryFilterChoiceBox.setItems(taskRegistry.getCategories()));

        toggleGroup1.selectedToggleProperty().addListener(button -> updateFilter());
        searchTextField.textProperty().addListener((obs, oldText, newText) -> updateFilter());
        categoryFilterChoiceBox.valueProperty().addListener(event -> updateFilter());

        taskTable.setOnMousePressed(mouseEvent -> {
            if (mouseEvent.isPrimaryButtonDown() && (mouseEvent.getClickCount() == 2)) {
                Task task = taskTable.getSelectionModel().getSelectedItem();
                if (task != null)
                    openTaskEditorStage(new TaskEditorController(taskRegistry, task));
            }
        });

    }

    private void updateFilter() {
        Integer status = null;
        String search = null;
        String category = null;

        Toggle selectedToggle = toggleGroup1.getSelectedToggle();
        if (selectedToggle != null) {
            int index = toggleGroup1.getToggles().indexOf(selectedToggle);
            if (index != 3)
                status = 2 - index;
        }

        if (!searchTextField.getText().equals("")) {
            search = searchTextField.getText();
        }

        if (categoryFilterChoiceBox.getValue() != null && !categoryFilterChoiceBox.getValue().equals("All categories")) {
            category = categoryFilterChoiceBox.getValue();
        }

        filteredTasks.setPredicate(TaskRegistry.filterPredicate(status, search, category));
    }

}
