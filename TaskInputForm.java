import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class TaskInputForm extends GridPane {

    private final TextField titleField = new TextField();
    private final TextArea descriptionArea = new TextArea();
    private final TextField dueDateField = new TextField(); // Use TextField for dueDate
    private final CheckBox highPriorityCheckbox = new CheckBox();

    private final BooleanBinding isTaskValid;

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
