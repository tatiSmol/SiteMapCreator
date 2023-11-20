import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.concurrent.ForkJoinPool;

public class Main {
    private static final String FOLDER_PATH = "data";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the website address in format like https://example.com/:");
        String rootLink = scanner.nextLine();

        try {
            new URL(rootLink);
        } catch (MalformedURLException e) {
            System.out.println("The entered URL is invalid. Please try again later.");
            return;
        }

        Path folderPath = Paths.get(FOLDER_PATH);
        try {
            if (!Files.exists(folderPath)) {
                Files.createDirectories(folderPath);
            }

            Node node = new Node(rootLink);
            try (ForkJoinPool pool = new ForkJoinPool()) {
                pool.invoke(new MapCreator(node));
            }

            String siteName = rootLink.substring(rootLink.indexOf("/") + 2, rootLink.lastIndexOf("."));
            try (FileWriter writer = new FileWriter(FOLDER_PATH + "/" + siteName + "Map.txt")) {
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
