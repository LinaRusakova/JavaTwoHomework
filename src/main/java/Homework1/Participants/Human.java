package Homework1.Participants;

public class Human implements Participant {

    public final String name;
    public final int jumpHeight;
    public final int runLength;
    public boolean isFailed;
    public int score;

    public Human(String name, int jumpHeight, int runLength) {
        this.name = name;
        this.jumpHeight = jumpHeight;
        this.runLength = runLength;
        this.isFailed = false;
        this.score = 0;
    }

    @Override
    public boolean getIsFailed() {
        return this.isFailed;
    }

    @Override
    public void addScore() {
        this.score ++;
    }

    @Override
    public int getScore() {
        return this.score;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int run() {
        System.out.printf("Человек %s бежит на дистанцию %d м.%n", name, runLength);
        return runLength;
    }

    @Override
    public int jump() {
        System.out.printf("Человек %s прыгает на высоту %d м.%n", name, jumpHeight);
        return jumpHeight;
    }

    @Override
    public String toString() {
        return "Human{" +
                "name='" + name + '\'' +
                ", jumpHeight=" + jumpHeight +
                ", runLength=" + runLength +
                '}';
    }

    @Override
    public void isFailed() {
        this.isFailed = true;
        System.out.printf("Человек %s не справился с препятствием и выбывает из соревнований%n", name);
    }
}
