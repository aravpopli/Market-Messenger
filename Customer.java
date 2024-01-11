import java.io.*;
import java.util.ArrayList;

/**
 * Customer Class
 * <p>
 * creates the customer object
 *
 * @author Lavangi, Lawrence, Michelle, Arav
 * @version 11.12.23
 */
public class Customer implements Serializable, Person {
    private String username;
    private String password;
    private String emailAddress;
    private ArrayList<String> contactedStores;
    private ArrayList<String> blockedBy;
    private ArrayList<String> invisibleBy;

    public Customer() {
        username = null;
        password = null;
        emailAddress = null;
        blockedBy = null;
        invisibleBy = null;
    }

    public Customer(String email, String username, String password) {
        if (username.toLowerCase().equals("exit"))
            throw new IllegalArgumentException();
        try {
            // creates a new customer file, writes the contents of the customer object to
            // the file
            File file = new File("Customers.ser");
            if (file.createNewFile()) {
                ArrayList<Customer> listForWriting = new ArrayList<>();
                // writer
                ObjectOutputStream objOutputSt = new ObjectOutputStream(new FileOutputStream("Customers.ser"));
                objOutputSt.writeObject(listForWriting);
                objOutputSt.flush();
                objOutputSt.close();
            }
            // reads the customer file
            ObjectInputStream objInputSt = new ObjectInputStream(new FileInputStream("Customers.ser"));
            // creates a new array list that casts the object from object to a customer
            ArrayList<Customer> listForReading = (ArrayList<Customer>) objInputSt.readObject();
            // checks if any existing user has the same email
            for (Customer c : listForReading) {
                if (c.getName().equals(username)) {
                    throw new IllegalArgumentException();
                }
                if (c.getEmail().equals(email)) {
                    throw new IllegalArgumentException();
                }
            }
            File sellerFile = new File("Sellers.ser");
            if (sellerFile.exists()) {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream("Sellers.ser"));
                ArrayList<Seller> sellerList = (ArrayList<Seller>) ois.readObject();
                for (Seller s : sellerList) {
                    if (s.getEmail().equals(email) || s.getName().equals(username)) {
                        throw new IllegalArgumentException();
                    }
                    if (s.getStoreCustomers().containsKey(username)) {
                        throw new IllegalArgumentException();
                    }
                }
            }

            contactedStores = new ArrayList<>();

        } catch (IllegalArgumentException e) {
            throw e;
        } catch (EOFException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.emailAddress = email;
        this.username = username;
        this.password = password;
        this.blockedBy = new ArrayList<String>();
        this.invisibleBy = new ArrayList<String>();

        try {
            // once all the inputs have been entered and the checks have been done, it will
            // be added to the file
            ObjectInputStream objInputSt = new ObjectInputStream(new FileInputStream("Customers.ser"));
            ArrayList<Customer> list = (ArrayList<Customer>) objInputSt.readObject();
            list.add(this);
            ObjectOutputStream objOutputSt = new ObjectOutputStream(new FileOutputStream("Customers.ser"));
            objOutputSt.writeObject(list);
            objOutputSt.flush();
            objOutputSt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public ArrayList getContactList() {
        return this.contactedStores;
    }

    public void addContactedStore(String store) {
        if (!contactedStores.contains(store))
            contactedStores.add(store);
    }

    public String[] getContactedStores() {
        return contactedStores.toArray(new String[0]);
    }

    public String getEmail() {
        return emailAddress;
    }

    public String getName() {
        // returns the username of the user
        return username;
    }

    public String getPassword() {
        // returns the password of the user
        return password;
    }

    public Person searchUser(String username1) {
        for (String seller : blockedBy) {
            if (seller.equals(username1)) {
                return null; // blocked
            }

        }
        for (String seller : invisibleBy) {
            if (seller.equals(username1)) {
                return null; // invisible
            }
        }
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("Sellers.ser"));
            ArrayList<Seller> list = (ArrayList<Seller>) ois.readObject();
            for (Seller s : list) {
                if (s.getName().equals(username1)) {
                    return s;
                }
            }

        } catch (Exception e) {
            return null;
        }
        return null;
    }

    public void blockedByUser(Person person) {
        Seller s = (Seller) person;
        if (!blockedBy.contains(s.getName())) {
            blockedBy.add(s.getName());
        }

    }

    public void unBlockedByUser(Person person) {
        if (blockedBy.contains(person.getName())) {
            blockedBy.remove(person.getName());
        }
    }

    public void invisibleByUser(Person person) {
        Seller s = (Seller) person;
        if (!invisibleBy.contains(s.getName())) {
            invisibleBy.add(s.getName());
        }
    }

    public void unInvisibleByUser(Person person) {
        Seller s = (Seller) person;
        if (invisibleBy.contains(s.getName())) {
            invisibleBy.remove(s.getName());
        }
    }

    public boolean isInvisibleBy(String username1) {
        for (int i = 0; i < invisibleBy.size(); i++) {
            if (invisibleBy.get(i).equals(username1)) {
                return true;
            }
        }
        return false;
    }

    public boolean isBlockedBy(String username1) {
        for (int i = 0; i < blockedBy.size(); i++) {
            if (blockedBy.get(i).equals(username1)) {
                return true;
            }
        }
        return false;
    }

    public String toString() {
        return "username: " + getName() + "," + "email address: " + getEmail();
    }

    public static void updateCustomers(Customer c) {
        try {
            // once all the inputs have been entered and the checks have been done, it will
            // be added to the file
            ObjectInputStream objInputSt = new ObjectInputStream(new FileInputStream("Customers.ser"));
            ArrayList<Customer> list = (ArrayList<Customer>) objInputSt.readObject();
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getEmail().equals(c.getEmail())) {
                    list.set(i, c);
                    break;
                }
            }
            ObjectOutputStream objOutputSt = new ObjectOutputStream(new FileOutputStream("Customers.ser"));
            objOutputSt.writeObject(list);
            objOutputSt.flush();
            objOutputSt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String[] printAllCustomers(Person user) {

        ArrayList<String> customers = new ArrayList<>();
        try {
            if (!new File("Customers.ser").exists()) {
                return null;
            }
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("Customers.ser"));
            ArrayList<Customer> list = (ArrayList<Customer>) ois.readObject();

            for (int i = 0; i < list.size(); i++) {
                if (!user.isInvisibleBy(list.get(i).getName())) {
                    customers.add(list.get(i).getName());


                }
            }
            if (customers.size() == 0) {
                return null;
            }
            return customers.toArray(new String[0]);
        } catch (Exception e) {
            return null;
        }
    }

    public static Customer logIn(String email, String password) {
        try {
            ObjectInputStream objInputSt = new ObjectInputStream(new FileInputStream("Customers.ser"));
            ArrayList<Customer> listForReading = (ArrayList<Customer>) objInputSt.readObject();
            for (Customer c : listForReading) {
                if (c.getEmail().equals(email)) {
                    if (c.getPassword().equals(password)) {
                        return c;
                    }
                }
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    /*
     * Checks if customer exists in database
     * 
     * @param customer name of customer
     */

    public static Person searchCustomer(String customer) {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("Customers.ser"));
            ArrayList<Customer> list = (ArrayList<Customer>) ois.readObject();
            for (Customer s : list) {
                if (s.getName().equals(customer)) {
                    return s;
                }
            }

        } catch (Exception e) {
            return null;
        }
        return null;

    }

    /*
     * Writes message to this customer object's message thread with a store
     * Persists beyond program length
     */
    public void writeMessage(String store, String customer, String message) {
        // a_thread_b = from a's perspective
        String filename = "..\\" + customer + "_thread_" + store + ".txt";
        try (PrintWriter pw = new PrintWriter(new FileWriter(filename, true))) {
            pw.println(message);
            pw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
