package enums;

public enum TaskStatus {
    TODO("To-Do"),
    IN_PROGRESS("In Progress"),
    COMPLETE("Complete");

    public String text;
    private TaskStatus(String text) {
        this.text = text;
    }
}
