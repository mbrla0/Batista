import java.util.*;

class A8E1 {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        String a = input.next();
        String b = input.next();

        System.out.println(levenshtein(a, b));
    }

    public static int levenshtein(String a, String b) {
        int[][] distance = new int[2][a.length() + 1];

        for(int i = 0; i < distance[0].length; ++i)
            distance[0][i] = i;

        for(int i = 1; i < b.length() + 1; ++i) {
            distance[i % 2][0] = i;
            for(int j = 1; j < a.length() + 1; ++j) {
                int c;
                if(a.charAt(j - 1) == b.charAt(i - 1))
                    c = 0;
                else
                    c = 1;

                distance[i % 2][j] = Math.min(
                    distance[(i - 1) % 2][j] + 1,
                    Math.min(
                        distance[i % 2][j - 1] + 1,
                        distance[(i - 1) % 2][j - 1] + c));
            }
        }

        return distance[b.length() % 2][a.length()];
    }
}
