import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

class A7E1 {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        int coinCount = input.nextInt();
        int target = input.nextInt();

        int[] coins = new int[coinCount];
        for(int i = 0; i < coinCount; ++i)
            coins[i] = input.nextInt();
        Arrays.sort(coins);

        HashMap<Integer, Integer> cache = new HashMap<>();
        int combinations = traverse(coins, 0, target, cache);

        System.out.println(combinations);
    }

    public static int traverse(int[] coins, int currentSum, int target, HashMap<Integer, Integer> cache) {
        if(currentSum > target)
            return 0;
        if(cache.containsKey(currentSum))
            return cache.get(currentSum);

        int total = 0;
        for(int coin : coins) {
            int sum = currentSum + coin;

            if(sum == target)
                total++;
            else
                total += traverse(coins, sum, target, cache);
        }

        cache.put(currentSum, total);
        return total;
    }
}
