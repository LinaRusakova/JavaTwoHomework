package ru.geekbrains.java2.network.client.models;

import javafx.application.Platform;
import ru.geekbrains.java2.network.client.NetworkChatClient;
import ru.geekbrains.java2.network.client.controllers.ViewController;
import ru.geekbrains.java2.network.clientserver.Command;

import java.io.*;
import java.net.Socket;

public class Network {

    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 8189;

    private final String host;
    private final int port;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private Socket socket;
    private String username;

    public Network() {
        this(SERVER_ADDRESS, SERVER_PORT);
    }

    public Network(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public boolean connect() {
        try {
            socket = new Socket(host, port);
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
            return true;
        } catch (IOException e) {
            System.err.println("Connection hasn't been established");
            e.printStackTrace();
            return false;
        }
    }

    public String sendAuthCommand(String login, String password) {
        try {
            Command authCommand = Command.authCommand(login, password);
            outputStream.writeObject(authCommand);
            Command command = readCommand();

            if (command == null) {
                NetworkChatClient.showNetworkError("Failed to read command from server.", "");
            }


        } catch (IOException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    public void sendMessage(String message) throws IOException {
        outputStream.writeUTF(String.format("%s %s %s", CLIENT_MSG_CMD_PREFIX, username, message));
    }

    public void sendPrivateMessage(String message, String recipient) throws IOException {
        String command = String.format("%s %s %s %s", PRIVATE_MSG_CMD_PREFIX, recipient, username, message);
        outputStream.writeUTF(command);
    }

    public void waitMessages(ViewController viewController) {
        Thread thread = new Thread(() -> {
            try {
                while (true) {
                    String message = inputStream.readUTF();
                    if (message.startsWith(CLIENT_MSG_CMD_PREFIX)) {
                        String[] parts = message.split("\\s+", 3);
                        Platform.runLater(() -> viewController.appendMessage(String.format("%s: %s", parts[1], parts[2])));
                    }
                    else if (message.startsWith(PRIVATE_MSG_CMD_PREFIX)) {
                        String[] parts = message.split("\\s+", 4);
                        if (parts[1].equals(this.username)) {
                            Platform.runLater(() -> viewController.appendMessage(String.format("%s: %s", parts[2], parts[3])));
                        }
                    }
                    else if (message.startsWith(SERVER_MSG_CMD_PREFIX)) {
                        String[] parts = message.split("\\s+", 2);
                        Platform.runLater(() -> viewController.appendMessage(parts[1]));
                    }
                    else {
                        Platform.runLater(() -> viewController.showError("Unknown command from server!", message));
                    }
                }
            } catch (IOException e) {
                System.out.println("Connection has been lost");
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    public void close() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getUsername() {
        return username;
    }

    private Command readCommand() throws IOException {
        try {
            return (Command) inputStream.readObject();
        } catch (ClassNotFoundException e) {
            String errorMessage = "Unknown type of object from client";
            System.err.println(errorMessage);
            e.printStackTrace();
            return null;
        }
    }
}
