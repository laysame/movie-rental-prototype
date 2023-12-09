import db.DatabaseManager;

public class Main {

    static final String DATABASE_HOST = "localhost";
    static final String DATABASE_USER = "root";
    static final String DATABASE_PASSWORD = "root";
    static final String DATABASE_NAME = "movie_rental_prototype";

    public static void main(String[] args) {
        /*
        TODO:
            Allow users to create and log into accounts with an email and a password.
            Allow a user to rent a movie.
            Movies and their prices will initially be provided in a csv file.
            When a customer is charged, output the customer, movie, and price to the console.
            Rental duration is 1 minute for each movie.
            Keep track of what movies a user has rented.
            Recommend to users the 5 most rented movies of the past 5 minutes.
         */
        /*
        TODO:
            Making the code easily adaptable to, and implementing, other/variable rental durations e.g. can rent for 1 day or 1 week.
            Making the code easily adaptable to, and implementing, data persistence, such as a database.
         */

        DatabaseManager databaseManager = new DatabaseManager();

        // Start a new connection with the database

        if (!databaseManager.connect(DATABASE_HOST, DATABASE_USER, DATABASE_PASSWORD, DATABASE_NAME)) {
            throw new RuntimeException("Unable to connect to the database: check your connection! ");
        }

    }
}