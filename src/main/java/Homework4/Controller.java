package Homework4;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableListBase;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.util.List;

public class Controller {

    @FXML
    private TextField userMessage;
    @FXML
    private Button sendButton;
    @FXML
    private ListView<String> chatsList;
    @FXML
    private ListView<String> chatArea;


    public void initialize() {
        ObservableList<Contact> contacts = FXCollections.observableArrayList(
                new Contact("Mama"),
                new Contact("Papa"),
                new Contact("Brother"),
                new Contact("Sister")
        );

        ObservableList<String> contactList = FXCollections.observableArrayList();

        for (Contact contact : contacts) {
            contactList.add(contact.getName());
        }

        contacts.get(0).addMessage("Привет!");

        chatsList.setItems(contactList);

        chatsList.getSelectionModel().selectFirst();

        chatArea.setItems(contacts.get(getCurrentContactIndex()).getMessages());

        chatsList.setOnMouseClicked(event -> chatArea.setItems(contacts.get(getCurrentContactIndex()).getMessages()));

        sendButton.setOnAction(event -> {
            if (userMessage.getText().length() > 0) {
                contacts.get(getCurrentContactIndex()).addMessage("You", userMessage.getText());
                userMessage.clear();
                System.out.println(getCurrentContactIndex());
            }
        });
        userMessage.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                if (userMessage.getText().length() > 0) {
                    contacts.get(getCurrentContactIndex()).addMessage("You", userMessage.getText());
                    userMessage.clear();
                    System.out.println(getCurrentContactIndex());
                }
            }
        });

    }

    private int getCurrentContactIndex() {
        return chatsList.getSelectionModel().getSelectedIndex();
    }
}
