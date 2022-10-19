// 

import basis.*;

import javax.swing.*;
import java.awt.*;

public class TreeGUI extends Fenster implements KnopfLauscher, ListAuswahlLauscher {
    private Knopf ende, insert, draw, export, printAsc, printDesc, search, depth, printLevel, getNodeLevel, delete;
    private ZahlenFeld id, searchId;
    private Stift stift;
    private TextFeld dateiName, value;
    private Leinwand l;
    private Tree tree;

    public TreeGUI() {
        this.initGui();
    }

    public void initGui() {

        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedLookAndFeelException e) {
            throw new RuntimeException(e);
        }

        this.setzeHintergrundFarbe(UIManager.getLookAndFeel().getDefaults().getColor("Viewport.background"));
        this.setzeGroesse(800, 800);
        this.setzeTitel("[SWING] Trees");
        ende = new Knopf("Ende", 650, 750, 100, 30);
        ende.setzeKnopfLauscher(this);
        BeschriftungsFeld label1 = new BeschriftungsFeld("TreeDemo 木", 10, 10, 580, 30);

        int columnWidth = 180;
        export = new Knopf("Export", 10, 510, columnWidth, 30);
        export.setzeKnopfLauscher(this);

        dateiName = new TextFeld(10, 540, columnWidth, 30);
        dateiName.setzeText("Path");

        printAsc = new Knopf("Print Asc", 10, 310, columnWidth, 30);
        printAsc.setzeKnopfLauscher(this);

        printDesc = new Knopf("Print (desc)", 10, 280, columnWidth, 30);
        printDesc.setzeKnopfLauscher(this);

        depth = new Knopf("Depth", 10, 250, columnWidth, 30);
        depth.setzeKnopfLauscher(this);

        search = new Knopf("Find", 10, 180, columnWidth, 30);
        search.setzeKnopfLauscher(this);
        searchId = new ZahlenFeld(10, 210, columnWidth, 30);

        getNodeLevel = new Knopf("Get node level", 10, 150, columnWidth, 30);
        getNodeLevel.setzeKnopfLauscher(this);

        printLevel = new Knopf("Print level (stdout)", 10, 120, columnWidth, 30);
        printLevel.setzeKnopfLauscher(this);

        delete = new Knopf("Delete", 10, 90, columnWidth, 30);
        delete.setzeKnopfLauscher(this);

        stift = new Stift();


        new BeschriftungsFeld("Value", 10, 380, columnWidth, 30);
        value = new TextFeld(10, 410, columnWidth, 30);

        new BeschriftungsFeld("ID", 10, 440, columnWidth, 30);
        id = new ZahlenFeld(10, 470, columnWidth, 30);
        id.setzeText("adf");

        insert = new Knopf("Insert", 10, 350, columnWidth, 30);
        insert.setzeKnopfLauscher(this);

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // CANVAS
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        l = new Leinwand(200, 100, 500, 500);
        Graphics2D g2d = (Graphics2D) l.getSwingComponent().getGraphics();
        g2d.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        tree = new Tree();
        tree.readFile("./test.json");

        tree.draw(0, l, stift, l.breite(), l.hoehe());

        System.out.println(tree.generateTikZ());
    }

    @Override
    public void bearbeiteKnopfDruck(Knopf k) {
        if (k == ende) {
            this.gibFrei();
        } else if (k == insert) {
            tree.insert(new Content(id.ganzZahl(), value.text()));
        } else if (k == export) {
            tree.save(dateiName.text());
        } else if (k == printAsc) {
            tree.printAsc();
        } else if (k == printDesc) {
            tree.printDesc();
        } else if (k == depth) {
            tree.depth();
        } else if (k == search) {
            tree.search(searchId.ganzZahl());
        } else if (k == getNodeLevel) {
            tree.getNodeLevel(searchId.ganzZahl());
        } else if (k == printLevel) {
            tree.printLevel(searchId.ganzZahl());
        } else if (k == delete) {
            tree.delete(searchId.ganzZahl());
        }
        tree.draw(0, l, stift, l.breite(), l.hoehe());
    }

    @Override
    public void bearbeiteAuswahl(ListAuswahl k) {

    }

    @Override
    public void bearbeiteDoppelklick(ListAuswahl k) {

    }
}