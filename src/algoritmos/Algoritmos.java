package algoritmos;

import grafos.*;
import java.util.*;

public class Algoritmos {
  public static List<Integer> bfs(IGrafo g, int s) {
    valid(g, s);
    boolean[] seen = new boolean[g.numeroVertices()];
    List<Integer> out = new ArrayList<>();
    Queue<Integer> q = new ArrayDeque<>();
    q.add(s);
    seen[s] = true;
    while (!q.isEmpty()) {
      int u = q.remove();
      out.add(u);
      for (Aresta e : g.adjacentes(u)) {
        int v = e.getV().getId();
        if (!seen[v]) {
          seen[v] = true;
          q.add(v);
        }
      }
    }
    return out;
  }

  public static List<Integer> dfs(IGrafo g, int s) {
    valid(g, s);
    boolean[] seen = new boolean[g.numeroVertices()];
    List<Integer> out = new ArrayList<>();
    dfsR(g, s, seen, out);
    return out;
  }

  private static void dfsR(IGrafo g, int u, boolean[] seen, List<Integer> o) {
    seen[u] = true;
    o.add(u);
    for (Aresta e : g.adjacentes(u)) {
      int v = e.getV().getId();
      if (!seen[v])
        dfsR(g, v, seen, o);
    }
  }

  public static List<Integer> ordenacaoTopologica(IGrafo g) {
    if (g.tipo() != TipoGrafo.DIRIGIDO)
      throw new IllegalArgumentException("Exige grafo dirigido");
    int n = g.numeroVertices();
    int[] d = new int[n];
    for (Aresta e : g.arestas())
      d[e.getV().getId()]++;
    Queue<Integer> q = new ArrayDeque<>();
    for (int i = 0; i < n; i++)
      if (d[i] == 0)
        q.add(i);
    List<Integer> o = new ArrayList<>();
    while (!q.isEmpty()) {
      int u = q.remove();
      o.add(u);
      for (Aresta e : g.adjacentes(u)) {
        int v = e.getV().getId();
        if (--d[v] == 0)
          q.add(v);
      }
    }
    if (o.size() != n)
      throw new IllegalArgumentException("Grafo contém ciclo");
    return o;
  }

  public static List<Aresta> kruskal(IGrafo g) {
    exigeNaoDirigido(g);
    List<Aresta> es = new ArrayList<>(g.arestas());
    es.sort(Comparator.comparingDouble(Aresta::getPeso));
    int[] p = new int[g.numeroVertices()];
    for (int i = 0; i < p.length; i++)
      p[i] = i;
    List<Aresta> out = new ArrayList<>();
    for (Aresta e : es) {
      int a = raiz(p, e.getU().getId()), b = raiz(p, e.getV().getId());
      if (a != b) {
        p[a] = b;
        out.add(e);
      }
    }
    return out;
  }

  public static List<Aresta> prim(IGrafo g, int inicio) {
    exigeNaoDirigido(g);
    valid(g, inicio);
    boolean[] seen = new boolean[g.numeroVertices()];
    List<Aresta> out = new ArrayList<>();
    PriorityQueue<Aresta> pq = new PriorityQueue<>(Comparator.comparingDouble(Aresta::getPeso));
    seen[inicio] = true;
    pq.addAll(g.adjacentes(inicio));
    while (!pq.isEmpty()) {
      Aresta e = pq.poll();
      int v = e.getV().getId();
      if (seen[v])
        continue;
      seen[v] = true;
      out.add(e);
      for (Aresta x : g.adjacentes(v))
        if (!seen[x.getV().getId()])
          pq.add(x);
    }
    return out;
  }

  private static int raiz(int[] p, int x) {
    while (p[x] != x) {
      p[x] = p[p[x]];
      x = p[x];
    }
    return x;
  }

  public static double[] bellmanFord(IGrafo g, int s) {
    valid(g, s);
    int n = g.numeroVertices();
    double[] d = new double[n];
    Arrays.fill(d, Double.POSITIVE_INFINITY);
    d[s] = 0;
    List<Aresta> es = g.arestas();
    for (int i = 1; i < n; i++) {
      boolean changed = false;
      for (Aresta e : es) {
        int u = e.getU().getId(), v = e.getV().getId();
        if (d[u] != Double.POSITIVE_INFINITY && d[u] + e.getPeso() < d[v]) {
          d[v] = d[u] + e.getPeso();
          changed = true;
        }
        if (g.tipo() == TipoGrafo.NAO_DIRIGIDO && d[v] != Double.POSITIVE_INFINITY && d[v] + e.getPeso() < d[u]) {
          d[u] = d[v] + e.getPeso();
          changed = true;
        }
      }
      if (!changed)
        break;
    }
    for (Aresta e : es) {
      int u = e.getU().getId(), v = e.getV().getId();
      if (d[u] != Double.POSITIVE_INFINITY && d[u] + e.getPeso() < d[v])
        throw new IllegalArgumentException("Ciclo negativo detectado");
      if (g.tipo() == TipoGrafo.NAO_DIRIGIDO && d[v] != Double.POSITIVE_INFINITY && d[v] + e.getPeso() < d[u])
        throw new IllegalArgumentException("Ciclo negativo detectado");
    }
    return d;
  }

  public static double[] dijkstra(IGrafo g, int s) {
    valid(g, s);
    for (Aresta e : g.arestas())
      if (e.getPeso() < 0)
        throw new IllegalArgumentException("Dijkstra não aceita peso negativo");
    int n = g.numeroVertices();
    double[] d = new double[n];
    Arrays.fill(d, Double.POSITIVE_INFINITY);
    d[s] = 0;
    PriorityQueue<No> pq = new PriorityQueue<>(Comparator.comparingDouble(x -> x.d));
    pq.add(new No(s, 0));
    boolean[] done = new boolean[n];
    while (!pq.isEmpty()) {
      No x = pq.poll();
      int u = x.v;
      if (done[u])
        continue;
      done[u] = true;
      for (Aresta e : g.adjacentes(u)) {
        int v = e.getV().getId();
        double nd = d[u] + e.getPeso();
        if (nd < d[v]) {
          d[v] = nd;
          pq.add(new No(v, nd));
        }
      }
    }
    return d;
  }

  private static class No {
    int v;
    double d;

    No(int v, double d) {
      this.v = v;
      this.d = d;
    }
  }

  public static double[][] floydWarshall(IGrafo g) {
    int n = g.numeroVertices();
    double[][] d = new double[n][n];
    for (int i = 0; i < n; i++) {
      Arrays.fill(d[i], Double.POSITIVE_INFINITY);
      d[i][i] = 0;
      for (Aresta e : g.adjacentes(i)) {
        int v = e.getV().getId();
        d[i][v] = Math.min(d[i][v], e.getPeso());
      }
    }
    for (int k = 0; k < n; k++)
      for (int i = 0; i < n; i++)
        for (int j = 0; j < n; j++)
          if (d[i][k] != Double.POSITIVE_INFINITY && d[k][j] != Double.POSITIVE_INFINITY && d[i][k] + d[k][j] < d[i][j])
            d[i][j] = d[i][k] + d[k][j];
    for (int i = 0; i < n; i++)
      if (d[i][i] < 0)
        throw new IllegalArgumentException("Ciclo negativo detectado");
    return d;
  }

  public static int fordFulkerson(IGrafo g, int s, int t) {
    valid(g, s);
    valid(g, t);
    if (s == t)
      throw new IllegalArgumentException("Origem e destino devem ser distintos");
    int n = g.numeroVertices();
    double[][] r = new double[n][n];
    for (Aresta e : g.arestas()) {
      int u = e.getU().getId(), v = e.getV().getId();
      if (e.getPeso() < 0 || e.getPeso() != Math.floor(e.getPeso()))
        throw new IllegalArgumentException("Capacidade deve ser inteira e não negativa");
      r[u][v] += e.getPeso();
    }
    double max = 0;
    int[] p = new int[n];
    while (bfsResidual(r, s, t, p)) {
      double f = Double.POSITIVE_INFINITY;
      for (int v = t; v != s; v = p[v])
        f = Math.min(f, r[p[v]][v]);
      for (int v = t; v != s; v = p[v]) {
        r[p[v]][v] -= f;
        r[v][p[v]] += f;
      }
      max += f;
    }
    return (int) Math.round(max);
  }

  private static boolean bfsResidual(double[][] r, int s, int t, int[] p) {
    Arrays.fill(p, -1);
    p[s] = s;
    Queue<Integer> q = new ArrayDeque<>();
    q.add(s);
    while (!q.isEmpty()) {
      int u = q.remove();
      for (int v = 0; v < r.length; v++)
        if (p[v] == -1 && r[u][v] > 1e-10) {
          p[v] = u;
          q.add(v);
        }
    }
    return p[t] != -1;
  }

  private static void valid(IGrafo g, int s) {
    if (s < 0 || s >= g.numeroVertices())
      throw new IndexOutOfBoundsException("Vértice inválido");
  }

  private static void exigeNaoDirigido(IGrafo g) {
    if (g.tipo() != TipoGrafo.NAO_DIRIGIDO)
      throw new IllegalArgumentException("Exige grafo não dirigido");
  }
}
