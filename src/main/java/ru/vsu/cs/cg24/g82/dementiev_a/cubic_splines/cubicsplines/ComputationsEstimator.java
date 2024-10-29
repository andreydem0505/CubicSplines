package ru.vsu.cs.cg24.g82.dementiev_a.cubic_splines.cubicsplines;

public class ComputationsEstimator {
    private long computations;
    private long time;

    public ComputationsEstimator() {
        reset();
    }

    public void reset() {
        this.computations = 0;
        this.time = 0;
    }

    public void incrementComputations() {
        computations++;
    }

    public long getComputations() {
        return computations;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
