package ru.geekbrains.java2.network.client.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import ru.geekbrains.java2.network.client.NetworkChatClient;
import ru.geekbrains.java2.network.client.models.Network;
import ru.geekbrains.java2.network.clientserver.Command;
import ru.geekbrains.java2.network.clientserver.CommandType;
import ru.geekbrains.java2.network.clientserver.commands.AuthErrorCommandData;

import java.io.IOException;

public class AuthDialogController {
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

        String authError = network.sendAuthCommand(login, password);
        if (authError == null) {
            clientApp.openChat();
        } else {
            NetworkChatClient.showNetworkError("Auth error", authError);
        }

    }

    public void setNetwork(Network network) {
        this.network = network;
    }

    public void setClientApp(NetworkChatClient clientApp) {
        this.clientApp = clientApp;
    }

}
