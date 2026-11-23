import grafos.*;
import algoritmos.*;
import util.*;
import java.nio.file.*;
import java.io.*;
import java.util.*;

public class Main {
  static Scanner sc = new Scanner(System.in);

  public static void main(String[] args) throws Exception {
    while (true) {
      System.out.println(
          "\nTP2 - Grafos\n1 - Demonstrar algoritmos\n2 - Executar experimento (grafos do professor)\n0 - Sair");
      int op = lerInt();
      if (op == 0)
        return;
      if (op == 1)
        demo();
      if (op == 2) {
        System.out.print("Pasta com os .gr (default grafos-professor): ");
        String p = sc.nextLine().trim();
        if (p.isEmpty())
          p = "grafos-professor";
        System.out.print("Repetições (default 10): ");
        String r = sc.nextLine().trim();
        benchmark(Path.of(p), r.isEmpty() ? 10 : Integer.parseInt(r));
      }
    }
  }

  static int lerInt() {
    try {
      return Integer.parseInt(sc.nextLine().trim());
    } catch (Exception e) {
      return -1;
    }
  }

  static void mostrarGrafo(IGrafo g) {
    System.out.println("--- Grafo gerado (" + g.tipo() + ") ---");
    System.out.print("Vértices: ");
    for (Vertice v : g.vertices())
      System.out.print(v + " ");
    System.out.println("\nArestas: " + g.numeroArestas());
    for (Aresta e : g.arestas())
      System.out.println("  " + e);
    System.out.println("--- Resultado ---");
  }

  static void demo() throws Exception {
    IGrafo gd = new GrafoLista(6, TipoGrafo.DIRIGIDO);
    gd.adicionarAresta(0, 1, 5);
    gd.adicionarAresta(0, 2, 3);
    gd.adicionarAresta(1, 3, 6);
    gd.adicionarAresta(2, 3, 2);
    gd.adicionarAresta(3, 4, 4);
    gd.adicionarAresta(4, 5, 1);
    IGrafo dag = gd;
    IGrafo mst = new GrafoLista(6, TipoGrafo.NAO_DIRIGIDO);
    mst.adicionarAresta(0, 1, 4);
    mst.adicionarAresta(0, 2, 3);
    mst.adicionarAresta(1, 2, 1);
    mst.adicionarAresta(1, 3, 2);
    mst.adicionarAresta(2, 3, 4);
    mst.adicionarAresta(3, 4, 2);
    mst.adicionarAresta(4, 5, 6);
    IGrafo fluxo = new GrafoLista(6, TipoGrafo.DIRIGIDO);
    fluxo.adicionarAresta(0, 1, 16);
    fluxo.adicionarAresta(0, 2, 13);
    fluxo.adicionarAresta(1, 2, 10);
    fluxo.adicionarAresta(2, 1, 4);
    fluxo.adicionarAresta(1, 3, 12);
    fluxo.adicionarAresta(3, 2, 9);
    fluxo.adicionarAresta(2, 4, 14);
    fluxo.adicionarAresta(4, 3, 7);
    fluxo.adicionarAresta(3, 5, 20);
    fluxo.adicionarAresta(4, 5, 4);
    System.out.println(
        "1 BFS  2 DFS  3 Topológica  4 Kruskal  5 Prim  6 Bellman-Ford  7 Dijkstra  8 Floyd-Warshall  9 Ford-Fulkerson");
    int op = lerInt();
    if (op < 1 || op > 9)
      return;
    IGrafo g = (op == 4 || op == 5) ? mst : (op == 9) ? fluxo : dag;
    mostrarGrafo(g);
    switch (op) {
      case 1:
      case 2:
        System.out.println((op == 1 ? Algoritmos.bfs(g, 0) : Algoritmos.dfs(g, 0)));
        break;
      case 3:
        System.out.println(Algoritmos.ordenacaoTopologica(g));
        break;
      case 4:
        System.out.println(Algoritmos.kruskal(g));
        break;
      case 5:
        System.out.println(Algoritmos.prim(g, 0));
        break;
      case 6:
        System.out.println(Arrays.toString(Algoritmos.bellmanFord(g, 0)));
        break;
      case 7:
        System.out.println(Arrays.toString(Algoritmos.dijkstra(g, 0)));
        break;
      case 8:
        System.out.println(Arrays.deepToString(Algoritmos.floydWarshall(g)));
        break;
      case 9:
        System.out.println("Fluxo máximo: " + Algoritmos.fordFulkerson(g, 0, 5));
        break;
    }
  }

  static void benchmark(Path pasta, int rep) throws Exception {
    List<Path> arquivos;
    try (var s = Files.list(pasta)) {
      arquivos = s.filter(p -> p.toString().endsWith(".gr")).sorted().toList();
    }
    if (arquivos.isEmpty()) {
      System.out.println("Nenhum .gr encontrado.");
      return;
    }
    Path csv = Path.of("resultados-experimento.csv");
    try (PrintWriter w = new PrintWriter(Files.newBufferedWriter(csv))) {
      w.println("algoritmo,representacao,arquivo_grafo,execucao,tempo_nanosegundos");
      for (Path arq : arquivos) {
        for (boolean mat : new boolean[] { false, true }) {
          IGrafo g = LeitorGrafo.ler(arq, mat, TipoGrafo.DIRIGIDO);
          for (String alg : new String[] { "Dijkstra", "Bellman-Ford" }) {
            for (int i = 1; i <= rep; i++) {
              if (alg.equals("Dijkstra"))
                Algoritmos.dijkstra(g, 0);
              else
                Algoritmos.bellmanFord(g, 0);
              long t0 = System.nanoTime();
              if (alg.equals("Dijkstra"))
                Algoritmos.dijkstra(g, 0);
              else
                Algoritmos.bellmanFord(g, 0);
              long dt = System.nanoTime() - t0;
              w.printf(Locale.US, "%s,%s,%s,%d,%d%n", alg, mat ? "MA" : "LA", arq.getFileName(), i, dt);
            }
          }
        }
      }
      System.out.println("Experimento concluído: " + csv.toAbsolutePath());
    }
  }
}
