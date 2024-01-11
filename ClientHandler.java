import java.net.Socket;
import javax.swing.*;
import java.io.*;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ClientHandler implements Runnable {

    private Socket socket;
    Person user = null;

    ObjectInputStream ois;
    ObjectOutputStream ous;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            Socket socket = this.socket;

            ous = new ObjectOutputStream(socket.getOutputStream());
            ous.flush();
            ois = new ObjectInputStream(socket.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }

        while (true) {
            try {

                Object entryCommandObject = ois.readObject();
                String entryCommand = (String) entryCommandObject;
                System.out.println(entryCommand);

                if (entryCommand.contains("login")) {
                    String entity = (String) ois.readObject();
                    String email = (String) ois.readObject();
                    String password = (String) ois.readObject();
                    Person user = login(entity, email, password);
                    if (user == null) {
                        ous.writeObject(new Error());
                    } else {
                        ous.writeUnshared(user);
                    }
                    ous.flush();
                } else if (entryCommand.contains("create")) {
                    int acc = (Integer) ois.readObject();
                    String email1 = (String) ois.readObject();
                    String name = (String) ois.readObject();
                    String pass = (String) ois.readObject();
                    user = create(acc, email1, name, pass);
                    if (user == null) {
                        System.out.println("writing error");
                        ous.writeObject(new Error());
                    } else {
                        ous.writeObject(user);
                    }
                    ous.flush();
                } else if (entryCommand.contains("message")) {
                    String type = (String) ois.readObject();
                    if (type.equals("customer")) {

                        Customer c = (Customer) ois.readObject();
                        Person other = (Person) ois.readObject();
                        Seller s = (Seller) other;
                        String storeName = (String) ois.readObject();
                        String timeStamp = (String) ois.readObject();
                        String messageSent = (String) ois.readObject();

                        c.addContactedStore(storeName);
                        c.writeMessage(storeName, c.getName(), timeStamp + "Me: " + messageSent);

                        s.addCustomer(c.getName(), storeName);
                        s.writeMessage(storeName, c.getName(), timeStamp + c.getName() + ": " + messageSent);
                        Seller.updateSellers(s);
                        Customer.updateCustomers(c);


                    } else if (type.equals("seller")) {
                        Seller s = (Seller) ois.readObject();
                        Person other = (Person) ois.readObject();
                        Customer c = (Customer) other;
                        String store = (String) ois.readObject();
                        String timeStamp = (String) ois.readObject();
                        String messageSent = (String) ois.readObject();

                        s.writeMessage(store, other.getName(), timeStamp + "Me: " + messageSent);
                        c.addContactedStore(store);
                        s.addCustomer(c.getName(), store);
                        c.writeMessage(store, other.getName(), timeStamp + store + ": " + messageSent);
                        Customer.updateCustomers(c);
                        Seller.updateSellers(s);
                    }

                } else if (entryCommand.contains("edit")) {
                    String filename = (String) ois.readObject();
                    ous.writeObject(editThread(filename));
                    ous.flush();
                } else if (entryCommand.contains("delete")) {
                    String file = (String) ois.readObject();
                    ous.writeObject(deleteThread(file));
                    ous.writeObject(false);
                    ous.flush();
                } else if (entryCommand.contains("exportFile")) {
                    Person exporter = (Person) ois.readObject();
                    exportFile(exporter);
                } else if (entryCommand.contains("importFile")) {
                    Person importer = (Person) ois.readObject();
                    importFile(importer);
                } else if (entryCommand.contains("update")) {
                    Person update = (Person) ois.readObject();
                    if (update instanceof Seller) {
                        Seller.updateSellers((Seller) update);
                    } else {
                        Customer.updateCustomers((Customer) update);
                    }
                } else if (entryCommand.contains("block")) {
                    String type = (String) ois.readObject();
                    if (type.equals("seller")) {
                        try {
                            String block = (String) ois.readObject();
                            Person user = (Person) ois.readObject();
                            Customer toBlock = (Customer) user.searchUser(block);
                            Seller s = (Seller) ois.readObject();
                            if (toBlock != null) {
                                toBlock.blockedByUser(s);
                                Customer.updateCustomers(toBlock);
                            }
                            ous.writeObject(toBlock);
                            ous.flush();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Person user = (Person) ois.readObject();
                        String block = (String) ois.readObject();
                        Seller toBlock = (Seller) user.searchUser(block);
                        Customer c = (Customer) ois.readObject();
                        if (toBlock != null) {
                            toBlock.blockedByUser(c);
                            Seller.updateSellers(toBlock);
                        }
                        ous.writeObject(toBlock);
                        ous.flush();
                    }
                } else if (entryCommand.contains("unblock")) {
                    String type = (String) ois.readObject();
                    if (type.equals("seller")) {
                        try {
                            String unblock = (String) ois.readObject();
                            Person user = (Person) ois.readObject();
                            Customer toUnblock = (Customer) user.searchUser(unblock);
                            ous.writeObject(toUnblock);
                            ous.flush();
                            Seller s = (Seller) ois.readObject();
                            if (toUnblock != null) {
                                toUnblock.unBlockedByUser(s);
                                Customer.updateCustomers(toUnblock);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        String unblock = (String) ois.readObject();
                        Person user = (Person) ois.readObject();
                        Seller toUnblock = (Seller) user.searchUser(unblock);
                        Customer c = (Customer) ois.readObject();
                        ous.writeObject(toUnblock);
                        ous.flush();
                        if (toUnblock != null) {
                            toUnblock.unBlockedByUser(c);
                            Seller.updateSellers(toUnblock);
                        }

                    }
                } else if (entryCommand.equals("stats")) {
                    try {
                        String type = (String) ois.readObject();
                        if (type.equals("seller")) {
                            Seller s = (Seller) ois.readObject();
                            Statistics.display(s);
                        } else {
                            Customer c = (Customer) ois.readObject();
                            Statistics.display(c);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (entryCommand.equals("getUser")) {
                    String username = (String) ois.readObject();
                    try {
                        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("Sellers.ser"));
                        ArrayList<Seller> sellerList = (ArrayList<Seller>) ois.readObject();
                        for (Seller s : sellerList) {
                            if (username.equals(s.getName())) {
                                ous.writeObject(s);
                                break;
                            }
                        }
                        ois = new ObjectInputStream(new FileInputStream("Customers.ser"));
                        ArrayList<Customer> customerList = (ArrayList<Customer>) ois.readObject();
                        for (Customer s : customerList) {
                            if (username.equals(s.getName())) {
                                ous.writeObject(s);
                                break;
                            }
                        }
                        ous.flush();
                    } catch (Exception e) {
                        ous.writeObject(new Error());
                        ous.flush();
                    }


                }
            } catch (ClassCastException cce) {
                continue; // so that it does not print the error message
            } catch (SocketException se) {
                System.out.println("socket closed");
                return;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    synchronized Person login(String entity, String email, String password) {
        Person user = null;
        if (entity.equals("seller")) {
            user = Seller.logIn(email, password);
        } else {
            user = Customer.logIn(email, password);
        }
        return user;
    }

    synchronized Person create(int entity, String email, String username, String password) {
        Person user = null;
        try {
            if (entity == 1) {
                user = new Seller(email, username, password);
            } else {
                user = new Customer(email, username, password);
            }
        } catch (IllegalArgumentException e) {
            return null;
        }

        return user;
    }

    synchronized public boolean editThread(String filename) {
        ArrayList<String> uneditedList = readFileArrayList(filename);
        ArrayList<String> list = readFileArrayList(filename);

        for (int i = list.size() - 1; i >= 0; i--) {
            String line = list.get(i);
            line = line.substring(line.indexOf(" ") + 1);
            if (!line.startsWith("Me:")) {
                list.remove(i);
            }
        }
        try {
            ous.writeObject(list);

        } catch (Exception e) {
            e.printStackTrace();
        }
        filename = filename.substring(3);

        String reverseFilename = "..\\" + filename.substring(filename.indexOf("_thread_") + 8, filename.indexOf(".txt"))
                + "_thread_" + filename.substring(0, filename.indexOf("_thread_")) + ".txt";
        ArrayList<String> reverseList = readFileArrayList(reverseFilename);
        filename = "..\\" + filename;
        try {
            String message = (String) ois.readObject();
            int lineNumber = 0;
            String timestamp = message.substring(0, message.indexOf(" "));
            for (int i = 0; i < uneditedList.size(); i++) {
                if (uneditedList.get(i).equals(message)) {
                    lineNumber = i;
                    break;
                }
            }
            String newMessage = (String) ois.readObject();

            uneditedList.set(lineNumber, uneditedList.get(lineNumber).substring(0, message.indexOf(": ") + 2) + newMessage);
            lineNumber = -1;
            for (int i = 0; i < reverseList.size(); i++) {
                String t = reverseList.get(i).substring(0, reverseList.get(i).indexOf(" "));
                if (t.equals(timestamp)) {
                    lineNumber = i;
                }
            }
            if (lineNumber != -1) {
                String line = reverseList.get(lineNumber);
                reverseList.set(lineNumber, line.substring(0, reverseList.get(lineNumber).indexOf(": ") + 2) + newMessage);
            }
            String line = reverseList.get(lineNumber);
            reverseList.set(lineNumber, line.substring(0, line.indexOf(": ") + 2) + newMessage);

            try (PrintWriter pw = new PrintWriter(new FileWriter(filename, false));
                 PrintWriter wp = new PrintWriter(new FileWriter(reverseFilename, false))) {
                for (int i = 0; i < uneditedList.size(); i++) {
                    pw.println(uneditedList.get(i));
                }
                for (int i = 0; i < reverseList.size(); i++) {
                    wp.println(reverseList.get(i));
                }
            } catch (IOException e) {
                System.out.println("Whoops");
                return false;
            }
            return true;
        } catch (ClassCastException cce) {
            return false;
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

    synchronized public boolean deleteThread(String filename) {
        ArrayList<String> list = readFileArrayList(filename);
        Object[] listToReadFrom = list.toArray();
        try {
            ous.writeObject(listToReadFrom);
            ous.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            String message = (String) ois.readObject();
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).equals(message)) {
                    list.remove(i);
                    break;
                }
            }
            try (PrintWriter pw = new PrintWriter(new FileWriter(filename, false))) {
                for (int i = 0; i < list.size(); i++) {
                    pw.println(list.get(i));
                }
            } catch (IOException e) {
                System.out.println("Whoops");
                return false;
            }

        } catch (ClassCastException cce) {
            return false;
        } catch (Exception e) {
            return false;
        }

        return true;

    }

    synchronized void exportFile(Person user) {
        try {
            if (user instanceof Customer) {
                Customer customer = (Customer) user;
                String[] stores = customer.getContactedStores();
                ous.writeObject(stores);
                String store = (String) ois.readObject();
                String fileName = "..\\" + customer.getName() + "_thread_" + store + ".txt";
                BufferedReader bfr = new BufferedReader(new FileReader(fileName));
                try {
                    File file = new File("..\\" + customer.getName() + "_exported_thread_" + store + ".csv");
                    FileWriter fileWriter = new FileWriter(file);
                    PrintWriter pw = new PrintWriter(fileWriter);
                    String line = bfr.readLine();

                    String seller = (line.split(": "))[0];
                    StringBuilder str = new StringBuilder();
                    str.append("Seller" + "," + seller.split(" ")[1] + "\n");
                    str.append("Store" + "," + store + "\n");
                    str.append("Customer" + "," + customer.getName() + "\n\n");
                    String[] replaced;
                    String[] words;
                    while (line != null) {
                        replaced = line.split(": ");
                        words = replaced[0].split(" ");
                        str.append(words[0].substring(1, words[0].length() - 1));
                        str.append(",");
                        str.append(words[1]);
                        str.append(",");
                        str.append(replaced[1].replace(',', ' '));
                        str.append("\n");
                        line = bfr.readLine();
                    }

                    pw.println(str.toString());
                    pw.flush();

                } catch (Exception e) {
                    e.printStackTrace();
                }


            } else {
                Seller seller = (Seller) user;
                String[] stores = seller.getStores();
                ous.writeObject(stores);
                ous.flush();
                String store = (String) ois.readObject();

                String[] customers = seller.getCustomers(store);
                ous.writeObject(customers);
                ous.flush();

                String customer = (String) ois.readObject();

                // Everything validated - Use the file to export another file
                String fileName = "..\\" + store + "_thread_" + customer + ".txt";
                try {
                    File file = new File("..\\" + store + "_exported_thread_" + customer + ".csv");
                    FileWriter fileWriter = new FileWriter(file);
                    BufferedReader bfr = new BufferedReader(new FileReader(fileName));
                    PrintWriter pw = new PrintWriter(fileWriter);
                    String line = bfr.readLine();
                    StringBuilder str = new StringBuilder();
                    str.append("Seller" + "," + seller.getName() + "\n");
                    str.append("Store" + "," + store + "\n");
                    str.append("Customer" + "," + customer + "\n\nConversation: \n");
                    String[] replaced;
                    String[] words;
                    while (line != null) {
                        replaced = line.split(": ");
                        words = replaced[0].split(" ");
                        str.append(words[0].substring(1, words[0].length() - 1));
                        str.append(",");
                        str.append(words[1]);
                        str.append(",");
                        str.append(replaced[1].replace(',', ' '));
                        str.append("\n");
                        line = bfr.readLine();
                    }

                    pw.println(str.toString());
                    pw.flush();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        } catch (ClassCastException cce) {
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    synchronized void importFile(Person user) {
        try {
            if (user instanceof Seller) {
                Seller seller = (Seller) user;
                String[] stores = seller.getStores();
                ous.writeObject(stores);
                ous.flush();
                String store = (String) ois.readObject();
                String[] customers = seller.getCustomers(store);
                ous.writeObject(customers);
                ous.flush();
                String customer = (String) ois.readObject();

                File f = (File) ois.readObject();

                // Validating the file format
                try {
                    BufferedReader bfr = new BufferedReader(new FileReader(f));
                    // Checking format
                    String line = bfr.readLine();
                    String[] components;

                    String sellerFile = "..\\" + store + "_thread_" + customer + ".txt";
                    String customerFile = "..\\" + customer + "_thread_" + store + ".txt";

                    FileWriter storeFw = new FileWriter(sellerFile, true);
                    FileWriter customerFw = new FileWriter(customerFile, true);

                    PrintWriter storeWriter = new PrintWriter(storeFw);
                    PrintWriter customerWriter = new PrintWriter(customerFw);

                    while (line != null) {
                        String timeStamp = new SimpleDateFormat("(dd/MM/yyyy-HH:mm:ss) ").format(new java.util.Date());
                        storeWriter.println(timeStamp + "Me: " + line + "*");
                        customerWriter.println(timeStamp + store + ": " + line + "*");

                        storeWriter.flush();
                        customerWriter.flush();

                        line = bfr.readLine();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            } else { // instanceOf customer
                Customer customer = (Customer) user;
                String[] stores = customer.getContactedStores();
                ous.writeObject(stores);

                String store = (String) ois.readObject();


                File f = (File) ois.readObject();

                try {
                    BufferedReader bfr = new BufferedReader(new FileReader(f));
                    // Checking format
                    String line = bfr.readLine();

                    String sellerFile = "..\\" + store + "_thread_" + customer.getName() + ".txt";
                    String customerFile = "..\\" + customer.getName() + "_thread_" + store + ".txt";

                    FileWriter storeFw = new FileWriter(sellerFile, true);
                    FileWriter customerFw = new FileWriter(customerFile, true);

                    PrintWriter storeWriter = new PrintWriter(storeFw);
                    PrintWriter customerWriter = new PrintWriter(customerFw);

                    while (line != null) {
                        String timeStamp = new SimpleDateFormat("(dd/MM/yyyy-HH:mm:ss) ").format(new java.util.Date());
                        storeWriter.println(timeStamp + customer.getName() + ": " + line + "*");
                        customerWriter.println(timeStamp + "Me: " + line + "*");

                        storeWriter.flush();
                        customerWriter.flush();

                        line = bfr.readLine();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        } catch (ClassCastException cce) {
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
