import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by danielbraithwt on 8/23/16.
 */
public class MazeCell {
    public enum Marking {
        DEAD, LIVE, GOLD, EAST, WEST, NORTH, SOUTH
    }

    public enum Type {
        WALL, EMPTY
    }

    public static List<MazeCell> filter(List<MazeCell> cells, Marking m) {
        List<MazeCell> notMarked = new ArrayList<>();

        for (MazeCell c : cells) {
            if (!c.containsMarking(m)) notMarked.add(c);
        }

        return notMarked;
    }

    public static List<MazeCell> filterElse(List<MazeCell> cells, Marking m) {
        List<MazeCell> marked = new ArrayList<>();

        for (MazeCell c : cells) {
            if (c.containsMarking(m)) marked.add(c);
        }

        return marked;
    }

    public static List<MazeCell> filterMarked(List<MazeCell> cells) {
        List<MazeCell> notMarked = new ArrayList<>();

        for (MazeCell c : cells) {
            if (!c.hasMarking()) notMarked.add(c);
        }

        return notMarked;
    }

    public static boolean firstArrival(MazeCell cell) {
        return !cell.containsMarking(Marking.EAST) &&
                !cell.containsMarking(Marking.WEST) &&
                !cell.containsMarking(Marking.NORTH) &&
                !cell.containsMarking(Marking.SOUTH);
    }



    private Type type;
    private Set<Marking> markings;
    private int x;
    private int y;


    public final Lock l = new ReentrantLock();
    private List<SearchParty> visiting;

    public MazeCell(Type t, int x, int y) {
        type = t;
        markings = new HashSet<>();
        this.x = x;
        this.y =y;

        visiting = new ArrayList<>();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Type getType() {
        return type;
    }

    public synchronized boolean hasMarking() {
        return markings.size() > 0;
    }

    public synchronized boolean containsMarking(Marking m) {
        return markings.contains(m);
    }

    public synchronized void addMarking(Marking m) {
        markings.add(m);
    }

    public void entering(SearchParty p) {
        visiting.add(p);
    }

    public void leaving (SearchParty p) {
        visiting.remove(p);
    }

    public List<SearchParty> getVisiting() {
        return visiting;
    }
}
