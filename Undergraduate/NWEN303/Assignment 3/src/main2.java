import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * Created by danielbraithwt on 8/24/16.
 */

public class main2 {
    public static void main(String[] args) {
        String mazeString = null;
        try {
            mazeString = new Scanner(new File("grid2.txt")).useDelimiter("\\Z").next();
        } catch (IOException e) {}


//        while (true) {

            Maze maze = Maze.parseMaze(mazeString, 1);

            SearchParty searchParty = new SearchParty(maze, maze.getStart(), 1);
            searchParty.start();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    MapDisplay display = new MapDisplay(maze);
                    display.refresh();
                }
            }).start();

            try {
                maze.l.lock();
                maze.finished.await();
                maze.l.unlock();
            } catch (InterruptedException e) {}
//        }
    }
}
