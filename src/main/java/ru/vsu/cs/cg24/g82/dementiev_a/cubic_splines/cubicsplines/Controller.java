package ru.vsu.cs.cg24.g82.dementiev_a.cubic_splines.cubicsplines;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;


public class Controller {
    @FXML
    AnchorPane anchorPane;
    @FXML
    private Canvas canvas;
    @FXML
    private Text text;
    @FXML
    private Button button;

    private Painter painter;
    private List<Point> points;
    private Point dragged = null;
    private ComputationsEstimator estimator;
    private final double[] STEPS = new double[] {0, 1, 0.5, 0.05, 0.005};
    private int stepPointer;

    @FXML
    private void initialize() {
        stepPointer = 0;
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
        estimator = new ComputationsEstimator();

        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            // Если нажали на существующую точку
            for (Point point : points) {
                if ((point.getX() - e.getX()) * (point.getX() - e.getX()) + (point.getY() - e.getY()) * (point.getY() - e.getY()) < Painter.POINT_RADIUS * Painter.POINT_RADIUS * 4) {
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

        button.setOnMouseClicked(mouseEvent -> {
            stepPointer = (stepPointer + 1) % STEPS.length;
            button.setText("Step: " + (STEPS[stepPointer] == 0 ? "flexible" : String.valueOf(STEPS[stepPointer])));
            repaint();
        });
    }

    private void repaint() {
        painter.clear();

        for (Point point : points) {
            painter.drawPoint(point.getX(), point.getY());
        }

        estimator.reset();
        CurveDrawer.drawCurve(painter, points, STEPS[stepPointer], estimator);

        text.setText("Computations: " + estimator.getComputations() + "\nTime: " + estimator.getTime() + "ms");
        Spline[] splines = Solver.getSplines(x, y);
        for (int i = 0; i < points.size() - 1; i++) {
            for (double k = x[i]; k < x[i + 1]; k += 0.05) {
                painter.putPixel((int) Math.round(k), (int) splines[i].calculate(k));
            }
        }
    }
}