package ru.geekbrains.java2.network.server.chat;

import ru.geekbrains.java2.network.server.chat.auth.AuthService;
import ru.geekbrains.java2.network.server.chat.auth.BaseAuthService;
import ru.geekbrains.java2.network.server.chat.handler.ClientHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class MyServer {

    private final ServerSocket serverSocket;
    private final List<ClientHandler> clients = new ArrayList<>();
    private final AuthService authService;


    public MyServer(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
        this.authService = new BaseAuthService();
    }

    public void start() throws IOException {
        System.out.println("Server has been started.");
        authService.start();
        try {
            while (true) {
                waitAndProcessNewConnection();
            }
        } catch (IOException e) {
            System.err.println("Failed to accept new connection.");
            e.printStackTrace();
        } finally {
            authService.stop();
            serverSocket.close();
        }
    }

    private void waitAndProcessNewConnection() throws IOException {
        System.out.println("Awaiting for new connections...");
        Socket clientSocket = serverSocket.accept();
        System.out.println("Client has been connected.");
        processClientConnection(clientSocket);
    }

    private void processClientConnection(Socket clientSocket) throws IOException {
        ClientHandler clientHandler = new ClientHandler(this, clientSocket);
        clientHandler.handle();
    }

    public AuthService getAuthService() {
        return authService;
    }

    public void broadcastMessage(String message, ClientHandler sender) throws IOException {
        for (ClientHandler client : clients) {
            if(client == sender) {
                continue;
            }
            client.sendMessage(message);
        }
    }

    public void subscribe(ClientHandler handler) {
        clients.add(handler);
    }

    public void unsubscribe(ClientHandler handler) {
        clients.remove(handler);
    }

    public boolean isUsernameAlreadyTaken(String username) {
        for (ClientHandler client : clients) {
            if (client.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

}
