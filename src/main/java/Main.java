import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ForkJoinPool;

public class Main {
    private static final String ROOT_LINK = "https://lenta.ru/";
    private static final String FOLDER_PATH = "data";

    public static void main(String[] args) {
        Path folderPath = Paths.get(FOLDER_PATH);
        try {
            if (!Files.exists(folderPath)) {
                Files.createDirectories(folderPath);
            }

            Node node = new Node(ROOT_LINK);
            try (ForkJoinPool pool = new ForkJoinPool()) {
                pool.invoke(new MapCreator(node));
            }

            try (FileWriter writer = new FileWriter(FOLDER_PATH + "/map.txt")) {
                String mapString = MapCreator.createMap(node, 0);
                writer.write(mapString);
            } catch (IOException e) {
                System.out.println("An error occurred while writing to the file: " + e.getMessage());
            }
        } catch (IOException e) {
            System.out.println("An error occurred while creating the directory: " + e.getMessage());
        }
    }
}
