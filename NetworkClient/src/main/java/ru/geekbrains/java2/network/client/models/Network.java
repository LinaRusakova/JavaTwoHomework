package ru.geekbrains.java2.network.client.models;

import javafx.application.Platform;
import ru.geekbrains.java2.network.client.NetworkChatClient;
import ru.geekbrains.java2.network.client.controllers.Controller;
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

    public void sendCommand(Command command) throws IOException {
        outputStream.writeObject(command);
    }

    public void sendMessage(String message) throws IOException {
        sendCommand(Command.publicMessageCommand(username, message));
    }

    public void sendPrivateMessage(String message, String recipient) throws IOException {
        sendCommand(Command.privateMessageCommand(recipient, message));
    }

    public void waitMessages(Controller controller) {
        Thread thread = new Thread(() -> {
            try {
                while (true) {
                    Command command = readCommand();

                    if (command == null) {
                        Platform.runLater(() -> NetworkChatClient.showNetworkError("Server error", "Unknown command from server!"));
                        continue;
                    }
                    switch (command.getType()) {
                        case AUTH_OK: {
                            AuthOkCommandData data = (AuthOkCommandData) command.getData();
                            setUsername(data.getUsername());
                            Platform.runLater(() -> controller.getClientApp().openChat());
                            return;
                        }
                        case AUTH_ERROR: {
                            AuthErrorCommandData data = (AuthErrorCommandData) command.getData();
                            Platform.runLater(() -> NetworkChatClient.showNetworkError("Server error", data.getErrorMessage()));
                            break;
                        }
                        case AUTH_TIMEOUT: {
                            AuthTimeoutCommandData data = (AuthTimeoutCommandData) command.getData();
                            Platform.runLater(() -> NetworkChatClient.showNetworkError("Server error", data.getAuthTimeoutMessage()));
                            getSocket().close();
                            break;
                        }
                        case INFO_MESSAGE: {
                            MessageInfoCommandData data = (MessageInfoCommandData) command.getData();
                            String message = data.getMessage();
                            String sender = data.getSender();
                            String formattedMessage = sender != null ? String.format("%s: %s", sender, message) : message;
                            Platform.runLater(() -> controller.appendMessage(formattedMessage));
                            break;
                        }
                        case UPDATE_USER_LIST: {
                            UpdateUserListCommandData data = (UpdateUserListCommandData) command.getData();
                            Platform.runLater(() -> {
                                controller.updateUserList(data.getUserList());
                            });
                            break;
                        }
                        case ERROR:
                            ErrorCommandData data = (ErrorCommandData) command.getData();
                            String errorMessage = data.getErrorMessage();
                            Platform.runLater(() -> NetworkChatClient.showNetworkError("Server error", errorMessage));
                            break;
                        default:
                            Platform.runLater(() -> NetworkChatClient.showNetworkError("Unknown command from server!", command.getType().toString()));
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

    public synchronized Command readCommand() throws IOException {
        try {
            return (Command) inputStream.readObject();
        } catch (ClassNotFoundException e) {
            String errorMessage = "Unknown type of object from client";
            System.err.println(errorMessage);
            e.printStackTrace();
            return null;
        }
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Socket getSocket() {
        return socket;
    }
}
