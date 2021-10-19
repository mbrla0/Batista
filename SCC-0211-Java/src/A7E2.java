import java.util.ArrayDeque;
import java.util.Scanner;

class A7E2 {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        int nodes;
        int edges;

        for(int i = 0;; ++i) {
            nodes = input.nextInt();
            edges = input.nextInt();

            if(nodes == 0 && edges == 0) break;

            run(input, nodes, edges);
        }
    }

    private static void run(Scanner input, int node_count, int edge_count) {
        boolean[][] edge_connections = new boolean[node_count][node_count];
        DirectedEdge[] edges = new DirectedEdge[edge_count];
        for(int i = 0; i < edge_count; ++i) {
            int a = input.nextInt() - 1;
            int b = input.nextInt() - 1;
            int c = -input.nextInt();

            edge_connections[a][b] = true;
            edges[i] = new DirectedEdge(a, b, c);
        }
        int beg = 0;

        long[] tentative = new long[node_count];
        for(int i = 0; i < node_count; ++i)
            tentative[i] = Long.MAX_VALUE - 1000;
        tentative[beg] = 0;

        /* Isolate the main island starting from the first edge. */
        boolean[] reachable = new boolean[node_count];

        ArrayDeque<Integer> reachability = new ArrayDeque<>();
        reachability.push(beg);
        while(!reachability.isEmpty()) {
            int index = reachability.poll();

            if(reachable[index]) continue;
            reachable[index] = true;

            for(int i = 0; i < node_count; ++i)
                if(edge_connections[index][i])
                    reachability.push(i);
        }

        /* Find all minimum distances, only really caring for reachable nodes. */
        for(int r = 0; r < node_count - 1; ++r) {
            boolean changed = false;
            for(DirectedEdge edge : edges) {
                if(!reachable[edge.source] || !reachable[edge.destination])
                    continue;

                if(tentative[edge.source] + edge.weight < tentative[edge.destination]) {
                    changed = true;
                    tentative[edge.destination] = tentative[edge.source] + edge.weight;
                }
            }
            if(!changed) break;
        }

        boolean negative_cycle = false;
        for(DirectedEdge edge : edges) {
            if(!reachable[edge.source] || !reachable[edge.destination])
                continue;

            if(tentative[edge.source] + edge.weight < tentative[edge.destination]) {
                negative_cycle = true;
                break;
            }
        }

        if(negative_cycle)
            System.out.println("Unlimited!");
        else {
            long min = 0;
            for(int i = 0; i < node_count; ++i)
                if(reachable[i] && tentative[i] < min)
                    min = tentative[i];
            System.out.println(-min);
        }
    }

    private static final class DirectedEdge {
        public final int source;
        public final int destination;
        public final int weight;

        private DirectedEdge(int source, int destination, int weight) {
            this.source = source;
            this.destination = destination;
            this.weight = weight;
        }
    }
}
