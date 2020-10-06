package Homework5;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class MultiThreadExample {

    static final int SIZE = 10000000;
    static final int HALF = SIZE / 2;

    public static void main(String[] args) throws InterruptedException {

        calculateBySingleThread();

        calculateByTwoThreads();

        calculateByFourThreads();

    }

    public static void calculateBySingleThread() {

        float[] array = new float[SIZE];
        Arrays.fill(array, 1);

        long tStart = System.currentTimeMillis();

        doMath(array, 0);

        long tEnd = System.currentTimeMillis();

//        Проверка на соответствие массивов
//        for (int i = array.length - HALF + 2; i > array.length - HALF - 3; i --) {
//            System.out.print(array[i]);
//            System.out.println("     " + i);
//        }

        System.out.printf("Однопоточный метод: %d ms%n", (tEnd - tStart));
    }

    public static synchronized void calculateByTwoThreads() throws InterruptedException {

        float[] array = new float[SIZE];
        Arrays.fill(array, 1);

        long tStart = System.currentTimeMillis();

        float[] a1 = new float[HALF];
        float[] a2 = new float[HALF];

        System.arraycopy(array, 0, a1, 0, HALF);
        System.arraycopy(array, HALF, a2, 0, HALF);

        Thread thread1 = new Thread(() -> doMath(a1, 0));
        Thread thread2 = new Thread(() -> doMath(a2, HALF));

        thread1.start();
        thread2.start();

//        thread1.join();
//        Вот этот момент нужно поточнее, можно джоинить только один тред, или все треды? Их джоин означает, что тред мейн (тот тред, из которого был запущен метод) ждет их окончания?
//        И если указать только один из них, как у меня, то в теории может случится так, что один из тредов не успеет отработать?
//        Потому что без джойнов так и было, треды просто не дожидались выполнения полного прохода по массивам, при джойне только первого треда тоже второй не дорабатывает до конца, а вот так все в порядке.
//        Это связано со временем их старта?
        thread2.join();

        System.arraycopy(a1, 0, array, 0, HALF);
        System.arraycopy(a2, 0, array, HALF, HALF);

        long tEnd = System.currentTimeMillis();

//        Проверка на соответствие концов массивов
//        for (int i = array.length - HALF + 2; i > array.length - HALF - 3; i --) {
//            System.out.print(array[i]);
//            System.out.println("     " + i);
//        }

        System.out.printf("Двухпоточный метод: %d ms%n", (tEnd - tStart));

    }

    public static synchronized void calculateByFourThreads() throws InterruptedException {

        float[] array = new float[SIZE];
        Arrays.fill(array, 1);

        long tStart = System.currentTimeMillis();

        float[] a1 = new float[HALF / 2];
        float[] a2 = new float[HALF / 2];
        float[] a3 = new float[HALF / 2];
        float[] a4 = new float[HALF / 2];

        System.arraycopy(array, 0, a1, 0, HALF / 2);
        System.arraycopy(array, HALF / 2, a2, 0, HALF / 2);
        System.arraycopy(array, HALF, a3, 0, HALF / 2);
        System.arraycopy(array, HALF + HALF / 2, a4, 0, HALF / 2);

        Thread thread1 = new Thread(() -> doMath(a1, 0));
        Thread thread2 = new Thread(() -> doMath(a2, HALF / 2f));
        Thread thread3 = new Thread(() -> doMath(a3, HALF));
        Thread thread4 = new Thread(() -> doMath(a4, HALF + HALF / 2f));

        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();

//        thread1.join();
//        thread2.join();
//        thread3.join();
        thread4.join();

        System.arraycopy(a1, 0, array, 0, HALF / 2);
        System.arraycopy(a2, 0, array, HALF / 2, HALF / 2);
        System.arraycopy(a3, 0, array, HALF, HALF / 2);
        System.arraycopy(a4, 0, array, HALF + HALF / 2, HALF / 2);

        long tEnd = System.currentTimeMillis();

//        Проверка на соответствие концов массивов
//        for (int i = array.length - HALF + 2; i > array.length - HALF - 3; i --) {
//            System.out.print(array[i]);
//            System.out.println("     " + i);
//        }

        System.out.printf("4-х поточный метод: %d ms%n", (tEnd - tStart));

    }

    public static void doMath(float[] array, float startIndex) {
        for (int i = 0; i < array.length; i++) {
            array[i] = (float) (array[i] * Math.sin(0.2f + (i + startIndex) / 5) * Math.cos(0.2f + (i + startIndex) / 5) * Math.cos(0.4f + (i + startIndex) / 2));
        }
    }
}
