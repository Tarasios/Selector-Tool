import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

/**
 * BaseLister encapsulates common logic for writing a CSV file listing
 * directories and their sizes.
 */
public abstract class BaseLister {

    /**
     * Returns the name of the output CSV file.
     */
    protected abstract String getOutputFileName();

    /**
     * Returns the output path for the generated CSV.
     */
    public Path getOutputPath() {
        return Paths.get(getOutputFileName());
    }

    /**
     * Generate the CSV given a list of directories.
     */
    public final void generateCsv(List<Path> folders) throws IOException {
        Path output = getOutputPath();
        try (BufferedWriter writer = Files.newBufferedWriter(output, StandardCharsets.UTF_8)) {
            writer.write("Title,Size (bytes),Watched");
            writer.newLine();
            for (Path folder : folders) {
                long size = getFolderSize(folder);
                String title = folder.getFileName().toString();
                writer.write(String.format("\"%s\",%d,FALSE", title, size));
                writer.newLine();
            }
        }
    }

    private static long getFolderSize(Path folder) throws IOException {
        try (Stream<Path> stream = Files.walk(folder)) {
            return stream
                    .filter(p -> !Files.isDirectory(p))
                    .mapToLong(p -> {
                        try {
                            return Files.size(p);
                        } catch (IOException e) {
                            return 0L;
                        }
                    })
                    .sum();
        }
    }
}
