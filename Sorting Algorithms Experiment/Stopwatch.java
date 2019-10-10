public class Stopwatch
{
    private final long start = System.currentTimeMillis();
    private final double start_nano = System.nanoTime();
    public double elapsedTime()
    {
        long now = System.currentTimeMillis();
        return (now - start) / 1000.0;
    }
    public double elapsed_Time_nano()
    {
        double now = System.nanoTime();
        return (now - start_nano)/1000;
    }
}