package session;

import authentication.AuthenticationManager;
import exceptions.InvalidUsernameOrPasswordException;
import exceptions.UsernameAlreadyUsedException;
import managers.DatabaseManager;
import input.DataInput;
import interfaces.SessionManagerInterface;
import models.Movie;
import models.Rental;
import models.User;

import java.util.Date;
import java.util.List;

public class SessionManager implements SessionManagerInterface {
    private final DatabaseManager databaseManager;
    private final AuthenticationManager authenticationManager;
    private final DataInput dataInput;
    private User currentUser = null;

    public SessionManager(DatabaseManager databaseManager, AuthenticationManager authenticationManager) {
        this.databaseManager = databaseManager;
        this.authenticationManager = authenticationManager;
        this.dataInput = new DataInput();
    }

    @Override
    public void start() {
        showMainMenu();
    }

    @Override
    public void showMainMenu() {
        // Displays Main Menu to the application
        // After select an option as integer, displays the appropriate page
        System.out.println("==== Main Menu ====");
        System.out.println("Select an option:");
        System.out.println("1. Login to the system");
        System.out.println("2. Register a new user");
        System.out.println("3. Exit");

        int choice = dataInput.askInteger("Enter your choice:", 1, 3);

        switch (choice) {
            case 1: {
                showLoginPage();
                break;
            }

            case 2: {
                showRegistrationPage();
                break;
            }

            case 3: {
                System.exit(0);
            }
        }
    }

    @Override
    public void showLoginPage() {
        // Displays Login Page
        System.out.println("==== Login Page ====");
        // Gets user's username and password and authenticate them
        // If user or password is wrong, an exception will be thrown
        // If user pass authentication, displays welcome back message
        do {
            String username = dataInput.askString("Username:");
            String password = dataInput.askString("Password:");

            try {
                currentUser = authenticationManager.authenticate(username, password);
            } catch (InvalidUsernameOrPasswordException e) {
                System.err.println("The credentials you've entered are not valid!");
            }
        } while (currentUser == null);

        System.out.println("Welcome back, " + currentUser.getFirstName() + "!");

        showUserActionMenu();
    }

    @Override
    public void showRegistrationPage() {
        // Displays Registration Page
        System.out.println("==== Registration Page ====");
        System.out.println("Please, enter your personal information.");

        // Ask user details to get the registration done
        // If user already exits, throw appropriate exception
        // If first time registration, displays welcome message
        do {
            String username = dataInput.askString("Username:");
            String password = dataInput.askString("Password:");
            String firstName = dataInput.askString("First name:");
            String lastName = dataInput.askString("Last name:");

            try {
                currentUser = authenticationManager.register(username, password, firstName, lastName);
            } catch (UsernameAlreadyUsedException e) {
                System.err.println("The username already exist, sorry!");
            }
        } while (currentUser == null);

        System.out.println("Welcome aboard, " + currentUser.getFirstName() + "!");

        showUserActionMenu();
    }

    @Override
    public void showUserActionMenu() {
        System.out.println("==== Action Menu ====");
        System.out.println("Select an option:");
        System.out.println("1. Rent a movie");
        System.out.println("2. Return a movie");
        System.out.println("3. Exit");

        int choice = dataInput.askInteger("Enter your choice:", 1, 3);

        switch (choice) {
            case 1: {
                showRentMovieMenu();
                break;
            }

            case 2: {
                showReturnMovieMenu();
                break;
            }

            case 3: {
                System.exit(0);
            }
        }
    }

    public void showRentMovieMenu() {
        List<Movie> movies = databaseManager.getAllMovies();

        System.out.println("==== Action Menu ====");
        System.out.println("Select a movie to rent:");

        for (int i = 0; i < movies.size(); i++) {
            Movie movie = movies.get(i);
            System.out.println(i + ". " + movie.getTitle() + " - €" + movie.getPrice());
        }

        System.out.println(movies.size() + ". Exit");

        int choice = dataInput.askInteger("Enter your choice:", 1, movies.size() - 1);

        if (choice < movies.size()) {
            Movie movie = movies.get(choice);

            Rental rental = new Rental();
            rental.setUser(currentUser);
            rental.setMovie(movie);
            rental.setStartedOn(new Date());
            rental.setEndedOn(null);
            rental.setTotalPrice(0);

            databaseManager.addRental(rental);

            System.out.println("Here is your copy of '" + movie.getTitle() + "'. Thank you for choosing us!");
        }

        System.exit(0);
    }

    public void showReturnMovieMenu() {
        List<Rental> rentals = databaseManager.getUserOngoingRentals(currentUser);

        if (rentals.size() == 0) {
            System.out.println("You have already returned all the movies!");
            System.exit(0);
        }

        System.out.println("==== Action Menu ====");
        System.out.println("Select a movie to return:");

        for (int i = 0; i < rentals.size(); i++) {
            Movie movie = rentals.get(i).getMovie();
            System.out.println(i + ". " + movie.getTitle());
        }

        System.out.println(rentals.size() + ". Exit");

        int choice = dataInput.askInteger("Enter your choice:", 1, rentals.size() - 1);

    }
}
