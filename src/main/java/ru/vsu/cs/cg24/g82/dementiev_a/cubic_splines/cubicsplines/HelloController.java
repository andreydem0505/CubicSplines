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
            painter.putPixel((int) e.getX(), (int) e.getY());
        });

        points.add(new Point(20, 30));
        points.add(new Point(40, 66));
        points.add(new Point(100, 14));
        points.add(new Point(200, 200));
        Spline[] splines = Solver.getSplines(points);
        for (int i = 0; i < points.size() - 1; i++) {
            for (int x = points.get(i).getX(); x < points.get(i + 1).getX(); x++) {
                painter.putPixel(x, (int) splines[i].calculate(x));
            }
        }
    }
}