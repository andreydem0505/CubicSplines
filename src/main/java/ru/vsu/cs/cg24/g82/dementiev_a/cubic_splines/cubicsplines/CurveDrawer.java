package ru.vsu.cs.cg24.g82.dementiev_a.cubic_splines.cubicsplines;


import java.util.ArrayList;
import java.util.List;

public class CurveDrawer {
    public static void drawCurve(PixelDrawer pixelDrawer, List<Point> points) {
        drawCurve(pixelDrawer, points, 0, new ComputationsEstimator());
    }

    public static void drawCurve(PixelDrawer pixelDrawer, List<Point> points, double step, ComputationsEstimator estimator) {
        long start = System.currentTimeMillis();

        // Не рисуем точку, которая на тех же координатах, что и прошлая
        List<Point> pointsToDraw = new ArrayList<>();
        if (!points.isEmpty())
            pointsToDraw.add(points.get(0));
        for (int i = 1; i < points.size(); i++) {
            if (points.get(i).getX() != points.get(i - 1).getX() || points.get(i).getY() != points.get(i - 1).getY()) {
                pointsToDraw.add(points.get(i));
            }
        }

        int[] x = new int[pointsToDraw.size()];
        int[] y = new int[pointsToDraw.size()];
        for (int i = 0; i < pointsToDraw.size(); i++) {
            x[i] = pointsToDraw.get(i).getX();
            y[i] = pointsToDraw.get(i).getY();
        }

        // Считаем параметр для каждой точки
        double[] t = Solver.getParams(x, y);

        // Сплайны отдельно для х и у
        Spline[] xSplines = Solver.getSplines(t, x);
        Spline[] ySplines = Solver.getSplines(t, y);

        if (step == 0 && xSplines.length > 0) {
            // Параметр перебирается динамически
            double lastP = 0;
            int lastX = (int) Math.round(xSplines[0].calculate(0));
            int lastY = (int) Math.round(ySplines[0].calculate(0));
            int curX, curY, counter = 0;
            for (int i = 0; i < xSplines.length; i++) {
                for (double p = t[i]; p <= t[i + 1]; p += 1) {
                    curX = (int) Math.round(xSplines[i].calculate(p));
                    curY = (int) Math.round(ySplines[i].calculate(p));
                    pixelDrawer.putPixel(curX, curY);
                    if (Math.abs(curX - lastX) + Math.abs(curY - lastY) >= 2 && counter <= 5) {
                        p = (lastP + p) / 2 - 1;
                        counter++;
                    } else {
                        lastX = curX;
                        lastY = curY;
                        lastP = p;
                        counter = 0;
                    }
                    estimator.incrementComputations();
                }
            }
        } else {
            // Параметр перебирается с определенным шагом
            for (int i = 0; i < xSplines.length; i++) {
                for (double p = t[i]; p <= t[i + 1]; p += step) {
                    pixelDrawer.putPixel((int) Math.round(xSplines[i].calculate(p)), (int) Math.round(ySplines[i].calculate(p)));
                    estimator.incrementComputations();
                }
            }
        }

        long end = System.currentTimeMillis();
        estimator.setTime(end - start);
    }
}
