import adsaufgabe1.IGraph;
import adsaufgabe1.Graph;
import adsaufgabe1.Kantentyp;
import adsaufgabe1.ITiefensuche;
import adsaufgabe1.Tiefensuche_Lennart_CarstensBehrens;

public class Main {
  public static void main(String[] args) throws Exception {
    Graph g = new Graph(4);
    g.setzeKante(0, 1);
    g.setzeKante(1, 2);
    g.setzeKante(3, 0);
    g.setzeKante(3, 2);
    g.setzeKante(2, 0);

    ITiefensuche t = new Tiefensuche_Lennart_CarstensBehrens();
    App.test(g, t, 0, 1, Kantentyp.Baumkante);
    App.test(g, t, 1, 2, Kantentyp.Baumkante);
    App.test(g, t, 3, 0, Kantentyp.Baumkante);
    App.test(g, t, 3, 2, Kantentyp.Baumkante);
    App.test(g, t, 2, 0, Kantentyp.Baumkante);
  }

  public static void test(IGraph g, ITiefensuche t, int von, int nach, Kantentyp erwartet) {
    Kantentyp typ = t.kantenStatus(g, von, nach);
    if (typ != erwartet) {
      System.out.println("[✘]: (" + von + "," + nach + ")[" + typ + "] !!![" + erwartet + "]");
    } else {
      System.out.println("[✓]: (" + von + "," + nach + ")[" + typ + "]");
    }
  }
}
