package Homework2;

public class MyArraySizeException extends RuntimeException {

    public MyArraySizeException() {
        super("Размер массива не соответствует размерности 4 x 4");
    }

}
