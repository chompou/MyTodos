package enums;

public enum TaskPriority {
    HIGH("High"),
    MEDIUM("Medium"),
    LOW("Low");

    public String text;
    private TaskPriority(String text) {
        this.text = text;
    }
}
