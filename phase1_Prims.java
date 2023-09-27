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
      bw.write(String.valueOf(this.vertex));
      bw.write(" ");
      bw.write(String.valueOf(this.weight));
      bw.newLine();
    }
  }

  // Update our adjancency list
  public static void addEdge(int u, int v, int t) {
    // adj.putIfAbsent(u, new ArrayList<Edge>());
    // adj.putIfAbsent(v, new ArrayList<Edge>());

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
      }
    }
  }

  public static void prims() throws IOException {
    PriorityQueue<Edge> pq = new PriorityQueue<>();
    HashSet<Integer> visited = new HashSet<>();
    // List<Edge> mstEdges = new ArrayList<>();

    // Loop through all vertices to ensure we cover all connected components
    for (int startNode : adj.keySet()) {
      if (visited.contains(startNode)) {
        continue; // Skip if this vertex is already visited
      }

      pq.add(new Edge(startNode, 0, -1)); // -1 indicates no parent

      while (!pq.isEmpty()) {
        Edge currentEdge = pq.poll();

        if (visited.contains(currentEdge.vertex)) {
          continue;
        }

        visited.add(currentEdge.vertex);

        if (currentEdge.parent != -1) {
          // mstEdges.add(currentEdge.parent + " " + currentEdge.vertex);
          // mstEdges.add(new Edge(currentEdge.parent, currentEdge.vertex));
          (new Edge(currentEdge.parent, currentEdge.vertex)).print();
        }

        for (Edge neighbor : adj.getOrDefault(currentEdge.vertex, new ArrayList<>())) {
          if (!visited.contains(neighbor.vertex)) {
            pq.add(new Edge(neighbor.vertex, neighbor.weight, currentEdge.vertex));
          }
        }
      }
    }

    // return mstEdges;
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

    // Random rand = new Random();
    // int randomStart = rand.nextInt(n) + 1;

    // List<Edge> mst = prims();
    prims();

    // BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

    // // Output all the edges
    // for (Edge e : mst) {
    // e.print();
    // }
    bw.flush();
    bw.close();
  }
}