package OnlineChat.client;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

import java.io.IOException;


public class Controller {

    @FXML
    private TextField userMessage;
    @FXML
    private Button sendButton;
    @FXML
    private ListView<String> chatsList;
    @FXML
    private ListView<String> chatArea;
    private Network network;
    private ObservableList<Contact> contacts;


    public void initialize() {

        /* Введеные вручную данные для проверки работы, от этого блока впоследствии надо избавиться */
        contacts = FXCollections.observableArrayList(
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
        focusLastMessage();

        chatsList.setOnMouseClicked(event -> {
            chatArea.setItems(contacts.get(getCurrentContactIndex()).getMessages());
            focusLastMessage();
        });

        sendButton.setOnAction(event -> sendMessage());

        userMessage.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                sendMessage();
            }
        });
    }

    private void focusLastMessage() {
        chatArea.getSelectionModel().selectLast();
        chatArea.scrollTo(chatArea.getSelectionModel().getSelectedIndex());
    }

    private void sendMessage() {
        if (userMessage.getText().length() > 0) {
            try {
                network.getOutputStream().writeUTF(userMessage.getText());
            } catch (IOException e) {
                e.printStackTrace();
                String errorMessage = "Failed to send message";
                EchoClient.showNetworkErrorAlert(e.getMessage(), errorMessage);
            }
            userMessage.clear();
        }
    }

    private int getCurrentContactIndex() {
        return chatsList.getSelectionModel().getSelectedIndex();
    }

    public void setNetwork(Network network) {
        this.network = network;
    }

    public void appendMessage(String message) {
        Platform.runLater(() -> contacts.get(getCurrentContactIndex()).addMessage("You", message));

    }
}
