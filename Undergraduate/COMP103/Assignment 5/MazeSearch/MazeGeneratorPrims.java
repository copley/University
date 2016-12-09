import java.util.*;
import java.awt.Color;
import ecs100.*;

/**
 * Got help from here
 * http://weblog.jamisbuck.org/2011/1/10/maze-generation-prim-s-algorithm
 */
public class MazeGeneratorPrims
{
    // instance variables - replace the example below with your own
    private int size;
    private Cell[][] maze;

    /**
     * Constructor for objects of class MazeGeneratorPrims
     */
    public MazeGeneratorPrims(int size)
    {
        this.size = size;
        maze = new Cell[size][size];

        for( int i = 0; i < size; i++ )
            for( int j = 0; j < size; j++ )
                maze[i][j] = new Cell(i, j);
    }

    public void generate(Map<Cell, Set<Cell>> nodes)
    {
        UI.println("Generatiing Maze");
        // Add all the cells to the map
        for( int i = 0; i < size; i++ )
            for( int j = 0; j < size; j++ )
                nodes.put(maze[i][j], new HashSet<Cell>());
                
        Random r = new Random();
        // Randomly select a starting cell
        int startX = 0; //r.nextInt(size);
        int startY = 0; //r.nextInt(size);
        maze[startX][startY].setVisited(true);

        ArrayList<Cell> frontier = new ArrayList<Cell>();
        addTouching(frontier, maze[startX][startY]);
        
        while( !frontier.isEmpty() )
        {
            Cell c = frontier.remove(r.nextInt(frontier.size()));
  
            draw();
            c.setVisited(true);
            c.setFrontier(false);
            
            if( c.x + 1 < size && maze[c.x+1][c.y].isVisited() ) 
            {
                //if( nodes.get(maze[c.x+1][c.y]) == null ) nodes.put(maze[c.x+1][c.y], new HashSet<Cell>());
                nodes.get(maze[c.x+1][c.y]).add(c);
            }
            else if( c.x - 1 >= 0 && maze[c.x-1][c.y].isVisited() ) 
            {
                //if( nodes.get(maze[c.x-1][c.y]) == null ) nodes.put(maze[c.x-1][c.y], new HashSet<Cell>());
                nodes.get(maze[c.x-1][c.y]).add(c);
            }
            else if( c.y + 1 < size && maze[c.x][c.y+1].isVisited() ) 
            {
                //if( nodes.get(maze[c.x][c.y+1]) == null ) nodes.put(maze[c.x][c.y+1], new HashSet<Cell>());
                nodes.get(maze[c.x][c.y+1]).add(c);
            }
            else if( c.y - 1 >= 0 && maze[c.x][c.y-1].isVisited() ) 
            {
                //if( nodes.get(maze[c.x][c.y-1]) == null ) nodes.put(maze[c.x][c.y-1], new HashSet<Cell>());
                nodes.get(maze[c.x][c.y-1]).add(c);
            }

            addTouching(frontier, c);
            
            
        }
        
        // Reset all the cells
        for( int i = 0; i < size; i++ )
            for( int j = 0; j < size; j++ )
                maze[i][j].setVisited(false);
                
        UI.println("Maze Finished");
    }

    private void draw()
    {
        UI.clearGraphics(false);
        for( int i = 0; i < size; i++ )
        {
            for( int j = 0; j < size; j++ )
            {
                Color c = Color.BLACK;
                if( maze[i][j].isVisited() ) c = Color.YELLOW;
                else if( maze[i][j].isFrontier() ) c = Color.RED;

                drawCell(maze[i][j], c, false);
            }
        }
        
        UI.repaintGraphics();
        UI.sleep(10);
    }

    public void drawCell(Cell cell, Color color, boolean redraw) {
        UI.setColor(color);

        int x = cell.x * MazeSearch.CELL_SIZE;
        int y = cell.y * MazeSearch.CELL_SIZE;
        int w = MazeSearch.CELL_SIZE;
        int h = MazeSearch.CELL_SIZE;

        UI.fillRect(x + 1, y + 1, w - 2, h - 2, redraw);
        //if (redraw) UI.sleep(MazeSearch.DELAY);
    }
    
    public Cell getEntrance() {
        return maze[0][0];
    }

    public Cell getExit() {
        return maze[size - 1][size - 1];
    }

    private void addTouching(ArrayList<Cell> f, Cell c)
    {
        if( c.x + 1 < size && !maze[c.x+1][c.y].isVisited() && !maze[c.x+1][c.y].isFrontier() ) 
        {
            maze[c.x+1][c.y].setFrontier(true);
            f.add(maze[c.x+1][c.y]);
        }
        if( c.x - 1 >= 0 && !maze[c.x-1][c.y].isVisited() && !maze[c.x-1][c.y].isFrontier() ) 
        {
            maze[c.x-1][c.y].setFrontier(true);
            f.add(maze[c.x-1][c.y]);
        }
        if( c.y + 1 < size && !maze[c.x][c.y+1].isVisited() && !maze[c.x][c.y+1].isFrontier() ) 
        {
            maze[c.x][c.y+1].setFrontier(true);
            f.add(maze[c.x][c.y+1]);
        }
        if( c.y - 1 >= 0 && !maze[c.x][c.y-1].isVisited() && !maze[c.x][c.y-1].isFrontier() ) 
        {
            maze[c.x][c.y-1].setFrontier(true);
            f.add(maze[c.x][c.y-1]);
        }
    }

}
