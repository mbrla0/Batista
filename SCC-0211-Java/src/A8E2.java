import java.util.*;

class A8E2 {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int runs = input.nextInt();
        while(runs-- != 0) {
           Network network = new Network(input);
           List<Integer> solutions = network.march();
           if(solutions.isEmpty())
               System.out.print("-1");
           else for(int i = 0; i < solutions.size(); ++i)
               System.out.print((i > 0 ? " " : "") + solutions.get(i));
           System.out.println();
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
        public final  int              nodes;
        public        int              penguins;
        private final List<Connection> connections;
        private final List<List<Edge>> adjacency;
        private boolean pushed = false;

        public Network(int nodes) {
            this.nodes = nodes;
            this.penguins = 0;

            connections = new ArrayList<>();
            adjacency = new ArrayList<>(nodes);
            for(int i = 0 ; i < nodes; ++i)
                adjacency.add(new ArrayList<>());
        }

        static final int SOURCE = 0;
        static final int SINK = 1;
        static final int BASE = 2;

        public Network(Scanner scanner) {
            this(scanner.nextInt() * 2 + 2);
            double maxDist = scanner.nextDouble();

            int nodes = (this.nodes - 2) / 2;
            int[] x = new int[nodes];
            int[] y = new int[nodes];

            for(int i = 0; i < nodes; ++i) {
                x[i] = scanner.nextInt();
                y[i] = scanner.nextInt();
                int penguins = scanner.nextInt();
                int maxJumps = scanner.nextInt();

                int index0 = BASE + i;
                int index1 = BASE + nodes + i;
                if(penguins > 0)
                    connect(SOURCE, index0, penguins);
                connect(index0, index1, maxJumps);

                for(int j = 0; j < i; ++j) {
                    int jIndex0 = BASE + j;
                    int jIndex1 = BASE + nodes + j;

                    int dx = x[i] - x[j];
                    int dy = y[i] - y[j];
                    if(Math.hypot(dx, dy) <= maxDist) {
                        connect(index1, jIndex0, Integer.MAX_VALUE);
                        connect(jIndex1, index0, Integer.MAX_VALUE);
                    }
                }

                this.penguins += penguins;
            }
        }

        public void connect(int a, int b, int c) {
            if(!pushed)
                this.connections.add(new Connection(a, b, c));

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

        private void push() {
            if(pushed)
               throw new AssertionError();
            pushed = true;
        }

        private void pop() {
            if(!pushed)
                throw new AssertionError();

            for(List<Edge> adj : adjacency)
                adj.clear();
            for(Connection connection : connections)
                connect(connection.a, connection.b, connection.c);

            pushed = false;
        }

        public List<Integer> march() {
            List<Integer> solutions = new ArrayList<>();

            int nodes = (this.nodes - 2) / 2;
            for(int i = 0; i < nodes; ++i) {
                push();

                int index = BASE + i;
                connect(index, SINK, Integer.MAX_VALUE);

                if(maxFlow(this, SOURCE, SINK) == penguins)
                    solutions.add(i);

                this.adjacency.get(index).remove(this.adjacency.get(index).size() - 1);
                this.adjacency.get(SINK).remove(this.adjacency.get(SINK).size() - 1);

                pop();
            }

            return solutions;
        }
    }

    protected static final class Edge {
        public  final int source;
        public  final int destination;
        public  int  weight;
        private Edge reverse;

        public Edge(int source, int destination, int weight) {
            this.source = source;
            this.destination = destination;
            this.weight = weight;
        }

        public Edge getReverse() {
            return reverse;
        }
    }

    protected static final class Connection {
        public final int a;
        public final int b;
        public final int c;

        public Connection(int a, int b, int c) {
            this.a = a;
            this.b = b;
            this.c = c;
        }
    }
}
