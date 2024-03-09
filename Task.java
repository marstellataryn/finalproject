public class Task {
    private String title;
    private String description;
    private String dueDate;
    private boolean highPriority;
    private boolean completed;

    // constructor to create new tasks
    public Task(String title, String description, String dueDate, boolean highPriority) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.highPriority = highPriority;
        this.completed = false;
    }

    // method to complete task
    public void markComplete() {
        this.completed = true;
    }

    @Override
    public String toString() {
        return "Task{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", dueDate='" + dueDate + '\'' +
                ", highPriority=" + highPriority +
                ", completed=" + completed +
                '}';
    }
}
