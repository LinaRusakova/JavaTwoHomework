package Homework6.client;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Contact {
    private final String name;
    private final ObservableList<String> messages;

    public Contact(String name) {
        this.name = name;
        messages = FXCollections.observableArrayList();
    }

    public ObservableList<String> getMessages() {
        return messages;
    }

    public String getName() {
        return name;
    }

    public void addMessage(String userName, String message) {
        this.messages.add(String.format("%s (%s):%n%s", userName, getCurrentDate(), message));
    }

    public void addMessage(String message) {
        this.messages.add(String.format("%s (%s):%n%s", this.name, getCurrentDate(), message));
    }

    private String getCurrentDate() {
        return DateTimeFormatter.ofPattern("dd-MM-yy HH:mm:ss").format(LocalDateTime.now());
    }
}
