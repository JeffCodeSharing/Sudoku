package Tool;

import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;

public class WinTool {
    public static Line CreateLine(int x, int y, int x1, int y1, Paint fill) {
        Line line = new Line();
        line.setStartX(x);
        line.setStartY(y);
        line.setEndX(x1);
        line.setEndY(y1);
        line.setStroke(fill);
        return line;
    }

    public static Button CreateButton(int x, int y, int width, int height, int font_size, String text) {
        Button button = new Button();
        button.setText(text);
        button.setLayoutX(x);
        button.setLayoutY(y);
        button.setMaxSize(width, height);
        button.setMinSize(width, height);
        button.setFont(Font.font(font_size));
        return button;
    }

    public static Label CreateLabel(int x, int y, int width, int height, int font_size, Paint fill, String text, boolean is_centre) {
        Label label = new Label(text);
        label.setText(text);
        label.setLayoutX(x);
        label.setLayoutY(y);
        label.setMaxSize(width, height);
        label.setMinSize(width, height);
        label.setTextFill(fill);
        label.setFont(Font.font(font_size));

        if (is_centre) {
            label.setAlignment(Pos.CENTER);
        }
        return label;
    }

    public static CheckBox CreateCheckBox(int x, int y, int width, int height, int font_size, String text) {
        CheckBox checkBox = new CheckBox();
        checkBox.setText(text);
        checkBox.setLayoutX(x);
        checkBox.setLayoutY(y);
        checkBox.setMaxSize(width, height);
        checkBox.setMinSize(width, height);
        checkBox.setFont(Font.font(font_size));
        return checkBox;
    }

    public static void CreateAlert(Alert.AlertType type, String title, String header_text, String content_text) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header_text);
        alert.setContentText(content_text);
        alert.showAndWait();
    }
}
