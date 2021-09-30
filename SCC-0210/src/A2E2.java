import java.util.Arrays;
import java.util.Scanner;

class A2E2 {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        String line = input.nextLine();

        System.out.println(Cursor.removeEqualPairs(line));
    }

    static final class Cursor {
        private final char[] string;

        private int left;
        private int right;

        private Cursor(String string) {
            this.string = string.toCharArray();
            this.left = 0;
            this.right = 1;
        }

        /** Slides the deletion window one character to the right. */
        private void stepOver() {
            if(left  < string.length) ++left;
            if(right < string.length) ++right;

            while(left  < string.length && string[left]  == '-')  ++left;
            while(right < string.length && string[right] == '-') ++right;
        }

        /** Slides the deletion window one character into the deletion sequence. */
        private void stepInto() {
            while(left  > 0             && string[left]  == '-') --left;
            while(right < string.length && string[right] == '-') ++right;
        }

        /** Whether the cursor is currently at a pair of equal characters. */
        private boolean isPair() {
            return string[left] == string[right];
        }

        /** Whether the cursor is done with its job. */
        private boolean isDone() {
            return left >= string.length || right >= string.length;
        }

        /** Removes the characters in the current window. */
        private void deleteWindow() {
            string[left]  = '-';
            string[right] = '-';
        }

        /** Removes all of the equal letter pairs in a string. */
        public static String removeEqualPairs(String source) {
            Cursor cursor = new Cursor(source);
            while(!cursor.isDone()) {
                if(cursor.isPair()) {
                    cursor.deleteWindow();
                    cursor.stepInto();
                } else cursor.stepOver();
            }

            /* Take off all of the characters in the source string that were
             * previously staged for removal by the deleteWindow() function. */
            char[] removalPassBuffer = new char[cursor.string.length];
            int copied = 0;
            for(int i = 0; i < cursor.string.length; ++i) {
                if(cursor.string[i] != '-') removalPassBuffer[copied++] = cursor.string[i];
            }

            char[] contiguous = Arrays.copyOf(removalPassBuffer, copied);
            return new String(contiguous);
        }
    }
}
