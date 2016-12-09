import org.knowm.xchart.*;
import org.knowm.xchart.XYChartBuilder;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by danielbraithwt on 7/21/16.
 */
public class RecursiveArraySum extends Thread {
    public static final int SIZE = 10000000;
    public static final int STEP = 100000;
    public static final int REPEAT = 70;

    public static int numThreads = 0;

    private static long sum(int[] a) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        int s = 0;
        for (int i = 0; i < a.length; i++) {
            s += a[i];
        }
        stopWatch.stop();
        return stopWatch.getElapsedTime();
    }

    public static void main(String[] args) {
        ArrayList<Long> avgTimes = new ArrayList<>();
        ArrayList<Integer> thresholds = new ArrayList<>();
        int[] numbers = new int[SIZE];
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = i+1;
        }

        long noThreadSumTime = 0;
        for (int i = 0; i < REPEAT; i++) {
            noThreadSumTime += sum(numbers);
        }

        for (int i = STEP; i < ((SIZE/2) + STEP); i+=STEP) {
            THRESHOLD = i;
            System.out.println(THRESHOLD);
//            thresholds.add((double) THRESHOLD);
            thresholds.add(i+1);

            long sumTime = 0;
            for (int j = 0; j < REPEAT; j++) {
                sumTime += runTest(numbers);
                System.out.println(numThreads);
                numThreads = 0;
            }

//            avgTimes.add((double) (sumTime/REPEAT));
//            System.out.println(sumTime);
            avgTimes.add(sumTime/REPEAT);
        }



//        org.knowm.xchart.XYChart chart = QuickChart.getChart("Avg Time vs Threshold", "Threshold", "Avg Time", null, thresholds, avgTimes);
        org.knowm.xchart.XYChart chart = new XYChartBuilder().xAxisTitle("Threshold").yAxisTitle("Avg Time").width(800).height(800).build();
        XYSeries series = chart.addSeries("Time", thresholds, avgTimes);

        try {
            BitmapEncoder.saveBitmap(chart, "./thres-vs-time", BitmapEncoder.BitmapFormat.PNG);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(noThreadSumTime/REPEAT);
    }

    public static long runTest(int[] numbers) {
        StopWatch timer = new StopWatch();
        RecursiveArraySum recursiveArraySum = new RecursiveArraySum(numbers, 0, numbers.length);

        timer.start();
        recursiveArraySum.start();

        try {
            recursiveArraySum.join();
        } catch (InterruptedException e) {
            System.out.println(e);
        }

//        System.out.println(recursiveArraySum.getSum());

        timer.stop();
        return timer.getElapsedTime();
    }

    private static int THRESHOLD = 2;

    private long sum = 0;
    private int low = -1;
    private int high = -1;
    private int[] numbers;

    public RecursiveArraySum(int[] numbers, int low, int high) {
        this.numbers = numbers;
        this.low = low;
        this.high = high;

        numThreads++;
    }

    public long getSum() {
        return sum;
    }

    @Override
    public void run() {
        if (high - low > THRESHOLD) {
//            System.out.println("Above threshold, creating new threads");

            int mid = low + (high - low)/2;
            RecursiveArraySum left = new RecursiveArraySum(numbers, low, mid);
            RecursiveArraySum right = new RecursiveArraySum(numbers, mid, high);

            left.start();
            right.start();

            try {
                left.join();
                right.join();
            } catch (InterruptedException e) {
                System.out.println(e);
            }

            sum = left.getSum() + right.getSum();
        } else {
//            System.out.println("Below threshold, summing");
            for (int i = low; i < high; i++) {
                sum += numbers[i];
            }
        }
    }
}
