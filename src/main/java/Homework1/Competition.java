package Homework1;

import Homework1.Obstacles.Obstacle;
import Homework1.Participants.Participant;

public class Competition {

    private final String title;
    private Obstacle[] obstacles;
    private Participant[] participants;

    public Competition(String title) {
        this.title = title;
    }

    public void setObstacles(Obstacle... obstacles) {
        this.obstacles = obstacles;
    }

    public void setParticipants(Participant... participants) {
        this.participants = participants;
    }

    public void competitionRun() {
        System.out.printf("Начинаются соревнования %s!%n", this.title);
        for (Obstacle obstacle : obstacles) {
            for (Participant participant : participants) {
                if (!participant.getIsFailed()) {
                    System.out.println(obstacle.isCompleteBy(participant));
                }
                System.out.println();
            }
        }

        System.out.println("Время объявить итоговые результаты!");

        for (Participant participant : participants) {
            System.out.printf("Результат %s составляет: %d%n", participant.getName(), participant.getScore());
        }


    }
}
