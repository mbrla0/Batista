import java.math.BigInteger;
import java.util.Scanner;

class A12E1 {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        long value = input.nextLong();
        double max = Math.sqrt(value);

        int count = 0;
        BigInteger sum = BigInteger.valueOf(0);
        BigInteger product = BigInteger.valueOf(1);

        for(long i = 1; i <= max; ++i) {
            if(value % i == 0) {
                long a = value / i;
                long b = value / a;

                count += 2;
                sum = sum.add(BigInteger.valueOf(a));
                sum = sum.add(BigInteger.valueOf(b));

                product = product.multiply(BigInteger.valueOf(a));
                product = product.multiply(BigInteger.valueOf(b));
            }
        }

        sum = sum.mod(BigInteger.valueOf(1000000007L));
        product = product.mod(BigInteger.valueOf(1000000007L));
        System.out.printf("%d %d %d", count, sum, product);
    }
}
