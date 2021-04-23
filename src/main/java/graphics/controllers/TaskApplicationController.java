package graphics.controllers;

import graphics.Settings;
import graphics.factories.StageFactory;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
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
import java.util.concurrent.Callable;

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

        // A custom cell factory for creating a column to three-state checkboxes for managing task statuses
        Callback<TableColumn<Task, Void>, TableCell<Task, Void>> statusCellFactory = new Callback<>() {
            @Override
            public TableCell<Task, Void> call(final TableColumn<Task, Void> taskVoidTableColumn) {
                final TableCell<Task, Void> cell = new TableCell<>() {
                    CheckBox checkBox = new CheckBox();

                    {
                        checkBox.setAllowIndeterminate(true);

                        // Triggers whenever a checkbox is clicked by the user
                        // Updates the task associated with the row with the new status and potential end date
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

                    // Triggers whenever rows are (re)loaded and checkboxes get rendered
                    // Ensures each checkbox matches the tasks current status
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

        // Column comparator must be overwritten as it will sort by the string values by default
        priorityColumn.setComparator(Comparator.comparingInt(Task.priorities::indexOf));

        // Ensures every column occupies the same width, regardless of how big the scene is
        statusColumn.prefWidthProperty().bind(taskTable.widthProperty().multiply(0.09));
        descriptionColumn.prefWidthProperty().bind(taskTable.widthProperty().multiply(0.4));
        categoryColumn.prefWidthProperty().bind(taskTable.widthProperty().multiply(0.2));
        deadlineColumn.prefWidthProperty().bind(taskTable.widthProperty().multiply(0.18));
        priorityColumn.prefWidthProperty().bind(taskTable.widthProperty().multiply(0.12));

        filteredTasks = new FilteredList<>(taskRegistry.getTasks());
        sortedTasks = new SortedList<>(filteredTasks);
        sortedTasks.comparatorProperty().bind(taskTable.comparatorProperty());
        taskTable.setItems(sortedTasks);

        // Updates the priority choice box every time a task is added, removed or a category is edited
        // Binds the existing ObservableList of tasks in a second list with a listener specifically for category updates
        Callback<Task, Observable[]> callback = task -> new Observable[] {task.categoryProperty()};
        ObservableList<Task> categoryListener = FXCollections.observableArrayList(callback);
        categoryListener.addListener((ListChangeListener<? super Task>) change -> updateCategories());
        Bindings.bindContentBidirectional(categoryListener, taskRegistry.getTasks());

        toggleGroup1.selectedToggleProperty().addListener(button -> updateFilter());
        searchTextField.textProperty().addListener((obs, oldText, newText) -> updateFilter());
        categoryFilterChoiceBox.valueProperty().addListener(event -> updateFilter());

    }

    private void updateCategories() {
        ObservableList<String> categoryList = taskRegistry.getCategories();

        String oldValue = categoryFilterChoiceBox.getValue();
        String newValue = "All categories";
        if (oldValue != null && categoryList.contains(oldValue))
            newValue = oldValue;

        categoryList.add(0, "All categories");
        categoryFilterChoiceBox.setItems(categoryList);
        categoryFilterChoiceBox.setValue(newValue);
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
