import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

class TaskManager {
    private ArrayList<Task> taskList;
    private ArrayList<Category> categoryList;

    // constructor to create a new task manager
    public TaskManager() {
        this.taskList = new ArrayList<>();
        this.categoryList = new ArrayList<>();
    }

    // method to add a task and category
    public void addTask(Task task, Category category) {
        taskList.add(task);
        category.addTask(task);
    }

    // method to delete task/category
    public void deleteTask(Task task, Category category) {
        taskList.remove(task);
        category.deleteTask(task);
    }

    // corrected return type
    public ArrayList<Task> getTaskList() {
        return taskList;
    }

    // corrected return type
    public ObservableList<Category> getCategoryList() {
        return FXCollections.observableArrayList(categoryList);

    }
}
