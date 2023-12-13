package authentication;

import managers.DatabaseManager;
import exceptions.InvalidUsernameOrPasswordException;
import models.User;
import exceptions.UserDoesNotExistException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

public class AuthenticationManager {
    private DatabaseManager databaseManager;

    public AuthenticationManager(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public User authenticate(String username, String password) {
        try {
            User user = this.databaseManager.getUserByUsername(username);
            String encodedPassword = encodePassword(password);

            // Check if the encoded password stored in the database is different the encoded password passed
            if (!user.getPassword().equals(encodedPassword)) {
                throw new InvalidUsernameOrPasswordException();
            }
            return user;
        } catch (UserDoesNotExistException e) {
            throw new InvalidUsernameOrPasswordException();
        }
    }

    public User register(String username, String password, String firstName, String lastName) {
        String encodedPassword = encodePassword(password);
        User user = new User();
        user.setUsername(username);
        user.setPassword(encodedPassword);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setCreatedOn(new Date());

        this.databaseManager.addUser(user);

        // Get the user from the database in order to retrieve the auto increment-generated primary key
        return this.databaseManager.getUserByUsername(user.getUsername());
    }

    private String encodePassword(String password) {
        MessageDigest messageDigest = null;

        try {
            messageDigest = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        return javax.xml.bind.DatatypeConverter.printHexBinary(
                messageDigest.digest(password.getBytes())
        );
    }
}
