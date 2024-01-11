
/**
 * Person Interface
 *
 * person interface that defines a person object
 *
 * @author Michelle
 *
 * @version 11.12.23
 *
 */
public interface Person {
    String getEmail(); // returns the email of the user

    String getName(); // returns the name of the user

    String getPassword(); // returns the password of the user

    Person searchUser(String name); // searches for a user with a specific name

    void blockedByUser(Person person); // blocks the user with the given email

    void unBlockedByUser(Person person); // unblocks user

    boolean isInvisibleBy(String username); // returns whether a user is invisible

    void invisibleByUser(Person person); // go invisible to someone

    void unInvisibleByUser(Person person);

    boolean isBlockedBy(String username); // returns whether a user is blocked

}
