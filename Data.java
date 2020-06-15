import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * @author: JASMINE.LIANG(yxl5521)
 * @create: 2020-06-13
 *
 * The class that read the data and create the graph
 */
public class Data {

  private static final String CITY_DATA = "city.dat";
  private static final String EDGE_DATA = "edge.dat";

  private Map<String, Map<String, Float>> graph = new HashMap<>();
  private Map<String, Vertex> cities = new HashMap<>();

  Data() {
    initGraph();
  }

  private void initGraph() {
    readVertices();
    File file = new File(EDGE_DATA);
    try {
      Scanner sc = new Scanner(file);
      while (sc.hasNextLine()) {
        String[] data = sc.nextLine().split("\\s+");
        if (data.length != 2) {
          continue;
        }
        Vertex v1 = cities.get(data[0]);
        Vertex v2 = cities.get(data[1]);
        float distance = getDistance(v1, v2);
        this.graph.get(v1.getCity()).put(v2.getCity(), distance);
        this.graph.get(v2.getCity()).put(v1.getCity(), distance);
      }
    } catch ( FileNotFoundException e ) {
      System.err.println(String.format("File not found: %s", EDGE_DATA));
      System.exit(0);
    }
  }

  public Map<String, Map<String, Float>> getGraph(){
    return this.graph;
  }

  private void readVertices() {
    try {
      File file = new File(CITY_DATA);
      Scanner sc = new Scanner(file);
      while (sc.hasNextLine()) {
        String[] city = sc.nextLine().split("\\s+");
        if (city.length != 4) {
          continue;
        }
        Vertex vertex = new Vertex(city[0], city[1],
            Float.parseFloat(city[2]), Float.parseFloat(city[3]));
        this.cities.put(city[0], vertex);
        this.graph.putIfAbsent(vertex.getCity(), new HashMap<>());
      }
    } catch ( FileNotFoundException e ) {
      System.err.println(String.format("File not found: %s", CITY_DATA));
      System.exit(0);
    }
  }

  public Map<String, Vertex> getCities(){
    return this.cities;
  }

  private float getDistance(Vertex city1, Vertex city2) {
    float lat = city1.getLatitude() - city2.getLatitude();
    float lon = city1.getLongitude() - city2.getLongitude();
    return (float) (Math.sqrt((lat * lat) + (lon * lon)) * 100);
  }

}
