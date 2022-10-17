// 
import basis.*;
import java.awt.*;

public class TreeDemo extends Fenster implements KnopfLauscher, RollbalkenLauscher, ListAuswahlLauscher  {

    //Deklaration
    private Knopf ende, insert, draw, export, printAsc, printDesc, search, depth, printLevel, getLevel, delete;
    private BeschriftungsFeld label1;
    private ZahlenFeld id, searchId;
    private Stift stift;
    private TextFeld dateiName, value;
    private Leinwand l;
    private Tree tree;

    public TreeDemo() {
        this.initGui();
    }

    public void initGui() {
        this.setzeGroesse(800,800);
        this.setzeTitel("TreeDemo");
        ende = new Knopf("Ende",650,750,100,30);
        ende.setzeKnopfLauscher(this);
        label1 = new BeschriftungsFeld("TreeDemo 木",10,10,580,30);
        
        draw = new Knopf("Zeichnen",10,460,120,30);
        draw.setzeKnopfLauscher(this);
        
        export = new Knopf("Exportieren", 10, 510, 120, 30);
        export.setzeKnopfLauscher(this);
        
        dateiName = new TextFeld(10, 540, 120, 30);
        dateiName.setzeText("Dateiname");
        
        printAsc = new Knopf("Print Asc", 10, 310, 120, 30);
        printAsc.setzeKnopfLauscher(this);
        
        printDesc = new Knopf("Print Desc", 10, 280, 120, 30);
        printDesc.setzeKnopfLauscher(this);
        
        depth = new Knopf("Tiefe", 10, 250, 120, 30);
        depth.setzeKnopfLauscher(this);
        
        search = new Knopf("Suchen", 10, 180, 120, 30);
        search.setzeKnopfLauscher(this);
        searchId = new ZahlenFeld(10, 210, 120, 30);
        
        getLevel = new Knopf("Get Level", 10, 150, 120, 30);
        getLevel.setzeKnopfLauscher(this);
        
        printLevel = new Knopf("Print Level", 10, 120, 120, 30);
        printLevel.setzeKnopfLauscher(this);
        
        delete = new Knopf("Löschen", 10, 90, 120, 30);
        delete.setzeKnopfLauscher(this);
        
        stift = new Stift();

        // Eingabe
        value = new TextFeld(10,380,120,30);
        value.setzeText("Value (↓ ID)");
        
        id = new ZahlenFeld(10,410,120,30);
        
        insert = new Knopf("Hinzufügen",10,350,120,30);
        insert.setzeKnopfLauscher(this);

        // Leinwand
        l = new Leinwand(200, 100, 500, 500);
        l.setzeHintergrundFarbe(Farbe.GELB);

        tree = new Tree();
        tree.readFile("./test.json");
    }


    @Override
    public void bearbeiteKnopfDruck(Knopf k) {
        if (k == ende) {
            this.gibFrei();
        } else if (k == draw) {
            tree.draw(0, l, stift, l.breite(), l.hoehe());
        } else if (k == insert) {
            tree.insert(new Content(id.ganzZahl(), value.text()));
        } else if (k == export) {
            tree.save(dateiName.text());
        } else if (k == printAsc) {
            System.out.println(tree.printAsc());
        } else if (k == printDesc) {
            tree.printDesc();
        } else if (k == depth) {
            tree.depth();
        } else if (k == search) {
            tree.search(searchId.ganzZahl());
        } else if (k == getLevel) {
            tree.getLevel(searchId.ganzZahl());
        } else if (k == printLevel) {
            tree.printLevel(searchId.ganzZahl());
        } else if (k == delete) {
            tree.delete(searchId.ganzZahl());
        }
    }

    @Override
    public void bearbeiteRollbalkenBewegung(Rollbalken k) {
        
    }

    @Override
    public void bearbeiteAuswahl(ListAuswahl k)
    {

    }

    @Override
    public void bearbeiteDoppelklick(ListAuswahl k)
    {

    }
}
