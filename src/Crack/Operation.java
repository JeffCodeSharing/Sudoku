package Crack;

import Block.Block;

public class Operation {
    private final Block[][] numbers;

    public Operation(Block[][] numbers) {
        this.numbers = numbers;
    }

    public boolean run() {
        for (int i=0; i<20; i++) {
            update_unknown_num();
        }


        return true;
    }

    // 刷新函数，用于推断一个指定格中行列宫中剩余的唯一余数
    void update_unknown_num() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                // 获取行列的已知数字
                for (int k = 0; k < 9; k++) {
                    numbers[i][j].replace(numbers[i][k].getConfirm());
                    numbers[i][j].replace(numbers[k][j].getConfirm());
                }

                // 获取宫的已知数字
                for (int k = 0; k < 3; k++) {
                    for (int l = 0; l < 3; l++) {
                        // 其中的i-(i%3)之类的，是为了将基准点回溯到每一个宫的最左上角的点
                        char remove_char = numbers[i - (i % 3) + k][j - (j % 3) + l].getConfirm();
                        numbers[i][j].replace(remove_char);
                    }
                }
            }
        }
    }
}
