import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.BufferedWriter;
import java.util.*;

class Main {

  static BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

  // We take as input n vertices, an MST of l edges, and k queries
  static int n, l, k;

  static Map<Integer, List<Edge>> adj = new HashMap<>();

  public static class Edge {
    int vertex;
    int weight;

    Edge(int vertex, int weight) {
      this.vertex = vertex;
      this.weight = weight;
    }
  }

  public static void addEdge(int u, int v, int t) {
    adj.get(u).add(new Edge(v, t));
    adj.get(v).add(new Edge(u, t));
  }

  public static Set<Integer> bfs(int u, int t) {
    Set<Integer> visited = new HashSet<>();
    Deque<Integer> q = new ArrayDeque<>();

    visited.add(u);
    q.add(u);

    while (!q.isEmpty()) {
      int cur = q.poll();
      // prevent null pointer exception
      for (Edge neighbor : adj.getOrDefault(cur, new ArrayList<>())) {
        int node = neighbor.vertex;
        if (!visited.contains(node) && neighbor.weight >= t) {
          visited.add(node);
          q.add(node);
        }
      }
    }

    return visited;
  }

  public static void main(String[] args) throws Exception {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    String[] line1 = br.readLine().split(" ");
    assert (line1.length == 3);

    // reading in n, l, k
    n = Integer.parseInt(line1[0]);
    l = Integer.parseInt(line1[1]);
    k = Integer.parseInt(line1[2]);

    // Preprocess adj
    for (int i = 1; i <= n; i++) {
      adj.put(i, new ArrayList<Edge>());
    }

    // reading in edges
    for (int i = 0; i < l; i++) {
      String[] line = br.readLine().split(" ");
      addEdge(Integer.parseInt(line[0]), Integer.parseInt(line[1]), Integer.parseInt(line[2]));
    }

    // reading in and deal with the queries
    for (int i = 0; i < k; i++) {
      String[] line = br.readLine().split(" ");
      StringBuilder sb = new StringBuilder();
      for (int num : bfs(Integer.parseInt(line[0]), Integer.parseInt(line[1]))) {
        sb.append(num).append(" ");
      }
      bw.write(sb.toString().trim());
      bw.newLine();
    }

    br.close();
    bw.flush();
    bw.close();
  }
}