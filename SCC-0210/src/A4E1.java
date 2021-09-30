import java.util.*;

class A4E1 {
    static final int MAX_SOURCES = 12;

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        while(input.hasNextLine()) {
            String line = input.nextLine();
            Scanner lineInput = new Scanner(line);

            /* Load the target integer for the sum. */
            if(!lineInput.hasNextInt()) continue;
            int target = lineInput.nextInt();

            /* Load the source integers for the sum. */
            int sourceCount = lineInput.nextInt();
            int[] sources = new int[sourceCount];

            if(sourceCount > MAX_SOURCES)
                throw new RuntimeException("Invalid input line: Maximum number of integer sources has been exceeded");
            if(sourceCount == 0)
                break;

            for(int i = 0; i < sourceCount; ++i)
                sources[i] = lineInput.nextInt();

            run(target, Arrays.copyOf(sources, sourceCount));
        }
    }

    /** Performs a single run of the algorithm with the given target and sources. */
    static void run(int target, int[] unrefinedSources) {
        /* Refine the source array down.
         *
         * We know that since this is a sum of positive integer values, we can
         * immediately rule out all numbers greater than the current target, as
         * well as any repeats of numbers equal in value to it. */
        Arrays.parallelSort(unrefinedSources);

        int cutoffLength = Arrays.binarySearch(unrefinedSources, target);
        if(cutoffLength < 0) {
            cutoffLength = -(cutoffLength + 1);
        } else {
            cutoffLength++;
            while(cutoffLength > 1 && unrefinedSources[cutoffLength - 2] == target)
                cutoffLength--;
        }

        int[] sources = Arrays.copyOf(unrefinedSources, cutoffLength);
        TreeSet<Combination> combinations = new TreeSet<>();
        ArrayList<String> results = new ArrayList<>(1000);

        findCombinations(target, sources, 0, sources.length, 0, combinations);

        /* Print the combinations. */
        for(Combination combination : combinations) {
            StringBuilder string = new StringBuilder();
            boolean first = true;
            for(int i = MAX_SOURCES - 1; i >= 0; --i) {
                if(((combination.value >> i) & 1) == 1) {
                    if(!first) {
                        string.append('+');
                        string.append(sources[i]);
                    } else {
                        string.append(sources[i]);
                        first = false;
                    }
                }
            }
            results.add(string.toString());
        }
        if(combinations.size() == 0)
            results.add("NONE");

        System.out.format("Sums of %d:\n", target);
        for(String result : results)
            System.out.println(result);
    }

    static void findCombinations(
        int target,
        int[] sources,
        int partial,
        int len,
        int numbers,
        TreeSet<Combination> combinations) {

        if(len == 0)
            /* Dead branch. */
            return;

        int greatest = sources[len - 1];
        for(int i = len - 1; i > 0; --i)
            findCombinations(target, sources, partial, i, numbers, combinations);

        if(partial + greatest > target) {
            /* Dead branch. */
            return;
        }

        int next = numbers | (1 << (len - 1));
        if(partial + greatest == target) {
            combinations.add(new Combination(next, sources));
            return;
        }

        for(int i = len - 1; i > 0; --i)
            findCombinations(target, sources, partial + greatest, i, next, combinations);
    }

    static final class Combination implements Comparable<Combination> {
        public final int value;
        private final int[] sources;

        private final int[] slots;
        private final int filledSlots;

        Combination(int value, int[] sources) {
            this.value = value;
            this.sources = sources;

            this.slots = new int[sources.length];
            int filledSlots = 0;
            for(int source : sources) {
                if(slots[filledSlots] != source)
                    slots[filledSlots++] = source;
            }
            this.filledSlots = filledSlots;
        }

        int[] histogram() {
            int[] histogram = new int[this.filledSlots];
            for(int i = MAX_SOURCES - 1; i >= 0; --i) {
                if(((value >> i) & 1) == 1) {
                    histogram[Arrays.binarySearch(slots, sources[i])]++;
                }
            }

            return histogram;
        }

        @Override
        public boolean equals(Object o) {
            if(this == o) return true;
            if(o == null || getClass() != o.getClass()) return false;
            Combination that = (Combination) o;

            int[] thisHistogram = this.histogram();
            int[] thatHistogram = that.histogram();

            return Arrays.equals(thatHistogram, thisHistogram);
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(this.histogram());
        }

        @Override
        public int compareTo(Combination that) {
            int[] a = this.slots;
            int[] b = that.slots;

            int i;
            for(i = Math.min(a.length, b.length) - 1; i >= 0; --i)
                if(a[i] != b[i]) break;
            if (i >= 0 && i < Math.min(a.length, b.length))
                return -Integer.compare(a[i], b[i]);

            a = this.histogram();
            b = that.histogram();

            for(i = Math.min(a.length, b.length) - 1; i >= 0; --i)
                if(a[i] != b[i]) break;
            if (i >= 0 && i < Math.min(a.length, b.length))
                return -Integer.compare(a[i], b[i]);
            return b.length - a.length;
        }
    }
}
