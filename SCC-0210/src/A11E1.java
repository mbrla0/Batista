import java.util.*;

class A11E1 {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        int cityCount = input.nextInt();
        int routeCount = input.nextInt();

        ArrayList<HashMap<Integer, Integer>> distances = new ArrayList<>(cityCount);
        for(int i = 0; i < cityCount; ++i)
            distances.add(new HashMap<>());
        for(int i = 0; i < routeCount; ++i) {
            int origin = input.nextInt() - 1;
            int destination = input.nextInt() - 1;
            int length = input.nextInt();

            if(distances.get(origin).containsKey(destination)) {
                /* Only keep the shortest route between two cities. */
                int currentLength = distances.get(origin).get(destination);
                if(length < currentLength)
                    distances.get(origin).put(destination, length);
            } else distances.get(origin).put(destination, length);
        }

        int visitedCount = 0;
        boolean[] visited = new boolean[cityCount];
        long[] minimumDistances = new long[cityCount];

        PriorityQueue<Route> queue = new PriorityQueue<>(
            routeCount,
            Comparator.comparingLong(route -> route.accumulatedDistance));
        queue.add(new Route(0, 0));

        while(!queue.isEmpty() && visitedCount < cityCount) {
            Route route = queue.poll();

            if(visited[route.currentNode])
                continue;
            visitedCount++;
            visited[route.currentNode] = true;

            /* Due to the nature of the algorithm, we always first reach a node
             * through the shortest path from the starting node. */
            minimumDistances[route.currentNode] = route.accumulatedDistance;

            /* Queue up all of the nodes that can be reached from this one. */
            for(Map.Entry<Integer, Integer> neighbour : distances.get(route.currentNode).entrySet()) {
                int node = neighbour.getKey();
                long distance = route.accumulatedDistance + neighbour.getValue();

                queue.add(new Route(node, distance));
            }
        }

        for(long distance : minimumDistances)
            System.out.print(distance + " ");
        System.out.println();
    }

    private static final class Route {
        public final int  currentNode;
        public final long accumulatedDistance;

        public Route(int currentNode, long accumulatedDistance) {
            this.currentNode = currentNode;
            this.accumulatedDistance = accumulatedDistance;
        }
    }
}
