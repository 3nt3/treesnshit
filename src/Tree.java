import java.nio.file.*;
import java.io.*;

import com.google.gson.*;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import basis.*;

public class Tree {
    private Node root;

    public Tree() {
        root = null;
    }

    // Insert
    private Node insertAt(Node node, Content content) {
        if (node == null) node = new Node(content);
        else {
            if (content.id < node.getContent().id) node.left = insertAt(node.left, content);
            if (content.id > node.getContent().id) node.right = insertAt(node.right, content);
            if (content.id == node.getContent().id) System.out.println("mach ich nicht");
        }
        return node;
    }

    public void insert(Content content) {
        root = insertAt(root, content);
    }

    // Level ausgeben
    private void printLvl(Node node, int level, int schritte) {
        if (node == null) return;
        printLvl(node.left, level, schritte += 1);
        if (level == schritte) {
            System.out.format("%d : %d %s \n", schritte, node.getContent().id, node.getContent().value);
        }
        printLvl(node.right, level, schritte);
    }

    // Datei lesen
    public void readFile(String filePath) {
        Gson gson = new Gson();
        try (Reader reader = new FileReader(filePath)) {
            JsonParser parser = new JsonParser();
            JsonArray data = (gson.fromJson(reader, JsonArray.class));
            for (JsonElement b : data) {
                int id = b.getAsJsonObject().get("id").getAsInt();
                String value = b.getAsJsonObject().get("value").getAsString();
                this.insert(new Content(id, value));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Zeichnen
    public void recDraw(Node node, int position, Stift s, int breite, int hoehe, int schritte) {
        if (node == null) return;
        int t = schritte * 10;
        s.bewegeBis(breite, hoehe);
        s.zeichneKreis(20);
        s.bewegeBis(breite - 5, hoehe);
        s.schreibeZahl(node.getContent().id);
        recDraw(node.left, position, s, breite - 40 - 2 * t, hoehe + 50 + t, schritte += 1);
        recDraw(node.right, position, s, breite + 40 + t, hoehe + 50 + t, schritte);
    }

    public void draw(int position, Leinwand l, Stift s, int breite, int hoehe) {
        l.loescheAlles();
        s.runter();
        s.maleAuf(l);
        s.bewegeBis((double) breite / 2, 20);
        this.recDraw(root, 0, s, (int) breite / 2, 20, 0);
    }

    // Baum exportieren
    JsonArray jArray;
    Gson g = new Gson();

    public void addToArray(Node node) {
        if (node == null) return;
        JsonObject data = new JsonObject();
        data.addProperty("id", node.getContent().id);
        data.addProperty("value", node.getContent().value);
        JsonElement element = g.fromJson(data.toString(), JsonElement.class);
        jArray.add(element);
        addToArray(node.left);
        addToArray(node.right);
    }

    public void save(String fileName) {
        jArray = new JsonArray();
        addToArray(root);
        fileName = fileName.endsWith(".json") == true ? fileName : fileName + ".json";
        try (Writer writer = new FileWriter(fileName)) {
            g.toJson(jArray, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int idAt(Node node) {
        if (node == null) return 0;
        return node.getContent().id;
    }

    private Node smallestNode(Node node) {
        if (node == null) return node;
        if (node.left != null) return smallestNode(node.left);
        return node;
    }

    private Node biggestNode(Node node) {
        if (node == null) return node;
        if (node.right != null) return biggestNode(node.right);
        return node;
    }

    private Node deleteAt(Node node, int id) {
        if (node == null) return node;
        if (id > node.getContent().id) {
            node.right = deleteAt(node.right, id);
            return node;
        }
        if (id < node.getContent().id) {
            node.left = deleteAt(node.left, id);
            return node;
        }
        // jetzt erst drei Fälle löschen
        // zuerst die beiden leichten
        if (node.left == null) return node.right;
        if (node.right == null) return node.left;
        // jetzt fullnode
        Node temp = smallestNode(node.right);
        node.right = deleteAt(node.right, idAt(temp));
        temp.right = node.right;
        temp.left = node.left;
        node = temp;
        return node;
    }

    private String printAscAt(Node node) {
        if (node == null) return "";
        String teilString = "";
        teilString = teilString + printAscAt(node.left);
        teilString = teilString + node.getContent().id + " ";
        teilString = teilString + printAscAt(node.right);
        return teilString;
    }

    /**
     * Prints all elements of the tree in ascending order
     */
    public void printAsc() {
        printAscAt(root);
        System.out.println();
    }

    /**
     * @param id the id you want to search for
     * @return Content of the node if it was found
     */
    public Content search(int id) {
        return searchAt(this.root, id);

    }

    /**
     * @param node the parent node from which the function will recurse
     * @param id   the id to search for
     * @return Content if it was found
     */
    private Content searchAt(Node node, int id) {
        if (node == null) return null;
        if (id > node.getContent().id) {
            return searchAt(node.right, id);
        } else if (id < node.getContent().id) {
            return searchAt(node.left, id);
        } else {
            return node.getContent();
        }
    }

    private void printDescAt(Node node) {
        if (node == null) return;
        printDescAt(node.right);
        System.out.print(node.getContent().id + " ");
        printDescAt(node.left);
    }

    /**
     * Prints all elements of the tree in descending order
     */
    public void printDesc() {
        printDescAt(root);
        System.out.println();
    }

    public void printLevel(int level) {
        printLevelFrom(this.root, level - 1);
        System.out.println();
    }

    private Node printLevelFrom(Node node, int level) {
        if (node == null) {
            System.out.print("❌ \t");
            return null;
        }
        if (level != 0) {
            printLevelFrom(node.left, level - 1);
            printLevelFrom(node.right, level - 1);
        } else {
            System.out.print(node.getContent().id + " \t");
        }

        return node;
    }

    // TODO: GetLevel(content) [root ist auf 1, Rückgabe "0", falls nicht vorhanden]
    public int getNodeLevel(int id) {
        return getLevelFrom(this.root, id, 1);
    }

    private int getLevelFrom(Node node, int id, int level) {
        if (node == null) return -1;
        if (node.getContent().id == id) {
            return level;
        }

        if (node.left != null && node.getContent().id > id) {
            return getLevelFrom(node.left, id, level + 1);
        }
        return getLevelFrom(node.right, id, level + 1);
    }

    public void fromFile(String fileName) throws IOException {
        Reader reader = Files.newBufferedReader(Paths.get(fileName));

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Content[] nodes = gson.fromJson(reader, Content[].class);
        for (Content node : nodes) {
            this.insert(node);
        }
    }

    public int depth() {
        return depthFrom(this.root, 1);
    }

    private int depthFrom(Node node, int depth) {
        if (node == null) return depth - 1;
        return Math.max(depthFrom(node.right, depth + 1),
                depthFrom(node.left, depth + 1));
    }

    public void delete(int id) {
        root = deleteAt(root, id);
    }

    public String generateTikZ() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\\documentclass[border=0.2cm]{standalone}\n" +
                " \n" +
                "\\usepackage{tikz}\n" +
                " \n" +
                "\\begin{document}\n" +
                " \n" +
                "\\begin{tikzpicture}\n");

        stringBuilder.append("\\");
        stringBuilder.append(tikzFrom(this.root));
        stringBuilder.append(";");

        stringBuilder.append(
                "\\end{tikzpicture}\n" +
                        " \n" +
                        "\\end{document}");

        return stringBuilder.toString();
    }

    private String tikzFrom(Node node) {
        if (node.left == null && node.right == null) {
            return "node { " + node.getContent().id + " } ";
        } else if (node.left != null && node.right == null) {
            return "node { " + node.getContent().id + " }\n" +
                    "    child { " + tikzFrom(node.left) + " }";
        } else if (node.left == null) {
            return "node { " + node.getContent().id + " }\n" +
                    "    child { " + tikzFrom(node.right) + " }";
        } else {
            return "node { " + node.getContent().id + " }\n" +
                    "    child { " + tikzFrom(node.left) + " }" +
                    "    child { " + tikzFrom(node.right) + " }";
        }
    }
}
