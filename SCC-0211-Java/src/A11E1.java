import java.util.Scanner;

public class A11E1 {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        while(true) {
            if(!input.hasNextLine()) break;
            String first  = input.nextLine().trim();
            if(!input.hasNextLine()) break;
            String second = input.nextLine().trim();

            if(first.isEmpty() || second.isEmpty())
                break;

            System.out.println(longestCommonSubsequence(first, second));
        }
    }

    public static int longestCommonSubsequence(String sa, String sb) {
        char[] a = sa.toCharArray();
        char[] b = sb.toCharArray();
        int[][] c = new int[a.length + 1][b.length + 1];

        for(int i = 0; i < a.length + 1; ++i)
            for(int j = 0; j < b.length + 1; ++j)
            {
                if(i == 0 || j == 0)
                    c[i][j] = 0;
                else if(a[i - 1] == b[j - 1])
                    c[i][j] = c[i - 1][j - 1] + 1;
                else
                    c[i][j] = Math.max(c[i - 1][j], c[i][j - 1]);
            }
        return c[a.length][b.length];
    }
}
