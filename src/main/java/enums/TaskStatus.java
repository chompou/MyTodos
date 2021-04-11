package enums;

public enum TaskStatus {
    TODO("To-Do"),
    IN_PROGRESS("In Progress"),
    COMPLETE("Complete");

    private String value;
    TaskStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static TaskStatus byValue(String value) {
        for (TaskStatus status: TaskStatus.values())
            if (status.getValue().equals(value))
                return status;
        return null;
    }
}
