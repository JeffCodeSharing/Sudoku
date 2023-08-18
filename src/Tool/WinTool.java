package Tool;

import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;

import java.util.Optional;

public class WinTool {
    public Line CreateLine(int x, int y, int x1, int y1, Paint fill) {
        Line line = new Line();
        line.setStartX(x);
        line.setStartY(y);
        line.setEndX(x1);
        line.setEndY(y1);
        line.setStroke(fill);
        return line;
    }

    public Button CreateButton(int x, int y, int width, int height, int font_size, String text) {
        Button button = new Button();
        button.setText(text);
        button.setLayoutX(x);
        button.setLayoutY(y);
        button.setMinWidth(width);
        button.setMaxWidth(width);
        button.setMinHeight(height);
        button.setMaxHeight(height);
        button.setFont(Font.font("Arial", font_size));
        return button;
    }

    public Label CreateLabel(int x, int y, int width, int height, int font_size, Paint fill, String text, boolean is_centre) {
        Label label = new Label();
        label.setText(text);
        label.setLayoutX(x);
        label.setLayoutY(y);
        label.setMinWidth(width);
        label.setMaxWidth(width);
        label.setMinHeight(height);
        label.setMaxHeight(height);
        label.setTextFill(fill);
        label.setFont(Font.font("Arial", font_size));
        label.setText(text);

        if (is_centre) label.setAlignment(Pos.CENTER);
        return label;
    }

    public CheckBox CreateCheckBox(int x, int y, int width, int height, int font_size, String text) {
        CheckBox checkBox = new CheckBox();
        checkBox.setText(text);
        checkBox.setLayoutX(x);
        checkBox.setLayoutY(y);
        checkBox.setMinWidth(width);
        checkBox.setMaxWidth(width);
        checkBox.setMinHeight(height);
        checkBox.setMaxHeight(height);
        checkBox.setFont(Font.font("Arial", font_size));
        return checkBox;
    }

    public ComboBox<String> CreateComboBox(int x, int y, int width, int height, int row_count, String... args) {
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.setLayoutX(x);
        comboBox.setLayoutY(y);
        comboBox.setMinWidth(width);
        comboBox.setMaxWidth(width);
        comboBox.setMinHeight(height);
        comboBox.setMaxHeight(height);
        comboBox.setItems(FXCollections.observableArrayList(args));
        comboBox.setEditable(false);
        comboBox.setVisibleRowCount(row_count);
        return comboBox;
    }

    public Optional<ButtonType> CreateAlert(Alert.AlertType type, String title, String header_text, String content_text) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header_text);
        alert.setContentText(content_text);
        return alert.showAndWait();
    }
}
