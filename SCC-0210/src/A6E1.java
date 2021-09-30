import java.util.Scanner;
import java.util.stream.IntStream;

class A6E1 {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        while(input.hasNextInt()) {
            int n = input.nextInt() + 1;
            int k = input.nextInt() + 1;

            int[] v = new int[n];
            for (int i = 0; i < n; ++i) v[i] = input.nextInt();

            Hike hike = new Hike(v, k);
            System.out.println(hike.minMaxWalk());
        }
    }

    static class Hike {
        final int[] stations;
        final int   days;

        public Hike(int[] stations, int days) {
            this.stations = stations;
            this.days = days;
        }

        boolean check(int middle) {
            int current = 0;
            int days = this.days;

            for(int station : stations) {
                current += station;
                if(current > middle) {
                    --days;
                    if(days <= 0 || station > middle)
                        /* Fuse. */
                        return false;
                    current = station;
                }
            }
            return true;
        }

        /** Find the minimum value for the maximum amount of walking to be done in a single day. */
        public int minMaxWalk() {
            int l = 0;
            int h = IntStream.of(stations).sum();

            while(l <= h) {
                int middle = (l + h) / 2;
                if(check(middle)) h = middle - 1;
                else l = middle + 1;
            }

            return check(l) ? l : h;
        }
    }
}
