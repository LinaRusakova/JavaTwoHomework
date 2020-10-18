package ru.geekbrains.java2.network.clientserver.commands;

import java.io.Serializable;

public class AuthTimeoutCommandData implements Serializable {

    private final String authTimeoutMessage;

    public AuthTimeoutCommandData(String authTimeoutMessage) {
        this.authTimeoutMessage = authTimeoutMessage;
    }

    public String getAuthTimeoutMessage() {
        return authTimeoutMessage;
    }
}
