package interfaces;

import models.User;

import java.util.List;

public interface DatabaseManagerInterface {
    /**
     * @param host
     * @param user
     * @param password
     * @param database
     * @return true if the database is connected or false if there is no connection
     */
    boolean connect(String host, String user, String password, String database);

    /**
     * @param username
     * @return a new user selected by its username
     */
    User getUserByUsername(String username);

    /**
     * @param user
     * @return the new user added to the database
     */
    boolean addUser(User user);

    /**
     * @param user
     * @return true or false if the user has been updated on the system
     */
    boolean updateUser(User user);

    /**
     * @param user
     * @return true or false if the user has been deleted from the system
     */
    public boolean deleteUser(User user);

}
