package ru.vsu.cs.cg24.g82.dementiev_a.cubic_splines.cubicsplines;

import java.util.List;

public class Solver {
    public static Spline[] getSplines(List<Point> points) {
        if (points.size() < 2)
            return new Spline[0];

        double[] h = new double[points.size() - 1];
        for (int i = 0; i < h.length; i++) {
            h[i] = points.get(i + 1).getX() - points.get(i).getX();
        }

        TridiagonalMatrix.MatrixRow[] rows = new TridiagonalMatrix.MatrixRow[h.length - 1];
        for (int i = 0; i < h.length - 1; i++) {
            rows[i] = new TridiagonalMatrix.MatrixRow(
                    2 * (h[i] + h[i + 1]),
                    h[i + 1],
                    h[i],
                    3 * ((points.get(i + 2).getY() - points.get(i + 1).getY()) / h[i + 1] - (points.get(i + 1).getY() - points.get(i).getY()) / h[i])
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
            b[i] = (points.get(i + 1).getY() - points.get(i).getY()) / h[i] - h[i] / 3 * (c[i + 1] + 2 * c[i]);
            d[i] = (c[i + 1] - c[i]) / (3 * h[i]);
        }

        Spline[] splines = new Spline[h.length];
        for (int i = 0; i < splines.length; i++) {
            splines[i] = new Spline(points.get(i).getY(), b[i], c[i], d[i], points.get(i).getX());
        }

        return splines;
    }
}
