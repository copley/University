package tests.model.board;

import model.Card;
import model.Player;
import model.PlayerCharacter;
import model.board.Board;
import model.board.Position;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by root on 5/08/15.
 */
public class BoardTest {

    /**
     * Path from Mrs. White's starting position to the hall (this path will use the trapdoor)
     */
    @Test
    public void testFindValidPath_1() throws Throwable {
        List<Position> path = findPath(new Position(9, 0), new Position(11, 18));
        assertEquals(18, path.size());
    }

    /**
     * Path from Dinning Room starting position to the Library
     */
    @Test
    public void testFindValidPath_2() throws Throwable {
        List<Position> path = findPath(new Position(7, 12), new Position(17, 16));
        assertEquals(14, path.size());
    }

    /**
     * Path from Dinning Room starting position to the Library
     */
    @Test
    public void testFindValidPath_3() throws Throwable {
        List<Position> path = findPath(new Position(7, 12), new Position(17, 16));
        assertEquals(14, path.size());
    }

    /**
     * Path from outside the Kitchen to outside the Study (this path should use the trapdoor under these two rooms).
     */
    @Test
    public void testFindValidPath_4() throws Throwable {
        List<Position> path = findPath(new Position(4, 7), new Position(17, 20));
        assertEquals(3, path.size());
    }

    /**
     * An out of bounds position should cause a IllegalArgumentException to be thrown.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFindInvalidPath_1() throws Throwable {
        findPath(new Position(-1, 12), new Position(17, 16));
    }

    /**
     * (0, 0) is in bounds of the board, but it's unreachable therefore null should be returned.
     */
    @Test
    public void testFindInvalidPath_2() throws Throwable {
        List<Position> path = findPath(new Position(17, 16), new Position(0, 0));
        assertEquals(null, path);
    }

    @Test
    public void testPlayerStarts_1() throws Throwable {
        List<Player> players = Arrays.asList(
                new Player(PlayerCharacter.COLONEL_MUSTARD, Collections.<Card>emptyList()),
                new Player(PlayerCharacter.MISS_SCARLETT, Collections.<Card>emptyList())
            );

        Board b = new Board(players);
        Position mustardPos = b.getPlayerPosition(players.get(0));
        Position scarlettPos = b.getPlayerPosition(players.get(1));

        assertEquals(new Position(0, 17), mustardPos);
        assertEquals(new Position(7, 24), scarlettPos);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPlayerInvalidStarts_1() throws Throwable {
        List<Player> players = Arrays.asList(
                new Player(PlayerCharacter.COLONEL_MUSTARD, Collections.<Card>emptyList()),
                new Player(PlayerCharacter.MISS_SCARLETT, Collections.<Card>emptyList())
        );

        Board b = new Board(players);
        b.getPlayerPosition(new Player(PlayerCharacter.MRS_WHITE, Collections.<Card>emptyList()));
    }

    @Test
    public void testFindValidPath_5() throws Throwable {
        List<Position> path = findPath(new Position(5, 7), new Position(17, 12));
        assertEquals(13, path.size());
    }

    /**
     * Test moving in the same room (the result should be 0 as moving within a room doesn't consume dice rolls)
     */
    @Test
    public void testFindValidPath_6() throws Throwable {
        List<Position> path = findPath(new Position(1, 1), new Position(2, 2));
        assertEquals(0, path.size());
    }

    private static List<Position> findPath(Position start, Position goal){
        Board b = new Board(Collections.<Player>emptyList());
        return b.findPath(start, goal);
    }
}
