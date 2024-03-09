import java.util.ArrayList;

class Category {
    private String categoryName;
    private String categoryColor;
    private ArrayList<Task> tasks;

    // constructor to create a new category
    public Category(String categoryName, String categoryColor) {
        this.categoryName = categoryName;
        this.categoryColor = categoryColor;
        this.tasks = new ArrayList<>();
    }

    // method to categorize task
    public void addTask(Task task) {
        tasks.add(task);
    }

    // method to remove task category
    public void deleteTask(Task task) {
        tasks.remove(task);
    }
}
