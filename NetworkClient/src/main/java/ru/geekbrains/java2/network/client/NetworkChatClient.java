package ru.geekbrains.java2.network.client;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.geekbrains.java2.network.client.controllers.AuthDialogController;
import ru.geekbrains.java2.network.client.controllers.ViewController;
import ru.geekbrains.java2.network.client.models.Network;

import java.io.IOException;
import java.util.List;


public class NetworkChatClient extends Application {

    public static final List<String> USERS_TEST_DATA = List.of("Oleg", "Alexey", "Peter");

    private Stage primaryStage;
    private Stage authDialogStage;
    private Network network;
    private ViewController viewController;
    private AuthDialogController authDialogController;


    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        network = new Network();
        if (!network.connect()) {
            showNetworkError("", "Failed to connect to server");
            return;
        }

        openAuthDialog(primaryStage);
        createChatDialog(primaryStage);
    }

    private void createChatDialog(Stage primaryStage) throws java.io.IOException {
        FXMLLoader mainLoader = new FXMLLoader();
        mainLoader.setLocation(NetworkChatClient.class.getResource("/view.fxml"));

        Parent root = mainLoader.load();

        primaryStage.setTitle("Messenger");
        primaryStage.setScene(new Scene(root, 600, 400));

        viewController = mainLoader.getController();
        viewController.setNetwork(network);

        primaryStage.setOnCloseRequest(event -> network.close());
    }

    private void openAuthDialog(Stage primaryStage) throws IOException {
        FXMLLoader authLoader = new FXMLLoader();

        authLoader.setLocation(NetworkChatClient.class.getResource("/authDialog.fxml"));
        Parent authDialogPanel = authLoader.load();
        authDialogStage = new Stage();

        authDialogStage.setTitle("Log In");
        authDialogStage.initModality(Modality.WINDOW_MODAL);
        authDialogStage.initOwner(primaryStage);
        Scene scene = new Scene(authDialogPanel);
        authDialogStage.setScene(scene);
        authDialogStage.show();


        authDialogController = authLoader.getController();
        authDialogController.setNetwork(network);
        authDialogController.setClientApp(this);

        network.waitMessages(authDialogController);
    }

    public static void showNetworkError(String errorTitle, String errorDetails) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Network Error");
        alert.setHeaderText(errorTitle);
        alert.setContentText(errorDetails);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void openChat() {
        authDialogStage.close();
        primaryStage.show();
        primaryStage.setTitle(network.getUsername());
        network.waitMessages(viewController);
    }
}