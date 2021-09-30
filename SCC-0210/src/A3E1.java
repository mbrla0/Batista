import java.util.Scanner;

class A3E1 {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        int badgeCount = input.nextInt();
        if(badgeCount <= 0) throw new RuntimeException("At least one badge is required.");

        int[] badges = new int[2 * badgeCount - 1];
        for(int i = 0; i < badgeCount; ++i) {
            int value = input.nextInt();
            if(value <= 0)
                throw new RuntimeException("Badges must have a greater than or equal to one");
            if(value > badges.length)
                throw new RuntimeException("Badges must have a value smaller than double the badge count");

            badges[value - 1]++;
        }

        int moves = 0;
        for(int i = 0; i < badges.length; ++i) {
            if(badges[i] > 1) {
                moves         += badges[i] - 1;
                badges[i + 1] += badges[i] - 1;
            }
        }

        System.out.println(moves);
    }
}
