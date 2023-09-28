package Block;

public class Block {
    private final byte[] data = new byte[9];

    public Block(String s) {
        for (int i=0; i<s.length(); i++) {
            char temp = s.charAt(i);
            data[i] = (byte) (temp - 48);
        }
    }

    public Block() {
        byte[] init_data = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9};
        System.arraycopy(init_data, 0, data, 0, 9);
    }


}
