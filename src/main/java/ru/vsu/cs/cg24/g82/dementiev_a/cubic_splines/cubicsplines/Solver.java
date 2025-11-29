package ru.vsu.cs.cg24.g82.dementiev_a.cubic_splines.cubicsplines;


public class Solver {
    public static Spline[] getSplines(double[] x, int[] f) {
        if (x.length < 2)
            return new Spline[0];

        double[] h = new double[x.length - 1];
        for (int i = 0; i < h.length; i++) {
            h[i] = x[i + 1] - x[i];
        }

        TridiagonalMatrix.MatrixRow[] rows = new TridiagonalMatrix.MatrixRow[h.length - 1];
        for (int i = 0; i < h.length - 1; i++) {
            rows[i] = new TridiagonalMatrix.MatrixRow(
                    2 * (h[i] + h[i + 1]),
                    h[i + 1],
                    h[i],
                    3 * ((f[i + 2] - f[i + 1]) / h[i + 1] - (f[i + 1] - f[i]) / h[i])
            );
        }
        double[] solution = TridiagonalMatrix.solve(rows);

        double[] c = new double[h.length + 1];
        c[0] = 0;
        c[h.length] = 0;
        System.arraycopy(solution, 0, c, 1, h.length - 1);
        double[] b = new double[h.length];
        double[] d = new double[h.length];
        for (int i = 0; i < h.length; i++) {
            b[i] = (f[i + 1] - f[i]) / h[i] - h[i] / 3 * (c[i + 1] + 2 * c[i]);
            d[i] = (c[i + 1] - c[i]) / (3 * h[i]);
        }

        Spline[] splines = new Spline[h.length];
        for (int i = 0; i < splines.length; i++) {
            splines[i] = new Spline(f[i], b[i], c[i], d[i], x[i]);
        }

        return splines;
    }

    public static double[] getParams(int[] x, int[] y) {
        double[] params = new double[x.length];
        if (params.length == 0) return params;

        params[0] = 0;
        for (int i = 1; i < params.length; i++) {
            params[i] = params[i - 1] + Math.sqrt(Math.pow(x[i] - x[i - 1], 2) + Math.pow(y[i] - y[i - 1], 2));
        }
        return params;
    }
}
