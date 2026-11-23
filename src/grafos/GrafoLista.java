package grafos;

import java.util.*;

public class GrafoLista implements IGrafo {
  private final List<Vertice> vertices;
  private final List<Aresta> arestas;
  private final List<List<Aresta>> adj;
  private final TipoGrafo tipo;

  public GrafoLista(int n, TipoGrafo t) {
    if (n < 0)
      throw new IllegalArgumentException("Número de vértices inválido");
    tipo = t;
    vertices = new ArrayList<>();
    arestas = new ArrayList<>();
    adj = new ArrayList<>();
    for (int i = 0; i < n; i++) {
      vertices.add(new Vertice(i));
      adj.add(new ArrayList<>());
    }
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
    Aresta e = new Aresta(vertices.get(u), vertices.get(v), w);
    arestas.add(e);
    adj.get(u).add(e);
    if (tipo == TipoGrafo.NAO_DIRIGIDO && u != v) {
      Aresta r = new Aresta(vertices.get(v), vertices.get(u), w);
      adj.get(v).add(r);
    }
  }

  public Aresta obterAresta(int u, int v) {
    validar(u);
    validar(v);
    for (Aresta e : adj.get(u))
      if (e.getV().getId() == v)
        return e;
    return null;
  }

  public List<Aresta> adjacentes(int u) {
    validar(u);
    return Collections.unmodifiableList(adj.get(u));
  }
}
