import java.time.LocalDate;

public class Task {
    private String description;
    private String category;
    private LocalDate deadline;
    private int priority;
    private int status;
    private LocalDate startDate;
    private LocalDate endDate;

    public Task(String description, String category, LocalDate deadline, int priority, int status, LocalDate startDate, LocalDate endDate) {
        this.description = description;
        this.category = category;
        this.deadline = deadline;
        this.priority = priority;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
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

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getStatus() {
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

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Task {" +
                " description = '" + description + '\'' +
                ", category='" + category + '\'' +
                ", deadline=" + deadline +
                ", priority=" + priority +
                '}';
    }
}
