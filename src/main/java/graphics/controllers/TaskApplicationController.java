package graphics.controllers;

import graphics.Settings;
import graphics.factories.StageFactory;
import javafx.beans.binding.Bindings;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import mytodos.Task;
import mytodos.TaskRegistry;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Comparator;

public class TaskApplicationController extends Controller {
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

    public TaskApplicationController(TaskRegistry taskRegistry, Settings settings) {
        super(taskRegistry, settings);
    }

    @FXML
    void onTaskCreate(ActionEvent event) throws IOException {
        Controller controller = new TaskEditorController(taskRegistry, settings);
        StageFactory.createStage("/mytodotask.fxml", controller).show();
    }

    @FXML
    void onDeleteTasks(ActionEvent event) throws IOException {
        DeleteController controller = new DeleteController(taskRegistry, settings);
        StageFactory.createStage("/mytododelete.fxml", controller).show();

    }

    @FXML
    void onSettings(ActionEvent event) throws IOException {
        SettingsController controller = new SettingsController(taskRegistry, settings);
        StageFactory.createStage("/mytodosettings.fxml", controller).show();
    }

    @FXML
    void onTableViewMousePressed(MouseEvent event) throws IOException {
        if (event.isPrimaryButtonDown() && (event.getClickCount() == 2)) {
            Task task = taskTable.getSelectionModel().getSelectedItem();
            if (task != null) {
                Controller controller = new TaskEditorController(taskRegistry, settings, task);
                StageFactory.createStage("/mytodotask.fxml", controller).show();
            }

        }
    }

    @FXML
    void initialize() {
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

        filteredTasks = new FilteredList<>(taskRegistry.getTasks());
        sortedTasks = new SortedList<>(filteredTasks);
        sortedTasks.comparatorProperty().bind(taskTable.comparatorProperty());
        taskTable.setItems(sortedTasks);

        categoryFilterChoiceBox.setOnShowing(event -> categoryFilterChoiceBox.setItems(taskRegistry.getCategories()));

        toggleGroup1.selectedToggleProperty().addListener(button -> updateFilter());
        searchTextField.textProperty().addListener((obs, oldText, newText) -> updateFilter());
        categoryFilterChoiceBox.valueProperty().addListener(event -> updateFilter());

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
