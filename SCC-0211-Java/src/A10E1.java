import java.util.*;

public class A10E1 {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        ArrayList<Short> elements = new ArrayList<>();
        for(int i = 1;; ++i) {
            elements.clear();
            while(true) {
                short value = input.nextShort();
                if(value < 0)
                    break;
                elements.add(value);
            }
            if(elements.isEmpty())
                break;

            if(i > 1)
                System.out.println();
            System.out.println("Test #" + i + ":");
            System.out.println("  maximum possible interceptions: " + catches(elements));
        }
    }

    private static int catches(List<Short> missiles) {
        int[] counts = new int[missiles.size()];
        Arrays.fill(counts, 1);

        for(int i = 0; i < counts.length - 1; ++i)
            for(int j = i + 1; j < counts.length; ++j)
                if(missiles.get(i) >= missiles.get(j) && counts[i] + 1 > counts[j])
                    counts[j] = counts[i] + 1;

        int max = counts[0];
        for(int i = 0; i < counts.length; ++i)
            if(counts[i] > max)
                max = counts[i];

        return max;
    }
}
