import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
import java.util.Scanner;

class A7E2 {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        int runs = input.nextInt();
        while(runs-- > 0) run(input);
    }

    public static void run(Scanner input) {
        int itemCount = input.nextInt();
        Product[] items = new Product[itemCount];
        for(int i = 0; i < itemCount; ++i) {
            int value = input.nextInt();
            int weight = input.nextInt();

            items[i] = new Product(value, weight);
        }

        int individuals = input.nextInt();
        int[] carryingCapacities = new int[individuals];
        for(int i = 0; i < individuals; ++i) {
            carryingCapacities[i] = input.nextInt();
        }

        HashMap<Invocation, Integer> cache = new HashMap<>();

        int totalMax = 0;
        for(int capacity : carryingCapacities) {
            Invocation start = new Invocation(items, capacity);
            totalMax += maximumValue(start, cache);
        }

        System.out.println(totalMax);
    }

    public static int maximumValue(Invocation invocation, HashMap<Invocation, Integer> cache) {
        if(cache.containsKey(invocation))
            return cache.get(invocation);
        if(!invocation.hasAnyForRoom()) {
            cache.put(invocation, 0);
            return 0;
        }

        int max = 0;
        for(int i = 0; i < invocation.size(); ++i){
            if(invocation.canTake(i)) {
                int vp = invocation.getProduct(i).value;
                int val = maximumValue(invocation.take(i), cache) + vp;
                if(val > max)
                    max = val;
            }
        }

        cache.put(invocation, max);
        return max;
    }

    static final class Invocation {
        final Product[] products;
        final boolean[] taken;
        final int room;

        public Invocation(Product[] products, int room) {
            this.products = products;
            this.taken = new boolean[products.length];
            this.room = room;
        }

        private Invocation(Product[] products, boolean[] taken, int room) {
            this.products = products;
            this.taken = taken;
            this.room = room;
        }

        public int size() {
            return products.length;
        }

        public Invocation take(int index) {
            boolean[] taken = Arrays.copyOf(this.taken, this.taken.length);
            taken[index] = true;

            return new Invocation(products, taken, room - products[index].weight);
        }

        public Product getProduct(int index) {
            return products[index];
        }

        public boolean getTaken(int index) {
            return taken[index];
        }

        public boolean hasAnyForRoom() {
            if(room <= 0) return false;
            for(int i = 0; i < size(); ++i)
                if(canTake(i)) return true;
            return false;
        }

        public boolean canTake(int index) {
            if(room <= 0) return false;

            int     weight = getProduct(index).weight;
            boolean taken  = getTaken(index);

            return !taken && weight <= room;
        }

        @Override
        public boolean equals(Object o) {
            if(this == o) return true;
            if(o == null || getClass() != o.getClass()) return false;
            Invocation that = (Invocation) o;
            return room == that.room && Arrays.equals(products, that.products) && Arrays.equals(taken, that.taken);
        }

        @Override
        public int hashCode() {
            int result = Objects.hash(room);
            result = 31 * result + Arrays.hashCode(products);
            result = 31 * result + Arrays.hashCode(taken);
            return result;
        }
    }

    static final class Product {
        public final int value;
        public final int weight;

        Product(int value, int weight) {
            this.value = value;
            this.weight = weight;
        }

        @Override
        public boolean equals(Object o) {
            if(this == o) return true;
            if(o == null || getClass() != o.getClass()) return false;
            Product product = (Product) o;
            return value == product.value && weight == product.weight;
        }

        @Override
        public int hashCode() {
            return Objects.hash(value, weight);
        }
    }
}
