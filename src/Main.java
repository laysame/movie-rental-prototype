import authentication.AuthenticationManager;
import managers.DatabaseManager;
import managers.CatalogueManager;
import session.SessionManager;

public class Main {

    static final String DATABASE_HOST = "localhost";
    static final String DATABASE_USER = "root";
    static final String DATABASE_PASSWORD = "root";
    static final String DATABASE_NAME = "movie_rental";

    public static void main(String[] args) {

        DatabaseManager databaseManager = new DatabaseManager();

        // Start a new connection with the database
        if (!databaseManager.connect(DATABASE_HOST, DATABASE_USER, DATABASE_PASSWORD, DATABASE_NAME)) {
            throw new RuntimeException("Unable to connect to the database: check your connection! ");
        }

        CatalogueManager catalogueManager = new CatalogueManager(databaseManager);
        catalogueManager.loadMovies("resources/movies.csv");

        AuthenticationManager authenticationManager = new AuthenticationManager(databaseManager);
        SessionManager sessionManager = new SessionManager(databaseManager, authenticationManager);
        sessionManager.start();
    }
}