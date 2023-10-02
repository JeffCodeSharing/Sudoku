package Block;

import java.util.ArrayList;
import java.util.List;

public class Block {
    private final List<Character> data = new ArrayList<>();

    public Block(String s) {
        for (char ch:s.toCharArray()) {
            data.add(ch);
        }
    }

    public Block() {
        char[] init_data = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9'};
        for (char temp : init_data) {
            data.add(temp);
        }
    }

    public boolean isConfirm() {
        return data.size() == 1;
    }

    public boolean isUnknown() {
        return !isConfirm();
    }

    public boolean replace(Character replace_char) {
        if (isUnknown()) {      // 如果本格已经是唯一数了，就不接受任何数字的剔除了
            return data.remove(replace_char);
        }
        return false;
    }

    public boolean contain(Character check_char) {
        // 如果存储的数据是Unknown状态的，才比较
        if (isUnknown()) {
            return data.contains(check_char);
        }
        return false;
    }

    public void writeConfirm(char write_num) {
        data.clear();
        data.add(write_num);
    }

    public char getConfirm() {
        if (isConfirm()) {
            return data.get(0);
        }
        return 0;    // 返回char 0，即没有 -> 在使用remove时会自动跳过
    }

    public String getData() {
        StringBuilder text = new StringBuilder();
        for (char temp:data) {
            text.append(temp);
        }
        return text.toString();
    }
}
