package ru.vsu.cs.cg24.g82.dementiev_a.cubic_splines.cubicsplines;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

public class Painter {
    PixelWriter pw;

    public Painter(final GraphicsContext graphicsContext) {
        pw = graphicsContext.getPixelWriter();
    }

    public void putPixel(int x, int y) {
        pw.setColor(x, y, Color.BLACK);
    }
}
