import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.io.*;

import javax.swing.border.Border;
import java.net.*;

public class Login extends JComponent implements Runnable, Serializable {
    Person user = null;
    JFrame frame;

    private ObjectInputStream ois;
    private ObjectOutputStream ous;

    Socket socket;
    static final String INVALID_INPUT = "Error! Invalid input!";
    static final String USER_NOT_FOUND = "User not found!";
    static final String ACCOUNT_TYPE = "Welcome to a simplified version of the Amazon Marketplace! Creating an account "
            + "is quick and easy!\nThere are two account " +
            "selections: Seller or Customer.\nPlease enter 1 for Seller or 2 for Customer.";

    public void invalidInputMessage() {
        ImageIcon errorImage = new ImageIcon("errorIcon.png");
        JLabel icon1 = new JLabel(errorImage);
        JPanel panel1 = new JPanel();
        panel1.setLayout(new BorderLayout());
        panel1.add(icon1, BorderLayout.WEST);
        JOptionPane.showMessageDialog(null, panel1, INVALID_INPUT, JOptionPane.ERROR_MESSAGE);
    }

    public Login(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            ous = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Login Error! Try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        ImageIcon userImage = new ImageIcon("userImageFinal.png");
        JLabel iconForUser = new JLabel(userImage);
        JLabel welcomeText = new JLabel("Welcome to Amazon!");
        welcomeText.setForeground(new Color(233, 110, 9));
        welcomeText.setHorizontalTextPosition(JLabel.CENTER);
        welcomeText.setVerticalTextPosition(JLabel.TOP);
        welcomeText.setFont(new Font("MV Boli", Font.BOLD, 25));
        welcomeText.setBackground(new Color(0, 0, 0));
        welcomeText.setOpaque(true);
        JPanel panelForUser = new JPanel();
        panelForUser.setLayout(new BorderLayout());
        panelForUser.add(iconForUser, BorderLayout.WEST);
        ImageIcon amazonImage = new ImageIcon("amazonImage.jpeg");
        JLabel icon = new JLabel(amazonImage);
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(icon, BorderLayout.CENTER);
        panel.add(welcomeText, BorderLayout.NORTH);
        ImageIcon userIcons = new ImageIcon("orangeUserIcons.png");

        int num = JOptionPane.showConfirmDialog(panel,
                "Already a member?", "Information Message",
                JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, userIcons);

        switch (num) {
            case 0:
                int accountType = JOptionPane.showConfirmDialog(null,
                        "Are you a returning seller?", "choose one",
                        JOptionPane.YES_NO_OPTION);
                switch (accountType) {
                    case 0:
                        user = loggingInSellerAndCustomer(true);
                        break;
                    case 1:
                        user = loggingInSellerAndCustomer(false);
                        break;
                }
                break;
            case 1:
                Object[] options = {1, 2};
                Object accountNum = JOptionPane.showInputDialog(null,
                        ACCOUNT_TYPE, "Creating a new account",
                        JOptionPane.INFORMATION_MESSAGE, userIcons,
                        options, options[0]);
                if (accountNum == null) return;


                switch ((Integer) accountNum) {
                    case 1:
                    case 2:
                        try {
                            user = createAccount((Integer) accountNum);
                        } catch (IllegalArgumentException e) {
                            invalidInputMessage();
                        }
                        break;
                    default:
                }
                break;
            default:

                return;
        }

        SwingUtilities.invokeLater(new Dashboard(socket, user, ous, ois));


    }

    public Person loggingInSellerAndCustomer(boolean isSeller) {
        String email;
        String password;
        while (true) {
            try {
                ous.writeObject("login");
                ous.flush();
                email = JOptionPane.showInputDialog(null, "Enter your email address",
                        "Email Address", JOptionPane.PLAIN_MESSAGE);
                password = JOptionPane.showInputDialog(null, "Enter your password",
                        "Password", JOptionPane.PLAIN_MESSAGE);

                if (email ==  null || email.isEmpty() || password == null || password.isEmpty()) {
                    JOptionPane.showMessageDialog(null, INVALID_INPUT, "Error Message",
                            JOptionPane.ERROR_MESSAGE);
                    ous.writeObject(new Error());
                    ous.flush();
                    continue;
                }

                try {
                    if (isSeller) {
                        ous.writeObject("seller");
                    } else {
                        ous.writeObject("customer");
                    }
                    ous.writeObject(email);
                    ous.writeObject(password);
                    ous.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                user = (Person) ois.readObject();
                JOptionPane.showMessageDialog(null, "Successfully logging you in...",
                        "Account found!",
                        JOptionPane.PLAIN_MESSAGE);
                return user;

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, INVALID_INPUT, "Error Message",
                        JOptionPane.ERROR_MESSAGE);
            }
        }

    }

    public Person createAccount(int accountNum) throws IllegalArgumentException {
        try {
            String emailAddress;
            String username;
            String password;
            while (true) {
                try {
                    ous.writeObject("create");
                    ous.flush();

                    emailAddress = JOptionPane.showInputDialog(null, "Enter a valid email address",
                            "Email Address", JOptionPane.PLAIN_MESSAGE);
                    username = JOptionPane.showInputDialog(null, "Enter a valid username:",
                            "Username", JOptionPane.PLAIN_MESSAGE);
                    password = JOptionPane.showInputDialog(null, "Enter a valid password:",
                            "Password", JOptionPane.PLAIN_MESSAGE);
                    if (emailAddress ==  null || emailAddress.isEmpty() || username == null || username.isEmpty() ||
                            password == null || password.isEmpty()) {
                        JOptionPane.showMessageDialog(null, INVALID_INPUT, "Error Message",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, INVALID_INPUT, "Error Message",
                            JOptionPane.ERROR_MESSAGE);
                    System.out.println("writing error");
                    ous.writeObject(new Error()); // signals restart on process
                    ous.flush();
                    continue;
                }
                ous.writeObject(accountNum);
                ous.writeObject(emailAddress);
                ous.writeObject(username);
                ous.writeObject(password);
                ous.flush();

                try {
                    user = (Person) ois.readObject();
                    JOptionPane.showMessageDialog(null, "Your account has been successfully made!",
                            "Success!", JOptionPane.PLAIN_MESSAGE);
                    return user;

                } catch (ClassCastException e) {
                    JOptionPane.showMessageDialog(null, INVALID_INPUT, "Error Message",
                            JOptionPane.ERROR_MESSAGE);
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}

