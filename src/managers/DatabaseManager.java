package managers;

import exceptions.MovieAlreadyExistException;
import exceptions.MovieDoesNotExistException;
import exceptions.UsernameAlreadyUsedException;
import interfaces.DatabaseManagerInterface;
import models.Movie;
import models.Rental;
import models.User;
import exceptions.UserDoesNotExistException;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager implements DatabaseManagerInterface {
    private Connection connection;
    private String database;

    @Override
    public boolean connect(String host, String user, String password, String database) {
        this.database = database;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://" + host + "/", user, password);
            Statement statement = connection.createStatement();

            // Switch to the specified database
            statement.execute("USE " + database);
            return true;

        } catch (SQLException e) {
            return false;
        }
    }

    public boolean addUser(User user) {
        try {
            Statement statement = connection.createStatement();
            statement.execute("USE " + database);

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            statement.execute("INSERT INTO user(username, password, first_name, last_name, created_on)" +
                    "VALUES(" + "'" + user.getUsername() + "'," + "'" + user.getPassword() + "'," +
                    "'" + user.getFirstName() + "'," + "'" + user.getLastName() + "'," +
                    "'" + dateFormat.format(user.getCreatedOn()) + "'" + ");");

            return true;

        } catch (SQLIntegrityConstraintViolationException e) {
            throw new UsernameAlreadyUsedException();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean updateUser(User user) {
        try {
            Statement statement = connection.createStatement();
            statement.execute("USE " + database);

            statement.execute(
                    "UPDATE user SET " +
                            "first_name = '" + user.getFirstName() + "', " +
                            "last_name = '" + user.getLastName() + "' " +
                            "WHERE id = " + user.getId() + ";"
            );

            return true;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public User getUser(int id) {
        try {
            Statement statement = connection.createStatement();
            statement.execute("USE " + database);

            ResultSet resultSet = statement.executeQuery("SELECT * FROM user WHERE id = '" + id + "';");
            resultSet.next();

            User user = new User();
            user.setId(resultSet.getInt("id"));
            user.setUsername(resultSet.getString("username"));
            user.setPassword(resultSet.getString("password"));
            user.setFirstName(resultSet.getString("first_name"));
            user.setLastName(resultSet.getString("last_name"));
            user.setCreatedOn(resultSet.getDate("created_on"));

            return user;

        } catch (SQLException e) {
            throw new MovieDoesNotExistException();
        }
    }

    public User getUserByUsername(String username) {
        try {
            Statement statement = connection.createStatement();
            statement.execute("USE " + database);

            ResultSet resultSet = statement.executeQuery("SELECT * FROM user WHERE username = '" + username + "';");
            resultSet.next();

            User user = new User();
            user.setId(resultSet.getInt("id"));
            user.setUsername(resultSet.getString("username"));
            user.setPassword(resultSet.getString("password"));
            user.setFirstName(resultSet.getString("first_name"));
            user.setLastName(resultSet.getString("last_name"));
            user.setCreatedOn(resultSet.getDate("created_on"));

            return user;

        } catch (SQLException e) {
            throw new UserDoesNotExistException();
        }
    }

    public boolean deleteUser(User user) {
        try {
            Statement statement = connection.createStatement();
            statement.execute("USE " + database);

            statement.execute(
                    "DELETE FROM user WHERE id = " + user.getId() + ";"
            );

            return true;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void addMovie(Movie movie) {
        try {
            Statement statement = connection.createStatement();
            statement.execute("USE " + database);

            statement.execute("INSERT INTO movie(id, title, price)" +
                    "VALUES('" + movie.getId() + "', '" + movie.getTitle() + "'," + movie.getPrice() + ");"
            );

        } catch (SQLIntegrityConstraintViolationException e) {
            throw new MovieAlreadyExistException();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateMovie(Movie movie) {
        try {
            Statement statement = connection.createStatement();
            statement.execute("USE " + database);
            statement.execute("UPDATE movie SET title = '" + movie.getTitle() + "', price = " + movie.getPrice() +
                    " WHERE id = " + movie.getId() + ";");

        } catch (SQLException e) {
            throw new MovieDoesNotExistException();
        }
    }

    public Movie getMovie(int id) {
        try {
            Statement statement = connection.createStatement();
            statement.execute("USE " + database);

            ResultSet resultSet = statement.executeQuery("SELECT * FROM movie WHERE id = '" + id + "';");
            resultSet.next();

            Movie movie = new Movie();
            movie.setId(resultSet.getInt("id"));
            movie.setTitle(resultSet.getString("title"));
            movie.setPrice(resultSet.getFloat("price"));

            return movie;

        } catch (SQLException e) {
            throw new MovieDoesNotExistException();
        }
    }

    public List<Movie> getAllMovies() {
        List<Movie> movies = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
            statement.execute("USE " + database);

            ResultSet resultSet = statement.executeQuery("SELECT * FROM movie ORDER BY id;");

            while (resultSet.next()) {
                Movie movie = new Movie();
                movie.setId(resultSet.getInt("id"));
                movie.setTitle(resultSet.getString("title"));
                movie.setPrice(resultSet.getFloat("price"));
                movies.add(movie);
            }

            return movies;

        } catch (SQLException e) {
            throw new MovieDoesNotExistException();
        }
    }

    public List<Movie> getTopFiveRentedMovies() {
        List<Movie> movies = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
            statement.execute("USE " + database);

            ResultSet resultSet = statement.executeQuery(
                    "SELECT movie_id FROM rental WHERE started_on > NOW() - 5 * 60 GROUP BY movie_id ORDER BY COUNT(*) DESC;");

            while (resultSet.next()) {
                Movie movie = getMovie(resultSet.getInt("movie_id"));
                movies.add(movie);
            }

            return movies;

        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

    public void addRental(Rental rental) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        try {
            Statement statement = connection.createStatement();
            statement.execute("USE " + database);
            statement.execute("INSERT INTO rental(user_id, movie_id, started_on, ended_on, total_price)" +
                    "VALUES('" + rental.getUser().getId() + "', '" + rental.getMovie().getId() + "', '" +
                    dateFormat.format(rental.getStartedOn()) + "', " +
                    (rental.getEndedOn() != null ? "'" + dateFormat.format(rental.getEndedOn()) + "'" : "NULL") + ", " +
                    rental.getTotalPrice() + ");"
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateRental(Rental rental) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        try {
            Statement statement = connection.createStatement();
            statement.execute("USE " + database);
            statement.execute("UPDATE rental SET user_id = '" + rental.getUser().getId() + "', movie_id = '" + rental.getMovie().getId() + "', started_on = '" +
                    dateFormat.format(rental.getStartedOn()) + "', ended_on = " +
                    (rental.getEndedOn() != null ? "'" + dateFormat.format(rental.getEndedOn()) + "'" : "NULL") +
                    ", total_price = " + rental.getTotalPrice() + " WHERE id = " + rental.getId() + ";");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Rental> getUserOngoingRentals(User user) {
        List<Rental> rentals = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
            statement.execute("USE " + database);

            ResultSet resultSet = statement.executeQuery("SELECT * FROM rental WHERE user_id = " + user.getId() +
                    " AND ended_on IS NULL ORDER BY started_on;");

            while (resultSet.next()) {
                Rental rental = new Rental();
                rental.setId(resultSet.getInt("id"));
                rental.setUser(getUser(resultSet.getInt("user_id")));
                rental.setMovie(getMovie(resultSet.getInt("movie_id")));
                rental.setStartedOn(new Date(resultSet.getTimestamp("started_on").getTime()));

                if (resultSet.getTimestamp("ended_on") != null) {
                    rental.setEndedOn(new Date(resultSet.getTimestamp("ended_on").getTime()));
                    rental.setTotalPrice(resultSet.getFloat("total_price"));
                }

                rentals.add(rental);
            }

            return rentals;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
