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
    private final int POINT_RADIUS = 3;
    private Point dragged = null;
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

        button.setOnMouseClicked(mouseEvent -> {
            stepPointer = (stepPointer + 1) % STEPS.length;
            button.setText("Step: " + (STEPS[stepPointer] == 0 ? "flexible" : String.valueOf(STEPS[stepPointer])));
            repaint();
        });
    }

    private void repaint() {
        painter.clear();

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
            painter.drawPoint(x[i], y[i], POINT_RADIUS);
        }
        // Считаем параметр для каждой точки
        double[] t = Solver.getParams(x, y);

        // Сплайны отдельно для х и у
        Spline[] xSplines = Solver.getSplines(t, x);
        Spline[] ySplines = Solver.getSplines(t, y);

        long computations = 0;
        long start = System.currentTimeMillis();

        if (STEPS[stepPointer] == 0 && xSplines.length > 0) {
            // Параметр перебирается динамически
            double lastP = 0;
            int lastX = (int) Math.round(xSplines[0].calculate(0));
            int lastY = (int) Math.round(ySplines[0].calculate(0));
            int curX, curY, counter = 0;
            for (int i = 0; i < xSplines.length; i++) {
                for (double p = t[i]; p <= t[i + 1]; p += 1) {
                    curX = (int) Math.round(xSplines[i].calculate(p));
                    curY = (int) Math.round(ySplines[i].calculate(p));
                    painter.putPixel(curX, curY);
                    if (Math.abs(curX - lastX) + Math.abs(curY - lastY) >= 2 && counter <= 5) {
                        p = (lastP + p) / 2 - 1;
                        counter++;
                    } else {
                        lastX = curX;
                        lastY = curY;
                        lastP = p;
                        counter = 0;
                    }
                    computations++;
                }
            }
        } else {
            // Параметр перебирается с определенным шагом
            for (int i = 0; i < xSplines.length; i++) {
                for (double p = t[i]; p <= t[i + 1]; p += STEPS[stepPointer]) {
                    painter.putPixel((int) Math.round(xSplines[i].calculate(p)), (int) Math.round(ySplines[i].calculate(p)));
                    computations++;
                }
            }
        }

        long end = System.currentTimeMillis();
        text.setText("Computations: " + computations + "\nTime: " + (end - start) + "ms");
    }
}