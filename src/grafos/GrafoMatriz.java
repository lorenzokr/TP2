package grafos;

import java.util.*;

public class GrafoMatriz implements IGrafo {
  private final List<Vertice> vertices;
  private final List<Aresta> arestas;
  private final Aresta[][] matriz;
  private final TipoGrafo tipo;

  public GrafoMatriz(int n, TipoGrafo t) {
    if (n < 0)
      throw new IllegalArgumentException("Número de vértices inválido");
    tipo = Objects.requireNonNull(t);
    vertices = new ArrayList<>();
    arestas = new ArrayList<>();
    matriz = new Aresta[n][n];
    for (int i = 0; i < n; i++)
      vertices.add(new Vertice(i));
  }

  private void validar(int u) {
    if (u < 0 || u >= vertices.size())
      throw new IndexOutOfBoundsException("Vértice inválido: " + u);
  }

  public TipoGrafo tipo() {
    return tipo;
  }

  public List<Vertice> vertices() {
    return Collections.unmodifiableList(vertices);
  }

  public List<Aresta> arestas() {
    return Collections.unmodifiableList(arestas);
  }

  public int numeroVertices() {
    return vertices.size();
  }

  public int numeroArestas() {
    return arestas.size();
  }

  public void adicionarAresta(int u, int v) {
    adicionarAresta(u, v, 1);
  }

  public void adicionarAresta(int u, int v, double w) {
    validar(u);
    validar(v);
    Aresta atual = matriz[u][v];
    if (atual == null || w < atual.getPeso()) {
      if (atual != null)
        arestas.remove(atual);
      Aresta e = new Aresta(vertices.get(u), vertices.get(v), w);
      matriz[u][v] = e;
      arestas.add(e);
      if (tipo == TipoGrafo.NAO_DIRIGIDO && u != v) {
        Aresta r = new Aresta(vertices.get(v), vertices.get(u), w);
        matriz[v][u] = r;
      }
    }
  }

  public Aresta obterAresta(int u, int v) {
    validar(u);
    validar(v);
    return matriz[u][v];
  }

  public List<Aresta> adjacentes(int u) {
    validar(u);
    List<Aresta> l = new ArrayList<>();
    for (Aresta e : matriz[u])
      if (e != null)
        l.add(e);
    return l;
  }
}
