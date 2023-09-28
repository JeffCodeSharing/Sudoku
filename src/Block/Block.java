package Block;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Block {
    private final List<Byte> data = new ArrayList<>();

    public Block(String s) {
        for (int i=0; i<s.length(); i++) {
            char temp = s.charAt(i);
            data.add((byte) (temp - 48));
        }
    }

    public Block() {
        byte[] init_data = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9};
        for (byte temp:init_data) {
            data.add(temp);
        }
    }

    public void out() {
        System.out.println(data);
    }
}
