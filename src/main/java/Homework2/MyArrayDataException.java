package Homework2;

public class MyArrayDataException extends RuntimeException {
    public MyArrayDataException(int row, int col) {
        super(String.format("Некорректные данные в массиве в строке номер %d ячейке номер %d", row + 1, col + 1));
    }
}
