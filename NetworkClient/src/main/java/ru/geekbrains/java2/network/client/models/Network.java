package ru.geekbrains.java2.network.client.models;

import javafx.application.Platform;
import ru.geekbrains.java2.network.client.controllers.ViewController;
import ru.geekbrains.java2.network.clientserver.Command;
import ru.geekbrains.java2.network.clientserver.commands.*;

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
                return "Failed to read command from server.";
            }

            switch (command.getType()) {
                case AUTH_OK: {
                    AuthOkCommandData data = (AuthOkCommandData) command.getData();
                    this.username = data.getUsername();
                    return null;
                }
                case AUTH_ERROR: {
                    AuthErrorCommandData data = (AuthErrorCommandData) command.getData();
                    return data.getErrorMessage();
                }
                default:
                    return "Unknown command type from server: " + command.getType();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    private void sendCommand(Command command) throws IOException {
        outputStream.writeObject(command);
    }

    public void sendMessage(String message) throws IOException {
        sendCommand(Command.publicMessageCommand(username, message));
    }

    public void sendPrivateMessage(String message, String recipient) throws IOException {
        sendCommand(Command.privateMessageCommand(recipient, message));
    }

    public void waitMessages(ViewController viewController) {
        Thread thread = new Thread(() -> {
            try {
                while (true) {
                    Command command = readCommand();

                    if (command == null) {
                        Platform.runLater(() -> viewController.showError("Server error","Unknown command from server!"));
                        continue;
                    }

                    switch (command.getType()) {
                        case INFO_MESSAGE: {
                            MessageInfoCommandData data = (MessageInfoCommandData) command.getData();
                            String message = data.getMessage();
                            String sender = data.getSender();
                            String formattedMessage = sender != null ? String.format("%s: %s", sender, message) : message;
                            Platform.runLater(() -> viewController.appendMessage(formattedMessage));
                            break;
                        }
                        case UPDATE_USER_LIST: {
                            UpdateUserListCommandData data = (UpdateUserListCommandData) command.getData();
                            Platform.runLater(() -> {
                                viewController.updateUserList(data.getUserList());
                            });
                            break;
                        }
                        case ERROR:
                            ErrorCommandData data = (ErrorCommandData) command.getData();
                            String errorMessage = data.getErrorMessage();
                            Platform.runLater(() -> viewController.showError("Server error", errorMessage));
                            break;
                        default:
                            Platform.runLater(() -> viewController.showError("Unknown command from server!", command.getType().toString()));
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
