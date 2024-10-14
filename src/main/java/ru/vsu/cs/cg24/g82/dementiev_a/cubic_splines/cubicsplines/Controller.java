package ru.vsu.cs.cg24.g82.dementiev_a.cubic_splines.cubicsplines;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.AnchorPane;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class Controller {
    @FXML
    AnchorPane anchorPane;
    @FXML
    private Canvas canvas;

    private Painter painter;
    private List<Point> points;
    private final int POINT_RADIUS = 3;
    private final int X_SPAN = 3;
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

            // Не можем ставить точку слишком близко по координате Х
            for (Point point : points) {
                if (point.getX() + X_SPAN >= e.getX() && point.getX() - X_SPAN <= e.getX())
                    return;
            }

            points.add(new Point((int) e.getX(), (int) e.getY()));
            repaint();
        });

        canvas.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> dragged = null);

        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, e -> {
            if (dragged == null) return;
            // Не можем перемещать точку слишком близко по координате Х к другой точке
            for (Point point : points) {
                if (point != dragged && point.getX() + X_SPAN >= e.getX() && point.getX() - X_SPAN <= e.getX())
                    return;
            }

            dragged.setX((int) e.getX());
            dragged.setY((int) e.getY());
            repaint();
        });
    }

    private void repaint() {
        points.sort(Comparator.comparing(Point::getX));
        painter.clear();

        int[] x = new int[points.size()];
        int[] y = new int[points.size()];
        for (int i = 0; i < points.size(); i++) {
            x[i] = points.get(i).getX();
            y[i] = points.get(i).getY();
            painter.drawPoint(x[i], y[i], POINT_RADIUS);
        }

        Spline[] splines = Solver.getSplines(x, y);
        for (int i = 0; i < points.size() - 1; i++) {
            for (double k = x[i]; k < x[i + 1]; k += 0.05) {
                painter.putPixel((int) Math.round(k), (int) splines[i].calculate(k));
            }
        }
    }
}