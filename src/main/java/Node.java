import java.util.concurrent.CopyOnWriteArrayList;

public class Node {
    private volatile Node parent;
    private volatile CopyOnWriteArrayList<Node> children;
    private volatile int tabCount;
    private String link;

    public Node(String link) {
        this.link = link;
        tabCount = 0;
        parent = null;
        children = new CopyOnWriteArrayList<>();
    }

    public Node getRootReference() {
        return parent == null ? this : parent.getRootReference();
    }

    public void setParent(Node parent) {
        this.parent = parent;
        this.tabCount = getTabCount();
    }

    public CopyOnWriteArrayList<Node> getChildren() {
        return children;
    }

    public synchronized void addChildren(Node child) {
        Node root = getRootReference();
        if (!root.contains(child.getLink())) {
            child.setParent(this);
            children.add(child);
        }
    }

    public boolean contains(String linkAddress) {
        if (this.link.contains(linkAddress)) {
            return true;
        }
        for (Node child : children) {
            if (child.contains(linkAddress)) {
                return true;
            }
        }
        return false;
    }

    public int getTabCount() {
        int count = 0;
        if (parent == null) {
            return count;
        }
        count = parent.getTabCount() + 1;
        return count;
    }

    public String getLink() {
        return link;
    }
}
