import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

class A8E2 {
    public static void main(String[] args) {
        ArrayList<Elephant> elephants = new ArrayList<>(1024);

        Scanner input = new Scanner(System.in);
        while(input.hasNextInt())
            elephants.add(new Elephant(input.nextInt(), input.nextInt()));
        Collections.sort(elephants, Comparator.comparingInt(a -> a.size));

        System.out.println(longestLength(elephants));
    }

    public static int longestLength(ArrayList<Elephant> list) {
        int len = 0;
        int[] tmp = new int[list.size()];

        for(int i = 0; i < list.size(); ++i) {
            tmp[i] = 1;

            for(int j = 0; j < i; ++j) {
                Elephant a = list.get(i);
                Elephant b = list.get(j);

                boolean increasingSize = a.size > b.size;
                boolean decreasingIq = a.iq < b.iq;
                boolean greaterThanCurrent = tmp[i] < tmp[j] + 1;

                if(increasingSize && decreasingIq && greaterThanCurrent)
                    tmp[i] = tmp[j] + 1;

                len = Math.max(len, tmp[i]);
            }
        }

        return len;
    }

    static final class Elephant implements Comparable<Elephant> {
        public final int size;
        public final int iq;

        Elephant(int size, int iq) {
            this.size = size;
            this.iq = iq;
        }

        @Override
        public int compareTo(Elephant o) {
            int order = Integer.compare(size, o.size);
            if(order == 0) order = Integer.compare(iq, o.iq);

            return order;
        }
    }
}
