package enums;

public enum TaskPriority {
    HIGH("High"),
    MEDIUM("Medium"),
    LOW("Low");

    private String value;
    TaskPriority(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static TaskPriority byValue(String value) {
        for (TaskPriority priority: TaskPriority.values())
            if (priority.getValue().equals(value))
                return priority;
        return null;
    }
}
