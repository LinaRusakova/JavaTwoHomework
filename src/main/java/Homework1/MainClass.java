package Homework1;

import Homework1.Obstacles.JumpingWall;
import Homework1.Obstacles.Obstacle;
import Homework1.Obstacles.RunningPath;
import Homework1.Participants.Cat;
import Homework1.Participants.Human;
import Homework1.Participants.Participant;
import Homework1.Participants.Robot;

import java.util.Random;

public class MainClass {

    public static void main(String[] args) {

        prepareCompetition().competitionRun();

    }

    private static Competition prepareCompetition() {
        Random random = new Random();

        Obstacle runningPath = new RunningPath(random.nextInt(1200) + 300);
        Obstacle jumpingWall = new JumpingWall(random.nextInt(7) + 3);

        Participant cat = new Cat("Barsik", random.nextInt(15) + 5, random.nextInt(1000) + 500);
        Participant human = new Human("Vasya", random.nextInt(10) + 5, random.nextInt(500) + 300);
        Participant robot = new Robot("Ultratron 5000", random.nextInt(8) + 2, random.nextInt(4000) + 1000);

        Competition competition = new Competition("Олимпиада");
        competition.setObstacles(runningPath, jumpingWall);
        competition.setParticipants(cat, human, robot);

        return competition;

    }

}
