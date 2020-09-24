package Homework1.Obstacles;

import Homework1.Participants.Participant;

public class RunningPath implements Obstacle {

    private final int length;

    public RunningPath(int length) {
        this.length = length;
    }

    @Override
    public boolean isCompleteBy(Participant participant) {
        System.out.printf("Бег на %d м.%n", length);
        if (participant.run() >= this.length) {
            participant.addScore();
            return true;
        } else {
            participant.isFailed();
            return false;
        }
    }

    @Override
    public String toString() {
        return "RunningPath{" +
                "length=" + length +
                '}';
    }
}
