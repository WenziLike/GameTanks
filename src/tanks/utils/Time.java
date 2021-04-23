package tanks.utils;

public class Time {
    public static final long SECOND = 100000000L;

    public static long get() {
        return System.nanoTime();
    }
}
