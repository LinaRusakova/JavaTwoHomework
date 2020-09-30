package Homework2;

public class MyArraySizeException extends RuntimeException {

    private static final int SIZE_V = 4;
    private static final int SIZE_H = 4;

    public MyArraySizeException() {
        super(String.format("Размер массива не соответствует размерности %d x %d", SIZE_V, SIZE_H));
    }

}
