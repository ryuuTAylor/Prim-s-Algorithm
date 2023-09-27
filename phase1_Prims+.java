import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.*;

class Main {

  static BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

  // n is the number of nodes, m is the number of input logs
  static int n, m;

  // The Adjacency List keeps only edges of the largest time stamp
  static Map<Integer, List<Edge>> adj = new HashMap<>();

  public static class Edge implements Comparable<Edge> {
    // Edge two stores information for both vertices
    int vertex;
    int weight;
    int parent; // The parent node helps output the edges

    // Used for constructing the adjancency list
    Edge(int vertex, int weight) {
      this.vertex = vertex;
      this.weight = weight;
    }

    // Used for the Prims Algorithm
    Edge(int vertex, int weight, int parent) {
      this.vertex = vertex;
      this.weight = weight;
      this.parent = parent;
    }

    @Override
    public int compareTo(Edge other) {
      return Integer.compare(this.weight, other.weight);
    }

    // weight here is actually the second vertex
    public void print() throws IOException {
      bw.write(String.valueOf(this.parent));
      bw.write(" ");
      bw.write(String.valueOf(this.vertex));
      bw.newLine();
    }
  }

  // Update our adjancency list
  public static void addEdge(int u, int v, int t) {

    // Add or Update edge to adj.get(u) and adj.get(v), keeping the minimum weight
    for (int[] uvedges : new int[][] { { u, v }, { v, u } }) {
      int from = uvedges[0], to = uvedges[1];
      boolean found = false;
      for (Edge edge : adj.get(from)) {
        if (edge.vertex == to) {
          found = true;
          // Update the edge weight
          edge.weight = Math.min(edge.weight, t);
          break;
        }
      }
      if (!found) {
        // Add this new edge to its parent
        adj.get(from).add(new Edge(to, t));
        adj.get(to).add(new Edge(from, t));
        break;
      }
    }
  }

  public static void prims() throws IOException {
    PriorityQueue<Edge> pq = new PriorityQueue<>();
    // Initialize unvisited set
    Set<Integer> unvisited = new HashSet<>();
    for (int i = 1; i <= n; i++) {
      unvisited.add(i);
    }

    // Loop until all vertices are visited
    while (!unvisited.isEmpty()) {
      Iterator<Integer> iterator = unvisited.iterator();
      int startNode = iterator.next(); // Get a vertex from unvisited

      pq.add(new Edge(startNode, 0, -1)); // -1 indicates no parent

      while (!pq.isEmpty()) {
        Edge currentEdge = pq.poll();

        if (!unvisited.contains(currentEdge.vertex)) {
          continue; // Skip if this vertex is already visited
        }

        unvisited.remove(currentEdge.vertex); // Mark as visited

        if (currentEdge.parent != -1) {
          // (new Edge(currentEdge.parent, currentEdge.vertex)).print();
          currentEdge.print();
        }

        for (Edge neighbor : adj.getOrDefault(currentEdge.vertex, new ArrayList<>())) {
          if (unvisited.contains(neighbor.vertex)) {
            pq.add(new Edge(neighbor.vertex, neighbor.weight, currentEdge.vertex));
          }
        }
      }
    }
  }

  public static void main(String[] args) throws Exception {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    String[] line1 = br.readLine().split(" ");
    assert (line1.length == 2);

    // reading in n, m
    n = Integer.parseInt(line1[0]);
    m = Integer.parseInt(line1[1]);

    // Preprocess adj
    for (int i = 1; i <= n; i++) {
      adj.put(i, new ArrayList<Edge>());
    }

    // reading in logs
    for (int i = 0; i < m; i++) {
      String[] line = br.readLine().split(" ");
      assert (line.length == 3);
      addEdge(Integer.parseInt(line[0]), Integer.parseInt(line[1]), -Integer.parseInt(line[2]));
    }

    br.close();

    prims();

    bw.flush();
    bw.close();
  }
}