package Homework2;

public class MainClass {

    public static void main(String[] args) {

        String[][] arrayCorrect = new String[][]{
                {"1", "2", "3", "4"},
                {"5", "6", "7", "8"},
                {"9", "10", "11", "12"},
                {"13", "14", "15", "16"}
        };

        String[][] arrayCorrectSize = new String[][]{
                {"1", "2", "3", "4"},
                {"5", "6", "7", "8"},
                {"9", "10", "Hi", "12"},
                {"13", "14", "15", "16"}
        };

        String[][] arrayTotallyIncorrect = new String[][]{
                {"1", "2", "3", "4"},
                {"5", "6", "7", "8"},
                {"9", "10", "11", "12"},
                {"13", "14", "15", "16"},
                {"44", "34", "234", "12", "5"}
        };

        try {
            System.out.println(sumArrayFourByFour(arrayCorrect));
        } catch (MyArraySizeException | MyArrayDataException e) {
            System.out.println(e.getMessage());
        }

        try {
            System.out.println(sumArrayFourByFour(arrayCorrectSize));
        } catch (MyArraySizeException | MyArrayDataException e) {
            System.out.println(e.getMessage());
        }

        try {
            System.out.println(sumArrayFourByFour(arrayTotallyIncorrect));
        } catch (MyArraySizeException | MyArrayDataException e) {
            System.out.println(e.getMessage());
        }

    }

    private static int sumArrayFourByFour(String[][] array) throws MyArrayDataException, MyArraySizeException {
        int result = 0;

        if (isArrayCorrectSize(array)) {
            for (int row = 0; row < array.length; row++) {
                for (int col = 0; col < array.length; col++) {
                    try {
                        result += Integer.parseInt(array[row][col]);
                    } catch (NumberFormatException e) {
                        throw new MyArrayDataException(row, col);
                    }
                }
            }
        }

        return result;
    }

    private static boolean isArrayCorrectSize(String[][] array) throws MyArraySizeException {
        if (array.length == 4) {
            for (String[] strings : array) {
                if (strings.length != 4) {
                    throw new MyArraySizeException();
                }
            }
        } else {
            throw new MyArraySizeException();
        }
        return true;
    }


}
