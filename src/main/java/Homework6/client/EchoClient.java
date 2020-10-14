package Homework6.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class EchoClient extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(EchoClient.class.getResource("/Client.fxml"));

        Parent root = loader.load();

        primaryStage.setTitle("Telegram Desktop (нет)");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();

        Network network = new Network();
        if (!network.connect()) {
            showNetworkErrorAlert("", "Failed to connect to server.");
        }
        Controller controller = loader.getController();
        controller.setNetwork(network);

        network.waitMessages(controller);

    }

    public static void showNetworkErrorAlert(String errorDetails, String errorTitle) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Network error");
        alert.setHeaderText(errorTitle);
        alert.setContentText(errorDetails);
        alert.showAndWait();
    }


    public static void main(String[] args) {
        launch(args);
    }
}