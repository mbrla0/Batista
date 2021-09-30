import java.util.*;

class A1E2 {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        Registration[] registrations = new Registration[3000];
        Bucket[] buckets = new Bucket[3000];

        int elements = 0;

        String token;
        while(!(token = input.next()).equals("#")) {
            if(!token.equals("Register"))
                throw new RuntimeException("invalid input");

            int ticket = input.nextInt();
            int period = input.nextInt();

            registrations[elements++] = new Registration(ticket, period);
        }

        /* Slice and sort the array. Then sort it into buckets by their period. */
        registrations = Arrays.copyOf(registrations, elements);
        Arrays.sort(
            registrations,
            Comparator.comparingInt(a -> a.period));

        int backIndex = 0;
        elements = 0;

        while(backIndex < registrations.length) {
            for (int i = backIndex; i <= registrations.length; ++i) {
                if (i < registrations.length && registrations[i].period == registrations[backIndex].period)
                    /* Continue scanning. */
                    continue;

                Registration[] local = Arrays.copyOfRange(registrations, backIndex, i);
                buckets[elements++] = new Bucket(local, local[0].period);

                backIndex = i;
                break;
            }
        }

        buckets = Arrays.copyOf(buckets, elements);
        Arrays.sort(
            buckets,
            Comparator.comparingInt(a -> a.period));

        /* Find the maximum amount of time that can be spent waiting for the given number of messages. */
        int messageCount = input.nextInt();
        ArrayList<Message> messages = new ArrayList<>(10000);

        if(messageCount <= 0)
            throw new RuntimeException("invalid number of messages");

        int maxTime = buckets[0].period * messageCount;

        /* Find the buckets that can be triggered in that amount of time or less. */
        int lastBucket = Arrays.binarySearch(
            buckets,
            new Bucket(null, maxTime),
            Comparator.comparingInt(a -> a.period));

        if(lastBucket < 0) {
            lastBucket = -lastBucket - 1;
            if(lastBucket <= 0)
                throw new RuntimeException("what");
            lastBucket -= 1;
        }

        for(int i = 0; i <= lastBucket; ++i) {
            Bucket local = buckets[i];
            for (int j = 1; j <= maxTime / local.period; ++j) {
                for (Registration reg : local.registrations)
                    messages.add(new Message(reg.ticket, j * local.period));
            }
        }
        messages.sort(new Comparator<Message>() {
            @Override
            public int compare(Message a, Message b) {
                int time = Integer.compare(a.time, b.time);
                if(time != 0) return time;

                return Integer.compare(a.ticket, b.ticket);
            }
        });

        for(int i = 0; i < messageCount; ++i) {
            System.out.println(messages.get(i).ticket);
            /* System.out.println("- " + messages.get(i).time); */
        }
    }

    static final class Message {
        public final int ticket;
        public final int time;

        Message(int ticket, int time) {
            this.ticket = ticket;
            this.time = time;
        }
    }

    static final class Bucket {
        public final Registration[] registrations;
        public final int period;

        Bucket(Registration[] registrations, int period) {
            this.registrations = registrations;
            this.period = period;
        }
    }

    static final class Registration {
        public final int ticket;
        public final int period;

        Registration(int ticket, int period) {
            this.ticket = ticket;
            this.period = period;
        }
    }
}
