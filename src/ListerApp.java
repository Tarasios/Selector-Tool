import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * ListerApp is the entry point that supports two modes: "tv" and "movie".
 */
public class ListerApp {
    public static void main(String[] args) {
        if (args.length < 2) {
            usage();
            System.exit(1);
        }

        String mode = args[0].toLowerCase();
        List<Path> folders = new ArrayList<>();
        for (int i = 1; i < args.length; i++) {
            Path path = Paths.get(args[i]);
            if (Files.isDirectory(path)) {
                folders.add(path);
            } else {
                System.err.println("Skipping non-directory: " + path);
            }
        }

        try {
            switch (mode) {
                case "tv":
                    TvSeriesLister tvLister = new TvSeriesLister();
                    tvLister.generateFromRoots(folders);
                    break;
                case "movie":
                    MovieLister movieLister = new MovieLister();
                    movieLister.generateFromRoots(folders);
                    break;
                default:
                    usage();
            }
        } catch (IOException e) {
            System.err.println("Failed to write CSV: " + e.getMessage());
        }
    }

    private static void usage() {
        System.err.println("Usage: java ListerApp <tv|movie> <folder> [folder...]");
    }
}
