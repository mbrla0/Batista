import java.util.*;

class A11E2 {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        int cityCount = input.nextInt();
        int routeCount = input.nextInt();
        int finalDestination = cityCount - 1;

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

        PriorityQueue<Route> queue = new PriorityQueue<>(
            routeCount,
            Comparator.comparingLong(route -> route.accumulatedDistance));
        queue.add(new Route(0, 0, 0, null));

        boolean[] visited = new boolean[cityCount];

        Route destinationRoute = null;
        while(!queue.isEmpty()) {
            Route route = queue.poll();

            if(route.currentNode == finalDestination) {
                destinationRoute = route;
                break;
            }
            if(visited[route.currentNode])
                continue;
            visited[route.currentNode] = true;

            /* Queue up all of the nodes that can be reached from this one. */
            for(Map.Entry<Integer, Integer> neighbour : distances.get(route.currentNode).entrySet()) {
                int node = neighbour.getKey();
                long distance = route.accumulatedDistance + neighbour.getValue();

                queue.add(new Route(node, neighbour.getValue(), distance, route));
            }
        }

        if(destinationRoute == null)
            throw new AssertionError("There must have been a route between the two cities.");


        long accumulatedDistance = destinationRoute.accumulatedDistance;
        int maximumLeg = 0;

        Route cursor = destinationRoute;
        do {
            if(cursor.traveledDistanceFromPreviousNode > maximumLeg)
                maximumLeg = cursor.traveledDistanceFromPreviousNode;
        } while((cursor = cursor.previousLeg) != null);


        accumulatedDistance -= (long) Math.ceil(maximumLeg / 2.0);
        System.out.println(accumulatedDistance);
    }

    private static final class Route {
        public final int  currentNode;
        public final int  traveledDistanceFromPreviousNode;
        public final long accumulatedDistance;
        public final Route previousLeg;

        public Route(int currentNode, int traveledDistanceFromPreviousNode, long accumulatedDistance, Route previousLeg) {
            this.currentNode = currentNode;
            this.traveledDistanceFromPreviousNode = traveledDistanceFromPreviousNode;
            this.accumulatedDistance = accumulatedDistance;
            this.previousLeg = previousLeg;
        }
    }
}
