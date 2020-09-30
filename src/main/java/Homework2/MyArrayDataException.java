package Homework2;

public class MyArrayDataException extends RuntimeException {
    public MyArrayDataException(int col, int row) {
        super(String.format("Некорректные данные в массиве в строке номер %d ячейке номер %d", row + 1, col + 1));
    }
}
