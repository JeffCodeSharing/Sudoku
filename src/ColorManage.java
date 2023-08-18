import Tool.WinTool;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.util.Optional;
import java.util.Scanner;

public class ColorManage extends Application {
    private final WinTool tools = new WinTool();
    private boolean is_save = false;

    @Override
    public void start(Stage stage) {
        Group group = new Group();
        Scene scene = new Scene(group);
        
        // 标题
        group.getChildren().addAll(
                tools.CreateLabel(0, 0, 80, 25, 20, Color.BLACK, "颜色设置", false),
                tools.CreateLabel(0, 40, 120, 20, 16, Color.BLACK, "数独线条-宫格", false),
                tools.CreateLabel(0, 100, 120, 20, 16, Color.BLACK, "数独线条-普通", false),
                tools.CreateLabel(0, 150, 90, 20, 16, Color.BLACK, "菜单颜色", false));

        // 列表
        String[] strings = getStrings();
        ComboBox<String> comboBox1 = tools.CreateComboBox(10, 70, 150, 25, 5, strings);
        ComboBox<String> comboBox2 = tools.CreateComboBox(10, 120, 150, 25, 5, strings);
        ComboBox<String> comboBox3 = tools.CreateComboBox(10, 170, 150, 25, 5, strings);
        group.getChildren().addAll(comboBox1, comboBox2, comboBox3);

        // 确定按钮
        Button confirm_button = tools.CreateButton(160, 300, 60, 30, 14, "确定");
        confirm_button.setOnAction(actionEvent -> setColor(comboBox1, comboBox2, comboBox3));
        group.getChildren().add(confirm_button);

        stage.setTitle("颜色设置");
        stage.setScene(scene);
        stage.setWidth(260);
        stage.setHeight(400);
        stage.setResizable(false);
        stage.setOnCloseRequest(windowEvent -> windowOnClose(comboBox1, comboBox2, comboBox3));
        stage.showAndWait();
    }

    @SafeVarargs
    final void windowOnClose(ComboBox<String>... args) {
        // 读取文件
        String[] system_data = read_systemFile();

        if (!is_save) {
            Optional<ButtonType> type = tools.CreateAlert(Alert.AlertType.CONFIRMATION, "更改尚未保存", "更改尚未保存", "是否保存？");
            if (type.get().getButtonData().equals(ButtonBar.ButtonData.OK_DONE)) {
                try {
                    for (int i=0; i<3; i++) {
                        String str = args[i].getValue();
                        if (!(str == null)) {
                            if (!str.equals("")) {
                                system_data[i] = str;
                            }
                        }
                    }
                    write_file(system_data);
                } catch (Exception e) {
                    tools.CreateAlert(Alert.AlertType.ERROR, "错误", "写入文件错误", "");
                }
            }
        }
    }

    @SafeVarargs
    final void setColor(ComboBox<String>... boxes) {
        // 读取文件
        String[] system_data = read_systemFile();

        // 读取ComboBox列表的数据并覆盖
        String[] args_data = {boxes[0].getValue(), boxes[1].getValue(), boxes[2].getValue()};
        for (int i=0; i < 3; i++) {
            String str = args_data[i];
            if (!(str == null)) {
                if (!str.equals("")) {
                    system_data[i] = args_data[i];
                }
            }
        }

        // 写入
        try {
            write_file(system_data);
        } catch (Exception e) {
            tools.CreateAlert(Alert.AlertType.INFORMATION, "错误", "文件无法写入", "");
        }
    }

    void write_file(String... args) throws Exception {
        FileWriter fileWriter = new FileWriter(System.getProperty("user.dir") + "/data");
        for (String temp:args) {
            fileWriter.append(temp).append("\n");
        }
        fileWriter.close();

        // 完成
        tools.CreateAlert(Alert.AlertType.INFORMATION, "成功", "已成功更改", "重启后生效");
        is_save = true;
    }

    String[] getStrings() {
        return new String[]{
                "BLACK", "BLUE", "BROWN", "CHOCOLATE", "CORAL", "CYAN", "DARKBLUE",
                "DARKCYAN", "DARKGREEN", "DARKGREY", "DARKORANGE", "DARKRED",
                "DEEPPINK", "DEEPSKYBLUE", "GOLD", "GREEN", "GREY", "LIGHTBLUE", "LIGHTCORAL", "LIGHTCYAN",
                "LIGHTGREEN", "LIGHTGREY", "LIGHTPINK", "LIGHTSKYBLUE", "LIGHTYELLOW",
                "ORANGE", "ORANGERED", "PINK", "PURPLE", "RED", "SEAGREEN",
                "SILVER", "SKYBLUE", "SPRINGGREEN", "WHITE", "YELLOW", "YELLOWGREEN"
        };
    }

    String[] read_systemFile() {
        String str1 = null, str2 = null, str3 = null;
        try {
            Scanner sc = new Scanner(new File(System.getProperty("user.dir") + "/data"));
            str1 = sc.nextLine();
            str2 = sc.nextLine();
            str3 = sc.nextLine();
        } catch (Exception e) {
            tools.CreateAlert(Alert.AlertType.ERROR, "失败", "系统文件损坏", "");
        }
        return new String[]{str1, str2, str3};
    }

    public Paint getColor(String color_type) {
        Paint color = null;
        switch (color_type) {
            case "BLACK" -> color = Color.BLACK;
            case "BLUE" -> color = Color.BLUE;
            case "BROWN" -> color = Color.BROWN;
            case "CHOCOLATE" -> color = Color.CHOCOLATE;
            case "CORAL" -> color = Color.CORAL;
            case "CYAN" -> color = Color.CYAN;
            case "DARKBLUE" -> color = Color.DARKBLUE;
            case "DARKCYAN" -> color = Color.DARKCYAN;
            case "DARKGREEN" -> color = Color.DARKGREEN;
            case "DARKGREY" -> color = Color.DARKGREY;
            case "DARKORANGE" -> color = Color.DARKORANGE;
            case "DARKRED" -> color = Color.DARKRED;
            case "DEEPPINK" -> color = Color.DEEPPINK;
            case "DEEPSKYBLUE" -> color = Color.DEEPSKYBLUE;
            case "GOLD" -> color = Color.GOLD;
            case "GREEN" -> color = Color.GREEN;
            case "GREY" -> color = Color.GREY;
            case "LIGHTBLUE" -> color = Color.LIGHTBLUE;
            case "LIGHTCORAL" -> color = Color.LIGHTCORAL;
            case "LIGHTCYAN" -> color = Color.LIGHTCYAN;
            case "LIGHTGREEN" -> color = Color.LIGHTGREEN;
            case "LIGHTGREY" -> color = Color.LIGHTGREY;
            case "LIGHTPINK" -> color = Color.LIGHTPINK;
            case "LIGHTSKYBLUE" -> color = Color.LIGHTSKYBLUE;
            case "LIGHTYELLOW" -> color = Color.LIGHTYELLOW;
            case "ORANGE" -> color = Color.ORANGE;
            case "ORANGERED" -> color = Color.ORANGERED;
            case "PINK" -> color = Color.PINK;
            case "PURPLE" -> color = Color.PURPLE;
            case "RED" -> color = Color.RED;
            case "SEAGREEN" -> color = Color.SEAGREEN;
            case "SILVER" -> color = Color.SILVER;
            case "SKYBLUE" -> color = Color.SKYBLUE;
            case "SPRINGGREEN" -> color = Color.SPRINGGREEN;
            case "WHITE" -> color = Color.WHITE;
            case "YELLOW" -> color = Color.YELLOW;
            case "YELLOWGREEN" -> color = Color.YELLOWGREEN;
        }
        return color;
    }
}
