package graphics.controllers;

import graphics.Settings;
import graphics.factories.StageFactory;
import graphics.factories.StatusCellFactory;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import mytodos.Task;
import mytodos.TaskRegistry;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Comparator;

/**
 * A controller for handling setup and listeners for the main window of the application.
 * Contains several wrapped ObservableList implementations that gives the embedded TableView it's sorting and filtering characteristics.
 * Extends the abstract Controller class.
 */
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

    /**
     * Instantiates a task management window with an empty Task object.
     * Should only be called as a GUI event.
     * @param event The event that triggered the method
     * @throws IOException
     */
    @FXML
    public void onTaskCreate(ActionEvent event) throws IOException {
        Controller controller = new TaskEditorController(taskRegistry, settings);
        StageFactory.createStage("/mytodotask.fxml", controller).show();
    }

    /**
     * Instantiates a task deletion window
     * Should only be called as a GUI event.
     * @param event The event that triggered the method
     * @throws IOException
     */
    @FXML
    public void onDeleteTasks(ActionEvent event) throws IOException {
        DeleteController controller = new DeleteController(taskRegistry, settings);
        StageFactory.createStage("/mytododelete.fxml", controller).show();

    }

    /**
     * Instantiates a settings management window
     * Should only be called as a GUI event.
     * @param event The event that triggered the method
     * @throws IOException
     */
    @FXML
    public void onSettings(ActionEvent event) throws IOException {
        SettingsController controller = new SettingsController(taskRegistry, settings);
        StageFactory.createStage("/mytodosettings.fxml", controller).show();
    }

    /**
     * Instantiates a task management window with a given Task object to be edited
     * Should only be called as a GUI event.
     * @param event The event that triggered the method
     * @throws IOException
     */
    @FXML
    public void onTableViewMousePressed(MouseEvent event) throws IOException {
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
        statusColumn.setCellFactory(new StatusCellFactory());
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

    /**
     * Updates the category ChoiceBox with a map of categories across the current set of tasks
     * Called when a task has it's category property updated, or is added or removed from the task list.
     */
    public void updateCategories() {
        ObservableList<String> categoryList = taskRegistry.getCategories();

        String oldValue = categoryFilterChoiceBox.getValue();
        String newValue = "All categories";
        if (oldValue != null && categoryList.contains(oldValue))
            newValue = oldValue;

        categoryList.add(0, "All categories");
        categoryFilterChoiceBox.setItems(categoryList);
        categoryFilterChoiceBox.setValue(newValue);
    }

    /**
     * Updates the bound predicate of the filter list, filtering the current TableView according to the filter inputs.
     * Called whenever the value property of any filter input updates.
     */
    public void updateFilter() {
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
