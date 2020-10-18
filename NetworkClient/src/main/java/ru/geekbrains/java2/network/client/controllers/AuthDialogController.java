package ru.geekbrains.java2.network.client.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import ru.geekbrains.java2.network.client.NetworkChatClient;
import ru.geekbrains.java2.network.client.models.Network;
import ru.geekbrains.java2.network.clientserver.Command;

import java.io.IOException;
import java.util.List;

public class AuthDialogController implements Controller {
    private @FXML
    TextField loginField;
    private @FXML
    PasswordField passwordField;
    private @FXML
    Button authButton;

    private Network network;
    private NetworkChatClient clientApp;

    @FXML
    public void executeAuth() {

        String login = loginField.getText();
        String password = passwordField.getText();
        if (login == null || login.isBlank() || password == null || password.isBlank()) {
            NetworkChatClient.showNetworkError("Auth error", "Username and password shouldn't be empty!");
            return;
        }
        if (!network.getSocket().isClosed()) {
            processAuth(login, password);
        } else {
            NetworkChatClient.showNetworkError("Server error", "Connection has been terminated");
        }
    }

    private void processAuth(String login, String password) {
        try {
            Command authCommand = Command.authCommand(login, password);
            network.sendCommand(authCommand);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setNetwork(Network network) {
        this.network = network;
    }

    public void setClientApp(NetworkChatClient clientApp) {
        this.clientApp = clientApp;
    }

    public NetworkChatClient getClientApp() {
        return clientApp;
    }

    @Override
    public void appendMessage(String message) {
    }

    @Override
    public void updateUserList(List<String> list) {

    }
}
