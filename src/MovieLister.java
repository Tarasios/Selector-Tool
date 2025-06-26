import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class MovieLister extends BaseLister {
    @Override
    protected String getOutputFileName() {
        return "movie_list.csv";
    }

    /**
     * Generate a CSV listing all movie files under the provided root folders.
     * Each file is listed individually. Subdirectories are scanned
     * recursively.
     */
    public void generateFromRoots(List<Path> rootFolders) throws IOException {
        List<Path> movies = new ArrayList<>();
        for (Path root : rootFolders) {
            if (!Files.isDirectory(root)) {
                System.err.println("Skipping non-directory: " + root);
                continue;
            }
            try (Stream<Path> stream = Files.walk(root)) {
                stream.filter(Files::isRegularFile).forEach(movies::add);
            }
        }
        if (movies.isEmpty()) {
            System.err.println("No movie files found.");
            return;
        }
        generateCsv(movies);
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Usage: java MovieLister <folder> [folder...]");
            System.exit(1);
        }

        List<Path> folders = new ArrayList<>();
        for (String arg : args) {
            Path path = Paths.get(arg);
            if (Files.isDirectory(path)) {
                folders.add(path);
            } else {
                System.err.println("Skipping non-directory: " + path);
            }
        }

        MovieLister lister = new MovieLister();
        try {
            lister.generateFromRoots(folders);
        } catch (IOException e) {
            System.err.println("Failed to write CSV: " + e.getMessage());
        }
    }
}
