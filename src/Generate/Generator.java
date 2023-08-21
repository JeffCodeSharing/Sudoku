package Generate;

import javafx.scene.control.Label;

import java.util.List;
import java.util.Random;

public class Generator {
    private final int[][] board;
    private final int SIZE = 9;
    private final int EMPTY = 0;

    public Generator() {
        board = new int[SIZE][SIZE];
    }

    public void generate() {
        fillBoard();
        removeCells();
    }

    private void fillBoard() {
        fillDiagonalBoxes();
        fillRemaining(0, 3);
    }

    private void fillDiagonalBoxes() {
        for (int i = 0; i < SIZE; i = i + 3) {
            fillBox(i, i);
        }
    }

    private void fillBox(int row, int col) {
        int num;
        Random random = new Random();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                do {
                    num = random.nextInt(SIZE) + 1;
                } while (!isValid(row, col, num));
                board[row + i][col + j] = num;
            }
        }
    }

    private boolean isValid(int row, int col, int num) {
        for (int i = 0; i < SIZE; i++) {
            if (board[row][i] == num || board[i][col] == num)
                return false;
        }

        int boxStartRow = row - row % 3;
        int boxStartCol = col - col % 3;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[boxStartRow + i][boxStartCol + j] == num)
                    return false;
            }
        }

        return true;
    }

    private boolean fillRemaining(int row, int col) {
        if (col >= SIZE && row < SIZE - 1) {
            row = row + 1;
            col = 0;
        }
        if (row >= SIZE && col >= SIZE)
            return true;

        if (row < 3) {
            if (col < 3)
                col = 3;
        } else if (row < SIZE - 3) {
            if (col == (row / 3) * 3)
                col = col + 3;
        } else {
            if (col == SIZE - 3) {
                row = row + 1;
                col = 0;
                if (row >= SIZE)
                    return true;
            }
        }

        for (int num = 1; num <= SIZE; num++) {
            if (isValid(row, col, num)) {
                board[row][col] = num;
                if (fillRemaining(row, col + 1))
                    return true;
                board[row][col] = EMPTY;
            }
        }
        return false;
    }

    private void removeCells() {
        Random random = new Random();

        int i = random.nextInt(11) + 30;
        while (i > 0) {
            int row = random.nextInt(SIZE);
            int col = random.nextInt(SIZE);

            if (board[row][col] != EMPTY) {
                board[row][col] = EMPTY;
                i--;
            }
        }
    }

    public void displayBoard(List<List<Label>> confirm, List<List<Label>> unknown) {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                // clean the list
                confirm.get(i).get(j).setText("");
                unknown.get(i).get(j).setText("");

                if (board[i][j] != 0) {
                    confirm.get(i).get(j).setText(String.valueOf(board[i][j]));
                }
            }
        }
    }
}
