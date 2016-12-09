package tests.model.board;

import model.board.BoardLoader;
import model.board.Tile;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by root on 5/08/15.
 */
public class BoardLoaderTest {

    @Test
    public void boardLoaderTest(){
        BoardLoader bl = new BoardLoader();
        int width = bl.getWidth();
        int height = bl.getHeight();
        assertEquals(24, width);
        assertEquals(25, height);

        Tile[][] tiles = bl.getTiles();
        assertEquals(width, tiles.length);
        for(Tile[] col : tiles)
            assertEquals(height, col.length);
    }
}
