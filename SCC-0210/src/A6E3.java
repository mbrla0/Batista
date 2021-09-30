import java.util.*;

class A6E3 {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        int runs = input.nextInt();
        for(int i = 0; i < runs; ++i) run(input);
    }

    public static void run(Scanner input) {
        /* Initialize the maze. */
        int width = input.nextInt();
        Point startPoint = new Point(input.nextInt(), input.nextInt()).transpose();
        Point endPoint   = new Point(input.nextInt(), input.nextInt()).transpose();

        int[][] maze = new int[width][width];
        for(int i = 0; i < width; ++i)
            for(int j = 0; j < width; ++j)
                maze[i][j] = input.nextInt();
        Path path = Path.maze(maze, startPoint, endPoint);

        /* Run A-Star to find the shortest path from the start to the end point. */
        PriorityQueue<Record> openNodes = new PriorityQueue<>(width * width);
        HashMap<Node, Record> backlog = new HashMap<>(width * width);
        HashSet<Node> visited = new HashSet<>(width * width);

        Record seed = new Record(path.start, 0, startPoint.cabDistance(endPoint), null);
        backlog.put(path.start, seed);
        openNodes.add(seed);
        visited.add(path.start);

        while(!openNodes.isEmpty()) {
            Record current = openNodes.poll();

            if(current.node.equals(path.end)) {
                /* We're done. */
                int length = 1;

                Record cursor = current;
                while(cursor.last != null) {
                    length++;
                    cursor = cursor.last;
                }

                System.out.format("Nro de posicoes: %d\n", length);
                return;
            }

            for(Node neighbourNode : current.node.neighbours) {
                if(visited.contains(neighbourNode))
                    /* No need to revisit nodes. */
                    continue;
                visited.add(neighbourNode);

                Record neighbour = backlog.getOrDefault(
                        neighbourNode,
                        Record.uninitialized(neighbourNode));

                int tScore = current.weight + current.node
                        .position
                        .cabDistance(neighbourNode.position);

                if(tScore < neighbour.weight) {
                    neighbour.last = current;
                    neighbour.weight = tScore;
                    neighbour.distance = neighbour
                            .node
                            .position
                            .cabDistance(path.end.position);
                    openNodes.add(neighbour);
                }
                backlog.put(neighbourNode, neighbour);
            }
        }

        /* The search failed. */
        System.out.println("Caminho Inexistente.");
    }

    /** Record used as an intermediary helper during A-Star. */
    static final class Record implements Comparable<Record> {
        /** Current open node at the end of its path chain. */
        public final Node node;
        /** Last record connected to this one in the path. */
        public Record last;
        /** The weight designated to this path. */
        public int weight;
        /** The distance from this node to the end point. */
        public int distance;

        Record(Node node, int weight, int distance, Record last) {
            this.node = node;
            this.weight = weight;
            this.distance = distance;
            this.last = last;
        }

        /** Create a new uninitialized record for the given node. */
        static Record uninitialized(Node node) {
            return new Record(node, Integer.MAX_VALUE, Integer.MAX_VALUE, null);
        }

        @Override
        public boolean equals(Object o) {
            if(this == o) return true;
            if(o == null || getClass() != o.getClass()) return false;
            Record record = (Record) o;
            return weight == record.weight && Objects.equals(node, record.node);
        }

        @Override
        public int hashCode() {
            return Objects.hash(node, weight);
        }

        /** Get the score of this record. */
        public int score() {
            return this.weight + this.distance;
        }

        @Override
        public int compareTo(Record that) {
            int thisScore = this.score();
            int thatScore = that.score();

            return Integer.compare(thisScore, thatScore);
        }
    }

    /** A path is a linked graph with a start and and end node. */
    static final class Path {
        /** The node to be considered the starting point. */
        public final Node start;
        /** The node to be considered the ending point. */
        public final Node end;

        private Path(Node start, Node end) {
            this.start = start;
            this.end = end;
        }

        /** Generate a maze from the given maze data and the given end points. */
        public static Path maze(int[][] maze, Point start, Point end) {
            if(maze.length == 0 || maze[0].length == 0)
                throw new IllegalArgumentException("Maze has collapsed dimensions.");

            int height = maze.length;
            int width  = maze[0].length;

            if(start.x >= width || start.y >= height || start.x < 0 || start.y < 0)
                throw new IllegalArgumentException("Invalid starting point.");
            if(end.x >= width || end.y >= height || end.x < 0 || end.y < 0)
                throw new IllegalArgumentException("Invalid end point.");

            Node[][] mazeNodes = new Node[height][width];

            for(int i = 0; i < height; ++i) {
                if(maze[i].length != width)
                    throw new IllegalArgumentException("Maze has non-uniform width");
                for(int j = 0; j < width; ++j)
                    if(maze[i][j] != 0)
                        mazeNodes[i][j] = new Node(new Point(j, i));
                    else
                        mazeNodes[i][j] = null;
            }

            for(int i = 0; i < height; ++i)
                for(int j = 0; j < width; ++j) {
                    if(mazeNodes[i][j] == null)
                        continue;

                    int    neighbourCount = 0;
                    Node[] neighbours = new Node[4];

                    if(i > 0 && mazeNodes[i - 1][j] != null) neighbours[neighbourCount++] = mazeNodes[i - 1][j];
                    if(j > 0 && mazeNodes[i][j - 1] != null) neighbours[neighbourCount++] = mazeNodes[i][j - 1];
                    if(i + 1 < height && mazeNodes[i + 1][j] != null) neighbours[neighbourCount++] = mazeNodes[i + 1][j];
                    if(j + 1 < width  && mazeNodes[i][j + 1] != null) neighbours[neighbourCount++] = mazeNodes[i][j + 1];

                    mazeNodes[i][j].neighbours = Arrays.copyOf(neighbours, neighbourCount);
                }

            Node startNode = mazeNodes[start.y][start.x];
            Node endNode   = mazeNodes[end.y][end.x];

            if(startNode == null) {
                throw new IllegalArgumentException(String.format(
                        "There is no start node at [%d][%d]", start.y, start.x));
            }
            if(endNode == null) {
                throw new IllegalArgumentException(String.format(
                        "There is no end node at [%d][%d]", end.y, end.x));
            }

            return new Path(mazeNodes[start.y][start.x], mazeNodes[end.y][end.x]);
        }
    }

    /** Node of a linked graph. */
    static final class Node {
        /** The coordinate in space associated with this node. */
        public final Point position;
        /** All the neighbours of this node. */
        public Node[] neighbours;

        protected Node(Point position) {
            this.position = position;
        }

        @Override
        public boolean equals(Object o) {
            if(this == o) return true;
            if(o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return Objects.equals(position, node.position) && Arrays.equals(neighbours, node.neighbours);
        }

        @Override
        public int hashCode() {
            int result = Objects.hash(position);
            for (Node element : neighbours)
                /* Only bother with the position of the neighbours, so we don't
                 * end up recursively calling this method over and over. */
                result = 31 * result + (element == null ? 0 : element.position.hashCode());

            return result;
        }
    }

    /** Coordinate values of a single point in space. */
    static final class Point {
        /** Horizontal offset from the starting position. */
        public final int x;
        /** Vertical offset from the starting position. */
        public final int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        /** The cab distance between this point and the given point. */
        public int cabDistance(Point that) {
            int x = Math.abs(this.x - that.x);
            int y = Math.abs(this.y - that.y);

            return x + y;
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

        public Point transpose() {
            return new Point(y, x);
        }
    }
}
