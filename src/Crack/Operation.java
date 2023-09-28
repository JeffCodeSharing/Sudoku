package Crack;

import Block.Block;

public class Operation {
    public boolean run(Block[][] num_list) {
        for (Block[] temps:num_list) {
            for (Block temp:temps) {
                temp.out();
            }
        }
        return false;
    }
}
