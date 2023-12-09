package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
    private Connection connection;
    private String database;

    /**
     * @param host     database host
     * @param user     database user name
     * @param password database user's password
     * @param database database name
     * @return true id the database is connected or false if there is no connection
     */
    public boolean connect(String host, String user, String password, String database) {
        this.database = database;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://" + host + "/", user, password);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
}

