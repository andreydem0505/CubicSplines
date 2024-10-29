package ru.vsu.cs.cg24.g82.dementiev_a.cubic_splines.cubicsplines;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

public class Painter implements PixelDrawer {
    private final Canvas canvas;
    private final GraphicsContext gc;
    private final PixelWriter pw;
    public static final int POINT_RADIUS = 3;

    public Painter(final Canvas canvas) {
        this.canvas = canvas;
        gc = canvas.getGraphicsContext2D();
        pw = gc.getPixelWriter();
    }

    @Override
    public void putPixel(int x, int y) {
        pw.setColor(x, y, Color.BLACK);
    }

    public void clear() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    public void drawPoint(int x, int y) {
        gc.fillOval(x - POINT_RADIUS, y - POINT_RADIUS, POINT_RADIUS * 2, POINT_RADIUS * 2);
    }
}
