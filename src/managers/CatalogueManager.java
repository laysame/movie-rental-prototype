package managers;


import exceptions.MovieAlreadyExistException;
import models.Movie;

import java.io.*;

public class CatalogueManager {
    private DatabaseManager databaseManager;

    public CatalogueManager(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public void loadMovies(String filepath) {
        try {
            File file = new File(filepath);
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line;
            String[] row;

            // Skip header
            bufferedReader.readLine();

            while ((line = bufferedReader.readLine()) != null) {
                row = line.split(",");

                Movie movie = new Movie();
                movie.setId(Integer.parseInt(row[0]));
                movie.setTitle(row[1]);
                movie.setPrice(Float.parseFloat(row[2]));

                System.out.println("Load '" + movie.getTitle() + "' into database ...");

                try {
                    databaseManager.addMovie(movie);
                } catch (MovieAlreadyExistException e) {
                    databaseManager.updateMovie(movie);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
