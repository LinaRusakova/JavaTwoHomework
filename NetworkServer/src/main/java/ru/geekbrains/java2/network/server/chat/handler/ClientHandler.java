package ru.geekbrains.java2.network.server.chat.handler;

import ru.geekbrains.java2.network.server.chat.MyServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler {
    public static final String AUTH_CMD_PREFIX = "/auth";
    public static final String AUTHOK_CMD_PREFIX = "/authok";
    public static final String AUTHERR_CMD_PREFIX = "/autherr";
    private static final String PRIVATE_MSG_CMD_PREFIX = "/w";
    private static final String CLIENT_MSG_CMD_PREFIX = "/clientMsg";
    private static final String SERVER_MSG_CMD_PREFIX = "/serverMsg";

    private final MyServer myServer;
    private final Socket clientSocket;

    private DataInputStream inputStream;
    private DataOutputStream outputStream;

    private String username;

    public ClientHandler(MyServer myServer, Socket clientSocket) {
        this.myServer = myServer;
        this.clientSocket = clientSocket;
    }

    public void handle() throws IOException {
        inputStream = new DataInputStream(clientSocket.getInputStream());
        outputStream = new DataOutputStream(clientSocket.getOutputStream());

        new Thread(() -> {
            try {
                authentication();
                readMessage();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    closeConnection();
                } catch (IOException e) {
                    System.err.println("Failed to close connection.");
                }
            }
        }).start();
    }

    public String getUsername() {
        return username;
    }

    private void authentication() throws IOException {
        while (true) {
            String message = inputStream.readUTF();
            if (message.startsWith(AUTH_CMD_PREFIX)) {
                String[] parts = message.split("\\s+", 3);
                String login = parts[1];
                String password = parts[2];
                this.username = myServer.getAuthService().getUsernameByLoginAndPassword(login, password);
                if (username != null) {
                    if (myServer.isUsernameAlreadyTaken(username)) {
                        outputStream.writeUTF(AUTHERR_CMD_PREFIX + " Username already taken. Please choose another one.");
                    }
                    outputStream.writeUTF(String.format("%s %s", AUTHOK_CMD_PREFIX, username));
                    myServer.broadcastMessage(username + " has come online", this);
                    myServer.subscribe(this);
                    break;
                } else {
                    outputStream.writeUTF(AUTHERR_CMD_PREFIX + " Login and/or password are invalid. Please try again.");
                }
            } else {
                outputStream.writeUTF(AUTHERR_CMD_PREFIX + " /auth command is required.");
            }
        }

    }

    private void readMessage() throws IOException {
        while (true) {
            String message = inputStream.readUTF();
            System.out.println("Message: " + message);
            if (message.startsWith("/end")) {
                return;
            }
            myServer.broadcastMessage(message, this);
        }
    }

    private void closeConnection() throws IOException {
        myServer.unsubscribe(this);
        clientSocket.close();
    }

    public void sendMessage(String message) throws IOException {
        outputStream.writeUTF(String.format("%s %s", CLIENT_MSG_CMD_PREFIX, message));
    }
}
