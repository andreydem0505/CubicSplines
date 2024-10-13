package ru.vsu.cs.cg24.g82.dementiev_a.cubic_splines.cubicsplines;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

public class Painter {
    private final Canvas canvas;
    private final GraphicsContext gc;
    private final PixelWriter pw;

    public Painter(final Canvas canvas) {
        this.canvas = canvas;
        gc = canvas.getGraphicsContext2D();
        pw = gc.getPixelWriter();
    }

    public void putPixel(int x, int y) {
        pw.setColor(x, y, Color.BLACK);
    }

    public void clear() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    public void drawPoint(int x, int y, int radius) {
        gc.fillOval(x - radius, y - radius, radius * 2, radius * 2);
    }
}
