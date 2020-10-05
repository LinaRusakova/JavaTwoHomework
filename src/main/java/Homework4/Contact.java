package Homework4;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Contact {
    private final String name;
    private boolean isOnline;
    private String lastMessage;
    private final ObservableList<String> messages;

    public Contact(String name) {
        this.name = name;
        this.isOnline = true;
        this.lastMessage = null;
        messages = FXCollections.observableArrayList();
    }

    public void addMessage(String userName, String message) {
        this.messages.add(String.format("%s: %s",userName, message));
        this.lastMessage = String.format("%s: %s",userName, message);
    }

    public void addMessage(String message) {
        this.messages.add(String.format("%s: %s",this.name, message));
        this.lastMessage = String.format("%s: %s",this.name, message);
    }

    public ObservableList<String> getMessages() {
        return messages;
    }

    public String getName() {
        return name;
    }
}
