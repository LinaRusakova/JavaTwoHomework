package Homework6;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;

public class Server {

    private static final int SERVER_PORT = 8189;

    public static void main(String[] args) {

        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {

            System.out.println("Waiting for connection...");
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client has been connected.");

            DataInputStream inputStream = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream outputStream = new DataOutputStream(clientSocket.getOutputStream());

            Thread awaitingMessages = new Thread(() -> {
                try {
                    while (true) {
                        String message = inputStream.readUTF();
                        System.out.println("Client: " + message);

                        if (message.equals("/end")) {
                            break;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            awaitingMessages.setDaemon(true);
            awaitingMessages.start();


            Scanner scanner = new Scanner(new InputStreamReader(System.in));

            while (true) {
                String message = scanner.nextLine();
                outputStream.writeUTF("Server: " + message);

                if (message.equals("/end")) {
                    break;
                }
            }

        } catch (SocketException e) {
            System.err.println("Connection reset");
        } catch (IOException e) {
            System.err.println("Server port is already opened!");
        }

    }

}
