import java.util.Scanner;

class A6E2 {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        while(input.hasNextFloat()) {
            Equation equation = new Equation(
                input.nextFloat(),
                input.nextFloat(),
                input.nextFloat(),
                input.nextFloat(),
                input.nextFloat(),
                input.nextFloat());

            Cursor cursor;
            try {
                cursor = new Cursor(equation, 0, 1);
            } catch (NoRootsException e) {
                System.out.println("No solution");
                continue;
            }

            double delta;
            do {
                delta = cursor.iterate();
            } while(Math.abs(delta) > 0.0000000001);

            System.out.format("%.04f\n", cursor.getCurrentRoot());
        }
    }

    /** Cursor for easing in on a solution for a equation from two end points. */
    static final class Cursor {
        /** Target equation. */
        final Equation equation;
        /** Lower bound of the range. */
        double lower;
        /** Upper bound of the range. */
        double upper;

        public Cursor(Equation equation, double a, double b)
            throws NoRootsException {

            this.equation = equation;
            this.lower = Math.min(a, b);
            this.upper = Math.max(a, b);

            if(Math.signum(equation.at(lower)) == Math.signum(equation.at(upper)))
                throw new NoRootsException();
        }

        /** Currently guessed value for the root value. */
        public double getCurrentRoot() {
            return (lower + upper) / 2;
        }

        /** Iterate on the guess once and refine it.
         *
         * @return The distance between the old and the new guess.
         */
        public double iterate() {
            double root   = getCurrentRoot();
            double left   = equation.at(lower);
            double right  = equation.at(upper);
            double middle = equation.at(root);

            if(Math.signum(left) == Math.signum(right)) {
                throw new AssertionError(
                    "The signs of the left and right sides must've been different by this point");
            } else if(Math.signum(left) == Math.signum(middle)) {
                double delta = root - lower;
                lower = root;

                return delta;
            } else if(Math.signum(right) == Math.signum(middle)) {
                double delta = root - upper;
                upper = root;

                return delta;
            } else
                throw new AssertionError(
                    "The sign of the middle value must've been equal to either the sign of the left value " +
                        "or that of the right value");
        }
    }

    /** Tool for evaluating the equation proposed by the problem.
     *
     * The main attribute of this equation is that, in the 0 to 1 range and
     * within its parameter constraints, it only has one root. So we can find
     * it by closing in from either side. */
    static final class Equation {
        final double p;
        final double q;
        final double r;
        final double s;
        final double t;
        final double u;

        public Equation(double p, double q, double r, double s, double t, double u) {
            if(p < 0   || p > 20) throw new IllegalArgumentException("Expected  0 <= p <= 20, got p = " + p);
            if(r < 0   || r > 20) throw new IllegalArgumentException("Expected  0 <= r <= 20, got p = " + r);
            if(q < -20 || q > 0)  throw new IllegalArgumentException("Expected -20 <= q <= 0, got p = " + q);
            if(s < -20 || s > 0)  throw new IllegalArgumentException("Expected -20 <= s <= 0, got p = " + s);
            if(t < -20 || t > 0)  throw new IllegalArgumentException("Expected -20 <= t <= 0, got p = " + t);

            this.p = p;
            this.q = q;
            this.r = r;
            this.s = s;
            this.t = t;
            this.u = u;
        }

        public double at(double x) {
            if(x < 0 || x > 1) throw new IllegalArgumentException("Evaluations must be in the [0, 1] range.");

            double a = p * Math.exp(-x);
            double b = q * Math.sin(x);
            double c = r * Math.cos(x);
            double d = s * Math.tan(x);
            double e = t * Math.pow(x, 2);

            return a + b + c + d + e + u;
        }
    }

    /** Exception used to signal that a given configuration of the proposed equation has no roots in our target range. */
    private static class NoRootsException extends Exception { }
}
