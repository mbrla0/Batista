import java.util.Scanner;

class A3E2 {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        int digitCount = input.nextInt();
        byte[] digits = new byte[digitCount];

        /* Read in the data for the number. */
        String stringDigits;
        do { stringDigits = input.nextLine(); } while (stringDigits.isEmpty());

        int readDigits = 0;

        for(char c : stringDigits.toCharArray()) {
            switch(c) {
                case '1': digits[readDigits++] = 1; break;
                case '2': digits[readDigits++] = 2; break;
                case '3': digits[readDigits++] = 3; break;
                case '4': digits[readDigits++] = 4; break;
                case '5': digits[readDigits++] = 5; break;
                case '6': digits[readDigits++] = 6; break;
                case '7': digits[readDigits++] = 7; break;
                case '8': digits[readDigits++] = 8; break;
                case '9': digits[readDigits++] = 9; break;
                default:
                    throw new RuntimeException("Encountered an invalid character while parsing a decimal number");
            }
        }

        if(readDigits != digits.length) {
            throw new RuntimeException(
                    "Expected a number with "
                            + digits.length
                            + " digits. Instead, got a number with " + readDigits + " digits");
        }

        /* Read in the data for the mapper function as an array of differences
         * from the input numbers to the numbers each gets mapped to. */
        int[] deltas = new int[9];
        for(int i = 0; i < deltas.length; i++) {
            int target = input.nextInt();
            deltas[i] = target - i - 1;
        }

        /* Traverse the number trying to find the its longest contiguous section
         * in which all deltas are positive. */
        int lo = -1;
        int hi = -1;
        for(int i = 0; i < digits.length; ++i) {
            int digit = digits[i];
            if(deltas[digit - 1] > 0) {
                if(lo < 0) {
                    /* Set the lower bound to include the current digit. */
                    lo = i;
                }

                /* Set the upper bound to include the current digit. */
                hi = i;
            } else if(deltas[digit - 1] < 0 && lo >= 0) {
                /* We've already found the longest range we're ever going to
                 * find. Since all of the numbers before this one are bigger
                 * than both itself and all of the numbers that come after it,
                 * no contiguous range after the current one will have us map to
                 * a number larger than applying the mapping to the current
                 * range.
                 *
                 * We can bail early. */
                break;
            }
        }

        /* Apply the transformation to the currently held range while
         * printing the digits in the array. */
        for(int i = 0; i < digits.length; ++i) {
            if(i >= lo && i <= hi) {
                /* We're inside of the mapping range, apply the mapping to the
                 * digit in the current iteration before we print it. */
                int target = digits[i] + deltas[digits[i] - 1];
                System.out.print(target);
            } else {
                /* We're outside of the mapping range, just print whatever digit
                 * the number has at this position, unchanged. */
                System.out.print(digits[i]);
            }
        }
    }
}
