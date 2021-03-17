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
