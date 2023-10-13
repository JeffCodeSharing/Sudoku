import Block.Block;
import Generate.Generator;
import Tool.IOTool;
import Tool.WinTool;
import Crack.Operation;
import Update.CheckUpdate;
import Update.Downloader;
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
        // 检查更新
        // todo 添加一个提示
        if (CheckUpdate.check()) {
            // 更新并检查是否更新成功
            if (Downloader.download()) {
                WinTool.CreateAlert(Alert.AlertType.INFORMATION, "更新成功", "更新成功", "");
            } else {
                WinTool.CreateAlert(Alert.AlertType.ERROR, "更新失败", "更新失败了，请重新尝试", "");
            }
        }
        
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

        Menu generate = new Menu("生成数独");
        MenuItem level1 = new MenuItem("低级");
        MenuItem level2 = new MenuItem("中级");
        MenuItem level3 = new MenuItem("高级");

        level1.setOnAction(actionEvent -> {
            Generator generator = new Generator();
            generator.generate(20);
            generator.displayBoard(confirm_num, unknown_num);
        });
        level2.setOnAction(actionEvent -> {
            Generator generator = new Generator();
            generator.generate(40);
            generator.displayBoard(confirm_num, unknown_num);
        });
        level3.setOnAction(actionEvent -> {
            Generator generator = new Generator();
            generator.generate(50);
            generator.displayBoard(confirm_num, unknown_num);
        });

        generate.getItems().addAll(level1, level2, level3);
        
        menuBar.getMenus().addAll(fileMenu, generate);
        group.getChildren().add(menuBar);

        // 清空按钮
        Button button_clear = WinTool.CreateButton(530, 380, 110, 50, 20, "清空数独");
        button_clear.setOnAction(actionEvent -> clean_sudoku());
        group.getChildren().add(button_clear);

        // 自动运算按钮
        Button button_run = WinTool.CreateButton(530, 450, 110, 50, 20, "自动运算");
        button_run.setOnAction(actionEvent -> {
            Block[][] blocks = get_blocks();
            boolean done = new Operation(blocks).run();

            // 结束后数据的刷新
            reload_numbers(blocks);

            // 弹出弹窗
            end(done);
        });
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

                unknown_label.setWrapText(true);

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
            String unknown_num = change_label[1].getText();    // 清除换行符

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

                change_label[1].setText(new String(numChars));
            } else {      // 是确定状态
                change_label[1].setText("");
                change_label[0].setText(add_num);
            }
        } catch (Exception ignored) {}
    }

    private Block[][] get_blocks() {
        Block[][] array = new Block[9][9];
        for (int i=0; i < 9; i++) {
            for (int j=0; j < 9; j++) {
                String confirm_str = confirm_num.get(i).get(j).getText();
                String unknown_str = unknown_num.get(i).get(j).getText();
                if (confirm_str == null) {
                    confirm_str = "";
                }
                if (unknown_str == null) {
                    unknown_str = "";
                }

                String final_str = confirm_str + unknown_str;
                if (final_str.equals("")) {
                    array[i][j] = new Block();
                } else {
                    array[i][j] = new Block(final_str);
                }
            }
        }
        return array;
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

    private void reload_numbers(Block[][] blocks) {   // 在完成自动运算之后对Label的覆盖
        for (int i=0; i<9; i++) {
            for (int j=0; j<9; j++) {
                // 清空数据
                confirm_num.get(i).get(j).setText("");
                unknown_num.get(i).get(j).setText("");

                // 写入数据
                String text = blocks[i][j].getData();
                if (text.length() == 1) {
                    confirm_num.get(i).get(j).setText(text);
                } else {
                    unknown_num.get(i).get(j).setText(text);
                }
            }
        }
    }

    void end(boolean type) {
        // Alert提醒
        if (type) {
            WinTool.CreateAlert(Alert.AlertType.INFORMATION, "完成", "程序已经完成了！", "要不再让来试一次");
        } else {
            WinTool.CreateAlert(Alert.AlertType.INFORMATION, "程序已无法推理", "OOO, 我们已经尽力了", "祝你好运");
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
