// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP103 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP 103, Assignment 3
 * Name:
 * Usercode:
 * ID:
 */

import ecs100.*;
import java.util.*;
import java.io.*;

/** Sokoban
 */

public class Sokoban implements UIButtonListener, UIKeyListener, UIMouseListener 
{

    // Fields
    private Square[][] squares;   // the array describing the current warehouse.
    private int rows;
    private int cols;

    private Coord agentPos;
    private String agentDirection = "left";

    private final int maxLevels = 4;
    private int level = 0;

    private Map<Character,Square> squareMapping;  // character in file to square type
    private Map<Square,String> imageMapping;    // square type to image of square
    private Map<String,String> agentMapping;    // direction to image of worker
    private Map<String,String> keyMapping;      // key string to direction

    private Stack<ActionRecord> actionStack;

    // Constructors
    /** Construct a new Sokoban object
     *  and set up the GUI
     */
    public Sokoban() {
        UI.addButton("New Level", this);
        UI.addButton("Restart", this);
        UI.addButton("Undo", this);
        UI.addButton("left", this);
        UI.addButton("up", this);
        UI.addButton("down", this);
        UI.addButton("right", this);

        UI.println("Put the boxes away.");
        UI.println("You may use keys (wasd or ijkl) but click on the graphics pane first");
        UI.println("Clicking on a location on the map will take you there but dont click to rappedly, it dosnt like that");
        UI.setKeyListener(this);
        UI.setMouseListener(this);

        initialiseMappings();
        load();
    }

    /** Respond to button presses */
    public void buttonPerformed(String button) {
        if (button.equals("New Level")) {
            level = (level+1)%maxLevels;
            load();
        }
        else if (button.equals("Restart"))
            load();
        else if(button.equals("Undo"))
            undo();
        else 
            doAction(button);
    }

    /** Respond to key actions */
    public void keyPerformed(String key) 
    {
        if( key.equals("Backspace") ) undo();
        else doAction(keyMapping.get(key));
    }

    /**
     * Respond to mouse actions
     */
    public void mousePerformed(String action, double x, double y)
    {

        if( action.equals("clicked") )
        {

            int[] clickPos = getClickedSquare(x, y);
            if( clickPos != null )
            {
                System.out.println("Click");
                findPath(new int[] {agentPos.row, agentPos.col} , clickPos);
            }
        }
    }

    /** Move the agent in the specified direction, if possible.
     *  If there is box in front of the agent and a space in front of the box,
     *    then push the box.
     *  Otherwise, if there is anything in front of the agent, do nothing.
     */
    public void doAction(String dir) {
        if (dir==null) return;
        agentDirection = dir;
        Coord newP = agentPos.next(dir);  // where the agent will move to
        Coord nextP = newP.next(dir);     // the place two steps over
        if ( squares[newP.row][newP.col].hasBox() && squares[nextP.row][nextP.col].free() ) {
            push(dir);
            actionStack.add(new ActionRecord("pull", dir));
        }
        else if ( squares[newP.row][newP.col].free() ) {
            move(dir);
            actionStack.add(new ActionRecord("push", dir));
        }
    }

    public void undo()
    {
        if( actionStack.isEmpty() ) 
        {
            System.out.println("There are no moves to be undone");
            return;
        }

        ActionRecord a = actionStack.pop();
        System.out.println("Undo: " + a);

        String oppDir = oppositeDirection(a.dir());
        agentDirection = oppDir;
        if( a.isMove() )
        { 
            pull(oppDir);
        }
        else
        {
            move(oppDir);
        }

    }

    /**
     * Using the A* algorythm
     * http://www.policyalmanac.org/games/aStarTutorial.htm
     */
    public void findPath(int[] start, int[] goal)
    {
        ArrayList<int[]> closed = new ArrayList<int[]>();
        ArrayList<int[]> open = new ArrayList<int[]>();
        int[][][] parents = new int[squares.length][squares[0].length][0];
        int[][] gCost = new int[squares.length][squares[0].length];

        boolean finished = false;

        open.add(start);

        int index = 0;

        while(open.size() != 0 && !finished)
        {
            // Remove the one we are currently looking at from open and add it to closed
            int[] current = open.remove(index);
            closed.add(current);

            // Check to see if the current is the goal
            if( current[0] == goal[0] && current[1] == goal[1] )
            {
                finished = true;
                continue;
            }

            // Add valid adjacent squares to the open array
            for( int i = -1; i <= 1; i++ )
            {
                for( int j = -1; j <= 1; j++ )
                {
                    // We want to avoid diagonals
                    if( (i == -1 && j == -1) || (i == -1 && j == 1) || (i == 1 && j == 1) || (i == 1 && j == -1) || (i == 0 && j == 0) ) continue;
                    
                    int y = current[0] + i;
                    int x = current[1] + j;

                    // Make sure the new cords are within bounds
                    if( ( x >= 0 && x < squares[0].length ) && ( y >= 0 && y < squares.length ) )
                    {

                        if( squares[y][x].free() && !searchArrayList(closed, new int[] {y, x}) ) 
                        {
                            open.add(new int[] {y, x});
                            parents[y][x] = current;
                            gCost[y][x] = gCost[current[0]][current[1]] + ((Math.abs(i)+Math.abs(j))*1);
                        }
                        else if( searchArrayList(closed, new int[] {y, x}) )
                        { 
                            if( gCost[current[0]][current[1]] + ((Math.abs(i)+Math.abs(j))*1) < gCost[y][x] ) parents[y][x] = current;
                        }
                    }

                }
            }

            double bestCost = Double.MAX_VALUE;
            // Find square with lowest cost
            for( int i = 0; i < open.size(); i++ )
            {
                int[] c = open.get(i);
                double cost = gCost[c[0]][c[1]] + Math.sqrt(Math.pow(goal[0]-c[0], 2) + Math.pow(goal[1]-c[1], 2));

                if( cost < bestCost )
                {
                    bestCost = cost; 
                    index = i;
                }
            }

        }

        // If there is no solution
        if( !finished ) return;

        // Walk back from the goal and find all steps
        Stack<Coord> moves = new Stack<Coord>();
        finished = false;
        int[] current = goal;

        while( !finished )
        {
            moves.push(new Coord(current[0], current[1]));
            current = parents[current[0]][current[1]];

            if( current[0] == start[0] && current[1] == start[1] ) finished = true;
        }

        // Move the charactor step by step
        while( !moves.isEmpty() )
        {
            doAction(agentPos.calculateMoveToCoord(moves.pop()));
        }
    }

    public int[] getClickedSquare(double x, double y)
    {
        x -= leftMargin;
        int xCoord = (int) (x/squareSize);

        y-= topMargin;
        int yCoord = (int) (y/squareSize);

        if( xCoord < squares[0].length && yCoord < squares.length ) return new int[] {yCoord,xCoord};
        return null;
    }

    public boolean searchArrayList(ArrayList<int[]> a, int[] tofind)
    {
        for( int i = 0; i < a.size(); i++ )
        {
            int[] c = a.get(i);
            if( tofind[0] == c[0] && tofind[1] == c[1] ) return true;
        }

        return false;
    }

    /** Move the agent into the new position (guaranteed to be empty) */
    public void move(String dir) {
        drawSquare(agentPos);
        agentPos = agentPos.next(dir);
        drawAgent();
        Trace.println("Move " + dir);
        UI.repaintGraphics();
    }

    /** Push: Move the agent, pushing the box one step */
    public void push(String dir) {
        drawSquare(agentPos);
        agentPos = agentPos.next(dir);
        drawAgent();
        Coord boxP = agentPos.next(dir);
        squares[agentPos.row][agentPos.col] = squares[agentPos.row][agentPos.col].moveOff();
        squares[boxP.row][boxP.col] = squares[boxP.row][boxP.col].moveOn();
        drawSquare(boxP);
        Trace.println("Push " + dir);
        UI.repaintGraphics();
    }

    /** Pull: (useful for undoing a push in the opposite direction)
     *  move the agent in direction from dir,
     *  pulling the box into the agent's old position
     */
    public void pull(String dir) {
        String opDir = oppositeDirection(dir);
        Coord boxP = agentPos.next(opDir);
        squares[boxP.row][boxP.col] = squares[boxP.row][boxP.col].moveOff();
        squares[agentPos.row][agentPos.col] = squares[agentPos.row][agentPos.col].moveOn();
        drawSquare(boxP);
        drawSquare(agentPos);
        agentPos = agentPos.next(dir);
        agentDirection = opDir;
        drawAgent();
        Trace.println("Pull " + dir);
        UI.repaintGraphics();
    }

    /** Load a grid of squares (and agent position) from a file */
    public void load() {
        File f = new File("warehouse" + level + ".txt");
        if (f.exists()) {
            List<String> lines = new ArrayList<String>();
            try {
                Scanner sc = new Scanner(f);
                while (sc.hasNext())
                    lines.add(sc.nextLine());
                sc.close();
            }
            catch(IOException e) {
                Trace.println("File error " + e);
            }

            rows = lines.size();
            cols = lines.get(0).length();

            squares = new Square[rows][cols];

            for(int row = 0; row < rows; row++) {
                String line = lines.get(row);
                for(int col = 0; col < cols; col++) {
                    if (col>=line.length())
                        squares[row][col] = Square.empty;
                    else {
                        char ch = line.charAt(col);
                        if ( squareMapping.containsKey(ch) )
                            squares[row][col] = squareMapping.get(ch);
                        else {
                            squares[row][col] = Square.empty;
                            UI.printf("Invalid char: (%d, %d) = %c \n",
                                row, col, ch);
                        }
                        if (ch=='A')
                            agentPos = new Coord(row,col);
                    }
                }
            }
            draw();
        }
    }

    // Drawing 

    private static final int leftMargin = 40;
    private static final int topMargin = 40;
    private static final int squareSize = 25;

    /** Draw the grid of squares on the screen, and the agent */
    public void draw() {
        UI.clearGraphics();
        // draw squares
        for(int row = 0; row<rows; row++)
            for(int col = 0; col<cols; col++)
                drawSquare(row, col);
        drawAgent();
        UI.repaintGraphics();
    }

    private void drawAgent() {
        UI.drawImage(agentMapping.get(agentDirection),
            leftMargin+(squareSize* agentPos.col),
            topMargin+(squareSize* agentPos.row),
            squareSize, squareSize, false);
    }

    private void drawSquare(Coord pos) {
        drawSquare(pos.row, pos.col);
    }

    private void drawSquare(int row, int col) {
        String imageName = imageMapping.get(squares[row][col]);
        if (imageName != null)
            UI.drawImage(imageName,
                leftMargin+(squareSize* col),
                topMargin+(squareSize* row),
                squareSize, squareSize, false);
    }

    /** Return true iff the warehouse is solved - 
     *  all the shelves have boxes on them 
     */
    public boolean isSolved() {
        for(int row = 0; row<rows; row++) {
            for(int col = 0; col<cols; col++)
                if(squares[row][col] == Square.shelf)
                    return  false;
        }
        return true;
    }

    /** Returns the direction that is opposite of the parameter */
    public String oppositeDirection(String dir) {
        if ( dir.equals("right")) return "left";
        if ( dir.equals("left"))  return "right";
        if ( dir.equals("up"))    return "down";
        if ( dir.equals("down"))  return "up";
        return dir;
    }

    private void initialiseMappings() {
        // character in files to square type
        squareMapping = new HashMap<Character,Square>();
        squareMapping.put('.', Square.empty);
        squareMapping.put('A', Square.empty);  // initial position of agent must be an empty square
        squareMapping.put('#', Square.wall);
        squareMapping.put('S', Square.shelf);
        squareMapping.put('B', Square.box);

        // square type to image of square
        imageMapping = new HashMap<Square, String>();
        imageMapping.put(Square.empty, "empty.gif");
        imageMapping.put(Square.wall, "wall.gif");
        imageMapping.put(Square.box, "box.gif");
        imageMapping.put(Square.shelf, "shelf.gif");
        imageMapping.put(Square.boxOnShelf, "boxOnShelf.gif");

        //direction to image of worker
        agentMapping = new HashMap<String, String>();
        agentMapping.put("up", "agent-up.gif");
        agentMapping.put("down", "agent-down.gif");
        agentMapping.put("left", "agent-left.gif");
        agentMapping.put("right", "agent-right.gif");

        // key string to direction 
        keyMapping = new HashMap<String,String>();
        keyMapping.put("i", "up");     keyMapping.put("I", "up");   
        keyMapping.put("k", "down");   keyMapping.put("K", "down"); 
        keyMapping.put("j", "left");   keyMapping.put("J", "left"); 
        keyMapping.put("l", "right");  keyMapping.put("L", "right");

        keyMapping.put("w", "up");     keyMapping.put("W", "up");   
        keyMapping.put("s", "down");   keyMapping.put("S", "down"); 
        keyMapping.put("a", "left");   keyMapping.put("A", "left"); 
        keyMapping.put("d", "right");  keyMapping.put("D", "right");

        actionStack = new Stack();
    }

    public static void main(String[] args) {
        new Sokoban();
    }
}
