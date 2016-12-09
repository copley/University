import ecs100.*;
import java.io.*;
import java.util.ArrayList;

/**
 * Write a description of class PlaybackBoard here.
 * 
 */
public class PlaybackBoard
{
    // Static Variables
    public static int BoardOffsetX = 100;
    public static int BoardOffsetY = 100;

    private PlaybackCell[][] board;
    private ArrayList<int[]> scores;
    private int maxCommit;
    private int playbackSpeed = 1;
    private int playbackDirection = 0;
    private int currentStep = 0;
    private boolean isRunning;

    public PlaybackBoard(String fp)
    {
        // Initilise the scores arraylist and add the array [0,0]
        // because the first score set will be that
        scores = new ArrayList<int[]>();
        scores.add(new int[] {0,0});

        // Load the board in
        loadBoard();

        isRunning = true;

        // Sround everything because if it fails then the log file was curupt
        try
        {

            String log = Log.readLog(fp);
            String[] commits = log.split(Log.COMMIT_DIV);

            for( int i = 1; i < commits.length; i++ )
            {
                // Split the commit into its sections [scores, tiles]
                String[] sections = commits[i].split(";;");

                // Add scores to arraylist
                // Get the score and roundScore numbers into array
                String[] s = sections[0].substring(sections[0].indexOf("[")+1, sections[0].indexOf("]")).split(", ");
                scores.add(new int[] {Integer.parseInt(s[0].trim()), Integer.parseInt(s[1].trim())});

                String[] tiles = sections[1].trim().split(";");
                for( int j = 0; j < tiles.length; j++ )
                {
                    String[] components = tiles[j].substring(tiles[j].indexOf("[")+1, tiles[j].indexOf("]")).split(", ");
                    // Create a new tile with specified letter, the value dosnt matter
                    Tile t = new Tile(components[2], 0);

                    board[Integer.parseInt(components[0])][Integer.parseInt(components[1])].setTile(t);
                    board[Integer.parseInt(components[0])][Integer.parseInt(components[1])].setStage(i);
                }

                maxCommit = i;
            }
        }
        catch( Exception e )
        {
            UI.println("The log file was currupt, please exit the playback mode");
            isRunning = false;
        }
    }
    
    //public void setPlaybackSpeed( int speed, int direction )
    //{
    //    playbackDirection = direction;
    //    playbackSpeed = speed;
    //}

    public boolean loadBoard()
    {
        board = new PlaybackCell[15][15];

        // Rest the board cells
        try
        {
            int x = PlaybackBoard.BoardOffsetX;
            int y = PlaybackBoard.BoardOffsetY;

            // Opens a reader to the file
            BufferedReader in = new BufferedReader( new FileReader("board.txt"));

            // Reads lines and turns them into tiles
            int i = 0;
            String line = in.readLine();
            while( line != null )
            {
                String[] cells = line.split(" ");

                for( int j = 0; j < cells.length; j++ )
                {
                    // Defaultly set the cell type to regular
                    CellType type = CellType.Regular;
                    switch(Integer.parseInt(cells[j]))
                    {
                        // Double Letter Score
                        case 1: type = CellType.DoubleLetter;
                        break;
                        // Tripple Letter Score
                        case 2: type = CellType.TrippleLetter;
                        break;
                        // Double Word Score
                        case 3: type = CellType.DoubleWord;
                        break;
                        // Tripple Word Score
                        case 4: type = CellType.TrippleWord;
                        break;
                        // Starting Square
                        case 5: type = CellType.Start;
                        break;
                    }

                    // Create new tile
                    board[i][j] = new PlaybackCell(type, x, y);

                    x += Tile.TileWidth;
                }

                i++;

                x = Board.BoardOffsetX;
                y += Tile.TileHeight;

                line = in.readLine();

            }

            Trace.println("[>] Board initilised");

            return false;
        }
        catch( IOException e )
        {
            UI.println("Failed to open board file");
            UI.println("Press the reset the button if you wish to try again");
            return true;
        }
    }

    public void draw(int sliderVal)
    {
        if( isRunning )
        {
            UI.clearGraphics();

            if( sliderVal < 0 && currentStep > 0 ) currentStep--;
            else if( sliderVal > 0 && currentStep < maxCommit ) currentStep++;
            
            // Need to scale the slider val to be bettween 0 and the max commit value
            //int scaled = (int) ((double) maxCommit/(double) ScrabbleSolitaire.SLIDER_MAX) * sliderVal;
            //int scaled = (int) ( ( (float) sliderVal ) / (((float) ScrabbleSolitaire.SLIDER_MAX) / ((float) maxCommit) ));
            //Trace.println(String.format("%s : %s", sliderVal, scaled));

            // Draw information
            UI.setFontSize(30);
            UI.drawString("Log Playback Mode", PlaybackBoard.BoardOffsetX, PlaybackBoard.BoardOffsetY-50);
            UI.setFontSize(15);
            UI.drawString(String.format("Commit: %d", currentStep), PlaybackBoard.BoardOffsetX, PlaybackBoard.BoardOffsetY-20);

            // Draw commit score
            UI.setFontSize(30);
            UI.drawString("Score: " + scores.get(currentStep)[0] + " ( +" + scores.get(currentStep)[1] + " )", Board.BoardOffsetX + 380, Board.BoardOffsetY-20);

            // Draw all the cells
            for( int i = 0; i < board.length; i++ )
            {
                for( int j = 0; j < board[i].length; j++ )
                {
                    board[i][j].draw(currentStep);
                }
            }

            UI.repaintGraphics();
        }
    }
}
