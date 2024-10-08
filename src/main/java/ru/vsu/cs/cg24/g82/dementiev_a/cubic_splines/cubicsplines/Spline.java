package ru.vsu.cs.cg24.g82.dementiev_a.cubic_splines.cubicsplines;

public class Spline {
    private double a;
    private double b;
    private double c;
    private double d;
    private double x0;

    public Spline(double a, double b, double c, double d, double x0) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.x0 = x0;
    }

    public double calculate(double x) {
        return a + b * (x - x0) + c * (x - x0) * (x - x0) + d * (x - x0) * (x - x0) * (x - x0);
    }

    @Override
    public String toString() {
        return "Spline{" +
                "a=" + a +
                ", b=" + b +
                ", c=" + c +
                ", d=" + d +
                '}';
    }
}
