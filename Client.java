import javax.swing.*;
import java.net.*;

public class Client {

    public static void main(String[] args) {
       Socket socket = null;
        try {
            socket = new Socket("localhost",16559);
            SwingUtilities.invokeLater(new Login(socket));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Server Connection Error", "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

}
