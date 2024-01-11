import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class Dashboard implements Runnable, Serializable {

    JButton option1;
    JButton option2;
    JButton option3;
    JButton option4;
    JButton option5;
    JButton option6;
    JButton option7;
    JButton option8;

    String accountType = "";
    Person user = null;
    JFrame frame;

    private BufferedReader bfr;
//    private PrintWriter pw;

    private ObjectInputStream ois;
    private ObjectOutputStream ous;

    Socket socket;
    static final String INVALID_INPUT = "Error! Invalid input!";
    static final String USER_NOT_FOUND = "User not found!";


    public void invalidInputMessage() {
        ImageIcon errorImage = new ImageIcon("errorIcon.png");
        JLabel icon1 = new JLabel(errorImage);
        JPanel panel1 = new JPanel();
        panel1.setLayout(new BorderLayout());
        panel1.add(icon1, BorderLayout.WEST);
        JOptionPane.showMessageDialog(null, panel1, INVALID_INPUT, JOptionPane.ERROR_MESSAGE);
    }

    public Dashboard(Socket socket, Person user, ObjectOutputStream ous, ObjectInputStream ois) {
        this.socket = socket;
        this.user = user;
        this.ous = ous;
        this.ois = ois;

    }

    public void run() {
        try {

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                "Login Error! Try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        if (user instanceof Seller) {
            accountType = "- Seller Account";
        }
        if (user instanceof Customer) {
            accountType = "- Customer Account";
        }
        frame = new JFrame();
        frame.setTitle(user.getName() + "'s" + " Dashboard " + accountType);
        Container content = frame.getContentPane();
        content.setLayout(new BorderLayout());

        frame.setSize(900, 500);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ous.writeObject("getUser");
                    ous.writeObject(user.getName());
                    ous.flush();
                    user = (Person) ois.readObject();
                } catch (Exception ignored) {

                }
                if (user instanceof Seller) {
                    //seller
                    if (e.getSource() == option1) {
                        sellerMenu(1, user);
                    }
                    if (e.getSource() == option2) {
                        sellerMenu(2, user);
                    }
                    if (e.getSource() == option3) {
                        sellerMenu(3, user);
                    }
                    if (e.getSource() == option4) {
                        sellerMenu(4, user);
                    }
                    if (e.getSource() == option5) {
                        sellerMenu(5, user);
                    }
                    if (e.getSource() == option6) {
                        sellerMenu(6, user);
                    }
                    if (e.getSource() == option7) {
                        sellerMenu(7, user);
                    }
                    if (e.getSource() == option8) {
                        sellerMenu(8, user);
                    }
                } else {
                    //customer
                    if (e.getSource() == option1) {
                        customerMenu(1, user);
                    }
                    if (e.getSource() == option2) {
                        customerMenu(2, user);
                    }
                    if (e.getSource() == option3) {
                        customerMenu(3, user);
                    }
                    if (e.getSource() == option4) {
                        customerMenu(4, user);
                    }
                    if (e.getSource() == option5) {
                        customerMenu(5, user);
                    }
                    if (e.getSource() == option6) {
                        customerMenu(6, user);
                    }
                    if (e.getSource() == option7) {
                        customerMenu(7, user);
                    }
                }

            }
        };
        JPanel dashboardPanelMessages = new JPanel();
        JPanel dashboardPanel = new JPanel();
        option1 = new JButton("View/Edit/Delete Messages");
        option1.addActionListener(actionListener);
        option2 = new JButton("Write a Message");
        option2.addActionListener(actionListener);
        option3 = new JButton("Import/Export File");
        option3.addActionListener(actionListener);
        dashboardPanelMessages.add(option1);
        dashboardPanelMessages.add(option2);
        dashboardPanelMessages.add(option3);

        if (user instanceof Customer) {

            //if customer
            option4 = new JButton("Block/Unblock a Seller");
            option4.addActionListener(actionListener);
            option5 = new JButton("Hide/Unhide Profile from a Seller");
            option5.addActionListener(actionListener);
            option6 = new JButton("View Statistics");
            option6.addActionListener(actionListener);
            option7 = new JButton("Logout");
            option7.addActionListener(actionListener);
            dashboardPanelMessages.add(option4);
            dashboardPanel.add(option5);
            dashboardPanel.add(option6);
            dashboardPanel.add(option7);
        } else {
            //if seller
            option4 = new JButton("Block/Unblock a Customer");
            option4.addActionListener(actionListener);
            option5 = new JButton("Hide/Unhide Profile from a Customer");
            option5.addActionListener(actionListener);
            option6 = new JButton("View Statistics");
            option6.addActionListener(actionListener);
            option7 = new JButton("Add Store");
            option7.addActionListener(actionListener);
            option8 = new JButton("Logout");
            option8.addActionListener(actionListener);
            dashboardPanelMessages.add(option4);
            dashboardPanel.add(option5);
            dashboardPanel.add(option6);
            dashboardPanel.add(option7);
            dashboardPanel.add(option8);
        }
        content.add(dashboardPanelMessages, BorderLayout.NORTH);
        content.add(dashboardPanel, BorderLayout.SOUTH);


    }

    public void sellerMenu(int num, Person user) {
        Seller s = (Seller) user;
        Object[] peopleToSelectFrom;
        switch (num) {
            case 1:
                messageOptions(user); // Allows any customer/seller to view their message threads
                break;
            case 2:
                directMessage(user); // Helper method for helping user write messages
                break;
            case 3:
                Object[] fileOptions = {1, 2};
                Object importOrExport = JOptionPane.showInputDialog(null,
                    "1. Export a File \n2. Import a File", "Export/Import",
                    JOptionPane.QUESTION_MESSAGE, null,
                    fileOptions, fileOptions[0]);
                if (importOrExport == null) return;
                int opt = (Integer) importOrExport;
                try {
                    switch (opt) {
                        case 1:
                            exportConversation(user);
                            break;
                        case 2:
                            importFile(user);
                            break;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 4:
                Object[] blockOptions = {1, 2};
                Object blockOrUnblock = JOptionPane.showInputDialog(null,
                    "Would you like to block (1) / unblock (2)", "Block",
                    JOptionPane.QUESTION_MESSAGE, null,
                    blockOptions, blockOptions[0]);
                if (blockOrUnblock == null) return;
                int optionChosen = (Integer) blockOrUnblock;
                peopleToSelectFrom = Customer.printAllCustomers(user);
                if (peopleToSelectFrom == null) {
                    JOptionPane.showMessageDialog(null, "No customers available.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                switch (optionChosen) {
                    case 1:

                        Object personBlocked = JOptionPane.showInputDialog(null,
                            "Who would you like to block?", "Block",
                            JOptionPane.QUESTION_MESSAGE, null,
                            peopleToSelectFrom, peopleToSelectFrom[0]);

                        String block = (String) personBlocked;
                        Customer toBlock = null;
                        try {
                            ous.writeObject("block");
                            ous.flush();
                            ous.writeObject("seller");
                            ous.writeObject(block);
                            ous.flush();
                            ous.writeObject(user);
                            ous.flush();
                            ous.writeObject(s);
                            ous.flush();
                            toBlock = (Customer) ois.readObject();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (toBlock == null) {
                            JOptionPane.showMessageDialog(null, USER_NOT_FOUND, null,
                                JOptionPane.ERROR_MESSAGE);
                            break;
                        }
                        toBlock.blockedByUser(s);
                        JOptionPane.showMessageDialog(null, "Blocked!",
                            "Successfully Blocked!", JOptionPane.PLAIN_MESSAGE);
                        try {
                            ous.writeObject("update");
                            ous.writeObject(toBlock);
                            ous.flush();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }                        break;
                    case 2:
                        Object personUnblocked = JOptionPane.showInputDialog(null,
                            "Who would you like to unblock?!",
                            "Unblock", JOptionPane.QUESTION_MESSAGE, null, peopleToSelectFrom,
                            peopleToSelectFrom[0]);
                        String unblock = (String) personUnblocked;
                        Customer toUnblock = null;
                        try {
                            ous.writeObject("unblock");
                            ous.writeObject("seller");
                            ous.writeObject(unblock);
                            ous.writeObject(user);
                            ous.writeObject(s);
                            ous.flush();
                            toUnblock = (Customer) ois.readObject();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (toUnblock == null) {
                            JOptionPane.showMessageDialog(null, USER_NOT_FOUND, null,
                                JOptionPane.ERROR_MESSAGE);
                            break;
                        }
                        JOptionPane.showMessageDialog(null, "Unblocked!", null,
                            JOptionPane.PLAIN_MESSAGE);
                        break;
                    default:
                        JOptionPane.showMessageDialog(null, "Invalid user", null,
                            JOptionPane.ERROR_MESSAGE);
                        break;
                }
                break;
            case 5:
                Object[] hideOptions = {1, 2};
                Object hideOrUnhideChosen = JOptionPane.showInputDialog(null,
                    "Would you like to hide (1) / unhide (2) from someone",
                    "Hide or Unhide", JOptionPane.QUESTION_MESSAGE, null, hideOptions, hideOptions[0]);
                if (hideOrUnhideChosen == null) return;
                int answer = (Integer) hideOrUnhideChosen;
                peopleToSelectFrom = Customer.printAllCustomers(user);
                if (peopleToSelectFrom == null) {
                    JOptionPane.showMessageDialog(null, "No customers available.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                switch (answer) {
                    case (1):
                        Object hideFromPerson = JOptionPane.showInputDialog(null,
                            "Who would you like to hide your profile from?", null,
                            JOptionPane.QUESTION_MESSAGE, null, peopleToSelectFrom,
                            peopleToSelectFrom[0]);

                        String hide = (String) hideFromPerson;
                        Customer toHide = (Customer) user.searchUser(hide);
                        if (toHide == null) {
                            JOptionPane.showMessageDialog(null, USER_NOT_FOUND, null,
                                JOptionPane.ERROR_MESSAGE);
                            break;
                        }
                        toHide.invisibleByUser(s);

                        JOptionPane.showMessageDialog(null, "Hidden!", null,
                            JOptionPane.PLAIN_MESSAGE);
                        try {
                            ous.writeObject("update");
                            ous.writeObject(toHide);
                            ous.flush();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }                        break;
                    case (2):
                        Object unhideFromPerson = JOptionPane.showInputDialog(null,
                            "Who would you like to unhide your profile from?", null,
                            JOptionPane.QUESTION_MESSAGE, null, peopleToSelectFrom,
                            peopleToSelectFrom[0]);

                        String unhide = (String) unhideFromPerson;
                        Customer toUnhide = (Customer) user.searchUser(unhide);
                        if (toUnhide == null) {
                            JOptionPane.showMessageDialog(null, USER_NOT_FOUND, null,
                                JOptionPane.ERROR_MESSAGE);
                            break;
                        }
                        toUnhide.unInvisibleByUser(s);
                        JOptionPane.showMessageDialog(null, "Unhidden!", null,
                            JOptionPane.PLAIN_MESSAGE);
                        try {
                            ous.writeObject("update");
                            ous.writeObject(toUnhide);
                            ous.flush();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }                        break;
                    default:
                        JOptionPane.showMessageDialog(null, "Invalid user", null,
                            JOptionPane.ERROR_MESSAGE);
                        break;
                }
                break;
            case 6:
                try {
                    ous.writeObject("stats");
                    ous.writeObject("seller");
                    ous.writeObject(s);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 8:
                try {
                    ous.writeObject("update");
                    ous.writeObject(user);
                    ous.flush();

                } catch (Exception e) {
                    e.printStackTrace();
                }

                JOptionPane.showMessageDialog(null, "Thank you for choosing our service! " +
                    "Have a good day!", null, JOptionPane.PLAIN_MESSAGE);
                frame.dispose();
                break;
            case 7:
                String storeName = JOptionPane.showInputDialog(null,
                    "What is the store name going to be?", "Store Name",
                    JOptionPane.QUESTION_MESSAGE);
                if (storeName == null) return;
                if (s.addStore(storeName)) {
                    // adds store to Seller
                    try {
                        ous.writeObject("update");
                        ous.writeObject(user);
                        ous.flush();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    JOptionPane.showMessageDialog(null, "Store added!", null,
                        JOptionPane.PLAIN_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid name!", null,
                        JOptionPane.ERROR_MESSAGE);
                }
                break;
        }
    }

    public void customerMenu(int num, Person user) {
        Customer c = (Customer) user;

        switch (num) {
            case 1:
                messageOptions(user);
                break;
            case 2:
                directMessage(user);
                break;
            case 3:
                Object[] fileOptions = {1, 2};
                Object importOrExport = JOptionPane.showInputDialog(null,
                    "1. Export a File \n2. Import a File", "Export/Import",
                    JOptionPane.QUESTION_MESSAGE, null,
                    fileOptions, fileOptions[0]);
                if (importOrExport == null) return;
                int opt = (Integer) importOrExport;
                try {
                    switch (opt) {
                        case 1:
                            exportConversation(user);
                            break;
                        case 2:
                            importFile(user);
                            break;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 4:
                Object[] blockOrUnblock = {1, 2};
                Object optionChosen = JOptionPane.showInputDialog(null,
                    "Would you like to block (1) / unblock (2)", null, JOptionPane.QUESTION_MESSAGE, null,
                    blockOrUnblock, blockOrUnblock[0]);
                if (optionChosen == null) return;
                Integer choice = (Integer) optionChosen;
                switch (choice) {
                    case 1:
                        Object[] peopleToBlockList = Seller.printAllSellers(user);
                        if (peopleToBlockList == null) {
                            JOptionPane.showMessageDialog(null, "No sellers available",
                                "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        Object personChosen = JOptionPane.showInputDialog(null,
                            "Who would you like to block?", null, JOptionPane.QUESTION_MESSAGE, null,
                            peopleToBlockList, peopleToBlockList[0]);
                        if (personChosen == null) return;
                        String block = (String) personChosen;
                        Seller toBlock = null;
                        try {
                            ous.writeObject("block");
                            ous.flush();
                            ous.writeObject("customer");
                            ous.writeObject(user);
                            ous.writeObject(block);
                            ous.writeObject(c);
                            ous.flush();
                            toBlock =(Seller) ois.readObject();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (toBlock == null) {
                            JOptionPane.showMessageDialog(null, "User not found!", null,
                                JOptionPane.ERROR_MESSAGE);

                            break;
                        }
                        JOptionPane.showMessageDialog(null, "Blocked!", null,
                            JOptionPane.INFORMATION_MESSAGE);
                        try {
                            ous.writeObject("update");
                            ous.writeObject(toBlock);
                            ous.flush();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case 2:
                        Object[] peopleToUnblockList = Seller.printAllSellers(user);
                        if (peopleToUnblockList == null) {
                            JOptionPane.showMessageDialog(null, "No sellers available",
                                "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        Object personChosenToUnblock = JOptionPane.showInputDialog(null,
                            "Who would you like to unblock?", null, JOptionPane.QUESTION_MESSAGE,
                            null, peopleToUnblockList, peopleToUnblockList[0]);
                        if (personChosenToUnblock == null) return;
                        String unblock = (String) personChosenToUnblock;
                        Seller toUnblock = null;
                        try {
                            ous.writeObject("unblock");
                            ous.writeObject("customer");
                            ous.writeObject(user);
                            ous.writeObject(unblock);
                            ous.writeObject(c);
                            ous.flush();
                            toUnblock = (Seller) ois.readObject();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (toUnblock == null) {
                            JOptionPane.showMessageDialog(null, "User not found!", null,
                                JOptionPane.ERROR_MESSAGE);
                            break;
                        }
                        JOptionPane.showMessageDialog(null, "Unblocked!", null,
                            JOptionPane.INFORMATION_MESSAGE);
                        break;
                    default:
                        JOptionPane.showMessageDialog(null, "A valid option was not selected",
                            null, JOptionPane.ERROR_MESSAGE);
                        break;
                }
                break;
            case 5:
                Object[] optionList = {1, 2};
                Object option = JOptionPane.showInputDialog(null,
                    "Would you like to hide (1) / unhide (2) from someone",
                    null, JOptionPane.QUESTION_MESSAGE, null, optionList, optionList[0]);
                if (option == null) return;
                Integer answer = (Integer) option;
                switch (answer) {
                    case 1:
                        Object[] usersToHideFromList = Seller.printAllSellers(user);
                        if (usersToHideFromList == null) {
                            JOptionPane.showMessageDialog(null, "No sellers available",
                                "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        Object userSelected = JOptionPane.showInputDialog(null,
                            "Who would you like to hide your profile from?",
                            null, JOptionPane.QUESTION_MESSAGE, null, usersToHideFromList,
                            usersToHideFromList[0]);
                        if (userSelected == null) return;
                        String hide = (String) userSelected;
                        Seller toHide = (Seller) user.searchUser(hide);
                        if (toHide == null) {
                            JOptionPane.showMessageDialog(null, "User not found!");
                            break;
                        }
                        toHide.invisibleByUser(c);
                        JOptionPane.showMessageDialog(null, "Hidden!");
                        try {
                            ous.writeObject("update");
                            ous.writeObject(toHide);
                            ous.flush();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }                        break;
                    case 2:
                        Object[] usersToUnhideFromList = Seller.printAllSellers(user);
                        if (usersToUnhideFromList == null) {
                            JOptionPane.showMessageDialog(null, "No sellers available",
                                "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        Object usersToUnhideFrom = JOptionPane.showInputDialog(null,
                            "Who would you like to unhide your profile from?",
                            null, JOptionPane.QUESTION_MESSAGE, null, usersToUnhideFromList,
                            usersToUnhideFromList[0]);
                        if (usersToUnhideFrom == null) return;
                        String unhide = (String) usersToUnhideFrom;
                        Seller toUnhide = (Seller) user.searchUser(unhide);
                        if (toUnhide == null) {
                            JOptionPane.showMessageDialog(null, "User not found!");
                            break;
                        }
                        toUnhide.unInvisibleByUser(c);
                        JOptionPane.showMessageDialog(null, "Unhidden!");
                        try {
                            ous.writeObject("update");
                            ous.writeObject(toUnhide);
                            ous.flush();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }                        break;
                    default:
                        JOptionPane.showMessageDialog(null, "Invalid user", null,
                            JOptionPane.ERROR_MESSAGE);
                        break;
                }
                break;
            case 6:
                try {
                    ous.writeObject("stats");
                    ous.writeObject("customer");
                    ous.writeObject(c);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 7:
                try {
                    ous.writeObject("update");
                    ous.writeObject(c);
                    ous.flush();

                } catch (Exception e) {
                    e.printStackTrace();
                }                JOptionPane.showMessageDialog(null, "Thank you for choosing our service! " +
                    "Have a good day!", null, JOptionPane.PLAIN_MESSAGE);
                frame.dispose();
                break;
            default:
                JOptionPane.showMessageDialog(null, "A valid option was not selected",
                    null, JOptionPane.ERROR_MESSAGE);
                break;
        }
    }

    public void messageOptions(Person user) {
        Object[] choiceSelection = {1, 2, 3};
        Object selection = JOptionPane.showInputDialog(null,
            "Would you like to\n1.View\n2.Edit\n3.Delete", "Message Threads",
            JOptionPane.INFORMATION_MESSAGE, null,
            choiceSelection, choiceSelection[0]);
        String key = "";
        String filename = "";
        if (selection == null) {
            return;
        }
        switch ((Integer) selection) {
            case 1:
                key = "view";
                break;
            case 2:
                key = "edit";
                break;
            case 3:
                key = "delete";
                break;
            default:
                return;
        }

        if (user instanceof Seller) { // Commands to run as a seller
            Seller s = (Seller) user;
            filename = sellerCommandFlow(s, key);
        } else {
            Customer c = (Customer) user;
            filename = customerCommandFlow(c);
        }

        if (filename == null) return;
        if (filename.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No message threads found", null,
                JOptionPane.PLAIN_MESSAGE);
        } else {
            //view messages between users
            filename = "..\\" + filename;
            JFrame f = viewMessages(filename);
            switch ((Integer) selection) {
                case 1:
                    break;
                case 2:
                    try {
                        if (editThread(filename)) {
                            f.dispose();
                            viewMessages(filename);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    break;
                case 3:
                    try {
                        if (deleteThread(filename)) {
                            f.dispose();
                            viewMessages(filename);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;
            }

        }
    }

    public String customerCommandFlow(Customer c) {
        String[] stores = c.getContactedStores();
        if (stores.length == 0) {
            return "";
        } else {
            Object storeSelected = JOptionPane.showInputDialog(null,
                "Select a store thread to view",
                "Select Store", JOptionPane.QUESTION_MESSAGE, null, stores, stores[0]);
            if (storeSelected == null) return null;
            String store = (String) storeSelected;

            return c.getName() + "_thread_" + store + ".txt";
        }
    }

    public String sellerCommandFlow(Seller seller, String key) {

        String[] stores = seller.getStores(); //their own store
        if (stores.length == 0) {
            return null;
        } else {
            Object storeChosen = JOptionPane.showInputDialog(null,
                String.format("Which store's customers do you want to %s?\n", key),
                "Message Threads", JOptionPane.QUESTION_MESSAGE, null, stores, stores[0]);
            if (storeChosen == null) {
                return null;
            }
            String store = (String) storeChosen;

            String[] customers = seller.getCustomers(store);
            if (customers.length == 0) {
                return "";
            }
            Object customerObj = JOptionPane.showInputDialog(null,
                String.format("Select a customer thread to %s or type exit", key),
                "Message Threads", JOptionPane.QUESTION_MESSAGE, null, customers,
                customers[0]);
            String customer = (String) customerObj;
            if (customer == null) {
                return null;
            }
            return store + "_thread_" + customer + ".txt";
        }
    }

    public JFrame viewMessages(String filename) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JFrame f = new JFrame();
        f.setSize(700, 500);
        f.setLayout(new BorderLayout());
        f.setVisible(true);
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line = br.readLine();
            while (line != null) {
                //this puts every text message into a label between users
                panel.add(new JLabel(line + "\n"), BorderLayout.WEST);
                line = br.readLine();
            }

            JScrollPane jsp = new JScrollPane(panel);
            jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            jsp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
            f.getContentPane().add(jsp);
            if (jsp.getComponentCount() == 0) {
                JOptionPane.showMessageDialog(null, INVALID_INPUT, "Error Message",
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, INVALID_INPUT, "Error Message",
                JOptionPane.ERROR_MESSAGE);
            return null;
        }
        return f;
    }

    public boolean editThread(String filename) {
        try {
            ous.writeObject("edit");
            ous.writeObject(filename);
            ous.flush();
            ArrayList<String> list = (ArrayList<String>) ois.readObject();
            if (list.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No messages to edit!",
                        "Edit", JOptionPane.ERROR_MESSAGE);
                ous.writeObject(new Error());
                ous.flush();
                return false;
            }
            Object[] messagesToEdit = list.toArray();
            Object messageChosen = JOptionPane.showInputDialog(null,
                    "Which message would you like to edit from this thread", null,
                    JOptionPane.QUESTION_MESSAGE, null, messagesToEdit, messagesToEdit[0]);
            if (messageChosen == null) {
                ous.writeObject(new Error());
                ous.flush();
                ois.readObject();
                return false;
            }
            String message = (String) messageChosen;
            ous.writeObject(message);
            ous.flush();

            String newMessage = JOptionPane.showInputDialog(null,
                    "What would you like to edit the message to?", "Edit", JOptionPane.QUESTION_MESSAGE);
            if (newMessage == null || newMessage.isEmpty()) {
                ous.writeObject(new Error());
                ous.flush();
                ois.readObject();
                return false;
            }
            ous.writeObject(newMessage);
            ous.flush();
            return (boolean) ois.readObject();
        } catch (ClassCastException cce) {
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * deletes a message from a message thread for the requesting party
     *
     * @param filename the file name of the message thread
     * @return whether a message is deleted successfully
     */
    public boolean deleteThread(String filename) {
        try {
            ous.writeObject("delete");
            ous.writeObject(filename);
            ous.flush();

            Object[] listToReadFrom = (Object[]) ois.readObject();
            Object messageChosen = JOptionPane.showInputDialog(null,
                    "Which message would you like to delete from this thread", "Delete",
                    JOptionPane.QUESTION_MESSAGE, null, listToReadFrom, listToReadFrom[0]);
            if (messageChosen == null) {
                ous.writeObject(new Error());
                ous.flush();
                ois.readObject();
                return false;
            } else {
                ous.writeObject(messageChosen);
                ous.flush();
            }

            return (boolean) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList<String> readFileArrayList(String filename) {
        ArrayList<String> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line = br.readLine();
            while (line != null) {
                list.add(line);
                line = br.readLine();
            }
            return list;
        } catch (IOException e) {
            System.out.println("Thread doesn't exist");
            return null;
        }
    }

    public void directMessage(Person user) {
        String key;
        Person p = null;
        if (user instanceof Customer) {
            key = "stores";
        } else {
            key = "customers";
        }
        Integer num = JOptionPane.showConfirmDialog(null,
            String.format("Would you like a list of all %s to message?\n", key),
            "Stores to Message", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
        if (num == -1) return;
        if (num.equals(0)) {
            String[] list = retrieveKey(user);
            if (list != null) {
                Object query = JOptionPane.showInputDialog(null,
                    String.format("Enter %s to message", key.substring(0, key.length() - 1)),
                    "Write Message", JOptionPane.QUESTION_MESSAGE, null, list, list[0]);
                if (query == null) return;
                p = searchQuery((String) query, user);

                writeMessage(user, p, (String) query);
            } else {
                JOptionPane.showMessageDialog(null, String.format("No %s in marketplace!\n", key),
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            String query = JOptionPane.showInputDialog(null,
                String.format("Enter %s to message\n", key.substring(0, key.length() - 1)),
                "Write Message", JOptionPane.QUESTION_MESSAGE);
            if (query == null) return;
            p = searchQuery(query, user);
            if (p == null) {
                JOptionPane.showMessageDialog(null, "User not found", "Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            writeMessage(user, p, query);

        }
    }

    public void writeMessage(Person user, Person other, String storeName) {

        if (user.isBlockedBy(other.getName())) {
            JOptionPane.showMessageDialog(null, "This user has blocked you.",
                "Blocked by User", JOptionPane.PLAIN_MESSAGE);
            return;
        }
        int response = 0;
        String messageSent;
        while (response == 0) {
            try {
                ous.writeObject("message");
                ous.flush();

                String timeStamp = new SimpleDateFormat("(dd/MM/yyyy-HH:mm:ss) ").format(new java.util.Date());
                messageSent = JOptionPane.showInputDialog(null, "What would you like to send?",
                        "Sending a message", JOptionPane.QUESTION_MESSAGE);
                if (messageSent == null) {
                    ous.writeObject(new Error());
                    ous.flush();
                    return;
                }
                if (messageSent.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Cannot send empty messages!", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    ous.writeObject(new Error());
                    ous.flush();
                    return;
                }
                if (user instanceof Customer) {
                    ous.writeObject("customer");
                    ous.flush();

                    Customer c = (Customer) (user);
                    Seller s = (Seller) other;

                    ous.writeObject(c);
                    ous.flush();
                    ous.writeObject(other);
                    ous.flush();
                    ous.writeObject(storeName);
                    ous.flush();
                    ous.writeObject(timeStamp);
                    ous.flush();
                    ous.writeObject(messageSent);
                    ous.flush();

                } else if (((Seller) user).getStores().length == 0) {
                    JOptionPane.showMessageDialog(null, "No Stores to Message From!",
                            null, JOptionPane.PLAIN_MESSAGE);
                    ous.writeObject(new Error());
                    ous.flush();
                } else {
                    ous.writeObject("seller");
                    ous.flush();

                    Seller s = (Seller) (user);
                    Customer c = (Customer) other;
                    Object[] listOfStores = s.getStores();
                    Object storeChosen = JOptionPane.showInputDialog(null, "Which store would you like to message from?",
                            null, JOptionPane.PLAIN_MESSAGE, null, listOfStores, listOfStores[0]);
                    if (storeChosen == null) {
                        ous.writeObject(new Error());
                        ous.flush();
                        return;
                    }
                    String store = (String) storeChosen;
                    ous.writeObject(s);
                    ous.flush();
                    ous.writeObject(other);
                    ous.flush();
                    ous.writeObject(store);
                    ous.flush();
                    ous.writeObject(timeStamp);
                    ous.flush();
                    ous.writeObject(messageSent);
                    ous.flush();
                }
                response = JOptionPane.showConfirmDialog(null, "Would you like to message anything else\n",
                        null, JOptionPane.YES_NO_OPTION);
            } catch (ClassCastException cce) {
                continue;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public String[] retrieveKey(Person user) {
        if (user instanceof Customer) {
            return Seller.printAllStores(user);
        } else {
            return Customer.printAllCustomers(user);
        }
    }

    /*
     * Checks whether user requested to message exists
     */
    public Person searchQuery(String query, Person user) {
        Person p = null;
        if (user instanceof Customer) {
            p = Seller.searchStore(query);
        } else {
            p = Customer.searchCustomer(query);
        }
        return p;
    }

    ImageIcon errorIcon = new ImageIcon("errorImage.png");
    ImageIcon successIcon = new ImageIcon("successIcon.png");
    ImageIcon fileIcon = new ImageIcon("fileImage.png");
    public void exportConversation(Person user) {
        try {
            System.out.println("exporting");
            ous.writeObject("exportFile");
            ous.writeObject(user);
            // Checking if it is an instance of Customer or Seller
            if (user instanceof Customer) {
                String[] stores = (String[]) ois.readObject();
                if (stores.length == 0) {
                    JOptionPane.showMessageDialog(null, "No message threads found",
                            "Exporting", JOptionPane.ERROR_MESSAGE, errorIcon);
                    ous.writeObject(new Error());
                    ous.flush();
                } else {
                    String store = (String)JOptionPane.showInputDialog(null,
                            "Select a store thread to export", "Exporting",
                            JOptionPane.INFORMATION_MESSAGE, fileIcon,
                            stores, stores[0]);
                    if (store == null) {
                        ous.writeObject(new Error());
                        ous.flush();
                        return;
                    } else {
                        ous.writeObject(store);
                        ous.flush();
                    }
                    JOptionPane.showMessageDialog(null,
                            "Exported successfully! Please logout to see the file.", "Exporting",
                            JOptionPane.INFORMATION_MESSAGE, successIcon);
                }


            } else {
                Seller seller = (Seller) user;
                String[] stores = (String[]) ois.readObject();
                if (stores.length == 0) {
                    JOptionPane.showMessageDialog(null, "You've created no message threads",
                            "Exporting", JOptionPane.ERROR_MESSAGE, errorIcon);
                    ous.writeObject(new Error());
                    ous.flush();
                } else {
                    String store = (String) JOptionPane.showInputDialog(null,
                            "Which store's customers do you want to export?", "Exporting",
                            JOptionPane.QUESTION_MESSAGE, fileIcon, stores, stores[0]);
                    if (store == null) {
                        ous.writeObject(new Error());
                        ous.flush();
                        return;
                    } else {
                        ous.writeObject(store);
                        ous.flush();
                    }
                    String[] customers = (String[]) ois.readObject();
                    if (customers.length == 0) {
                        JOptionPane.showMessageDialog(null, "No customers found",
                                "Exporting", JOptionPane.ERROR_MESSAGE, errorIcon);
                        ous.writeObject(new Error());
                        ous.flush();
                        return;
                    }
                    String customer = (String) JOptionPane.showInputDialog(null,
                            "Select a customer thread to export", "Exporting",
                            JOptionPane.QUESTION_MESSAGE, fileIcon, customers, customers[0]);
                    if (customer == null) {
                        ous.writeObject(new Error());
                        ous.flush();
                        return;
                    } else {
                        ous.writeObject(customer);
                        ous.flush();
                    }

                    JOptionPane.showMessageDialog(null,
                            "Exported successfully! Please logout to see the file.", "Exporting",
                            JOptionPane.INFORMATION_MESSAGE, successIcon);
                }



            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "An Error has occurred", "Exporting",
                    JOptionPane.ERROR_MESSAGE, errorIcon);
        }

    }
    public void importFile(Person user) {
        try {
            ous.writeObject("importFile");
            ous.writeObject(user);
            ous.flush();
            if (user instanceof Seller) {
                String[] stores = (String[]) ois.readObject();
               if (stores.length == 0) {
                    JOptionPane.showMessageDialog(null, "You've created no message threads",
                            "Importing", JOptionPane.ERROR_MESSAGE, errorIcon);
                    ous.writeObject(new Error());
                    ous.flush();
                } else {
                    String store = (String) JOptionPane.showInputDialog(null,
                            "Which store's customers do you want to import the conversation to?",
                            "Importing", JOptionPane.QUESTION_MESSAGE, fileIcon, stores, stores[0]);

                    if (store == null) {
                        ous.writeObject(new Error());
                        ous.flush();
                    }
                    ous.writeObject(store);
                    ous.flush();

                    String[] customers = (String[]) ois.readObject();
                    if (customers.length == 0) {
                        JOptionPane.showMessageDialog(null, "No messaging threads found.",
                                "Importing", JOptionPane.ERROR_MESSAGE, errorIcon);
                        ous.writeObject(new Error());
                        ous.flush();
                        return;
                    }
                    String customer = (String) JOptionPane.showInputDialog(null,
                            "Select a customer thread to import conversation", "Importing",
                            JOptionPane.QUESTION_MESSAGE, fileIcon, customers, customers[0]);
                    if (customer == null) {
                        ous.writeObject(new Error());
                        ous.flush();
                        return;
                    }
                    ous.writeObject(customer);
                    ous.flush();

                    String fileName = JOptionPane.showInputDialog(null, "Enter the file name: ",
                            "Importing", JOptionPane.QUESTION_MESSAGE);
                    if (fileName == null) {
                        ous.writeObject(new Error());
                        return;
                    }
                    File f = new File("..\\" + fileName);
                    if (!f.exists() || f.isDirectory()) {
                        fileName = JOptionPane.showInputDialog(null,
                                "Invalid file path. Please enter the correct name.", "Importing",
                                JOptionPane.QUESTION_MESSAGE);
                        if (fileName == null) return;
                        f = new File("..\\" + fileName);
                    }
                    ous.writeObject(f);
                    JOptionPane.showMessageDialog(null, "Successfully imported the file!",
                            "Importing", JOptionPane.INFORMATION_MESSAGE, successIcon);
                }
            } else { // instanceOf customer
                Customer customer = (Customer) user;
                String[] stores = (String[]) ois.readObject();
                if (stores.length == 0) {
                    JOptionPane.showMessageDialog(null, "No message threads found",
                            "Importing", JOptionPane.ERROR_MESSAGE, errorIcon);
                    ous.writeObject(new Error());
                    ous.flush();
                } else {
                    String store = (String) JOptionPane.showInputDialog(null,
                            "Select a store thread to import", "Importing", JOptionPane.QUESTION_MESSAGE,
                            null, stores, stores[0]);
                    if (store == null) {
                        ous.writeObject(new Error());
                        ous.flush();
                        return;
                    }
                    ous.writeObject(store);
                    ous.flush();
                    String fileName = JOptionPane.showInputDialog(null, "Enter the file name: ",
                            "Importing", JOptionPane.QUESTION_MESSAGE);
                    if (fileName == null) {
                        ous.writeObject(new Error());
                        ous.flush();
                        return;
                    }
                    File f = new File("..\\" + fileName);
                    if (!f.exists() || f.isDirectory()) {
                        fileName = JOptionPane.showInputDialog(null,
                                "Invalid file path. Please enter the correct name.", "Importing",
                                JOptionPane.QUESTION_MESSAGE);
                        if (fileName == null) return;
                        f = new File("..\\" + fileName);
                    }
                    ous.writeObject(f);

                    JOptionPane.showMessageDialog(null, "Successfully imported the file!",
                            "Importing", JOptionPane.INFORMATION_MESSAGE, successIcon);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

