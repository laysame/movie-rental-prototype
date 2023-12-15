package interfaces;

import managers.DatabaseManager;
import models.User;

public interface AuthenticationManagerInterface {
    public User authenticate(String username, String password);

    public User register(String username, String password, String firstName, String lastName);




}
