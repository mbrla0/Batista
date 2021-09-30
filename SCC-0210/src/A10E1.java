import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

class A10E1 {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        int townCount = input.nextInt();
        int roadCount = input.nextInt();

        HashMap<Integer, HashMap<Integer, Integer>> roads = new HashMap<>(townCount);
        for(int i = 0; i < townCount; ++i) roads.put(i, new HashMap<>(roadCount));
        for(int j = 0; j < roadCount; ++j) {
            int a = input.nextInt() - 1;
            int b = input.nextInt() - 1;
            int c = input.nextInt();

            roads.get(a).put(b, c);
            roads.get(b).put(a, c);
        }

        /* Find the minimum spanning tree from the first vertex. */
        PriorityQueue<WeighedEdge> candidates = new PriorityQueue<>(
            roadCount,
            Comparator.comparingInt(a -> a.weight));

        HashSet<Integer> inTree = new HashSet<>();
        ArrayList<WeighedEdge> edges = new ArrayList<>();

        Consumer<Integer> addVertexAndCandidates = a -> {
            List<WeighedEdge> l = roads.get(a)
                .entrySet()
                .stream()
                .filter(b -> !inTree.contains(b.getKey()))
                .map(b -> new WeighedEdge(b.getKey(), b.getValue()))
                .collect(Collectors.toList());

            inTree.add(a);
            candidates.addAll(l);
        };

        addVertexAndCandidates.accept(0);
        while(!candidates.isEmpty()) {
            WeighedEdge candidate = candidates.poll();
            if(inTree.contains(candidate.targetNode))
                continue;

            edges.add(candidate);
            addVertexAndCandidates.accept(candidate.targetNode);
        }

        if(edges.size() == townCount - 1) {
            long lengthOfRoad = 0;
            for(WeighedEdge edge : edges)
                lengthOfRoad += edge.weight;

            System.out.println(lengthOfRoad);
        } else {
            System.out.println("IMPOSSIBLE");
        }
    }


    static final class WeighedEdge {
        public final int targetNode;
        public final int weight;

        WeighedEdge(int targetNode, int weight) {
            this.targetNode = targetNode;
            this.weight = weight;
        }
    }
}
