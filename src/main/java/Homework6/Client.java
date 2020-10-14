package Homework6;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;

public class Client {

    private static final String SERVER_ADDRESS = "localhost";
    public static final int SERVER_PORT = 8189;

    public static void main(String[] args) {

        try (Socket clientSocket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {

            DataInputStream inputStream = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream outputStream = new DataOutputStream(clientSocket.getOutputStream());

            Thread awaitingMessages = new Thread(() -> {
                try {
                    while (true) {
                        String message = inputStream.readUTF();
                        System.out.println(message);

                        if (message.equals("/end")) {
                            break;
                        }
                    }
                } catch (SocketException e) {
                    System.err.println("Connection reset");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            awaitingMessages.setDaemon(true);
            awaitingMessages.start();

            Scanner scanner = new Scanner(System.in);

            while (true) {
                String message = scanner.nextLine();
                outputStream.writeUTF(message);

                if (message.equals("/end")) {
                    break;
                }
            }

        } catch (ConnectException e) {
            System.err.println("There's no server to connect");
        } catch (IOException e) {
            System.err.println("Connection lost");
        }

    }

}
