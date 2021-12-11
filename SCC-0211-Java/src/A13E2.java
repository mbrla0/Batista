import java.util.Scanner;

public class A13E2 {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        int runs = input.nextInt();
        while(runs-- > 0) run(input);
    }

    private static void run(Scanner input) {
        int heaps = input.nextInt();
        int sum = 0;
        for(int i = 0; i < heaps; ++i)
            sum ^= input.nextInt();

        if(sum == 0)
            System.out.println("First");
        else switch(heaps % 2) {
        case 0:
            System.out.println("First");
            break;
        case 1:
            System.out.println("Second");
            break;
        }
    }
}
