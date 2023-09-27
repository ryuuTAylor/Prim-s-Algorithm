import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.BufferedWriter;
import java.util.*;

class Main {

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
    adj.putIfAbsent(u, new ArrayList<Edge>());
    adj.putIfAbsent(v, new ArrayList<Edge>());
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

    // preparing answer output
    List<Set<Integer>> ans = new ArrayList<>();

    // reading in edges
    for (int i = 0; i < l; i++) {
      String[] line = br.readLine().split(" ");
      addEdge(Integer.parseInt(line[0]), Integer.parseInt(line[1]), Integer.parseInt(line[2]));
    }

    // reading in and deal with the queries
    for (int i = 0; i < k; i++) {
      String[] line = br.readLine().split(" ");
      ans.add(bfs(Integer.parseInt(line[0]), Integer.parseInt(line[1])));
    }

    br.close();

    // output the arraylist ans
    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
    for (Set<Integer> output : ans) {
      // Convert each inner list to a string where elements are separated by spaces
      StringBuilder sb = new StringBuilder();
      for (int num : output) {
        sb.append(num).append(" ");
      }
      // Remove the trailing space and write to standard output
      bw.write(sb.toString().trim());
      bw.newLine();
    }
    bw.flush();
    bw.close();
  }
}