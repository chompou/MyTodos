package mytodos;

import enums.TaskPriority;
import enums.TaskStatus;

import java.time.LocalDate;
import java.util.Objects;

public class Task {
    private String description;
    private String category;
    private LocalDate deadline;
    private TaskPriority priority;
    private TaskStatus status;
    private LocalDate startDate;
    private LocalDate endDate;

    public Task() {

    }

    public Task(String description, String category, LocalDate deadline, TaskPriority priority) {
        this.description = description;
        this.category = category;
        this.deadline = deadline;
        this.priority = priority;
        this.status = TaskStatus.TODO;
        this.startDate = LocalDate.now();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public TaskPriority getPriority() {
        return priority;
    }

    public void setPriority(TaskPriority priority) {
        this.priority = priority;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, category, deadline, priority, status, startDate, endDate);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(description, task.description) && Objects.equals(category, task.category) && Objects.equals(deadline, task.deadline) && priority == task.priority && status == task.status && Objects.equals(startDate, task.startDate) && Objects.equals(endDate, task.endDate);
    }

    @Override
    public String toString() {
        return "mytodos.Task {" +
                " description = '" + description + '\'' +
                ", category='" + category + '\'' +
                ", deadline=" + deadline +
                ", priority=" + priority +
                '}';
    }
}
