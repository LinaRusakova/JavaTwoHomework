package ru.geekbrains.java2.network.server.chat.handler;

import ru.geekbrains.java2.network.clientserver.Command;
import ru.geekbrains.java2.network.clientserver.CommandType;
import ru.geekbrains.java2.network.clientserver.commands.AuthCommandData;
import ru.geekbrains.java2.network.clientserver.commands.PrivateMessageCommandData;
import ru.geekbrains.java2.network.clientserver.commands.PublicMessageCommandData;
import ru.geekbrains.java2.network.server.chat.MyServer;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.Timer;
import java.util.TimerTask;

public class ClientHandler {

    private final long CONNECTION_TIMEOUT = 10000;

    private final MyServer myServer;
    private final Socket clientSocket;

    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;

    private String username;

    public ClientHandler(MyServer myServer, Socket clientSocket) {
        this.myServer = myServer;
        this.clientSocket = clientSocket;
    }

    public void handle() throws IOException {
        inputStream = new ObjectInputStream(clientSocket.getInputStream());
        outputStream = new ObjectOutputStream(clientSocket.getOutputStream());

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    sendMessage(Command.authErrorCommand("Connection timeout"));
                    closeConnection();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        Timer closeConnectionOnTime = new Timer();

        closeConnectionOnTime.schedule(timerTask, CONNECTION_TIMEOUT);

        new Thread(() -> {
            try {
                authentication();
                if (username != null) {
                    closeConnectionOnTime.cancel();
                }
                readMessages();
            } catch (SocketException e) {
                System.err.println("Connection has been interrupted");
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

    private Command readCommand() throws IOException {
        try {
            return (Command) inputStream.readObject();
        } catch (ClassNotFoundException e) {
            String errorMessage = "Unknown type object type from client";
            System.err.println(errorMessage);
            e.printStackTrace();
            sendMessage(Command.errorCommand(errorMessage));
            return null;
        }
    }

    private void authentication() throws IOException {
        while (true) {
            Command command = readCommand();
            if (command == null) {
                continue;
            }
            if (command.getType() == CommandType.AUTH) {
                boolean isSuccessAuth = processAuthCommand(command);
                if (isSuccessAuth) {
                    break;
                }
            } else {
                sendMessage(Command.authErrorCommand("Auth command is required."));
            }
        }
    }

    private boolean processAuthCommand(Command command) throws IOException {
        AuthCommandData cmdData = (AuthCommandData) command.getData();
        String login = cmdData.getLogin();
        String password = cmdData.getPassword();
        this.username = myServer.getAuthService().getUsernameByLoginAndPassword(login, password);
        if (username != null) {
            if (myServer.isUsernameAlreadyTaken(username)) {
                sendMessage(Command.authErrorCommand("Username already taken. Please choose another one."));
                return false;
            }
            sendMessage(Command.authOkCommand(username));
            String message = username + " has come online.";
            myServer.broadcastMessage(this, Command.messageInfoCommand(message, null));
            myServer.subscribe(this);
            return true;

        } else {
            sendMessage(Command.authErrorCommand("Login and/or password are invalid. Please try again."));
            return false;
        }
    }

    private void readMessages() throws IOException {
        while (true) {
            Command command = readCommand();
            if (command == null) {
                continue;
            }

            switch (command.getType()) {
                case END:
                    return;
                case PRIVATE_MESSAGE: {
                    PrivateMessageCommandData data = (PrivateMessageCommandData) command.getData();
                    String recipient = data.getRecipient();
                    String privateMessage = data.getMessage();
                    myServer.sendPrivateMessage(recipient, Command.messageInfoCommand(privateMessage, username));
                    break;
                }
                case PUBLIC_MESSAGE: {
                    PublicMessageCommandData data = (PublicMessageCommandData) command.getData();
                    String sender = data.getSender();
                    String message = data.getMessage();
                    myServer.broadcastMessage(this, Command.messageInfoCommand(message, sender));
                    break;
                }
                default:
                    System.err.println("Unknown command type from server: " + command.getType());
            }
        }
    }

    private void closeConnection() throws IOException {
        myServer.unsubscribe(this);
        clientSocket.close();
    }

    public void sendMessage(Command command) throws IOException {
        outputStream.writeObject(command);
    }
}
