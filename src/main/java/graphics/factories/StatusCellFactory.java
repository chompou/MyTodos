package graphics.factories;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;
import mytodos.Task;

import java.time.LocalDate;

public class StatusCellFactory implements Callback<TableColumn<Task, Void>, TableCell<Task, Void>> {
    @Override
    public TableCell<Task, Void> call(TableColumn<Task, Void> taskVoidTableColumn) {
        final TableCell<Task, Void> cell = new TableCell<>() {
            CheckBox checkBox = new CheckBox();

            {
                checkBox.setAllowIndeterminate(true);

                // Triggers whenever the checkbox is clicked by the user
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
}
