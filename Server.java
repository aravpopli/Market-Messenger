import java.io.*;
import java.net.*;

public class Server {
    public static void main(String[] args) {
        ServerSocket serverSocket;

        try {
            serverSocket = new ServerSocket(16559);
            System.out.println("Server Started, Waiting for clients to connect........");
            int i = 0;
            while (true) {
                Socket clientSocket = serverSocket.accept();
                ++i;
                System.out.println("Client " + i  + " successfully connected");

                // Creating and starting the thread
                Thread th = new Thread(new ClientHandler(clientSocket));
                th.start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
