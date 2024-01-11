import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Seller Class
 * <p>
 * seller object
 *
 * @author Michelle
 * @version 11.12.23
 */
public class Seller implements Serializable, Person {
    private String email;
    private String name;
    private String password;
    private ArrayList<String> blockedBy;
    private ArrayList<String> invisibleBy;

    private HashMap<String, ArrayList<String>> storeCustomers;

    public Seller() {
        email = null;
        name = null;
        password = null;
        blockedBy = null;
        invisibleBy = null;
    }

    /**
     * creates a seller object and throws an illegal argument exception if a user
     * with the same email already exists
     *
     * @param email    email of the user
     * @param name     name of the user
     * @param password password of the user
     */
    public Seller(String email, String name, String password) {
        try {
            // Sellers.ser stores all sellers object in an arraylist
            File f = new File("Sellers.ser");
            if (f.createNewFile()) {
                ArrayList<Seller> list = new ArrayList<>();
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("Sellers.ser"));
                oos.writeObject(list);
                oos.flush();
                oos.close();
            }
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("Sellers.ser"));
            ArrayList<Seller> list = (ArrayList<Seller>) ois.readObject();
            // checks if any existing user has the same email or name
            for (Seller s : list) {
                if (s.getEmail().equals(email) || s.getName().equals(name)) {
                    throw new IllegalArgumentException();
                }
            }
            File customerFile = new File("Customers.ser");
            if (customerFile.exists()) {
                ois = new ObjectInputStream(new FileInputStream("Customers.ser"));
                ArrayList<Customer> customerList = (ArrayList<Customer>) ois.readObject();
                for (Customer c : customerList) {
                    if (c.getEmail().equals(email) || c.getName().equals(name)) {
                        throw new IllegalArgumentException();
                    }
                }
            }
            storeCustomers = new HashMap<>();

        } catch (IllegalArgumentException e) {
            throw e;
        } catch (EOFException e) {
            // reaches end of file; do nothing
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.email = email;
        this.name = name;
        this.password = password;
        this.blockedBy = new ArrayList<String>();
        this.invisibleBy = new ArrayList<String>();

        try {
            // adds newly created seller to file
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("Sellers.ser"));
            ArrayList<Seller> list = (ArrayList<Seller>) ois.readObject();
            list.add(this);
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("Sellers.ser"));
            oos.writeObject(list);
            oos.flush();
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void addCustomer(String customer, String storename) {
        ArrayList<String> customers = storeCustomers.get(storename);
        if (!customers.contains(customer)){
            customers.add(customer);

        }
        storeCustomers.put(storename, customers);
    }

    public boolean addStore(String store) {
        if (store.isEmpty() || store.toLowerCase().equals("exit"))
            return false;
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("Sellers.ser"));
            ArrayList<Seller> list = (ArrayList<Seller>) ois.readObject();
            for (int i = 0; i < list.size(); i++) {
                for (String s : list.get(i).storeCustomers.keySet()) {
                    if (s.equals(store))
                        return false;
                }
            }
            if (new File("Customers.ser").exists()) {
                ois = new ObjectInputStream(new FileInputStream("Customers.ser"));
                ArrayList<Customer> customers = (ArrayList<Customer>) ois.readObject();
                for (int i = 0; i < customers.size(); i++) {
                    if (customers.get(i).getName().equals(store)) {
                        return false;
                    }
                }
            }
        } catch (Exception e) {
            return false;
        }
        if (storeCustomers.containsKey(store))
            return false;
        storeCustomers.put(store, new ArrayList<>());
        return true;
    }

    public String[] getCustomers(String storename) {
        return storeCustomers.get(storename).toArray(new String[0]);
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public HashMap<String, ArrayList<String>> getStoreCustomers() {
        return this.storeCustomers;
    }

    /**
     * Searches all visible customers with a specific name
     *
     * @param name1 name of the user
     * @return the user with the name or null if the customer does not exist
     */
    public Person searchUser(String name1) {
        for (String customer : blockedBy) {
            if (customer.equals(name1)) {
                return null; // blocked
            }
        }
        for (String customer : invisibleBy) {
            if (customer.equals(name1)) {
                return null; // invisible
            }
        }
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("Customers.ser"));
            ArrayList<Customer> list = (ArrayList<Customer>) ois.readObject();
            for (Customer c : list) {
                if (c.getName().equals(name1)) {
                    return c;
                }
            }

        } catch (Exception e) {
            return null;
        }
        return null;

    }

    /**
     * Searches all existing customers with a specific email
     *
     * @param store name of store
     * @return returns null if store doesn't exist in any of the seller objects
     */
    public static Person searchStore(String store) {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("Sellers.ser"));
            ArrayList<Seller> list = (ArrayList<Seller>) ois.readObject();
            for (Seller s : list) {
                for (String st : s.storeCustomers.keySet()) {
                    if (st.equals(store)) {
                        return s;
                    }
                }
            }

        } catch (Exception e) {
            return null;
        }
        return null;

    }

    public void blockedByUser(Person person) {
        if (!blockedBy.contains(person.getName())) {
            blockedBy.add(person.getName());
        }
    }

    public void unBlockedByUser(Person person) {
        if (blockedBy.contains(person.getName())) {
            blockedBy.remove(person.getName());
        }
    }

    public void invisibleByUser(Person person) {
        if (!invisibleBy.contains(person.getName())) {
            invisibleBy.add(person.getName());
        }
    }

    public void unInvisibleByUser(Person person) {
        if (invisibleBy.contains(person.getName())) {
            invisibleBy.remove(person.getName());
        }
    }

    public boolean isInvisibleBy(String username) {
        for (int i = 0; i < invisibleBy.size(); i++) {
            if (invisibleBy.get(i).equals(username)) {
                return true;
            }
        }
        return false;
    }

    public boolean isBlockedBy(String username) {
        for (int i = 0; i < blockedBy.size(); i++) {
            if (blockedBy.get(i).equals(username)) {
                return true;
            }
        }
        return false;
    }

    public String toString() {
        return "username: " + name + "," + "email address: " + email;
    }

    public static String[] printAllSellers(Person user) {
        ArrayList<String> sellers = new ArrayList<>();
        try {
            if (!new File("Sellers.ser").exists()) {
                return null;
            }
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("Sellers.ser"));
            ArrayList<Seller> list = (ArrayList<Seller>) ois.readObject();
            for (int i = 0; i < list.size(); i++) {
                if (!user.isInvisibleBy(list.get(i).getName())) {
                    sellers.add(list.get(i).getName());
                }
            }
            if (sellers.size() == 0) {
                return null;
            }
            return sellers.toArray(new String[0]);
        } catch (Exception e) {
            return null;
        }
    }
    /**
     * Prints all stores
     */
    public static String[] printAllStores(Person user) {
         ArrayList<String> stores = new ArrayList<>();
        try {
            if (!new File("Sellers.ser").exists()) {
                return null;
            }
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("Sellers.ser"));
            ArrayList<Seller> list = (ArrayList<Seller>) ois.readObject();
            for (int i = 0; i < list.size(); i++) {
                if (!user.isInvisibleBy(list.get(i).getName())) {
                    String[] sellerStores = list.get(i).getStores();
                    for (String store : sellerStores) {
                        stores.add(store);
                    }

                }
            }
            if (stores.size() == 0) {
                return null;
            }
            return stores.toArray(new String[0]);
        } catch (Exception e) {
            return null;
        }
    }

    /*
     * Returns the stores for this seller object
     */
    public String[] getStores() {
        return storeCustomers.keySet().toArray(new String[0]);
    }

    /**
     * logs the user in given an email and a password
     *
     * @param email    email of the user
     * @param password password of the user
     * @return the seller if both information is correct and null if password does
     *         not match or user
     *         with the email does not exist
     */
    public static Seller logIn(String email, String password) {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("Sellers.ser"));
            ArrayList<Seller> list = (ArrayList<Seller>) ois.readObject();
            for (Seller s : list) {
                if (s.getEmail().equals(email)) {
                    if (s.getPassword().equals(password)) {
                        return s;
                    } else {
                        return null;
                    }
                }
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    public void writeMessage(String store, String customer, String message) {
        // a_thread_b = from a's perspective
        String filename = "..\\" + store + "_thread_" + customer + ".txt";
        try (PrintWriter pw = new PrintWriter(new FileWriter(filename, true))) {
            pw.println(message);
            pw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void updateSellers(Seller s) {
        try {
            // once all the inputs have been entered and the checks have been done, it will
            // be added to the file
            ObjectInputStream objInputSt = new ObjectInputStream(new FileInputStream("Sellers.ser"));
            ArrayList<Seller> list = (ArrayList<Seller>) objInputSt.readObject();
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getEmail().equals(s.getEmail())) {
                    list.set(i, s);
                    break;
                }
            }
            ObjectOutputStream objOutputSt = new ObjectOutputStream(new FileOutputStream("Sellers.ser"));
            objOutputSt.writeObject(list);
            objOutputSt.flush();
            objOutputSt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}