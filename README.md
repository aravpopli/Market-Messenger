Welcome to our Amazon replica marketplace messenger! You can run and compile this project by simply running Server.java and then making as many instances as you want of Client.java. 

Arav Popli - Submitted the Report 

Irfan Firosh - Video Presentation

Michelle Jyi - Submitted the Vocareum Workspace 

<ins>**Statistics.java**</ins>
**Functionality:** 
Statistics works for both customers and sellers, with _3 main methods_. 

The _counter method_ reads all message threads of a user and counts the number of messages from each side, which is later used.

The _commonWord method_ takes the message threads of a user and looks for the most common words using hashmaps, returning a string with the most used words.

The previous two methods are helper methods for the final one, _display_, which holds all the UI and puts everything together. Here, a user can interact with the statistics, and allows them to sort by message count or by alphabetical order, printing message counts and up to the 5 most common words in a user's given conversations.

Statistics is used in the client handler to handle back end operations. 

<ins>**Customer.java**</ins>

  **Functionality:** This class is represents the state and behavior of a customer. It's main relation to the other classes is in both the back end files like Client Handler when it is used to manipulate data and the front end when users create customer log ins. The functionality implemented in this class is getting and setting the private fields of this class. Constructing customer objects which remain persistent between runs via SER files, listing all the customers names in the SER file, blocking users, and writing messages to stores. 

<ins>**Seller.java**</ins>

  **Functionality:** This class is represents the state and behavior of a customer. It's main relation to the other classes is in both the back end files like Client Handler when it is used to manipulate data and the front end when users create customer log ins. The functionality implemented in this class is getting and setting the private fields of this class. Constructing store objects which remain persistent between runs via SER files, listing all the customers names in the SER file, blocking users, and writing messages to customers. It also stores all the customers stored by a user via a HashMap which is used to retrieve text threads with existing customers. This also remains persistent via the SER files

<ins>**Person.java**</ins>

  **Functionality:** This interface represents all users who will login and the standard methods to be implemented for a user regardless of whether they're a customer or seller. This functionality includes getting names, passwords, and searching users. 


<ins>**FileImportsandExports.java**</ins>

  **Functionality:** This class's functionality includes reading pre-existing message threads that are in txts and exporting the messages as a CSV. In addition it reads txts and imports them into one of the programs pre-existing message threads. It's only relationship is to the Client Handler where its methods are called. 

<ins>**Client.java**</ins>

 **Functionality:** This class's functionality is to instantiate instances of the client/user interface side so that a user can interact with the application via the GUI. It's connection is with Login.java and Dashboard.java as the user will be guided through the functions of those files via the front end.

<ins>**ClientHandler.java**</ins>

 **Functionality:** This class's functionality is to act as a helper class for Server.java. Every time that the server starts it will create an instance of client handler which hosts all functions regarding manipulating data in our project. This Client Handler also implements the runnable interace in order to make it applicable with threads. It also has a relation with Login and Dashboard as those files will write to ClientHandler via the TCI/IP socket protocol by implementing Java.net. 

<ins>**Dashboard.java**</ins>

**Functionality:** This class's functionality is to behave as the GUI interface/client side that the user interacts with after logging in to the application. This contains all the different JButtons and user interactions that the file will look out for and accordingly interact with the client handler via the socket protocol and socket input output streams. 

<ins>**Error.java**</ins>

**Functionality:** This class's functionality is to notify the server of a cancellation operation by the user (ie: cancel view message) and it will catch the exception then reset the run() method in ClientHandler.java to make sure no exceptions are actually throw by the server.

<ins>**Login.java**</ins>

**Functionality:** This class's functionality is to behave as the GUI interface/client side that the user interacts with when logging in to the application. This contains all the different JButtons and user interactions that the file will look out for and accordingly interact with the client handler via the socket protocol and socket input output streams. It will call methods like create account in the Seller and Customer classes.

<ins>**Server.java**</ins>

**Functionality:** This class's functionality is to behave as a back end server that each client instance must interact with when manipulating data and storing data. It's relation to other classes is that it will create instances of the Client Handler (its helper class) which will listen for entry signals from the client telling it which computation to perform. This class is crucial for the project to be functional as the client file doesn't run without the server file running. 




