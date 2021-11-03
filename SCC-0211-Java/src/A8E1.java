import java.util.*;

class A8E1 {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        int network = 1;
        while(true) {
            int nodes = input.nextInt();
            if(nodes == 0) break;
            int source = input.nextInt() - 1;
            int sink = input.nextInt() - 1;
            int edges = input.nextInt();

            Network net = new Network(input, nodes, edges);
            long flow = maxFlow(net, source, sink);

            System.out.println("Network " + network);
            System.out.println("The bandwidth is " + flow + ".");
            System.out.println();

            network += 1;
        }
    }

    static long maxFlow(Network network, int source, int sink) {
        long flow = 0;
        Edge[] parents = new Edge[network.nodes];
        int newFlow;

        while((newFlow = path(network, source, sink, parents)) != 0) {
            flow += newFlow;

            Edge index = parents[sink];
            while(index != null) {
                Edge prev = parents[index.source];

                index.weight -= newFlow;
                index.getReverse().weight += newFlow;

                index = prev;
            }
        }

        return flow;
    }

    static int path(Network network, int source, int destination, Edge[] parents) {
        Arrays.fill(parents, null);

        boolean[] visited = new boolean[network.nodes];
        ArrayDeque<Long> queue = new ArrayDeque<>(network.nodes);

        queue.push(((long) Integer.MAX_VALUE << 32) | (long) source);
        while(!queue.isEmpty()) {
            long combo = queue.poll();
            int index = (int)combo;
            int flow = (int)(combo >> 32);

            if(visited[index])
                continue;
            visited[index] = true;

            for(Edge neighbor : network.neighbors(index)) {
                if(parents[neighbor.destination] == null && neighbor.destination != source && neighbor.weight != 0) {
                    parents[neighbor.destination] = neighbor;
                    int newFlow = Math.min(flow, neighbor.weight);
                    if(neighbor.destination == destination)
                        return newFlow;
                    queue.push(((long) newFlow << 32) | (long) neighbor.destination);
                }
            }
        }

        return 0;
    }

    protected static final class Network {
        public final int nodes;
        protected final List<List<Edge>> adjacency;

        public Network(int nodes) {
            this.nodes = nodes;

            adjacency = new ArrayList<>(nodes);
            for(int i = 0 ; i < nodes; ++i)
                adjacency.add(new ArrayList<>());
        }

        public Network(Scanner scanner, int nodes, int edges) {
            this(nodes);
            while(edges-- != 0) {
                int a = scanner.nextInt();
                int b = scanner.nextInt();
                int c = scanner.nextInt();

                connect(a - 1, b - 1, c);
                connect(b - 1, a - 1, c);
            }
        }

        public void connect(int a, int b, int c) {
            Edge edge = new Edge(a, b, c);
            Edge reverse = new Edge(b, a, 0);

            edge.reverse = reverse;
            reverse.reverse = edge;

            adjacency.get(a).add(edge);
            adjacency.get(b).add(reverse);
        }

        public List<Edge> neighbors(int node) {
            if(node >= nodes)
                throw new IllegalArgumentException("Out of bounds access: " + node);
            return adjacency.get(node);
        }
    }

    protected static final class Edge {
        public final int source;
        public final int destination;
        public int weight;
        protected Edge reverse;

        public Edge(int source, int destination, int weight) {
            this.source = source;
            this.destination = destination;
            this.weight = weight;
        }

        public Edge getReverse() {
            return reverse;
        }
    }
}
