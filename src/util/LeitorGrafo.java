package util;

import grafos.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class LeitorGrafo {
  public static IGrafo ler(Path p, boolean matriz, TipoGrafo tipo) throws IOException {
    List<String> ls = Files.readAllLines(p);
    int n = -1;
    List<double[]> es = new ArrayList<>();
    for (String l : ls) {
      l = l.trim();
      if (l.isEmpty() || l.startsWith("c") || l.startsWith("#"))
        continue;
      String[] a = l.split("\\s+");
      if (a[0].equals("p")) {
        n = Integer.parseInt(a[a.length - 2]);
      } else if (a[0].equals("a") || a[0].equals("e")) {
        es.add(new double[] { Integer.parseInt(a[1]) - 1, Integer.parseInt(a[2]) - 1, Double.parseDouble(a[3]) });
      }
    }
    if (n < 0)
      throw new IOException("Cabeçalho p não encontrado: " + p);
    IGrafo g = matriz ? new GrafoMatriz(n, tipo) : new GrafoLista(n, tipo);
    for (double[] e : es)
      g.adicionarAresta((int) e[0], (int) e[1], e[2]);
    return g;
  }
}
