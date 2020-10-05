package Homework4;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


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

        //Введеные вручную данные для проверки работы, от этого блока впоследствии надо избавиться
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

        contacts.get(0).addMessage("Привет!", getCurrentDate());
        contacts.get(1).addMessage("Как дела?", getCurrentDate());
        contacts.get(2).addMessage("Ты тут?", getCurrentDate());
        contacts.get(3).addMessage("Дай денег, плз, зарплату задерживают, а мелкой надо штаны зимние купить.", getCurrentDate());
        //Конец данных инициализации

        chatsList.setItems(contactList);

        chatsList.getSelectionModel().selectFirst();

        chatArea.setItems(contacts.get(getCurrentContactIndex()).getMessages());

        chatsList.setOnMouseClicked(event -> chatArea.setItems(contacts.get(getCurrentContactIndex()).getMessages()));

        sendButton.setOnAction(event -> {
            sendMessage(contacts);
        });
        userMessage.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                sendMessage(contacts);
            }
        });

    }

    private void sendMessage(ObservableList<Contact> contacts) {
        if (userMessage.getText().length() > 0) {
            contacts.get(getCurrentContactIndex()).addMessage("You", userMessage.getText(), getCurrentDate());
            userMessage.clear();
        }
    }

    private int getCurrentContactIndex() {
        return chatsList.getSelectionModel().getSelectedIndex();
    }

    private String getCurrentDate() {
        return DateTimeFormatter.ofPattern("dd-MM-yy hh:mm:ss").format(LocalDateTime.now());
    }
}
