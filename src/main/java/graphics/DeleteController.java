package graphics;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import mytodos.Task;
import mytodos.TaskRegistry;

import java.time.LocalDate;
import java.util.HashMap;

public class DeleteController {
    final private TaskRegistry taskRegistry;
    final private TaskApplicationController controller;
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
    private Button DeleteByCategoriesButton;

    @FXML
    private Button CancelButton;

    @FXML
    private Button DeleteButton;


    public DeleteController(TaskRegistry taskRegistry, TaskApplicationController controller) {
        this.taskRegistry = taskRegistry;
        this.controller = controller;

    }

    void updateFilter() {
        String search = null;
        if (!searchTextField.getText().equals("")) {
            search = searchTextField.getText();
        }
        filteredTasks.setPredicate(TaskRegistry.filterPredicate(null, search, null));
    }

    @FXML
    void initialize() {
        // ## Code snippet now unusable due to TaskPriority enum being removed
        // List<String> mappedPriorities = Arrays.stream(TaskPriority.values()).map(TaskPriority::getValue).collect(Collectors.toList());
        // ObservableList<String> taskPriorities = FXCollections.observableArrayList(mappedPriorities);

        updateTable();

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
    }

    void updateTable() {
        ObservableList<Task> observableTasks = FXCollections.observableList(taskRegistry.getTasks());
        filteredTasks = new FilteredList<>(observableTasks);
        SortedList<Task> sortedTasks = new SortedList<>(filteredTasks);
        sortedTasks.comparatorProperty().bind(taskTable.comparatorProperty());
        taskTable.setItems(sortedTasks);

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
