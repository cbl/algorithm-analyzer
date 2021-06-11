package sorts;

import java.util.Arrays;
import contracts.Algorithm;

public class Tiefensuche implements Algorithm {
    Graph graph;

    public Tiefensuche(Graph graph) {
        this.graph = graph;
    }

    public void run()
    {
        boolean bekannt[] = new boolean[graph.anfrageKnotenanzahl()];
        int vorgaenger[] = new int[graph.anfrageKnotenanzahl()];
        int entdeckt[] = new int[graph.anfrageKnotenanzahl()];
        int fertig[] = new int[graph.anfrageKnotenanzahl()];
        int zeit = 0;

        for (int u = 0; u < graph.anfrageKnotenanzahl(); u++) {
            if (!bekannt[u])
                zeit = expand(u, entdeckt, fertig, vorgaenger, bekannt, zeit);
        }

        if (entdeckt[von] < entdeckt[nach] && entdeckt[nach] < fertig[nach] && fertig[nach] < fertig[von])
            return vorgaenger[nach] == von ? Kantentyp.Baumkante : Kantentyp.Vorwaertskante;
        else if (entdeckt[nach] < fertig[nach] && fertig[nach] < entdeckt[von] && entdeckt[von] < fertig[von])
            return Kantentyp.Querkante;
    }

    @Override
    public Kantentyp kantenStatus(int von, int nach) {
        if (!graph.anfrageKante(von, nach))
            return Kantentyp.KeineKante;

        
        System.out.println(Arrays.toString(entdeckt));
        System.out.println(Arrays.toString(fertig));

        

        for (int i = 0; i < fertig.length; i++) {
            System.out.println(i + "(entdeckt):" + entdeckt[i]);
            System.out.println(i + "(Fertig):" + fertig[i]);
        }

        return Kantentyp.Rueckwaertskante;
    }

    protected int expand(int u, int[] entdeckt, int[] fertig, int[] vorgaenger, boolean[] bekannt,
            int zeit) {
        bekannt[u] = true;
        entdeckt[u] = ++zeit;
        
        for (int v = 0; v < graph.anfrageKnotenanzahl(); v++) {
            if (bekannt[v] || !graph.anfrageKante(u, v))
                continue;
            vorgaenger[v] = u;
            zeit = expandiere(graph, v, entdeckt, fertig, vorgaenger, bekannt, zeit);
        }
        fertig[u] = ++zeit;

        return zeit;
    }
}