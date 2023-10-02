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
