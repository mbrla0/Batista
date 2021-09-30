import java.util.HashSet;
import java.util.Objects;
import java.util.Scanner;

class A3E2 {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int runs = input.nextInt();
        for(int i = 0; i < runs; ++i) run(input);
    }

    static void run(Scanner in) {
        Board board = Board.fromScanner(in);
        System.out.println(drill(board, board.startingX, board.startingY, new HashSet<>()));
    }

    static int drill(
        Board board,
        int x,
        int y,
        HashSet<Point> taken) {

        Jumper jumper = (a, b) -> {
            int targetX = x + 2 * a;
            int targetY = y + 2 * b;
            int checkX = x + a;
            int checkY = y + b;

            if(targetX < 0 || targetX >= board.edge)
                /* Would leave the board. */
                return 0;
            if(targetY < 0 || targetY >= board.edge)
                /* Would leave the board. */
                return 0;
            if(checkX < 0 || checkX >= board.edge)
                /* Would leave the board. */
                return 0;
            if(checkY < 0 || checkY >= board.edge)
                /* Would leave the board. */
                return 0;
            if(!board.get(checkX, checkY))
                /* No piece to capture. */
                return 0;
            if(board.get(targetX, targetY))
                /* Target position is occupied. */
                return 0;

            Point take = new Point(checkX, checkY);
            if(taken.contains(take)) return 0;

            HashSet<Point> newTaken = new HashSet<>(taken);
            newTaken.add(take);

            return drill(board, targetX, targetY, newTaken) + 1;
        };

        return Integer.max(
            Integer.max(
                jumper.jump(1, 1),
                jumper.jump(-1, -1)),
            Integer.max(
                jumper.jump(-1, 1),
                jumper.jump(1, -1)));
    }

    interface Jumper {
        int jump(int x, int y);
    }

    static final class Point {
        public final int x;
        public final int y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if(this == o) return true;
            if(o == null || getClass() != o.getClass()) return false;
            Point point = (Point) o;
            return x == point.x && y == point.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }
    static final class Board {
        public final int edge;
        public final int startingX;
        public final int startingY;
        final boolean[] data;

        private Board(int edge, int startingX, int startingY, boolean[] data) {
            this.edge = edge;
            this.startingX = startingX;
            this.startingY = startingY;
            this.data = data;
        }

        public static Board fromScanner(Scanner in) {
            int edge = 10;
            boolean[] data = new boolean[edge * edge];

            int black_x = -1;
            int black_y = -1;

            for(int i = 0; i < edge; ++i) {
                String line = in.nextLine().trim();
                if(line.isEmpty()) { --i; continue; }

                char[] chars = line.toCharArray();
                if(chars.length != edge)
                    throw new RuntimeException("Invalid length for line" + i + ": " + chars.length);

                for(int j = 0; j < edge; ++j) {
                    char c = chars[j];
                    if(c == 'B') data[i * edge + j] = true;
                    else if(c == 'W') {
                        black_x = j;
                        black_y = i;
                    } else if(c != '.' && c != '#')
                        throw new RuntimeException("Invalid input character at (" + j + ", " + i + "): " + c);
                }
            }
            if(black_x < 0 || black_y < 0)
                throw new RuntimeException("No starting position");

            return new Board(edge, black_x, black_y, data);
        }

        public boolean get(int x, int y) {
            if(x < 0 || x >= this.edge)
                throw new IllegalArgumentException();
            if(y < 0 || y >= this.edge)
                throw new IllegalArgumentException();

            return data[y * edge + x];
        }
    }
}
