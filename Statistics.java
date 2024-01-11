import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

/**
 * Statistics Class
 * <p>
 * statistics for project
 *
 * @author Lawrence
 * @version 11.12.23
 */
public class Statistics {
    static ArrayList<String> customers = new ArrayList<>();
    static ArrayList<String> stores = new ArrayList<>();
    static int sellerCount = 0;
    static int custCount = 0;
    static ArrayList<String> contents = new ArrayList<>();
    static HashMap<String, Integer> custMessageCount = new HashMap<>();
    static HashMap<String, Integer> sellMessageCount = new HashMap<>();
    static ArrayList<String> custByCount = new ArrayList<>();
    static ArrayList<String> sellByCount = new ArrayList<>();



    public static boolean counter(Person user) {
        if (user instanceof Seller) {
            Seller seller = (Seller) user;
            String[] storesArr = seller.getStores();
            String[] customerArr;
            for (String store : storesArr) {
                if (seller.getCustomers(store).length == 0) {
                    JOptionPane.showMessageDialog(null, "No messages were sent!",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return false;
                } else {
                    customerArr = seller.getCustomers(store);
                    String fileName;
                    for (String customer : customerArr) {
                        // Now making a file
                        int custCounts = 0;
                        fileName = "..\\" + store + "_thread_" + customer + ".txt";
                        try {
                            BufferedReader bfr = new BufferedReader(new FileReader(fileName));
                            String[] components;
                            String[] words;
                            String line = bfr.readLine();
                            while (line != null) {
                                components = line.split(": ");
                                if (components[0].contains("Me")) {
                                    ++sellerCount;
                                } else {
                                    ++custCounts;
                                }
                                contents.add(components[1]);
                                line = bfr.readLine();
                            }
                            customers.add(customer);
                            custMessageCount.put(customer, custCounts);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }

            }
            return true;
        } else {
            Customer customer = (Customer) user;
            String[] contacted = customer.getContactedStores();
            if (contacted.length == 0) {
                JOptionPane.showMessageDialog(null, "No messages were sent!",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            } else {
                for (String store : contacted) {
                    int sellerCounts = 0;
                    String fileName;
                    try {
                        fileName = "..\\" + store + "_thread_" + customer.getName() + ".txt";
                        BufferedReader bfr = new BufferedReader(new FileReader(fileName));
                        String[] components;
                        String[] words;
                        String line = bfr.readLine();
                        while (line != null) {
                            components = line.split(": ");
                            if (components[0].contains("Me")) {
                                sellerCounts++;
                            } else {
                                custCount++;
                            }
                            contents.add(components[1]);
                            line = bfr.readLine();
                        }
                        stores.add(store);
                        sellMessageCount.put(store, sellerCounts);

                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null,
                                "There was an error reading the file", "Error", JOptionPane.ERROR_MESSAGE);

                    }

                }
            }
            return true;
        }
    }

    static ImageIcon statisticsIcon = new ImageIcon("statisticsIcon.png");
    public static void display(Person user) {

        if (!counter(user)) return;
        String[] options = {"Alphabetically", "Word Count"};

        String option = (String) JOptionPane.showInputDialog(null,
                "Would you rather sort alphabetically (1) or by word count (2)?",
                "Statistics", JOptionPane.QUESTION_MESSAGE, statisticsIcon, options, options[0]);
        if (option == null) return;
        if (user instanceof Seller) {
            Seller seller = (Seller) user;

            //top five words most commonly used,
            String[] used = new String[] { commonWord(seller) };
            String words = "";
            if (used.length < 5) {
                for (int i = used.length - 1; i >= 0; i--) {
                    words += used[i] + " ";
                }
            } else {
                for (int i = 4; i >= 0; i--) {
                    words += used[i] + " ";
                }
            }

            JOptionPane.showMessageDialog(null, "Seller Detail:\nName: " + seller.getName() +
                    ", Message Count: " + sellerCount + "\nMost Common Words: " + words,
                    "Statistics", JOptionPane.INFORMATION_MESSAGE, statisticsIcon);
            if (option.equals(options[0])) {
                JOptionPane.showMessageDialog(null, "Messages Sorted Alphabetically",
                        "Statistics", JOptionPane.INFORMATION_MESSAGE, statisticsIcon);
                customers.sort(Comparator.naturalOrder());
                for (int i = 0; i < customers.size(); i++) {
                    JOptionPane.showMessageDialog(null, "Name: " + customers.get(i) +
                            ", Message Count: " + custMessageCount.get(customers.get(i)),
                    "Statistics", JOptionPane.INFORMATION_MESSAGE, statisticsIcon);
                }

            } else {
                // sort customers by messages
                // iterate through unsorted customers and add to sorted list
                custByCount.add(customers.get(0));
                for (int i = 1; i < customers.size(); i++) {
                    for (int y = 0; y < custByCount.size(); y++) {
                        int notInList = custMessageCount.get(customers.get(i));
                        int currentInList = custMessageCount.get(customers.get(y));
                        if (notInList >= currentInList) {
                            custByCount.add(y, customers.get(i));
                        }
                        y = custByCount.size() + 1;
                    }
                }
                JOptionPane.showMessageDialog(null, "Messages Sorted by Count",
                        "Statistics", JOptionPane.INFORMATION_MESSAGE, statisticsIcon);
                for (int i = 0; i < customers.size(); i++) {
                    JOptionPane.showMessageDialog(null, "Name: " + custByCount.get(i) +
                                    ", Message Count: " + custMessageCount.get(custByCount.get(i)),
                            "Statistics", JOptionPane.INFORMATION_MESSAGE, statisticsIcon);
                }
            }

        } else {
            Customer customer = (Customer) user;
            String[] used = new String[] { commonWord(customer) };
            String words = "";
            if (used.length < 5) {
                for (int i = used.length - 1; i >= 0; i--) {
                    words += used[i] + " ";
                }
            } else {
                for (int i = 4; i >= 0; i--) {
                    words += used[i] + " ";
                }
            }
            JOptionPane.showMessageDialog(null, "Customer Detail:\nName: " + customer.getName() +
                            ", Message Count: " + custCount + "\nMost Common Words: " + words,
                    "Statistics", JOptionPane.INFORMATION_MESSAGE, statisticsIcon);

            if (option.equals(options[0])) {
                JOptionPane.showMessageDialog(null, "Messages Sorted Alphabetically",
                        "Statistics", JOptionPane.INFORMATION_MESSAGE, statisticsIcon);
                stores.sort(Comparator.naturalOrder());
                for (int i = 0; i < stores.size(); i++) {
                    JOptionPane.showMessageDialog(null, "Name: " + stores.get(i) +
                            ", Message Count: " + sellMessageCount.get(stores.get(i)),
                            "Statistics", JOptionPane.INFORMATION_MESSAGE, statisticsIcon);
                }
            } else {
                // sort customers by messages
                // iterate through unsorted customers and add to sorted list
                sellByCount.add(stores.get(0));
                for (int i = 0; i < stores.size(); i++) {
                    for (int y = 0; y < sellByCount.size(); y++) {
                        int notInList = sellMessageCount.get(stores.get(i));
                        int currentInList = sellMessageCount.get(stores.get(y));
                        if (notInList >= currentInList) {
                            sellByCount.add(y, stores.get(i));
                            y = sellByCount.size() + 1;
                        }
                    }
                }
                JOptionPane.showMessageDialog(null, "Messages Sorted By Count",
                        "Statistics", JOptionPane.INFORMATION_MESSAGE, statisticsIcon);
                for (int i = 0; i < stores.size(); i++) {
                    JOptionPane.showMessageDialog(null, "Name: " + sellByCount.get(i) +
                            ", Message Count: " + sellMessageCount.get(sellByCount.get(i)),
                            "Statistics", JOptionPane.INFORMATION_MESSAGE, statisticsIcon);
                }
            }



        }
    }

    public static String commonWord(Person user) {
        String mostUsed = "";

        Map<String, Integer> counter = new HashMap<>();
        for (int i = 0; i < contents.size(); i++) {
            String line = contents.get(i);
            String[] words = line.toLowerCase().split("([,.\\s]+) ");
            for (String str : words) {
                Integer fre = counter.getOrDefault(str, 0);
                counter.put(str, fre + 1);
            }
        }

        // Using for each loop
        while (!counter.isEmpty()) {
            int count = 0;
            String commonWord = null;
            for (Map.Entry<String, Integer> a : counter.entrySet()) {
                if (a.getValue() > count) {
                    commonWord = a.getKey();
                    count = a.getValue();
                }
            }
            counter.remove(commonWord);
            mostUsed += commonWord + " ";
        }

        return mostUsed;
    }
}
