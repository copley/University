import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;

import java.io.IOException;
import java.util.ArrayList;

/** Sum an array, using threads, with timing
 * Create an array and sum it using several threads and time.
 *
 * This puts the sum in a static so the threads can share it.
 * Also uses a static counter to generate thread id's - or we can use default
 * names given by getName().
 *
 * Uses this.yield() to mix it up!
 *
 * This version optionally takes number of threads and array size as
 * arguments; defaults are 4 and 100.
 */

public class ArraySum {

    static int N = 4;	           // Number of threads
    static int M = 10000000;//1000000;            // Array size
    static int rep = 70;
    static int x[] = new int[M];

    public static Object lock = new Object();
    public static int sum;

    // Sum without threads

    static long Sum0() {
        System.out.print("\nNo threads");
        StopWatch w = new StopWatch();
        w.start();

        int sum = 0;
        for (int i = 0; i < M; i++) sum = sum + x[i];

        w.stop();
        System.out.print("   Sum = " + sum);
        System.out.println("  elapsed time: " + w.getElapsedTime() + " ms");

        return w.getElapsedTime();
    }

    // Sum with threads

    static long[] SumN(int N) {

        System.out.print("\n" + N + " threads");
        StopWatch w = new StopWatch();
        w.start();

        sum = 0;
        Adder[] adder = new Adder[N];

        int chunkSize = M/N;	// Size of chunk given to each process


        StopWatch createTime = new StopWatch();
        createTime.start();
        // Give chunkSize to the first N-1 threads
        for (int i = 0; i < N-1; i++) {
            adder[i] = new Adder(x, i*chunkSize, (i+1)*chunkSize-1);
        }

        // Give the rest to the last thread
        adder[N-1] = new Adder(x, (N-1)*chunkSize, M-1);

        // Now start them

        for (int i = 0; i < N; i++) {
            adder[i].start();
        }

        createTime.stop();
        StopWatch sumTime = new StopWatch();
        sumTime.start();
        // Wait till they're all finished, then print a message
        try {
            for (int k = 0; k < N; k++) {
                adder[k].join();
            }
        }
        catch (InterruptedException e) {
            System.out.println("Ouch");
        }
        sumTime.stop();
        w.stop();
        System.out.print("   Sum = " + sum);
        System.out.println("  elapsed time: " + w.getElapsedTime() + " ms");
        return new long[] {w.getElapsedTime(), createTime.getElapsedTime(), sumTime.getElapsedTime()};
    }


    public static void main(String[] args) {
        // Check for arguments

        if ( args.length != 0 && args.length != 2 ) {
            System.out.println("Error: Must provide two arguments or none!");
            System.exit(1);
        }

        if ( args.length == 2 ) {
            N = Integer.parseInt(args[0]);
            M = Integer.parseInt(args[1]);
        }

        // Create and initialise array to 0..M-1.

        x = new int[M];
        for (int i = 0; i < M; i++) x[i] = i+1;

//        System.out.println("ArraySum with " + N + " threads, " + M + " elements");
//        System.out.println("Sum is " + (M*(M+1)/2));

        long noThreadTimeSum = 0;
        for (int i = 0; i < rep; i++) {
            noThreadTimeSum += Sum0();
        }

        long noThreadTime = noThreadTimeSum/rep;


        ArrayList<Long> avgTotalTime = new ArrayList<>();
        ArrayList<Long> avgCreateTime = new ArrayList<>();
        ArrayList<Long> avgSumTime = new ArrayList<>();
        ArrayList<Integer> numThreads = new ArrayList<>();

        for (int n = 1; n <= 50; n++) {
            long totalTime = 0;
            long createTime = 0;
            long sumTime = 0;

            for (int i = 0; i < rep; i++) {
                long[] times = SumN(n);

                totalTime += times[0];
                createTime += times[1];
                sumTime += times[2];
            }

            avgTotalTime.add(totalTime/rep);
            avgCreateTime.add(createTime/rep);
            avgSumTime.add(sumTime/rep);
            numThreads.add(n);
        }

        System.out.println("Time for no thread: " + noThreadTime);


        org.knowm.xchart.XYChart chart = new XYChartBuilder().xAxisTitle("Threads").yAxisTitle("Avg Time").width(800).height(800).build();
        XYSeries series1 = chart.addSeries("Total Time", numThreads, avgTotalTime);
        XYSeries series2 = chart.addSeries("Create Time", numThreads, avgCreateTime);
        XYSeries series3 = chart.addSeries("Sum Time", numThreads, avgSumTime);

        try {
            BitmapEncoder.saveBitmap(chart, "./threads-vs-time", BitmapEncoder.BitmapFormat.PNG);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}


class Adder extends Thread {

    private static int count = 0;

    private int sum = 0;
    long time = 0;
    int id = count;
    int lo, hi;
    int[] x;

    public Adder(int[] xx, int l, int h) {
        count++; x = xx; lo = l; hi = h;
    }

    public void run() {
        for (int i = lo; i <= hi; i++) {
            this.sum += x[i];
//            synchronized(ArraySum.lock) { ArraySum.sum = ArraySum.sum + x[i]; }
//            this.yield();

        }
        synchronized(ArraySum.lock) { ArraySum.sum = ArraySum.sum + this.sum; }

    }

}