import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.io.*;
import com.google.gson.*;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
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
            if (content.id < node.getContent().id) node.left = insertAt(node.left,content);
            if (content.id > node.getContent().id) node.right = insertAt(node.right,content);
            if (content.id == node.getContent().id) System.out.println("mach ich nicht");            
        }
        return node;
    }

    public void insert(Content content) {
        root = insertAt(root,content);
    }


    // Herabsteigende Ausgabe
    private void printDescAt(Node node) {
        if (node == null) return;
        printDescAt(node.right);
        System.out.print(node.getContent().id + " ");
        printDescAt(node.left);
    }

    public void printDesc() {
        System.out.println("Print Desc");
        printDescAt(root);
        System.out.println();
    }

    // Suchen
    private void searchTree(Node node, int id) {
        if (node == null) return;
        if (node.getContent().id == id) {
            System.out.println("Search: " + node.getContent().id + " - " + node.getContent().value);
        }
        if (node.getContent().id < id) searchTree(node.right, id);
        else searchTree(node.left, id);
    }

    public void search(int id) {
        searchTree(root, id);
    }

    // Tiefe
    private ArrayList<Integer> list;
    private void getDepth(Node node, int schritte) {
        if (node == null) return;
        getDepth(node.left, schritte += 1);
        getDepth(node.right, schritte);
        list.add(schritte);
    }

    public void depth() {
        list = new ArrayList<Integer>();
        getDepth(root, 0);
        System.out.println("Tiefe: " + Collections.max(list));
    }

    // Level ausgeben
    private void printLvl(Node node, int level, int schritte) {
        if (node == null) return;
        printLvl(node.left, level, schritte +=1);
        if (level == schritte) {
            System.out.format("%d : %d %s \n", schritte, node.getContent().id, node.getContent().value);
        }
        printLvl(node.right, level, schritte);
    }

    public void printLevel(int level) {
        System.out.println("Print Level: " + level);
        printLvl(root, level, 0);

    }

    // Content suchen    
    public void getLevel(int id) {
        getLvl(root, id, 0);
    }

    private void getLvl(Node node, int id, int schritte) {
        if (node == null) {return;};
        if (node.getContent().id == id) {
            System.out.format("Get level: %s - %d \n", node.getContent().id, schritte+1);
        }
        if (node.getContent().id < id) getLvl(node.right, id, schritte+=1);
        else getLvl(node.left, id, schritte+=1);
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
        s.bewegeBis(breite-5, hoehe);
        s.schreibeZahl(node.getContent().id); 
        recDraw(node.left, position, s, breite - 40-2*t, hoehe + 50+t, schritte+=1);
        recDraw(node.right, position, s, breite + 40+t, hoehe + 50 + t, schritte);
    }

    public void draw(int position, Leinwand l, Stift s, int breite, int hoehe) {
        l.loescheAlles();
        s.runter();
        s.maleAuf(l);
        s.bewegeBis((double) breite/2, 20);
        this.recDraw(root, 0, s, (int) breite/2, 20, 0);
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
        fileName = fileName.endsWith(".json") == true ? fileName : fileName+".json"; 
        try (Writer writer = new FileWriter(fileName)) {
            g.toJson(jArray, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int idAt(Node node)
    {
        if (node == null) return 0;
        return node.getContent().id;        
    }

    private Node smallestNode(Node node)
    {
        if (node == null) return node;
        if (node.left != null) return smallestNode(node.left);
        return node;
    }

    private Node biggestNode(Node node)
    {
        if (node == null) return node;
        if (node.right != null) return biggestNode(node.right);
        return node;
    }

    private Node deleteAt(Node node, int id)
    {
        if (node == null) return node;
        if (id > node.getContent().id) 
        {
            node.right=deleteAt(node.right,id); 
            return node;
        }
        if (id < node.getContent().id) 
        {
            node.left=deleteAt(node.left,id);
            return node;
        }
        // jetzt erst drei Fälle löschen
        // zuerst die beiden leichten
        if (node.left==null) return node.right;
        if (node.right==null) return node.left;  
        // jetzt fullnode
        Node temp=smallestNode(node.right);
        node.right=deleteAt(node.right,idAt(temp)); 
        temp.right=node.right;
        temp.left=node.left;
        node=temp;       
        return node;
    }
    
    private String printAscAt(Node node)
    {
        if (node == null) return "";
        String teilString="";
        teilString=teilString+printAscAt(node.left);
        teilString=teilString+node.getContent().id+" ";
        teilString=teilString+printAscAt(node.right);
        return teilString;
    }

    public String printAsc()
    {
        return printAscAt(root);
    }

    public void delete(int id)
    {
        root=deleteAt(root,id);
    }

}
