import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;

/**
 * @author: JASMINE.LIANG(yxl5521)
 * @create: 2020-06-13
 *
 * The main class to do the search
 */
public class Search {

  private static final String STANDARD_CMD = "-";

  private static Map<String, Vertex> cities;

  /**
   *  heuristic file, use Euclidean distance
   * @param current the current node
   * @param dest the end node
   * @return the heuristic cost
   */
  private static float getHeuristic(String current, String dest) {
    Vertex currentCity = cities.get(current);
    Vertex destCity = cities.get(dest);
    float lat = currentCity.getLatitude() - destCity.getLatitude();
    float lon = currentCity.getLongitude() - destCity.getLongitude();
    return (float) (Math.sqrt((lat * 2) + (lon * 2)) * 100);
  }

  /**
   * method to read the input file or inputs
   * @param input the input, whether is a '-' or a file
   * @return inputs
   */
  private static String[] getInput(String input) {
    try {
      Scanner sc = input.equals(STANDARD_CMD) ?
          new Scanner(System.in) : new Scanner(new File(input));
      return new String[]{sc.nextLine(), sc.nextLine()};
    } catch ( FileNotFoundException e ) {
      System.err.println("File not found: " + input);
      System.exit(0);
    }
    throw new RuntimeException();
  }

  /**
   * Breadth first search algorithm
   * @param graph the graph to search
   * @param start start city
   * @param dest destination city
   * @return solution list
   */
  private static List<String> bfsSearch(Map<String, Map<String, Float>> graph, String start, String dest) {
    Map<String, String> visited = new HashMap<>();
    Queue<String> queue = new LinkedList<>();
    queue.add(start);
    visited.put(start, null);
    while (!queue.isEmpty()) {
      String current = queue.poll();
      List<String> neighbors = new ArrayList<>(graph.get(current).keySet());
      Collections.sort(neighbors);
      for (String vertex : neighbors) {
        if (!visited.containsKey(vertex)) {
          visited.put(vertex, current);
          queue.add(vertex);
        }
        if (dest.equals(vertex)) {
          queue.clear();
          break;
        }
      }
    }
    return findPath(visited, dest);
  }

  /**
   * Depth first search
   * @param graph the graph to search
   * @param start the start city
   * @param dest the destination city
   * @return the solution list
   */
  private static List<String> dfsSearch(Map<String, Map<String, Float>> graph, String start, String dest) {
    Map<String, String> visited = new HashMap<>();
    Stack<String> stack = new Stack<>();
    List<String> path = new ArrayList<>();
    visited.put(start, null);
    stack.push(start);
    while (!stack.isEmpty()) {
      String current = stack.pop();
      List<String> neighbors = new ArrayList<>(graph.get(current).keySet());
      neighbors.sort(Collections.reverseOrder());
      for (String vertex : neighbors) {
        if (!visited.containsKey(vertex)) {
          visited.put(vertex, current);
          stack.push(vertex);
          path.add(current);
        }
        if (dest.equals(vertex)) {
          path.add(dest);
          stack.clear();
          break;
        }
      }
    }
    return findPath(visited, dest);
  }

  /**
   * A* search algorithm
   * @param graph the graph to search
   * @param start the start city
   * @param dest the end city
   * @return the solution list
   */
  private static List<String> aStarSearch(Map<String, Map<String, Float>> graph, String start, String dest) {
    Comparator<AStarVertex> distanceComparator = (s1, s2) -> (int) (s1.getF() - s2.getF());
    PriorityQueue<AStarVertex> openList = new PriorityQueue<>(distanceComparator);
    Map<String, String> visited = new HashMap<>();
    Map<String, Float> gCost = new HashMap<>();

    openList.add(new AStarVertex(start, getHeuristic(start, dest)));
    gCost.put(start, (float) 0);
    visited.put(start, null);

    while (!openList.isEmpty()) {
      AStarVertex current = openList.poll();
      String currentCity = current.getCity();
      if (dest.equals(currentCity)) {
        openList.clear();
        break;
      }
      Set<String> neighbors = graph.get(currentCity).keySet();
      for (String vertex : neighbors) {
        float tempG = gCost.get(currentCity) + graph.get(currentCity).get(vertex);
        if (!gCost.containsKey(vertex) || tempG < gCost.get(vertex)) {
          gCost.put(vertex, tempG);
          openList.add(new AStarVertex(vertex, tempG + getHeuristic(vertex, dest)));
          visited.put(vertex, currentCity);
        }
      }
    }
    return findPath(visited, dest);
  }

  /**
   * find the path from the visited nodes
   * @param visited the visited list get from search
   * @param dest the destination city
   * @return the solution list
   */
  private static List<String> findPath(Map<String, String> visited, String dest) {
    List<String> path = new ArrayList<>();
    // Start from the end and find the path from the back
    String current = dest;
    while (current != null) {
      path.add(0, current);
      current = visited.get(current);
    }
    return path;
  }

  /**
   * the output for each search
   * @param search the search algorithm
   * @param path the solution path
   * @param graph the graph that was searched
   * @return the output
   */
  private static String getOutput(String search, List<String> path, Map<String, Map<String, Float>> graph) {
    StringBuilder solution = new StringBuilder();
    solution.append(String.format("\n%s Search Results: \n", search));
    float totalDistance = 0;
    String prev = null;
    for (String city : path) {
      solution.append(city);
      solution.append("\n");
      if (prev != null) {
        totalDistance += graph.get(prev).get(city);
      }
      prev = city;
    }
    solution.append(String.format("That took %d hops to find.\n", path.size() - 1));
    solution.append(String.format("Total distance = %d miles.\n", Math.round(totalDistance)));
    return solution.toString();
  }

  /**
   * print the output to file or standard output
   * @param output the output, if is a '-' than print to stdout, else to a file
   * @param solution the solution string to print
   */
  private static void printOutput(String output, String solution) {
    try {
      PrintWriter pw = output.equals(STANDARD_CMD) ?
          new PrintWriter(System.out) : new PrintWriter(new File(output));
      pw.println(solution);
      pw.close();
    } catch ( Exception e ) {
      System.err.println(e.toString());
      System.exit(0);
    }
  }

  public static void main(String[] args) {
    if (args.length != 2) {
      System.err.println("Usage: java Search inputFile outputFile");
      System.exit(0);
    }

    String input = args[0];
    String output = args[1];

    String[] inputs = getInput(input);

    Data data = new Data();
    cities = data.getCities();
    for (String city : inputs) {
      if (!cities.keySet().contains(city)) {
        System.err.println(String.format("No such city: (%s)", city));
        System.exit(0);
      }
    }

    String start = inputs[0];
    String dest = inputs[1];

    Map<String, Map<String, Float>> graph = data.getGraph();
    StringBuilder solution = new StringBuilder();

    solution.append(getOutput("Breadth-First", bfsSearch(graph, start, dest), graph) + "\n");
    solution.append(getOutput("Depth-First", dfsSearch(graph, start, dest), graph) + "\n");
    solution.append(getOutput("A*", aStarSearch(graph, start, dest), graph));

    printOutput(output, solution.toString());

  }
}
