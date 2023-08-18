package Tool;

import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.util.List;
import java.util.Scanner;

public class IOTool {
    public static void input(List<List<Label>> confirm_list, List<List<Label>> unknown_list) {
        // initialize FileChooser
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Sudoku Source File (*.sudoku)", "*.sudoku"));
        
        try {
            File file = chooser.showOpenDialog(new Stage());

            if (!(file == null)) {
                Scanner sc = new Scanner(file);

                for (int i = 0; i < 9; i++) {
                    String[] temps = sc.nextLine().split("\0");
                    for (int j = 0; j < 9; j++) {
                        // 清空Label
                        confirm_list.get(i).get(j).setText("");
                        unknown_list.get(i).get(j).setText("");

                        // 写入Label，分unknown、confirm、未填写进行写入
                        String temp = temps[j];
                        if (temp.length() == 1 && !temp.equals("0")) {
                            confirm_list.get(i).get(j).setText(temp);
                        } else if (!temp.equals("0")) {
                            unknown_list.get(i).get(j).setText(temp);
                        }
                    }
                }
            }
        } catch (Exception e) {
            WinTool.CreateAlert(Alert.AlertType.ERROR, "错误", "文件损坏", "");
        }
    }

    public static void output(List<List<Label>> confirm_list, List<List<Label>> unknown_list) {
        try {
            // initialize FileChooser
            FileChooser chooser = new FileChooser();
            chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Sudoku Source File (*.sudoku)", "*.sudoku"));

            File file = chooser.showSaveDialog(new Stage());
            if (!(file == null)) {
                file.createNewFile();

                FileWriter writer = new FileWriter(file);
                for (int i = 0; i < 9; i++) {
                    for (int j = 0; j < 9; j++) {
                        String confirm_str = confirm_list.get(i).get(j).getText();
                        String unknown_str = unknown_list.get(i).get(j).getText().replace("\n", "");

                        if (confirm_str.equals("") && unknown_str.equals("")) {
                            writer.append("0");
                        } else {
                            // 因为confirm_Str和unknown_str只有一个可能有数据，所以并排存储
                            writer.append(confirm_str).append(unknown_str);
                        }

                        writer.append("\0");
                    }

                    writer.append("\n");
                }

                writer.close();
            }
        } catch (Exception e) {
            WinTool.CreateAlert(Alert.AlertType.ERROR, "错误", "写入错误", "");
        }
    }
}
