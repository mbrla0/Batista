import java.util.HashMap;
import java.util.Scanner;

class A2E1 {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        int hosts = input.nextInt();
        int commands = input.nextInt();

        /* Prepare the hosts lookup table. */
        HashMap<String, String> lookup = new HashMap<>(hosts);
        for(int i = 0; i < hosts; ++i) {
            String name = input.next();
            String address = input.next();

            lookup.put(address, name);
        }

        /* Process the commands. */
        for(int i = 0; i < commands; ++i) {
            String command = input.next();
            String address = input.next().split(";")[0];

            String label = lookup.get(address);
            if(label == null)
                throw new RuntimeException(String.format("Unknown address %s", address));

            System.out.format("%s %s; #%s\n", command, address, label);
        }
    }
}
