import java.util.Scanner;

class A4E2 {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        while(input.hasNextInt()) {
            String[] dictionary = new String[input.nextInt()];
            for(int j = 0; j < dictionary.length; ++j) {
                String next = input.nextLine().trim();
                if(next.isEmpty()) {
                    --j;
                    continue;
                }

                dictionary[j] = next;
            }

            String[] rules = new String[input.nextInt()];
            for(int j = 0; j < rules.length; ++j) {
                String next = input.nextLine().trim();
                if(next.isEmpty()) {
                    --j;
                    continue;
                }

                rules[j] = next;
            }

            System.out.println("--");
            run(dictionary, rules);
        }
    }

    /** Performs a single run of the combination generation procedure. */
    static void run(String[] dictionary, String[] rules) {
        for(String rule : rules) {
            CombinationController p = new CombinationController(dictionary, rule);
            while(p.hasNext())
                System.out.println(p.next());
        }
    }

    static final class CombinationController {
        private final String[] dictionary;
        private final char[]   rule;
        private final int[]    pos;
        private boolean canHaveNext;

        CombinationController(String[] dictionary, String rule) {
            this.dictionary = dictionary;
            this.rule = rule.toCharArray();
            this.pos = new int[this.rule.length];
            this.canHaveNext = true;
        }

        /** Return the number of combinations for a given index of the rule set. */
        private int combinationsForIndex(int index) {
            if(rule[index] == '#') return dictionary.length;
            else if(rule[index] == '0') return 10;
            else throw new AssertionError("No invalid rule character must have made it to this point in execution: " + rule[index]);
        }

        /** Advance the position of the current combination by one. */
        private boolean advanceCombinationNumber() {
            int carry = 1;
            for(int i = pos.length - 1; i >= 0; --i) {
                if (carry == 0)
                    /* No need to do anything past this point. */
                    break;
                pos[i] += carry;

                carry  = pos[i] / combinationsForIndex(i);
                pos[i] = pos[i] % combinationsForIndex(i);
            }

            return carry == 0;
        }

        /** Check whether there is another combination to be made. */
        public boolean hasNext() {
            return canHaveNext;
        }

        /** Produce the next combination in the sequence. */
        public String next() {
            StringBuilder builder = new StringBuilder();
            for(int i = 0; i < rule.length; ++i) {
                switch(rule[i]) {
                case '#':
                    builder.append(dictionary[pos[i]]);
                    break;
                case '0':
                    builder.append(pos[i]);
                    break;
                default:
                    throw new AssertionError("No invalid rule character must have made it to this point in execution: " + rule[i]);
                }
            }

            this.canHaveNext = advanceCombinationNumber();
            return builder.toString();
        }
    }
}
