import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * TV Series Lister
 *
 * <p>This program receives one or more folder paths as arguments. It outputs a
 * CSV spreadsheet named {@code tv_series_list.csv} with three columns:
 *
 * <ul>
 *   <li>Title &ndash; the folder name</li>
 *   <li>Size (bytes) &ndash; the total size of the folder</li>
 *   <li>Watched &ndash; a column of unchecked boxes (represented as {@code FALSE})</li>
 * </ul>
 */
public class TvSeriesLister extends BaseLister {

    /**
     * Generate a CSV listing all immediate subdirectories of the given root
     * folders. Each subdirectory is treated as a separate TV series. The size
     * of each series is calculated recursively.
     */
    public void generateFromRoots(List<Path> rootFolders) throws IOException {
        List<Path> shows = new ArrayList<>();
        for (Path root : rootFolders) {
            if (!Files.isDirectory(root)) {
                System.err.println("Skipping non-directory: " + root);
                continue;
            }
            try (Stream<Path> stream = Files.list(root)) {
                stream.filter(Files::isDirectory).forEach(shows::add);
            }
        }
        if (shows.isEmpty()) {
            System.err.println("No series folders found.");
            return;
        }
        // BaseLister's generateCsv will create the CSV for these folders
        generateCsv(shows);
    }

    @Override
    protected String getOutputFileName() {
        return "tv_series_list.csv";
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Usage: java TvSeriesLister <folder> [folder...]");
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

        TvSeriesLister lister = new TvSeriesLister();
        try {
            lister.generateFromRoots(folders);
        } catch (IOException e) {
            System.err.println("Failed to write CSV: " + e.getMessage());
        }
    }

}
