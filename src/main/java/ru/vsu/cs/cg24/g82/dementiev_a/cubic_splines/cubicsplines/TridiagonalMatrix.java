package ru.vsu.cs.cg24.g82.dementiev_a.cubic_splines.cubicsplines;


public class TridiagonalMatrix {
    public static class MatrixRow {
        private double a;
        private double b;
        private double c;
        private double d;

        public MatrixRow(double a, double b, double c, double d) {
            this.a = a;
            this.b = b;
            this.c = c;
            this.d = d;
        }

        public double getA() {
            return a;
        }

        public double getB() {
            return b;
        }

        public double getC() {
            return c;
        }

        public double getD() {
            return d;
        }
    }

    public static double[] solve(MatrixRow[] rows) {
        if (rows.length == 0)
            return new double[0];

        double[] y = new double[rows.length];
        double[] u = new double[rows.length];
        double[] v = new double[rows.length];
        v[0] = - rows[0].getB() / rows[0].getA();
        u[0] = rows[0].getD() / rows[0].getA();
        for (int i = 1; i < rows.length; i++) {
            v[i] = - rows[i].getB() / (rows[i].getC() * v[i - 1] + rows[i].getA());
            u[i] = (rows[i].getD() - rows[i].getC() * u[i - 1]) / (rows[i].getC() * v[i - 1] + rows[i].getA());
        }
        y[rows.length - 1] = u[rows.length - 1];
        for (int i = rows.length - 2; i >= 0; i--) {
            y[i] = v[i] * y[i + 1] + u[i];
        }
        return y;
    }
}
