package ru.geekbrains.java2.network.clientserver.commands;

import java.io.Serializable;

public class PublicMessageCommandData implements Serializable {

    private final String sender;
    private final String message;

    public PublicMessageCommandData(String username, String message) {
        this.sender = username;
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public String getMessage() {
        return message;
    }
}
