import java.util.*;

class A3E1 {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int runs = input.nextInt();

        for(int i = 0; i < runs; ++i) {
            if(i > 0) System.out.println();
            run(input);
        }
    }

    static void run(Scanner in) {
        Table table = Table.fromScanner(in);

        HashSet<String> words = new HashSet<>();
        for(int i = 0; i < table.edge; ++i)
            for(int j = 0; j < table.edge; ++j) {
                probeFrom(table, j, i, words);
            }

        String[] dict = words.toArray(new String[0]);
        Arrays.sort(dict, Comparator.comparingInt(String::length).thenComparing(a -> a));

        for(String word : dict)
            System.out.println(word);
    }

    static void probeFrom(
        Table table,
        int x,
        int y,
        HashSet<String> target) {

        ArrayDeque<Visit> queue = new ArrayDeque<>();

        char c = table.get(x, y);
        queue.add(new Visit(x, y, new String(new char[] { c }), c));

        while(!queue.isEmpty()) {
            Visit current = queue.poll();

            if(current.accumulated.length() >= 3)
                target.add(current.accumulated);


            Next next = (int a, int b) -> {
                if(a < 0 || a >= table.edge) return;
                if(b < 0 || b >= table.edge) return;

                char cc = table.get(a, b);
                if(cc <= current.lastChar) {
                    return;
                }

                String acc = current.accumulated + cc;

                queue.push(new Visit(a, b, acc, cc));
            };

            next.run(current.x + 0, current.y + 1);
            next.run(current.x + 0, current.y - 1);
            next.run(current.x + 1, current.y + 0);
            next.run(current.x - 1, current.y + 0);

            next.run(current.x + 1, current.y + 1);
            next.run(current.x - 1, current.y + 1);
            next.run(current.x + 1, current.y - 1);
            next.run(current.x - 1, current.y - 1);
        }
    }

    static interface Next {
        void run(int a, int b);
    }

    static final class Table {
        public final int edge;
        final char[] data;

        private Table(int edge) {
            this.edge = edge;
            this.data = new char[edge * edge];
        }

        public static Table fromScanner(Scanner in) {
            int edge = in.nextInt();
            Table table = new Table(edge);

            for(int i = 0; i < edge; ++i) {
                String line = in.nextLine().trim();
                if(line.isEmpty()) { --i; continue; }

                char[] chars = line.toCharArray();
                System.arraycopy(chars, 0, table.data, i * edge, edge);
            }

            return table;
        }

        char get(int x, int y) {
            if(x < 0 || x >= edge)
                throw new IllegalArgumentException(String.format(
                    "Given value for x is out of range: %d < 0 || %d >= %d",
                    x, x, edge));
            if(y < 0 || y >= edge)
                throw new IllegalArgumentException(String.format(
                    "Given value for y is out of range: %d < 0 || %d >= %d",
                    y, y, edge));
            return data[y * edge + x];
        }
    }

    static final class Visit {
        public final int    x;
        public final int    y;
        public final String accumulated;
        public final char   lastChar;

        public Visit(int x, int y, String accumulated, char lastChar) {
            this.x = x;
            this.y = y;
            this.accumulated = accumulated;
            this.lastChar = lastChar;
        }

        @Override
        public boolean equals(Object o) {
            if(this == o) return true;
            if(o == null || getClass() != o.getClass()) return false;
            Visit visit = (Visit) o;
            return x == visit.x && y == visit.y && lastChar == visit.lastChar && Objects.equals(accumulated, visit.accumulated);
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y, accumulated, lastChar);
        }
    }
}
