import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by danielbraithwt on 8/23/16.
 */
public class SearchParty extends Thread {
    public static final Random r = new Random();

    private MazeCell current;
    private MazeCell prev;
    private Maze maze;
    private int numPeople;
    private boolean deadend;

    private SearchParty toJoin;

    private final Lock l = new ReentrantLock();
    private Condition joined;

    public SearchParty(Maze maze, MazeCell start, int numPeople) {
        this.maze = maze;
        this.current = start;
        this.numPeople = numPeople;
    }

    @Override
    public void run() {
        while (true) {
            current.l.lock();
            current.entering(this);
            current.l.unlock();

            if (Maze.DELAY != 0) {
                try {
                    Thread.sleep(r.nextInt(Maze.DELAY));
                } catch (InterruptedException e) {}
            }

            // If this is the first time arriving at this cell then we want to store how we arrived here
            if (prev != null && MazeCell.firstArrival(current)) {
                current.addMarking(Maze.getArrivalDirection(prev, current));
            }

            List<MazeCell> neighbours = maze.getNeighbours(current);
            if (prev != null) neighbours.remove(prev);

            List<MazeCell> nondead = MazeCell.filter(neighbours, MazeCell.Marking.DEAD);
            List<MazeCell> unmarked = MazeCell.filterMarked(neighbours);
            List<MazeCell> golden = MazeCell.filterElse(neighbours, MazeCell.Marking.GOLD);

            MazeCell next = null;

            if (maze.isFinish(current)) {
                maze.l.lock();
                boolean f = maze.hasBeenFinished();
                if (!f) {
                    RetraceParty p = new RetraceParty(maze, current);
                    p.start();

                    maze.finished(numPeople - 1);
                } else {
                    maze.finished(numPeople);
                }

                if (maze.hasAllFinished()) {
                    maze.finished.signalAll();
                }
                maze.l.unlock();

                // Now this thread is finished
                break;
            } else if (golden.size() > 0) {
                next = golden.get(0);
            } else if (deadend && nondead.size() == 1) {
                // We are returning from a dead end

                // Attempt to join with another thread, but first check if we are being joined with to avoid deadlock
                current.l.lock();
                boolean b = checkJoin();
                if (b) {
                    // This thread was joined to another, exit
                    current.l.unlock();
                    break;
                } else {
                    attemptJoin();
                }
                current.l.unlock();

                current.addMarking(MazeCell.Marking.DEAD);
                next = nondead.get(0);
            } else if (deadend && nondead.size() == 0) {
                // Search partys can get stuck in deadends depending on order
                // of execution so this allowes a stuck thread to escape.
                next = maze.getArrivalCell(current);
            } else if (nondead.size() > 1) {
//                System.out.println("Junction");
                deadend = false;

                // Attempt to join with another thread, but first check if we are being joined with to avoid deadlock
                current.l.lock();
                boolean b = checkJoin();
                if (b) {
                    // This thread was joined to another, exit
                    current.l.unlock();
                    break;
                } else {
                    attemptJoin();
                }
                current.l.unlock();

                // Then we are at a junction, now need to check if we want to split up or not, if there is only one
                // person in our search party then just choose a random direction and take it
                if (unmarked.size() > 0) {
                    // If there are unmarked paths we want to split up down them

                    int newPeople = (numPeople/unmarked.size()) > 0 ? (numPeople/unmarked.size()) : 0;
                    int i;
                    for (i = 0; i < unmarked.size()-1 && numPeople > 1; i++) {
                        numPeople -= newPeople;
                        SearchParty p = new SearchParty(maze, unmarked.get(i), newPeople);
                        p.prev = current;
                        p.start();
                    }

                    next = unmarked.get(i);
                } else {
                    next = nondead.get(r.nextInt(nondead.size()));
                }
            } else if (neighbours.size() == 0) {
//                System.out.println("Deadend");
                deadend = true;
                current.addMarking(MazeCell.Marking.DEAD);

                next = prev;
            } else {
                current.l.lock();
                boolean b = checkJoin();
                if (b) {
                    // This thread was joined to another, exit
                    current.l.unlock();
                    break;
                }
                current.l.unlock();

                if (nondead.size() == 0) {
                    next = neighbours.get(0);
                } else {
                    next = nondead.get(0);
                }
            }

            // Any waiting join has failed
            current.l.lock();
            if (toJoin != null) {
                joined.signalAll();
                toJoin = null;
            }
            current.leaving(this);
            current.l.unlock();


            prev = current;
            current = next;
        }
    }

    private void attemptJoin() {
        // Remove our selves and the party we are trying to join so someone cant join with us
        List<SearchParty> partys = current.getVisiting();
        current.leaving(this);

        if (partys.size() == 0) {
            return;
        }

        SearchParty toJoin = partys.remove(0);

        toJoin.l.lock();
        boolean b = toJoin.requestJoin(this);
        toJoin.l.unlock();

        if (!b) {
            return;
        }

        try {
            // This is so the current cell lock can be released while this thread is waiting
            toJoin.joined = current.l.newCondition();
            toJoin.joined.await();
        } catch (InterruptedException e) {}
    }

    private boolean checkJoin() {
        boolean r = false;

        if (toJoin != null) {
            toJoin.numPeople += numPeople;
            joined.signalAll();
            r = true;
        }

        return r;
    }

    public boolean requestJoin(SearchParty party) {
        if (toJoin == null) {
            toJoin = party;
            return true;
        }

        return false;
    }
}
