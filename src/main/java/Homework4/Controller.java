package Homework4;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;


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

        /* Введеные вручную данные для проверки работы, от этого блока впоследствии надо избавиться */
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
        contacts.get(1).addMessage("Как дела?");
        contacts.get(2).addMessage("Ты тут?");
        contacts.get(3).addMessage("Дай денег, плз, зарплату задерживают, а мелкой надо штаны зимние купить.");
        /* Конец данных инициализации */

        chatsList.setItems(contactList);

        chatsList.getSelectionModel().selectFirst();

        chatArea.setItems(contacts.get(getCurrentContactIndex()).getMessages());
        selectLastMessage();

        chatsList.setOnMouseClicked(event -> {
            chatArea.setItems(contacts.get(getCurrentContactIndex()).getMessages());
            focusLastMessage();
        });

        sendButton.setOnAction(event -> {
            sendMessage(contacts);
        });

        userMessage.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                sendMessage(contacts);
            }
        });
    }

    private void focusLastMessage() {
        selectLastMessage();
        chatArea.scrollTo(chatArea.getSelectionModel().getSelectedIndex());
    }

    private void selectLastMessage() {
        chatArea.getSelectionModel().selectLast();
    }

    private void sendMessage(ObservableList<Contact> contacts) {
        if (userMessage.getText().length() > 0) {
            contacts.get(getCurrentContactIndex()).addMessage("You", userMessage.getText());
            userMessage.clear();
            focusLastMessage();
        }
    }

    private int getCurrentContactIndex() {
        return chatsList.getSelectionModel().getSelectedIndex();
    }
}
