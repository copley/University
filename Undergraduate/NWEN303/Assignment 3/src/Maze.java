import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by danielbraithwt on 8/23/16.
 */
public class Maze {
    public static int DELAY;

    public static Maze parseMaze(String mazeString, int numPeople) {
        String[] mazeLines = mazeString.split("\n");

        String[] mazeInfo = mazeLines[0].split(" ");
        int N = Integer.parseInt(mazeInfo[0]);
        int startY = Integer.parseInt(mazeInfo[1]);
        int startX = Integer.parseInt(mazeInfo[2]);

        MazeCell[][] maze = new MazeCell[N][N];

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                MazeCell.Type t = MazeCell.Type.EMPTY;
                if (mazeLines[i+1].charAt(j) == 'X') {
                    t = MazeCell.Type.WALL;
                }


                maze[i][j] = new MazeCell(t, j, i);
            }
        }

        return new Maze(maze, numPeople, startX, startY);
    }

    public static MazeCell.Marking getArrivalDirection(MazeCell prev, MazeCell current) {
        if (current.getX() == prev.getX() + 1) return MazeCell.Marking.WEST;
        if (current.getX() == prev.getX() - 1) return MazeCell.Marking.EAST;
        if (current.getY() == prev.getY() - 1) return MazeCell.Marking.SOUTH;
        if (current.getY() == prev.getY() + 1) return MazeCell.Marking.NORTH;

        throw new RuntimeException("Prev and current must be neighbours");
    }

    public MazeCell getArrivalCell(MazeCell cell) {
        if (cell.containsMarking(MazeCell.Marking.WEST)) return maze[cell.getY()][cell.getX() - 1];
        if (cell.containsMarking(MazeCell.Marking.EAST)) return maze[cell.getY()][cell.getX() + 1];
        if (cell.containsMarking(MazeCell.Marking.SOUTH)) return maze[cell.getY() + 1][cell.getX()];
        if (cell.containsMarking(MazeCell.Marking.NORTH)) return maze[cell.getY() - 1][cell.getX()];

        return null;
    }

    public MazeCell getStart() {
        return maze[sy][sx];
    }


    public final Lock l = new ReentrantLock();
    public final Condition finished = l.newCondition();

    public MazeCell[][] maze;
    private int numPeople;
    private int sx;
    private int sy;
    private boolean hasBeenFinished;
    private int totalFinished;

    private Maze(MazeCell[][] maze, int numPeople, int sx, int sy) {
        this.maze = maze;
        this.numPeople = numPeople;
        this.sx = sx;
        this.sy = sy;
    }

    public List<MazeCell> getNeighbours(MazeCell cell) {
        int x = cell.getX();
        int y = cell.getY();

        List<MazeCell> neighbours = new ArrayList<>();

        if (x + 1 < maze.length && maze[y][x+1].getType() != MazeCell.Type.WALL) neighbours.add(maze[y][x+1]);
        if (x - 1 > 0 && maze[y][x-1].getType() != MazeCell.Type.WALL) neighbours.add(maze[y][x-1]);
        if (y + 1 < maze.length && maze[y+1][x].getType() != MazeCell.Type.WALL) neighbours.add(maze[y+1][x]);
        if (y - 1 > 0 && maze[y-1][x].getType() != MazeCell.Type.WALL) neighbours.add(maze[y-1][x]);

        return neighbours;
    }

    public boolean isFinish(MazeCell cell) {
        return cell != getStart() && (cell.getX() == (maze.length - 1) || cell.getY() == (maze.length - 1));
    }

    public boolean hasBeenFinished() {
        return hasBeenFinished;
    }

    public boolean hasAllFinished() {
        return totalFinished == numPeople;
    }

    public void finished(int numPeople) {
        hasBeenFinished = true;
        totalFinished += numPeople;
    }
}
