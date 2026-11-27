package grafos;

public class Vertice {
  private final int id;
  private String rotulo;

  public Vertice(int id) {
    this(id, Integer.toString(id));
  }

  public Vertice(int id, String rotulo) {
    this.id = id;
    this.rotulo = rotulo;
  }

  public int getId() {
    return id;
  }

  public String getRotulo() {
    return rotulo;
  }

  public void setRotulo(String rotulo) {
    this.rotulo = rotulo;
  }

  public String toString() {
    return rotulo;
  }
}
