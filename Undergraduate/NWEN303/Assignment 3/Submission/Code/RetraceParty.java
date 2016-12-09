/**
 * Created by danielbraithwt on 8/23/16.
 */
public class RetraceParty extends Thread {

    private Maze maze;
    private MazeCell current;
    private MazeCell prev;

    public RetraceParty(Maze maze, MazeCell startCell) {
        this.maze = maze;
        this.current = startCell;
    }

    @Override
    public void run() {
        while (true) {

            if (Maze.DELAY != 0) {
                try {
                    Thread.sleep(SearchParty.r.nextInt(Maze.DELAY));
                } catch (InterruptedException e) {}
            }

            current.addMarking(MazeCell.Marking.GOLD);

            if (current == maze.getStart()) {
//                System.out.println("Retraced to start");

                SearchParty p = new SearchParty(maze, current, 1);
                p.start();

                break;
            } else {
                prev = current;
                current = maze.getArrivalCell(current);
            }
        }
    }
}
