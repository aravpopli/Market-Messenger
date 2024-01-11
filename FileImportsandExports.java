import com.sun.tools.javac.Main;

import javax.swing.*;
import java.io.*;
import java.sql.SQLOutput;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * FileImportsandExports Class
 * <p>
 * handles imports and exports (all file related )
 *
 * @author Irfan
 * @version 11.12.23
 */

public class FileImportsandExports {
    static ImageIcon errorIcon = new ImageIcon("errorImage.png");
    static ImageIcon successIcon = new ImageIcon("successIcon.png");
    static ImageIcon fileIcon = new ImageIcon("fileImage.png");
    public static void exportConversation(Person user, ObjectInputStream ois, ObjectOutputStream ous) {
        try {
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

    // Important file type: cannot be exported conversation, should be text file
    // It does not make sense for a customer to import a seller's message to him
    // Likewise, it does not make sense for a seller to be allowed to import a
    // customer's message
    // Additionally, in the txt files the "*" following the message indicates that
    // it is imported

    public static void importFile(Person user, ObjectInputStream ois, ObjectOutputStream ous) {
        try {
            ous.writeObject("importFile");
            ous.writeObject(user);
            ous.flush();

            if (user instanceof Seller) {
                String[] stores = (String[]) ois.readObject();
                System.out.println("got stores");
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
                    System.out.println("sent store");

                    String[] customers = (String[]) ois.readObject();
                    System.out.println("read customers");
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
