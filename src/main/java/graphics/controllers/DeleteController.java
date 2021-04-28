package graphics.controllers;

import graphics.Settings;
import graphics.factories.StageFactory;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import mytodos.Task;
import mytodos.TaskRegistry;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

/**
 * This class represents the controller for a delete window,
 * which displays the tasks and let's the user select the ones they wish to delete.
 * It has a field for the list of tasks it displays for the user to delete
 * and a field for an HashMap that keeps track of which Tasks are selected.
 * It inherits from the abstract controller class.
 */
public class DeleteController extends Controller {
    private FilteredList<Task> filteredTasks;
    private HashMap<Task, Boolean> isSelected;

    @FXML
    private TextField searchTextField;

    @FXML
    private Button UnselectAllButton;

    @FXML
    private Button SelectAllCompletedButton;

    @FXML
    private Button SelectAllButton;

    @FXML
    private TableView<Task> taskTable;

    @FXML
    private TableColumn<Task, Void> selectedColumn;

    @FXML
    private TableColumn<Task, String> descriptionColumn;

    @FXML
    private TableColumn<Task, String> categoryColumn;

    @FXML
    private TableColumn<Task, LocalDate> deadlineColumn;

    @FXML
    private TableColumn<Task, String> statusColumn;

    @FXML
    private ChoiceBox<String> selectByCategoriesChoiceBox;

    @FXML
    private Button CancelButton;

    @FXML
    private Button DeleteButton;

    /**
     * Creates a DeleteController object
     * @param taskRegistry the taskRegistry the deleteController is deleting from
     * @param settings The settings the delete controller is using.
     */
    public DeleteController(TaskRegistry taskRegistry, Settings settings) {
        super(taskRegistry, settings);
    }


    /**
     * Checks if the user is using the text field for searching, and updates the table accordingly.
     */
    void updateFilter() {
        String search = null;
        if (!searchTextField.getText().equals("")) {
            search = searchTextField.getText();
        }
        filteredTasks.setPredicate(TaskRegistry.filterPredicate(null, search, null));
    }

    /**
     * Initializes the deleteController, defines most of the tableView data, and sets various fields.
     */
    @FXML
    void initialize() {
        ObservableList<Task> observableTasks = FXCollections.observableList(taskRegistry.getTasks());
        filteredTasks = new FilteredList<>(observableTasks);
        SortedList<Task> sortedTasks = new SortedList<>(filteredTasks);
        sortedTasks.comparatorProperty().bind(taskTable.comparatorProperty());
        taskTable.setItems(sortedTasks);

        isSelected = new HashMap<>();
        for (Task task: taskRegistry.getTasks()) {
            isSelected.put(task, false);
        }

        searchTextField.textProperty().addListener((obs, oldText, newText) -> updateFilter());

        selectedColumn = new TableColumn<>("Selected");

        Callback<TableColumn<Task, Void>, TableCell<Task, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Task, Void> call(final TableColumn<Task, Void> taskVoidTableColumn) {
                final TableCell<Task, Void> cell = new TableCell<>() {
                    CheckBox checkBox = new CheckBox();
                    {
                        checkBox.setAllowIndeterminate(false);
                        checkBox.setOnAction((ActionEvent event) -> {
                            Task newTask = getTableView().getItems().get(getIndex());

                            if (isSelected.get(newTask)){
                                isSelected.put(newTask, false);
                            } else {
                                isSelected.put(newTask, true);
                            }
                        });

                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            Task task = getTableView().getItems().get(getIndex());
                            checkBox.setSelected(isSelected.get(task));
                            setGraphic(checkBox);
                        }
                    }
                };
                cell.setAlignment(Pos.CENTER);
                return cell;
            }
        };

        selectedColumn.setCellFactory(cellFactory);
        taskTable.getColumns().add(0, selectedColumn);

        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        deadlineColumn.setCellValueFactory(new PropertyValueFactory<>("deadline"));
        statusColumn.setCellValueFactory(cellData -> Bindings.stringValueAt(Task.statuses, cellData.getValue().statusProperty()));

        selectedColumn.prefWidthProperty().bind(taskTable.widthProperty().multiply(0.09));
        descriptionColumn.prefWidthProperty().bind(taskTable.widthProperty().multiply(0.4));
        categoryColumn.prefWidthProperty().bind(taskTable.widthProperty().multiply(0.2));
        deadlineColumn.prefWidthProperty().bind(taskTable.widthProperty().multiply(0.18));
        statusColumn.prefWidthProperty().bind(taskTable.widthProperty().multiply(0.12));

        ObservableList<String> categoryList = taskRegistry.getCategories();
        categoryList.add(0, "All Categories");

        selectByCategoriesChoiceBox.setItems(categoryList);
        selectByCategoriesChoiceBox.setValue("All Categories");
    }

    /**
     * Closes the stage when the cancel button is pressed.
     * @param event the event of the cancel button being pressed.
     */
    @FXML
    void onCancelButton(ActionEvent event) {
        closeStage(event);
    }

    /**
     * Pulls up a confirmation window when the delete button is pushed.
     * Deletes the selected tasks if the user answer's yes, and returns to the delete window if not.
     * @param event the event of the user pressing the delete button.
     */
    @FXML
    void onDeleteButton(ActionEvent event){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Comfirm Delete?");
        alert.setHeaderText("Are you sure you wish to delete tasks?");
        alert.setContentText("This action cannot be undone.");
        Optional<ButtonType> result = alert.showAndWait();

        if(result.get() == ButtonType.OK){
            for (Task task: isSelected.keySet()) {
                if(isSelected.get(task)){
                    taskRegistry.removeTask(task);
                }
            }
            closeStage(event);
        }

    }

    /**
     * Selects all the tasks of a category when the user selects it from the dropdown menu.
     * @param event The event of the user clicking the dropdown menu.
     */
    @FXML
    void onSelectByCategories(ActionEvent event) {
        if (!selectByCategoriesChoiceBox.getValue().equals("All Categories")){
            String category = selectByCategoriesChoiceBox.getValue();
            Iterator<Map.Entry<Task, Boolean>> it = isSelected.entrySet().iterator();
            while(it.hasNext()){
                Map.Entry<Task, Boolean> entry = it.next();
                if(entry.getKey().getCategory().equals(category)){
                    isSelected.put(entry.getKey(), true);
                }
            }
            taskTable.refresh();
        }


    }

    /**
     * Selects all the tasks when the user clicks the select all button.
     * @param event The event of the user clicking the select all button.
     */
    @FXML
    void onSelectAllButton(ActionEvent event) {
        isSelected.replaceAll((task, selected) -> true);
        taskTable.refresh();
    }

    /**
     * Selects all completed tasks when the user clicks the select all completed button.
     * @param event The event of the user clicking the select all completed button.
     */
    @FXML
    void onSelectAllCompletedButton(ActionEvent event) {
        Iterator<Map.Entry<Task, Boolean>> it = isSelected.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry<Task, Boolean> entry = it.next();
            if(entry.getKey().getStatus()==2){
                isSelected.put(entry.getKey(), true);
            }
        }
        taskTable.refresh();
    }

    /**
     * Unselects all tasks when the user clicks the unselect all button.
     * @param event The event of the user clicking the unselect all button.
     */
    @FXML
    void onUnselectAllButton(ActionEvent event) {
        isSelected.replaceAll((task, selected) -> false);
        taskTable.refresh();
        selectByCategoriesChoiceBox.setValue("All Categories");
    }

}
