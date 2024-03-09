import javafx.application.Application;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ToDoListApp extends Application {

    private TaskManager taskManager;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        taskManager = new TaskManager(); // Initialize TaskManager

        primaryStage.setTitle("To-Do List App");

        // Create main layout
        BorderPane mainLayout = new BorderPane();
        mainLayout.setPadding(new Insets(10));

        // Create task list view
        ListView<Task> taskListView = new ListView<>();
        taskListView.setId("taskListView"); // Set a string identifier for the ListView

        // Create task details area
        TextArea taskDetailsTextArea = new TextArea();
        taskDetailsTextArea.setEditable(false);

        // Assuming you have the category names stored in a list
        List<String> categoryNames = new ArrayList<>(Arrays.asList("Phone Call", "Schedule", "Fax", "Reminder"));

        // Use the list of category names when initializing the ComboBox
        final ComboBox<Category> categoryComboBox = new ComboBox<>(FXCollections.observableArrayList(categoryNames));
        categoryComboBox.setPromptText("Select Category");

        // Create buttons
        Button addButton = new Button("Add Task");
        Button deleteButton = new Button("Delete Task");
        Button markCompleteButton = new Button("Mark Complete");

        // Handle events
        taskListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                taskDetailsTextArea.setText(newValue.toString());
            }
        });

        addButton.setOnAction(event -> {
            // Handle adding a new task
            TaskDialog dialog = new TaskDialog("Add Task", "Enter task details:");
            dialog.showAndWait().ifPresent(task -> {
                taskManager.addTask(task, categoryComboBox.getValue());
            });
        });

        deleteButton.setOnAction(event -> {
            // Handle deleting a task
            Task selectedTask = taskListView.getSelectionModel().getSelectedItem();
            if (selectedTask != null) {
                Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
                confirmation.setTitle("Delete Task");
                confirmation.setHeaderText("Confirm Deletion");
                confirmation.setContentText("Are you sure you want to delete this task?");
                confirmation.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        taskManager.deleteTask(selectedTask, null);
                    }
                });
            }
        });

        markCompleteButton.setOnAction(event -> {
            // Handle marking a task as complete
            Task selectedTask = taskListView.getSelectionModel().getSelectedItem();
            if (selectedTask != null) {
                selectedTask.markComplete();
                taskListView.refresh(); // Refresh the list view to update completed status
                showAlert("Task Marked Complete", "Task has been marked as complete.");
            }
        });

        // Create the top layout with buttons and combo box
        HBox topLayout = new HBox(10, categoryComboBox, addButton, deleteButton, markCompleteButton);
        topLayout.setPadding(new Insets(10));

        // Set layouts to the main layout
        mainLayout.setTop(topLayout);
        mainLayout.setLeft(taskListView);
        mainLayout.setCenter(taskDetailsTextArea);

        Scene scene = new Scene(mainLayout, 600, 400);
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static class TaskDialog extends Dialog<Task> {

        public TaskDialog(String title, String headerText) {
            setTitle(title);
            setHeaderText(headerText);

            // Set the button types
            ButtonType addButton = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
            getDialogPane().getButtonTypes().addAll(addButton, ButtonType.CANCEL);

            // Create the task input form
            TaskInputForm taskInputForm = new TaskInputForm();
            getDialogPane().setContent(taskInputForm);

            // Enable/Disable add button depending on whether a task is entered
            Node addButtonNode = getDialogPane().lookupButton(addButton);
            addButtonNode.setDisable(true);

            // Do some validation (using the JavaFX binding API)
            taskInputForm.isTaskValidProperty()
                    .addListener((observable, oldValue, newValue) -> addButtonNode.setDisable(!newValue));

            setResultConverter(dialogButton -> {
                if (dialogButton == addButton) {
                    return taskInputForm.getTask();
                }
                return null;
            });
        }
    }

    public static class TaskInputForm extends GridPane {

        private final TextField titleField = new TextField();
        private final TextArea descriptionArea = new TextArea();
        private final TextField dueDateField = new TextField(); // Use TextField for dueDate
        private final CheckBox highPriorityCheckbox = new CheckBox();

        private final BooleanBinding isTaskValid;

        /**
         * 
         */
        public TaskInputForm() {
            setHgap(10);
            setVgap(10);

            titleField.setPromptText("Title");
            descriptionArea.setPromptText("Description");
            dueDateField.setPromptText("Due Date");

            add(new Label("Title:"), 0, 0);
            add(titleField, 1, 0, 2, 1);
            add(new Label("Description:"), 0, 1);
            add(descriptionArea, 1, 1, 2, 1);
            add(new Label("Due Date:"), 0, 2);
            add(dueDateField, 1, 2);
            add(new Label("High Priority:"), 0, 3);
            add(highPriorityCheckbox, 1, 3);

            // Validation: Title and Due Date
            isTaskValid = new BooleanBinding() {
                {
                    bind(titleField.textProperty(), dueDateField.textProperty());
                }

                @Override
                protected boolean computeValue() {
                    return !titleField.getText().trim().isEmpty() && !dueDateField.getText().trim().isEmpty();
                }
            };

            // Bind the validation to the form
            titleField.disableProperty().bind(isTaskValid.not());
            dueDateField.disableProperty().bind(isTaskValid.not());
        }

        /**
         * @return
         */
        public Task getTask() {
            String title = titleField.getText().trim();
            String description = descriptionArea.getText().trim();
            String dueDate = dueDateField.getText().trim(); // Use String for dueDate
            boolean highPriority = highPriorityCheckbox.isSelected();

            return new Task(title, description, dueDate, highPriority);
        }

        public BooleanProperty isTaskValidProperty() {
            return new SimpleBooleanProperty(isTaskValid.get());
        }
    }
}