package reader;


import java.io.IOException;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class DataReader {
        Stream<String> lines;

    {
        try {
            lines = java.nio.file.Files.lines(Paths.get("resources/movies.csv"));
            lines.forEach(line -> {
                // do something with each line
                System.out.println(line);
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
