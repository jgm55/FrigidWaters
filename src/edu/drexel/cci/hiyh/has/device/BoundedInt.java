package edu.drexel.cci.hiyh.has.device;

public class BoundedInt implements ParamType<Integer> {
    public final int lower, upper;
    public BoundedInt(int lower, int upper) {
        this.lower = lower;
        this.upper = upper;
    }
}
