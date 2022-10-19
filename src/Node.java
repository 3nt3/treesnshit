
public class Node {
    private Content content;
    public Node left; // kleiner 
    public Node right; // größer

    public Node(Content content) {
        this.content = content;
        this.left = null;
        this.right = null;
    }

    public Content getContent() {
        return this.content;
    }
}
