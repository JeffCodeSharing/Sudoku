package Crack;

import Block.Block;

public class Operation {
    private final Block[][] numbers;

    public Operation(Block[][] numbers) {
        this.numbers = numbers;
    }

    public boolean run() {
        boolean is_change = true;
        boolean is_done = false;
        while (is_change && !is_done) {
            // 刷新
            is_change = update_unknown_num();

            /*
              第一种推理方式
              本方式共分3个步骤，是唯一可能性的方式
              分别判断：行、列、宫
            */
            // 因为在刷新处已经进行了update操作，不用再进行update操作

            // 判断行
            for (int i=0; i<9; i++) {
                for (char check='1'; check <= '9'; check++) {
                    boolean is_first = false, is_second = false;
                    char only_num = 0;
                    int index = 0;
                    for (int k=0; k < 9; k++) {
                        if (numbers[i][k].contain(check)) {
                            if (!is_first) {
                                is_first = true;
                                only_num = check;
                                index = k;
                            } else {
                                is_second = true;
                                break;
                            }
                        }
                    }

                    // 写入
                    if (is_first && !is_second) {
                        is_change = true;
                        numbers[i][index].writeConfirm(only_num);
                    }
                }
            }

            // 判断列
            update_unknown_num();
            for (int i=0; i<9; i++) {
                // 判断
                for (char check='1'; check <= '9'; check++) {
                    boolean is_first = false, is_second = false;
                    char only_num = 0;
                    int index = 0;
                    for (int k=0; k < 9; k++) {
                        if (numbers[k][i].contain(check)) {
                            if (!is_first) {
                                is_first = true;
                                only_num = check;
                                index = k;
                            } else {
                                is_second = true;
                                break;
                            }
                        }
                    }

                    // 写入
                    if (is_first && !is_second) {
                        is_change = true;
                        numbers[index][i].writeConfirm(only_num);
                    }
                }
            }

            // 判断宫
            update_unknown_num();
            for (int i=0; i<=6; i+=3) {
                for (int j=0; j<=6; j+=3) {
                    // 判断
                    boolean is_first = false, is_second = false;
                    char only_num = 0;
                    int[] cell_type = new int[2];
                    for (char check='1'; check <= '9'; check++) {
                        for (int l=0; l<3; l++) {
                            for (int c=0; c<3; c++) {
                                if (numbers[i+l][j+c].contain(check)) {
                                    if (!is_first) {
                                        is_first = true;
                                        only_num = check;
                                        cell_type[0] = i+l;
                                        cell_type[1] = j+c;
                                    } else is_second = true;
                                }
                            }
                        }

                        // 写入
                        if (is_first && !is_second) {
                            numbers[cell_type[0]][cell_type[1]].writeConfirm(only_num);
                            is_change = true;
                        }
                    }
                }
            }

            // 第二种判断方式
            // 检查宫中的行
            update_unknown_num();
            for (int i=0; i<=6; i+=3) {
                for (int j=0; j<=6; j+=3) {
                    Block[] blocks = new Block[9];
                    for (int k=0; k<3; k++) {
                        System.arraycopy(numbers[i + k], j, blocks, k * 3, 3);
                    }

                    // 开始检查
                    for (char flag='1'; flag<='9'; flag++) {
                        for (int line1=0; line1<3; line1++) {
                            boolean is_appear = false, can_revise = false;
                            final int line2 = (line1 + 1) % 3;
                            final int line_not_check = (line2 + 1) % 3;

                            for (int k=0; k<3; k++) {
                                if (blocks[line1 * 3 + k].contain(flag) ||
                                        blocks[line2 * 3 + k].contain(flag)) {
                                    is_appear = true;
                                    break;
                                }

                                if (blocks[line_not_check * 3 + k].contain(flag)) {
                                    can_revise = true;
                                }
                            }

                            if (!is_appear && can_revise) {
                                for (int k=0; k<9; k++) {
                                    if (k >= j && k < (j+3)) {
                                        continue;
                                    }

                                    // 更改
                                    Block change_block = numbers[i+line_not_check][k];
                                    is_change = change_block.replace(flag) || is_change;
                                }
                            }
                        }
                    }
                }
            }

            // 检查宫中的列
            update_unknown_num();
            for (int i=0; i<=6; i+=3) {
                for (int j=0; j<=6; j+=3) {
                    Block[] blocks = new Block[9];
                    for (int k=0; k<3; k++) {
                        for (int l=0; l<3; l++) {
                            blocks[l*3+k] = numbers[i+k][j+l];
                        }
                    }

                    // 开始检查
                    for (char flag='1'; flag<='9'; flag++) {
                        for (int line1=0; line1<3; line1++) {
                            boolean is_appear = false, can_revise = false;
                            int line2 = (line1+1) % 3;
                            int line_not_check = (line2+1) % 3;
                            for (int c=0; c<3; c++) {
                                if (blocks[line1 * 3 + c].contain(flag) ||
                                        blocks[line2 * 3 + c].contain(flag)) {
                                    is_appear = true;
                                    break;
                                }

                                if (blocks[line_not_check*3+c].contain(flag)) {
                                    can_revise = true;
                                }
                            }

                            // 开始写入
                            if (!is_appear && can_revise) {
                                for (int c=0; c<9; c++) {
                                    if (c >= i && c < (i+3)) {
                                        continue;
                                    }

                                    Block change_block = numbers[c][j+line_not_check];
                                    is_change = change_block.replace(flag) || is_change;
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
                    if (numbers[i][j].isUnknown()) {
                        is_done = false;
                        break;    // 判断提前结束
                    }
                }
            }
        }

        return is_done;
    }

    // 刷新函数，用于推断一个指定格中行列宫中剩余的唯一余数
    private boolean update_unknown_num() {
        boolean changed = false;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                for (int k = 0; k < 9; k++) {
                    changed = numbers[i][j].replace(numbers[i][k].getConfirm()) || changed;
                    changed = numbers[i][j].replace(numbers[k][j].getConfirm()) || changed;
                }

                for (int k = 0; k < 3; k++) {
                    for (int l = 0; l < 3; l++) {
                        // 其中的i-(i%3)之类的，是为了将基准点回溯到每一个宫的最左上角的点
                        char remove_char = numbers[i - (i % 3) + k][j - (j % 3) + l].getConfirm();
                        changed = numbers[i][j].replace(remove_char) || changed;
                    }
                }
            }
        }

        return changed;
    }
}
