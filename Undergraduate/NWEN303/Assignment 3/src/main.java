import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by danielbraithwt on 8/23/16.
 */
public class main {
    public static int AVG = 50;

    public static void main(String[] args) {
        int MAX_PEOPLE = 0;
        String mazeloc;
        if (args.length == 3) {
            MAX_PEOPLE = Integer.parseInt(args[0]);
            Maze.DELAY = Integer.parseInt(args[1]);
            mazeloc = args[2];
        } else {
            throw new RuntimeException();
        }

        ArrayList<Long> times = new ArrayList<>();
        ArrayList<Integer> numPeople = new ArrayList<>();

        String mazeString = null;
        try {
            mazeString = new Scanner(new File(mazeloc)).useDelimiter("\\Z").next();
        } catch (IOException e) {}


        for (int i = 1; i <= MAX_PEOPLE; i++) {
            System.out.println("Running Search With " + i + " People");
            long total = 0;

            for (int j = 0; j < AVG; j++) {
                StopWatch s = new StopWatch();

                Maze maze = Maze.parseMaze(mazeString, i);

                s.start();
                SearchParty searchParty = new SearchParty(maze, maze.getStart(), i);
                searchParty.start();

                try {
                    maze.l.lock();
                    maze.finished.await();
                    s.stop();
                    maze.l.unlock();
                } catch (InterruptedException e) {}

                total += s.getElapsedTime();
            }


            System.out.println(total/AVG);
            times.add(total/AVG);
            numPeople.add(i);
        }

        org.knowm.xchart.XYChart chart = new XYChartBuilder().xAxisTitle("Num People").yAxisTitle("Time").width(800).height(800).build();
        XYSeries series1 = chart.addSeries("Total Time", numPeople, times);

        try {
            BitmapEncoder.saveBitmap(chart, "./numpeople-vs-time", BitmapEncoder.BitmapFormat.PNG);
        } catch (IOException e) {}
    }
}
