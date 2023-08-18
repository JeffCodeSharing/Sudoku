import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class WinMain extends Application {
    // 使用工具
    private final Tools tools = new Tools();
    private final ColorManage colorManage = new ColorManage();

    // 通用函数
    private final String[] system_data = read_systemFile();
    private final List<List<Label>> confirm_num = new ArrayList<>();
    private final List<List<Label>> unknown_num = new ArrayList<>();
    private final Label[] change_label = new Label[2];
    
    @Override
    public void start(Stage stage) {
        Group group = new Group();
        Scene scene = new Scene(group);

        // 绘制线条
        for (int i = 0; i <= 9; i++) {
            int type = (i % 3 == 0) ? 0 : 1;
            Paint fill = colorManage.getColor(system_data[type]);
            group.getChildren().addAll(
                    tools.CreateLine(50*(i+1), 50, 50*(i+1), 500, fill),
                    tools.CreateLine(50, 50*(i+1), 500, 50*(i+1), fill)
            );
        }

        // 批注多选框
        CheckBox checkBox = tools.CreateCheckBox(500, 500, 100, 50, 20, "批注");
        checkBox.setOnKeyPressed(keyEvent -> keyBoardListener(keyEvent.getCode().getName(), checkBox.isSelected()));
        group.getChildren().add(checkBox);

        // 菜单
        MenuBar menuBar = new MenuBar();
        menuBar.setBackground(Background.fill(colorManage.getColor(system_data[2])));
        menuBar.setLayoutX(0);
        menuBar.setLayoutY(0);
        menuBar.setMinWidth(700);
        menuBar.setMaxWidth(700);
        menuBar.setMinHeight(25);
        menuBar.setMaxHeight(25);

        Menu fileMenu = new Menu("文件");
        MenuItem input = new MenuItem("导入");
        input.setOnAction(actionEvent -> new IOControl().input(confirm_num, unknown_num));
        MenuItem output = new MenuItem("导出");
        output.setOnAction(actionEvent -> new IOControl().output(confirm_num, unknown_num));
        fileMenu.getItems().addAll(input, output);

        Menu setMenu = new Menu("设置");
        MenuItem color_settings = new MenuItem("颜色设置");
        color_settings.setOnAction(actionEvent -> new ColorManage().start(new Stage()));
        setMenu.getItems().addAll(color_settings);
        
        menuBar.getMenus().addAll(fileMenu, setMenu);
        group.getChildren().add(menuBar);

        // 清空按钮
        Button button_clear = tools.CreateButton(530, 380, 110, 50, 20, "清空数独");
        button_clear.setOnAction(actionEvent -> clean_sudoku());
        button_clear.setOnKeyPressed(keyEvent -> keyBoardListener(keyEvent.getCode().getName(), checkBox.isSelected()));
        group.getChildren().add(button_clear);

        // 自动运算按钮
        Button button_run = tools.CreateButton(530, 450, 110, 50, 20, "自动运算");
        button_run.setOnAction(actionEvent -> new Operation().run(confirm_num, unknown_num, get_labelText(confirm_num), get_labelText(unknown_num)));
        button_run.setOnKeyPressed(keyEvent -> keyBoardListener(keyEvent.getCode().getName(), checkBox.isSelected()));
        group.getChildren().add(button_run);

        // 按钮数字
        for (int i=1; i <= 9; i++) {
            final int j = i;
            Button button = tools.CreateButton(50*i, 510, 40, 40, 18, String.valueOf(i));
            button.setOnAction(actionEvent -> label_add_num(String.valueOf(j), checkBox.isSelected()));
            button.setOnKeyPressed(keyEvent -> keyBoardListener(keyEvent.getCode().getName(), checkBox.isSelected()));
            group.getChildren().add(button);
        }

        // 线条格子中的Label控件
        for (int i = 1; i <= 9; i++) {
            List<Label> list_confirm = new ArrayList<>();
            List<Label> list_unknown = new ArrayList<>();
            for (int j = 1; j <= 9; j++) {
                Label label1 = tools.CreateLabel(j*50, i*50, 50, 50, 25, Color.BLACK, "", true);
                Label label2 = tools.CreateLabel(j*50, i*50, 50, 50, 16, Color.BLUE, "", true);
                // 用于检测按键按下并执行
                final int column_final = i - 1;
                final int row_final = j - 1;
                label2.setOnMousePressed(mouseEvent -> {
                    Label confirm = confirm_num.get(column_final).get(row_final);
                    Label unknown = unknown_num.get(column_final).get(row_final);

                    System.arraycopy(new Label[]{confirm, unknown}, 0, change_label, 0, 2);
                });

                group.getChildren().addAll(label1, label2);
                list_confirm.add(label1);
                list_unknown.add(label2);
            }
            confirm_num.add(list_confirm);
            unknown_num.add(list_unknown);
        }
        
        stage.setTitle("数独");
        stage.setScene(scene);
        stage.setWidth(700);
        stage.setHeight(600);
        stage.setResizable(false);
        stage.setOnCloseRequest(windowEvent -> System.exit(0));
        stage.show();
    }

    void label_add_num(String add_num, boolean is_unknown) {
        try {
            String unknown_num = change_label[1].getText();
            if (is_unknown) {
                change_label[0].setText("");

                String add_str = unknown_num.contains(add_num) ?
                        unknown_num.replace(add_num, "") : unknown_num + add_num;
                add_str = add_str.replace("\n", "");

                // 排序
                String[] str = add_str.split("");
                int[] num = new int[str.length];
                for (int i=0; i < num.length; i++) {
                    num[i] = Integer.parseInt(str[i]);
                }
                Arrays.sort(num);

                StringBuilder stringBuilder = new StringBuilder();
                for (int i:num) stringBuilder.append(i);
                add_str = stringBuilder.toString();

                if (add_str.length() > 5) add_str = add_str.substring(0, 5) + "\n" + add_str.substring(5);
                change_label[1].setText(add_str);
            } else {
                change_label[1].setText("");
                change_label[0].setText(add_num);
            }
        } catch (Exception ignored) {}
    }
    
    String[] read_systemFile() {
        String str1 = "DARKBLUE";
        String str2 = "DARKGRAY";
        String str3 = "LIGHTCYAN";
        try {
            Scanner sc = new Scanner(new File(System.getProperty("user.dir") + "/data"));
            str1 = sc.nextLine();
            str2 = sc.nextLine();
            str3 = sc.nextLine();
        } catch (Exception e) {
            tools.CreateAlert(Alert.AlertType.ERROR, "错误", "无法读取系统文件", "");
            System.exit(1);
        }
        return new String[]{str1, str2, str3};
    }

    String[][] get_labelText(List<List<Label>> label_list) {
        String[][] list = new String[9][9];
        for (int i=0; i < 9; i++) {
            for (int j=0; j < 9; j++) {
                String str = label_list.get(i).get(j).getText();
                if (str == null) str = "";
                str = str.replace("\n", "");
                list[i][j] = str;
            }
        }
        return list;
    }

    void clean_sudoku() {
        for (int i=0; i<9; i++) {
            for (int j=0; j<9; j++) {
                confirm_num.get(i).get(j).setText("");
                unknown_num.get(i).get(j).setText("");
            }
        }
    }

    void keyBoardListener(String type, boolean is_unknown) {
        switch (type) {
            case "1", "2", "3", "4", "5", "6", "7", "8", "9" -> label_add_num(type, is_unknown);
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
