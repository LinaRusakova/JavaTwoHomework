package Homework1.Obstacles;

import Homework1.Participants.Participant;

public class JumpingWall implements Obstacle {

    private final int height;

    public JumpingWall(int height) {
        this.height = height;
    }

    @Override
    public boolean isCompleteBy(Participant participant) {
        System.out.printf("Прыжок на на %d м.%n", height);
        if (participant.jump() >= this.height) {
            participant.addScore();
            return true;
        } else {
            participant.isFailed();
            return false;
        }
    }

    @Override
    public String toString() {
        return "JumpingWall{" +
                "height=" + height +
                '}';
    }
}
