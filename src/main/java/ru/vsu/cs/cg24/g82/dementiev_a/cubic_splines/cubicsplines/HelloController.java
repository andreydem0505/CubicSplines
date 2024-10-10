package ru.vsu.cs.cg24.g82.dementiev_a.cubic_splines.cubicsplines;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.AnchorPane;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class HelloController {
    @FXML
    AnchorPane anchorPane;
    @FXML
    private Canvas canvas;

    private Painter painter;
    private GraphicsContext gc;
    private List<Point> points;
    private final int POINT_DIAMETER = 6;
    private Point dragged = null;

    @FXML
    private void initialize() {
        anchorPane.widthProperty().addListener((ov, oldValue, newValue) -> canvas.setWidth(newValue.doubleValue()));
        anchorPane.widthProperty().addListener((ov, oldValue, newValue) -> canvas.setHeight(newValue.doubleValue()));

        gc = canvas.getGraphicsContext2D();
        painter = new Painter(gc);
        points = new ArrayList<>();

        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            for (Point point : points) {
                if ((point.getX() - e.getX()) * (point.getX() - e.getX()) + (point.getY() - e.getY()) * (point.getY() - e.getY()) < POINT_DIAMETER * POINT_DIAMETER) {
                    dragged = point;
                }
            }
            if (dragged != null) return;

            for (Point point : points) {
                if (point.getX() == e.getX()) return;
            }

            points.add(new Point((int) e.getX(), (int) e.getY()));
            points.sort(Comparator.comparing(Point::getX));
            repaint();
        });

        canvas.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            dragged = null;
        });

        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, e -> {
            if (dragged == null) return;
            for (Point point : points) {
                if (point.getX() + 3 >= e.getX() && point.getX() - 3 <= e.getX()) {
                    return;
                }
            }
            dragged.setX((int) e.getX());
            dragged.setY((int) e.getY());
            points.sort(Comparator.comparing(Point::getX));
            repaint();
        });
    }

    private void repaint() {
        canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        int[] x = new int[points.size()];
        int[] y = new int[points.size()];
        for (int i = 0; i < points.size(); i++) {
            x[i] = points.get(i).getX();
            y[i] = points.get(i).getY();
            gc.fillOval(x[i] - POINT_DIAMETER / 2.0, y[i] - POINT_DIAMETER / 2.0, POINT_DIAMETER, POINT_DIAMETER);
        }

        Spline[] splines = Solver.getSplines(x, y);
        for (int i = 0; i < points.size() - 1; i++) {
            int minX = Math.min(x[i], x[i + 1]);
            int maxX = Math.max(x[i], x[i + 1]);
            for (double k = minX; k < maxX; k += 0.05) {
                painter.putPixel((int) Math.round(k), (int) splines[i].calculate(k));
            }
        }
    }
}