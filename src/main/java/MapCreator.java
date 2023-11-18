import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Collections;
import java.util.concurrent.RecursiveAction;

public class MapCreator extends RecursiveAction {
    private Node node;

    public MapCreator(Node node) {
        this.node = node;
    }

    @Override
    protected void compute() {
        try {
            Thread.sleep(100);
            Document source = Jsoup.connect(node.getLink()).maxBodySize(0).get();
            Elements elements = source.select("body").select("a");

            if (!elements.isEmpty()) {
                for (Element element : elements) {
                    String absLink = element.absUrl("href");
                    if (absLink.startsWith(node.getLink()) && absLink.endsWith("/")) {
                        node.addChild(new Node(absLink));
                    }
                }
            }
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }

        for (Node child : node.getChildren()) {
            MapCreator task = new MapCreator(child);
            task.fork();
            task.compute();
        }
    }

    public static String createMap(Node node, int tabCount) {
        String tabs = String.join("", Collections.nCopies(tabCount, "\t"));
        StringBuilder builder = new StringBuilder(tabs + node.getLink());

        for (Node child : node.getChildren()) {
            builder.append("\n").append(createMap(child, tabCount + 1));
        }

        return builder.toString();
    }
}
