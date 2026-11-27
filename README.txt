
Compilação a partir da pasta TP2:
  javac -d out src/grafos/*.java src/algoritmos/*.java src/util/*.java src/Main.java

Execução:
  java -cp out Main

No menu, a demonstração executa os algoritmos em grafos pequenos construídos no programa.
O experimento lê arquivos .gr da pasta indicada, por padrão grafos-professor, compara
Lista de Adjacência (LA) e Matriz de Adjacência (MA) para Dijkstra e Bellman-Ford,
com origem 0 (vértice 1 nos arquivos), e registra tempos em resultados-experimento.csv.
