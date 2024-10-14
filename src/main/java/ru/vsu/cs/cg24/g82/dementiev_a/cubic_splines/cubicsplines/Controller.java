package ru.vsu.cs.cg24.g82.dementiev_a.cubic_splines.cubicsplines;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.AnchorPane;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.List;


public class Controller {
    @FXML
    AnchorPane anchorPane;
    @FXML
    private Canvas canvas;

    private Painter painter;
    private List<Point> points;
    private final int POINT_RADIUS = 3;
    private Point dragged = null;

    @FXML
    private void initialize() {
        anchorPane.widthProperty().addListener((ov, oldValue, newValue) -> {
            canvas.setWidth(newValue.doubleValue());
            repaint();
        });
        anchorPane.widthProperty().addListener((ov, oldValue, newValue) -> {
            canvas.setHeight(newValue.doubleValue());
            repaint();
        });

        painter = new Painter(canvas);
        points = new ArrayList<>();

        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            // Если нажали на существующую точку
            for (Point point : points) {
                if ((point.getX() - e.getX()) * (point.getX() - e.getX()) + (point.getY() - e.getY()) * (point.getY() - e.getY()) < POINT_RADIUS * POINT_RADIUS * 4) {
                    dragged = point;
                }
            }
            if (dragged != null) return;

            points.add(new Point((int) e.getX(), (int) e.getY()));
            repaint();
        });

        canvas.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> dragged = null);

        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, e -> {
            if (dragged == null) return;

            dragged.setX((int) e.getX());
            dragged.setY((int) e.getY());
            repaint();
        });
    }

    private void repaint() {
        painter.clear();

        int[] x = new int[points.size()];
        int[] y = new int[points.size()];
        for (int i = 0; i < points.size(); i++) {
            x[i] = points.get(i).getX();
            y[i] = points.get(i).getY();
            painter.drawPoint(x[i], y[i], POINT_RADIUS);
        }
        // Считаем параметр для каждой точки
        double[] t = Solver.getParams(x, y);

        // Сплайны отдельно для х и у
        Spline[] xSplines = Solver.getSplines(t, x);
        Spline[] ySplines = Solver.getSplines(t, y);

        for (int i = 0; i < xSplines.length; i++) {
            for (double p = t[i]; p <= t[i + 1]; p += 0.05) {
                painter.putPixel((int) xSplines[i].calculate(p), (int) ySplines[i].calculate(p));
            }
        }
    }
}