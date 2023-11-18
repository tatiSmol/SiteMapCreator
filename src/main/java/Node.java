import java.util.concurrent.CopyOnWriteArrayList;

public class Node {
    private String link; // рабочая ссылка
    private volatile int tabCount; // количество отступов
    private volatile Node parent; // родитель
    private volatile CopyOnWriteArrayList<Node> children; // дети


    /** Конструктор дерева
     *
     * @param link- ссылка на родительскую, на данный момент, ветку
     */
    public Node(String link) {
        this.link = link;
        tabCount = 0;
        parent = null;
        children = new CopyOnWriteArrayList<>();
    }

    /** Получение корневой ссылки
     *
     * @return - главный родитель
     */
    public Node getRootReference() {
        return parent == null ? this : parent.getRootReference();
    }

    /** Устанавливаем родительскую ветку
     *
     * @param parent - данная ветка станет родительской
     */
    public void setParent(Node parent) {
        this.parent = parent;
        this.tabCount = getTabCount();
    }

    /** Получаем список детей
     *
     * @return наследники родительской, на данный момент, ветки
     */
    public CopyOnWriteArrayList<Node> getChildren() {
        return children;
    }

    /** Добавляем ссылку на наследника
     *
     * @param child наследник
     */
    public synchronized void addChild(Node child) {
        Node root = getRootReference();
        if (!root.contains(child.getLink())) {
            child.setParent(this);
            children.add(child);
        }
    }

    /** Проверка на возможное повторение ссылки (исключение возможности циклического перебора ссылок)
     *
     * @param linkAddress адрес ссылки, которую проверяем
     * @return да/нет
     */
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

    /** Проверка на табуляцию
     *
     * @return валидное количество отступов
     */
    public int getTabCount() {
        int count = 0;
        if (parent == null) {
            return count;
        }
        count = parent.getTabCount() + 1;
        return count;
    }

    /**
     *
     * @return ссылка на объект, у которого запрошена ссылка
     */
    public String getLink() {
        return link;
    }
}
