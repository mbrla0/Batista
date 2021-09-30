import java.util.*;

class A9E1 {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        int nodes = input.nextInt();
        int edges = input.nextInt();

        int first = 0;
        int last  = nodes - 1;

        ArrayList<HashSet<Integer>> connections = new ArrayList<>(nodes);
        for(int i = 0; i < nodes; ++i)
            connections.add(new HashSet<>(edges));
        for(int i = 0; i < edges; ++i) {
            int a = input.nextInt() - 1;
            int b = input.nextInt() - 1;

            connections.get(a).add(b);
            connections.get(b).add(a);
        }

        ArrayDeque<Stop> queue = new ArrayDeque<>(nodes * edges);
        HashSet<Integer> visited = new HashSet<>(nodes);

        queue.add(new Stop(null, first));

        Stop destination = null;
        while(!queue.isEmpty()) {
            Stop stop = queue.poll();
            if(visited.contains(stop.node))
                continue;
            visited.add(stop.node);

            if(stop.node == last) {
                /* We're done. */
                destination = stop;
                break;
            }

            for(int neighbour : connections.get(stop.node))
                queue.add(new Stop(stop, neighbour));
        }

        if(destination == null)
            System.out.println("IMPOSSIBLE");
        else {
            int hops = 0;
            while(destination != null) {
                hops += 1;
                destination = destination.previous;
            }
            System.out.println(hops);
        }
    }

    private static final class Stop {
        public final Stop previous;
        public final int node;

        private Stop(Stop previous, int node) {
            this.previous = previous;
            this.node = node;
        }
    }
}
