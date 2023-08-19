import Tool.WinTool;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;

import java.util.List;

public class Operation {
    public void run(List<List<Label>> confirm_label, List<List<Label>> unknown_label,
                    String[][] confirm_list, String[][] unknown_list) {
        // 一开始的设置
        for (int i=0; i<9; i++) {
            for (int j=0; j<9 ;j++) {
                if (confirm_list[i][j].equals("")) {
                    unknown_list[i][j] = "123456789";
                }
            }
        }

        boolean is_change = true;
        boolean is_done = false;
        while (is_change && !is_done) {
            // 刷新
            is_change = false;

            // 第一种推理方式：整合，如果unknown_list中有项是只剩一个的，移交到confirm_list中
            update_unknown_num(confirm_list, unknown_list);
            for (int i=0; i<9; i++) {
                for (int j=0; j<9; j++) {
                    String unknown_str = unknown_list[i][j];
                    if (unknown_str.length() == 1) {
                        confirm_list[i][j] = unknown_str;
                        unknown_list[i][j] = "";
                        is_change = true;
                    }
                }
            }

            // 第二种推理方式
            /*
              本方式共分3个步骤，是唯一可能性的方式
              分别判断：行、列、宫
            */

            // 判断行
            update_unknown_num(confirm_list, unknown_list);
            for (int i=0; i<9; i++) {
                for (int j=1; j <= 9; j++) {
                    boolean is_first = false, is_second = false;
                    String only_num = "";
                    int k_num = 0;
                    for (int k=0; k < 9; k++) {
                        String type = String.valueOf(j);
                        if (unknown_list[i][k].contains(type)) {
                            if (!is_first) {
                                is_first = true;
                                only_num = type;
                                k_num = k;
                            } else is_second = true;
                        }
                    }

                    // 写入
                    if (is_first && !is_second) {
                        is_change = true;
                        unknown_list[i][k_num] = "";
                        confirm_list[i][k_num] = only_num;
                    }
                }
            }

            // 判断列
            update_unknown_num(confirm_list, unknown_list);
            for (int i=0; i<9; i++) {
                // 判断
                for (int j=1; j <= 9; j++) {
                    boolean is_first = false, is_second = false;
                    String only_num = "";
                    int k_num = 0;
                    for (int k=0; k < 9; k++) {
                        String type = String.valueOf(j);
                        if (unknown_list[k][i].contains(type)) {
                            if (!is_first) {
                                is_first = true;
                                only_num = type;
                                k_num = k;
                            } else is_second = true;
                        }
                    }

                    // 写入
                    if (is_first && !is_second) {
                        is_change = true;
                        unknown_list[k_num][i] = "";
                        confirm_list[k_num][i] = only_num;
                    }
                }
            }

            // 判断宫
            update_unknown_num(confirm_list, unknown_list);
            for (int i=0; i<=6; i+=3) {
                for (int j=0; j<=6; j+=3) {
                    // 判断
                    boolean is_first = false, is_second = false;
                    String only_num = "";
                    int[] cell_type = new int[2];
                    for (int k=1; k <= 9; k++) {
                        for (int l=0; l<3; l++) {
                            for (int c=0; c<3; c++) {
                                String num = String.valueOf(k);
                                if (unknown_list[i+l][j+c].contains(num)) {
                                    if (!is_first) {
                                        is_first = true;
                                        only_num = num;
                                        cell_type[0] = i+l;
                                        cell_type[1] = j+c;
                                    } else is_second = true;
                                }
                            }
                        }

                        // 写入
                        if (is_first && !is_second) {
                            unknown_list[cell_type[0]][cell_type[1]] = "";
                            confirm_list[cell_type[0]][cell_type[1]] = only_num;
                            is_change = true;
                        }
                    }
                }
            }

            // 第三种判断方式
            // 检查宫中的行
            update_unknown_num(confirm_list, unknown_list);
            for (int i=0; i<=6; i+=3) {
                for (int j=0; j<=6; j+=3) {
                    String[] strings = new String[9];
                    for (int k=0; k<3; k++) {
                        System.arraycopy(unknown_list[i + k], j, strings, k * 3, 3);
                    }

                    // 开始检查
                    for (int s=1; s<=9; s++) {
                        String s_str = String.valueOf(s);
                        for (int line1=0; line1<3; line1++) {
                            boolean is_appear = false;
                            boolean can_revise = false;
                            final int line2 = (line1 + 1) % 3;
                            final int line_unchecked = (line2 + 1) % 3;

                            for (int k=0; k<3; k++) {
                                if (strings[line1 * 3 + k].contains(s_str) ||
                                strings[line2 * 3 + k].contains(s_str)) {
                                    is_appear = true;
                                    break;
                                }

                                if (strings[line_unchecked * 3 + k].contains(s_str)) {
                                    can_revise = true;
                                }
                            }

                            if (!is_appear && can_revise) {
                                for (int k=0; k<9; k++) {
                                    if (k >= j && k < (j+3)) {
                                        continue;
                                    }

                                    // 更改
                                    String temp = unknown_list[i+line_unchecked][k];
                                    if (temp.contains(s_str)) is_change = true;
                                    temp = temp.replace(s_str, "");
                                    unknown_list[i+line_unchecked][k] = temp;
                                }
                            }
                        }
                    }
                }
            }

            // 检查宫中的列
            // todo 算法有问题
            update_unknown_num(confirm_list, unknown_list);
            for (int i=0; i<=6; i+=3) {
                for (int j=0; j<=6; j+=3) {
                    String[] strings = new String[9];
                    for (int k=0; k<3; k++) {
                        for (int l=0; l<3; l++) {
                            strings[l*3+k] = unknown_list[i+k][j+l];
                        }
                    }

                    // 开始检查
                    for (int k=1; k<=9; k++) {
                        String k_str = String.valueOf(k);
                        for (int line1=0; line1<3; line1++) {
                            boolean is_appear = false;
                            boolean can_revise = false;
                            int line2 = (line1+1) % 3;
                            int line_unchecked = (line2+2) % 3;
                            for (int c=0; c<3; c++) {
                                if (strings[line1 * 3 + c].contains(k_str) ||
                                        strings[line2 * 3 + c].contains(k_str)) {
                                    is_appear = true;
                                    break;
                                }

                                if (strings[line_unchecked*3+c].contains(k_str)) {
                                    can_revise = true;
                                }
                            }

                            // 开始写入
                            if (!is_appear && can_revise) {
                                for (int c=0; c<9; c++) {
                                    if (c >= i && c < (i+3)) {
                                        continue;
                                    }

                                    String str = unknown_list[c][i+line_unchecked];
                                    if (str.contains(k_str)) is_change = true;

                                    str = str.replace(k_str, "");
                                    unknown_list[c][i+line_unchecked] = str;
                                }
                            }
                        }
                    }
                }
            }

            // 判断是否完成
            is_done = true;
            for (int i=0; i < 9; i++) {
                for (int j=0; j < 9; j++) {
                    if (confirm_list[i][j].equals("")) {
                        is_done = false;
                        break;    // 判断提前结束
                    }
                }
            }
        }
        end(confirm_label, unknown_label, confirm_list, unknown_list, is_done);
    }

    // 刷新函数，用于推断一个指定格中行列宫中剩余的唯一余数
    void update_unknown_num(String[][] confirm_list, String[][] unknown_list) {
        for (int i=0; i<9; i++) {
            for (int j=0; j<9 ;j++) {
                if (confirm_list[i][j].equals("")) {
                    String str_unknown = unknown_list[i][j];
                    // 获取行列的已知数字
                    for (int k=0; k < 9; k++) {
                        str_unknown = str_unknown.replace(confirm_list[i][k], "");
                        str_unknown = str_unknown.replace(confirm_list[k][j], "");
                    }

                    // 获取宫的已知数字
                    for (int k=0; k < 3; k++) {
                        for (int l=0; l < 3; l++) {
                            // 其中的i-(i%3)之类的，是为了将基准点回溯到每一个宫的最左上角的点
                            String str = confirm_list[i-(i % 3)+k][j-(j % 3)+l];
                            str_unknown = str_unknown.replace(str, "");
                        }
                    }
                    unknown_list[i][j] = str_unknown;
                }
            }
        }
    }

    void end(List<List<Label>> confirm_label, List<List<Label>> unknown_label, String[][] confirm, String[][] unknown, boolean type) {
        for (int i=0; i<9; i++) {
            for (int j=0; j<9; j++) {
                String str = confirm[i][j];
                if (str.length()>5) {
                    str = str.substring(0, 5) + "\n" + str.substring(5);
                }
                confirm_label.get(i).get(j).setText(str);

                String str2 = unknown[i][j];
                if (str2.length()>5) {
                    str2 = str2.substring(0, 5) + "\n" + str2.substring(5);
                }
                unknown_label.get(i).get(j).setText(str2);
            }
        }

        // Alert提醒
        if (type) {
            WinTool.CreateAlert(Alert.AlertType.INFORMATION, "完成", "程序已经完成了！", "要不再让来试一次");
        } else {
            WinTool.CreateAlert(Alert.AlertType.INFORMATION, "程序已无法推理", "OOO, 我们已经尽力了", "祝你好运");
        }
    }
}
