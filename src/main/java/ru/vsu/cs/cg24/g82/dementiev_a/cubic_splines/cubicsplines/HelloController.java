package ru.vsu.cs.cg24.g82.dementiev_a.cubic_splines.cubicsplines;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.AnchorPane;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.List;


public class HelloController {
    @FXML
    AnchorPane anchorPane;
    @FXML
    private Canvas canvas;

    Painter painter;
    List<Point> points;

    @FXML
    private void initialize() {
        anchorPane.widthProperty().addListener((ov, oldValue, newValue) -> canvas.setWidth(newValue.doubleValue()));
        anchorPane.widthProperty().addListener((ov, oldValue, newValue) -> canvas.setHeight(newValue.doubleValue()));

        painter = new Painter(canvas.getGraphicsContext2D());
        points = new ArrayList<>();

        canvas.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            points.add(new Point((int) e.getX(), (int) e.getY()));
            canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

            Spline[] splines = Solver.getSplines(points);
            for (int i = 0; i < points.size() - 1; i++) {
                int minX = Math.min(points.get(i).getX(), points.get(i + 1).getX());
                int maxX = Math.max(points.get(i).getX(), points.get(i + 1).getX());
                for (double x = minX; x < maxX; x += 0.05) {
                    painter.putPixel((int) Math.round(x), (int) splines[i].calculate(x));
                }
            }
        });
    }
}