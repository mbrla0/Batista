import java.util.*;

class A1E1 {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        int lines = input.nextInt();
        Entry[] queue = new Entry[lines];

        for(int i = 0; i < lines; ++i) {
            String name = input.next();
            String kind = input.next();

            int priority;
                 if(kind.equals("captain")) priority = 0;
            else if(kind.equals("man"))     priority = 1;
            else if(kind.equals("woman"))   priority = 2;
            else if(kind.equals("child"))   priority = 2;
            else if(kind.equals("rat"))     priority = 3;
            else throw new RuntimeException("invalid entry");

            queue[i] = new Entry(name, priority);
        }

        /* Sort the entries stably. */
        Arrays.sort(
            queue,
            Comparator.comparingInt((Entry a) -> a.priority).reversed());

        for(Entry entry : queue)
            System.out.println(entry.name);
    }

    static final class Entry {
        public final String name;
        public final int priority;

        Entry(String name, int priority) {
            this.name = name;
            this.priority = priority;
        }
    }
}
