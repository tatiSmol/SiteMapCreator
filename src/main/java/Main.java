import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ForkJoinPool;

public class Main {
    private static final String ROOT_LINK = "https://lenta.ru/";
    private static final String FOLDER_PATH = "data";

    public static void main(String[] args) throws IOException {
        Path folderPath = Paths.get(FOLDER_PATH);
        if (!Files.exists(folderPath)) {
            Files.createDirectories(folderPath);
        }

        Node node = new Node(ROOT_LINK);
        new ForkJoinPool().invoke(new MapCreator(node));

        FileWriter writer = new FileWriter(FOLDER_PATH + "/map.txt");
        String mapString = MapCreator.createMap(node, 0);
        writer.write(mapString);

        writer.flush();
        writer.close();
    }
}
