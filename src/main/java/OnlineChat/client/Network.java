package OnlineChat.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


public class Network {

    public static final String SERVER_ADDRESS = "localhost";
    public static final int SERVER_PORT = 8189;

    private final String host;
    private final int port;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;

    public Network() {
        this(SERVER_ADDRESS, SERVER_PORT);
    }


    public Network(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public boolean connect() {
        try {
            Socket clientSocket = new Socket(host, port);
            inputStream = new DataInputStream(clientSocket.getInputStream());
            outputStream = new DataOutputStream(clientSocket.getOutputStream());
            return true;
        } catch (IOException e) {
            System.err.println("Connection has not been established");
            e.printStackTrace();
            return false;
        }
    }

    public DataInputStream getInputStream() {
        return inputStream;
    }

    public DataOutputStream getOutputStream() {
        return outputStream;
    }

    public void waitMessages(Controller controller) {
        Thread thread = new Thread(() -> {
            try {
                while (true) {
                    String message = inputStream.readUTF();
                    controller.appendMessage(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Connection has been lost.");
            }
        });
        thread.setDaemon(true);
        thread.start();
    }
}
