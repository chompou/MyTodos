package mytodos;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.io.*;
import java.time.LocalDate;
import java.util.Objects;

public class Task implements Serializable {
    final public static String[] priorities = new String[]{"High", "Medium", "Low"};
    final public static String[] statuses = new String[]{"TODO", "In Progress", "Completed"};

    private SimpleStringProperty description;
    private SimpleStringProperty category;
    private SimpleObjectProperty<LocalDate> deadline;
    private SimpleIntegerProperty priority;
    private SimpleIntegerProperty status;
    private SimpleObjectProperty<LocalDate> startDate;
    private SimpleObjectProperty<LocalDate> endDate;

    public Task() {
        this.description = new SimpleStringProperty(null);
        this.category = new SimpleStringProperty(null);
        this.deadline = new SimpleObjectProperty<>(null);
        this.priority = new SimpleIntegerProperty(0);

        this.status = new SimpleIntegerProperty(0);
        this.startDate = new SimpleObjectProperty<>(LocalDate.now());
        this.endDate = new SimpleObjectProperty<>(null);
    }

    public Task(String description, String category, LocalDate deadline, int priority) {
        this.description = new SimpleStringProperty(description);
        this.category = new SimpleStringProperty(category);
        this.deadline = new SimpleObjectProperty<>(deadline);
        this.priority = new SimpleIntegerProperty(priority);

        this.status = new SimpleIntegerProperty(0);
        this.startDate = new SimpleObjectProperty<>(LocalDate.now());
        this.endDate = new SimpleObjectProperty<>(null);
    }

    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeUTF(getDescription());
        out.writeUTF(getCategory());
        out.writeObject(getDeadline());
        out.writeInt(getPriority());
        out.writeInt(getStatus());
        out.writeObject(getStartDate());
        out.writeObject(getEndDate());
    }

    @Serial
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        this.description = new SimpleStringProperty(in.readUTF());
        this.category = new SimpleStringProperty(in.readUTF());
        this.deadline = new SimpleObjectProperty<>((LocalDate) in.readObject());
        this.priority = new SimpleIntegerProperty(in.readInt());
        this.status = new SimpleIntegerProperty(in.readInt());
        this.startDate = new SimpleObjectProperty<>((LocalDate) in.readObject());
        this.endDate = new SimpleObjectProperty<>((LocalDate) in.readObject());
    }

    public void setPriority(String priority) {
        for (int i = 0; i < priorities.length; i++) {
            if (priorities[i].equals(priority)){
                setPriority(i);
            }
        }
    }

    public void setStatus(String status) {
        for (int i = 0; i < statuses.length; i++) {
            if (statuses[i].equals(status)){
                setStatus(i);
            }
        }
    }

    public String getDescription() {
        return description.get();
    }

    public SimpleStringProperty descriptionProperty() {
        return description;
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public String getCategory() {
        return category.get();
    }

    public SimpleStringProperty categoryProperty() {
        return category;
    }

    public void setCategory(String category) {
        this.category.set(category);
    }

    public LocalDate getDeadline() {
        return deadline.get();
    }

    public SimpleObjectProperty<LocalDate> deadlineProperty() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline.set(deadline);
    }

    public int getPriority() {
        return priority.get();
    }

    public SimpleIntegerProperty priorityProperty() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority.set(priority);
    }

    public int getStatus() {
        return status.get();
    }

    public SimpleIntegerProperty statusProperty() {
        return status;
    }

    public void setStatus(int status) {
        this.status.set(status);
    }

    public LocalDate getStartDate() {
        return startDate.get();
    }

    public SimpleObjectProperty<LocalDate> startDateProperty() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate.set(startDate);
    }

    public LocalDate getEndDate() {
        return endDate.get();
    }

    public SimpleObjectProperty<LocalDate> endDateProperty() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate.set(endDate);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(getDescription(), task.getDescription()) &&
                Objects.equals(getCategory(), task.getCategory()) &&
                Objects.equals(getDeadline(), task.getDeadline()) &&
                Objects.equals(getPriority(), task.getPriority()) &&
                Objects.equals(getStatus(), task.getStatus()) &&
                Objects.equals(getStartDate(), task.getStartDate()) &&
                Objects.equals(getEndDate(), task.getEndDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDescription(), getCategory(), getDeadline(), getPriority(), getStatus(), getStartDate(), getEndDate());
    }
}
