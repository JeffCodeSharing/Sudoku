import Tool.IOTool;
import Tool.WinTool;
import crack.Operation;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WinMain extends Application {
    private final List<List<Label>> confirm_num = new ArrayList<>();
    private final List<List<Label>> unknown_num = new ArrayList<>();
    private Label[] change_label = new Label[2];
    
    @Override
    public void start(Stage stage) {
        Group group = new Group();
        Scene scene = new Scene(group);

        // 绘制线条
        for (int i = 0; i <= 9; i++) {
            // 能被3整除的是宫线，不被整除的是普通格线
            Paint fill = (i % 3 == 0) ? Color.DARKBLUE : Color.DARKGREY;

            // 添加横线和竖线
            group.getChildren().addAll(
                    WinTool.CreateLine(50*(i+1), 50, 50*(i+1), 500, fill),
                    WinTool.CreateLine(50, 50*(i+1), 500, 50*(i+1), fill)
            );
        }

        // 批注多选框
        CheckBox checkBox = WinTool.CreateCheckBox(500, 500, 100, 50, 20, "批注");
        group.getChildren().add(checkBox);

        // 菜单
        MenuBar menuBar = new MenuBar();
        menuBar.setLayoutX(0);
        menuBar.setLayoutY(0);
        menuBar.setMinWidth(700);
        menuBar.setMaxWidth(700);
        menuBar.setMinHeight(25);
        menuBar.setMaxHeight(25);

        Menu fileMenu = new Menu("文件");
        MenuItem input = new MenuItem("导入");
        input.setOnAction(actionEvent -> IOTool.input(confirm_num, unknown_num));
        MenuItem output = new MenuItem("导出");
        output.setOnAction(actionEvent -> IOTool.output(confirm_num, unknown_num));
        fileMenu.getItems().addAll(input, output);
        
        menuBar.getMenus().addAll(fileMenu);
        group.getChildren().add(menuBar);

        // 清空按钮
        Button button_clear = WinTool.CreateButton(530, 380, 110, 50, 20, "清空数独");
        button_clear.setOnAction(actionEvent -> clean_sudoku());
        group.getChildren().add(button_clear);

        // 自动运算按钮
        Button button_run = WinTool.CreateButton(530, 450, 110, 50, 20, "自动运算");
        button_run.setOnAction(actionEvent -> new Operation().run(confirm_num, unknown_num, get_label_text(confirm_num), get_label_text(unknown_num)));
        group.getChildren().add(button_run);

        // 按钮数字
        for (int i=1; i <= 9; i++) {
            final int j = i;
            Button button = WinTool.CreateButton(50*i, 510, 40, 40, 18, String.valueOf(i));
            button.setOnAction(actionEvent -> add_label_num(String.valueOf(j), checkBox.isSelected()));
            group.getChildren().add(button);
        }

        // 线条格子中的Label控件
        for (int i = 1; i <= 9; i++) {
            List<Label> list_confirm = new ArrayList<>();
            List<Label> list_unknown = new ArrayList<>();
            for (int j = 1; j <= 9; j++) {
                Label confirm_label = WinTool.CreateLabel(j*50, i*50, 50, 50, 25, Color.BLACK, "", true);
                Label unknown_label = WinTool.CreateLabel(j*50, i*50, 50, 50, 16, Color.BLUE, "", true);
                // 用于检测按键按下并执行
                final int column_num = i - 1;
                final int row_num = j - 1;

                unknown_label.setOnMousePressed(mouseEvent -> update_change_label(column_num, row_num));
                confirm_label.setOnMousePressed(mouseEvent -> update_change_label(column_num, row_num));

                group.getChildren().addAll(confirm_label, unknown_label);
                list_confirm.add(confirm_label);
                list_unknown.add(unknown_label);
            }
            confirm_num.add(list_confirm);
            unknown_num.add(list_unknown);
        }

        // add global keyboard listener
        scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            String type = event.getCode().getName();
            switch (type) {
                case "1", "2", "3", "4", "5", "6", "7", "8", "9" -> add_label_num(type, checkBox.isSelected());
            }
        });

        stage.setTitle("数独");
        stage.setScene(scene);
        stage.setWidth(700);
        stage.setHeight(600);
        stage.setResizable(false);
        stage.setOnCloseRequest(windowEvent -> System.exit(0));
        stage.show();
    }

    private void add_label_num(String add_num, boolean is_unknown) {
        try {
            String unknown_num = change_label[1].getText().replace("\n", "");    // 清除换行符

            if (is_unknown) {     // 是批注状态
                change_label[0].setText("");

                // 排序数字
                String add_str;
                if (unknown_num.contains(add_num)) {
                    add_str = unknown_num.replace(add_num, "");
                } else {
                    add_str = unknown_num + add_num;
                }

                char[] numChars = add_str.toCharArray();
                Arrays.sort(numChars);

                StringBuilder builder = new StringBuilder(new String(numChars));
                if (builder.length() > 5) {
                    builder.insert(5, "\n");
                }

                change_label[1].setText(builder.toString());
            } else {      // 是确定状态
                change_label[1].setText("");
                change_label[0].setText(add_num);
            }
        } catch (Exception ignored) {}
    }

    private String[][] get_label_text(List<List<Label>> list) {
        String[][] return_array = new String[9][9];
        for (int i=0; i < 9; i++) {
            for (int j=0; j < 9; j++) {
                String str = list.get(i).get(j).getText();
                if (str == null) {
                    str = "";
                }
                str = str.replace("\n", "");
                return_array[i][j] = str;
            }
        }
        return return_array;
    }

    private void update_change_label(int column_num, int row_num) {
        Label confirm = confirm_num.get(column_num).get(row_num);
        Label unknown = unknown_num.get(column_num).get(row_num);

        change_label = new Label[]{confirm, unknown};
    }

    private void clean_sudoku() {
        for (int i=0; i<9; i++) {
            for (int j=0; j<9; j++) {
                confirm_num.get(i).get(j).setText("");
                unknown_num.get(i).get(j).setText("");
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

class WinMainLauncher {
    public static void main(String[] args) {
        WinMain.main(args);
    }
}
