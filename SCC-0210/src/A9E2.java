import java.util.HashSet;
import java.util.Scanner;

class A9E2 {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        int people = input.nextInt();
        int groups = input.nextInt();

        /* Map out the connections. */
        boolean[][] connections = new boolean[people][people];

        int[] currentGroup = new int[people];
        int currentGroupSize = 0;
        for(int i = 0; i < groups; ++i) {
            currentGroupSize = input.nextInt();
            for(int j = 0; j < currentGroupSize; ++j)
                currentGroup[j] = input.nextInt() - 1;

            for(int j = 0; j < currentGroupSize; ++j)
                for(int k = j; k < currentGroupSize; ++k) {
                    connections[currentGroup[j]][currentGroup[k]] = true;
                    connections[currentGroup[k]][currentGroup[j]] = true;
                }
        }

        for(int i = 0; i < people; ++i)
            System.out.format("%d ", maxReachFrom(connections, i));
        System.out.println();
    }

    static int maxReachFrom(boolean[][] connections, int startingNode) {
        HashSet<Integer> visited = new HashSet<>(connections.length);
        maxReachFromImpl(connections, startingNode, visited);

        return visited.size();
    }

    static void maxReachFromImpl(
        boolean[][] connections,
        int node,
        HashSet<Integer> visited) {

        if(visited.contains(node))
            return;
        visited.add(node);

        for(int i = 0; i < connections[node].length; ++i) {
            if(!connections[node][i]) continue;
            maxReachFromImpl(connections, i, visited);
        }
    }
}
