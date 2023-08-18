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
    // 全局工具
    private final FileChooser fileChooser = new FileChooser();

    public IOTool() {
        this.fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Sudoku Source File (*.sudoku)", "*.sudoku"));
    }

    public void input(List<List<Label>> confirm_list, List<List<Label>> unknown_list) {
        try {
            File file = this.fileChooser.showOpenDialog(new Stage());

            if (!(file == null)) {
                Scanner sc = new Scanner(file);

                for (int i = 0; i < 9; i++) {
                    String[] temps = sc.nextLine().split("\0");
                    for (int j = 0; j < 9; j++) {
                        // 清空Label
                        confirm_list.get(i).get(j).setText("");
                        unknown_list.get(i).get(j).setText("");

                        // 写入Label
                        String temp = temps[j];
                        if (temp.length() == 1 && !temp.equals("0")) confirm_list.get(i).get(j).setText(temp);
                        else if (!temp.equals("0")) unknown_list.get(i).get(j).setText(temp);
                    }
                }
            }
        } catch (Exception e) {
            WinTool.CreateAlert(Alert.AlertType.ERROR, "错误", "文件损坏", "");
        }
    }

    public void output(List<List<Label>> confirm_list, List<List<Label>> unknown_list) {
        try {
            File file = this.fileChooser.showSaveDialog(new Stage());
            if (!(file == null)) {
                file.createNewFile();

                StringBuilder append_str = new StringBuilder();
                for (int i = 0; i < 9; i++) {
                    for (int j = 0; j < 9; j++) {
                        String confirm_get = confirm_list.get(i).get(j).getText();
                        String unknown_get = unknown_list.get(i).get(j).getText().replace("\n", "");

                        if (confirm_get.equals("") && unknown_get.equals("")) append_str.append("0");
                        else append_str.append(confirm_get).append(unknown_get);
                        if (j != 8) append_str.append("\0");
                    }
                    if (i != 8) append_str.append("\n");
                }

                FileWriter fileWriter = new FileWriter(file);
                fileWriter.append(append_str);
                fileWriter.close();
            }
        } catch (Exception e) {
            WinTool.CreateAlert(Alert.AlertType.ERROR, "错误", "写入错误", "");
        }
    }
}
