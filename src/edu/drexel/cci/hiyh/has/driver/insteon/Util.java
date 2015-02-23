package edu.drexel.cci.hiyh.has.driver.insteon;

import java.util.Arrays;

/**
 * Utilities. These should probably be moved up a few levels.
 */
class Util {
    public static byte[] concat(byte[] a, byte[] b) {
        byte[] rv = Arrays.copyOf(a, a.length + b.length);
        System.arraycopy(b, 0, rv, a.length, b.length);
        return rv;
    }

}
