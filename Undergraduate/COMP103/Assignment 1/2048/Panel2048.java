import javax.swing.*;
import java.awt.*;
import java.util.Random;
import java.awt.Graphics2D;
import javax.swing.border.*;

/**
 */
public class Panel2048 extends JPanel
{
    private static final int BOARD_SIZE = 4;

    private Cell2048[][] board;
    private Random r;
    private ScoreBar score;

    public Panel2048(ScoreBar s)
    {
        setBorder(new EmptyBorder(10, 10, 10, 10));
        
        score = s;
        
        r = new Random();
        board = new Cell2048[BOARD_SIZE][BOARD_SIZE];
        
        resetBoard();
    }

    /**
     * Resets the board to its default state, ie empty with two randomly placed cells
     */
    public void resetBoard()
    {   
        // Set all locations on the board to new cells
        for( int i = 0; i < BOARD_SIZE; i++ )
        {
            for( int j = 0; j < BOARD_SIZE; j++ )
            {
                board[i][j] = new Cell2048(0, (i * Cell2048.CELL_WIDTH), (j * Cell2048.CELL_HEIGHT) );
            }
        }

        // Insert two randomly places cells
        insertCell();
        insertCell();
        
        repaint();
    }
    
    /**
     * Returns true if the board is in a win state, ie if the
     * player has created a tile with the value 2048
     */
    public boolean hasWon()
    {
        for( int i = 0; i < BOARD_SIZE; i++ )
            for( int j = 0; j < BOARD_SIZE; j++ )
                if( board[i][j].getValue() == 2048 ) return true;
            
        return false;
    }
    
    /**
     * Returns true if the board is in a loosing state, ie if the eitire
     * board is full and no moves can be made
     */
    public boolean hasLost()
    {
        for( int i = 0; i < BOARD_SIZE; i++ )
        {
            for( int j = 0; j < BOARD_SIZE; j++ )
            {
                // If the current cell is 0 then the game cant be over
                if( board[i][j].getValue() == 0 ) return false;
                // If the cell below the currennt cell is the same value then the game cant be over
                else if( i + 1 < BOARD_SIZE && board[i][j].getValue() == board[i+1][j].getValue() ) return false;
                // If the cell to the right of the current cell is the same value then the game cant be over
                else if( j + 1 < BOARD_SIZE && board[i][j].getValue() == board[i][j+1].getValue() ) return false;
            }
        }
        
        return true;
    }

    /**
     * Inserts a cell at a random location on the board, it starts from 0,0 and moves right and down
     * covering all the cells till it inserts a cell. For every place that is empty and dosnt insert
     * anything it increases the chance of inserting one next time, ensuring that by the end there is a 100% chance
     */
    private void insertCell()
    {
        // Find the number of free cells
        int n = 0;

        for( int i = 0; i < BOARD_SIZE; i++ )
            for( int j = 0; j < BOARD_SIZE; j++ )
                if( board[i][j].getValue() == 0 ) n++;

        int currentProb = 1;

        for( int i = 0; i < BOARD_SIZE && currentProb != 0; i++ )
        {
            for( int j = 0; j < BOARD_SIZE && currentProb != 0; j++ )
            {
                if( board[i][j].getValue() == 0 ) 
                {
                    int rand = r.nextInt(n+1);

                    if( rand <= currentProb )
                    {
                        if( r.nextFloat() <= 0.7 ) board[i][j].setValue(2);
                        else board[i][j].setValue(4);

                        currentProb = 0;
                    } else currentProb++;
                }
            }
        }
    }

    public void left()
    {
        boolean somethingHappened = false;
        
        for( int j = 0; j < BOARD_SIZE; j++ )
        {
            // Start from the right side of the row and combine all the pairs
            for( int i = 0; i < BOARD_SIZE; i++ )
            {
                if( i + 1 < BOARD_SIZE && board[i][j].getValue() != 0 && board[i][j].getValue() == board[i+1][j].getValue() )
                {
                    score.updateScore(board[i][j].getValue()*2);
                    board[i][j].setValue(board[i][j].getValue()*2);
                    board[i+1][j].setValue(0);
                    
                    somethingHappened = true;
                }
                else if( i + 1 < BOARD_SIZE && board[i][j].getValue() != 0 && board[i+1][j].getValue() == 0 )
                {
                    for( int k = i+1; k < BOARD_SIZE; k++ )
                    {
                        if( board[k][j].getValue() != 0 )
                        {
                            if( board[k][j].getValue() == board[i][j].getValue() && !board[k][j].isCellUpdated() ) 
                            {
                                score.updateScore(board[k][j].getValue()*2);
                                board[i][j].setValue(board[k][j].getValue()*2);
                                board[k][j].setValue(0);
                            
                                somethingHappened = true;
                            }
                            
                            k = BOARD_SIZE;
                        }
                    }
                }
            }
            
            // Now move all cells as left as they can go
            for( int i = 0; i < BOARD_SIZE; i++ )
            {
                if( board[i][j].getValue() == 0 )
                {
                    for( int k = i+1; k < BOARD_SIZE; k++ )
                    {
                        if( board[k][j].getValue() != 0 )
                        {
                            board[i][j].setValue(board[k][j].getValue());
                            board[k][j].setValue(0);
                            
                            k = BOARD_SIZE;
                            somethingHappened = true;
                        }
                    }
                }
            }
        }
        
        if( somethingHappened ) insertCell();
        
        repaint();
    }

    public void right()
    {
        boolean somethingHappened = false;
        
        for( int j = 0; j < BOARD_SIZE; j++ )
        {
            // Start from the right side of the row and combine all the pairs
            for( int i = BOARD_SIZE-1; i >= 0; i-- )
            {
                if( i - 1 >= 0 && board[i][j].getValue() != 0 && board[i][j].getValue() == board[i-1][j].getValue() )
                {
                    score.updateScore(board[i][j].getValue()*2);
                    board[i][j].setValue(board[i][j].getValue()*2);
                    board[i-1][j].setValue(0);
                    
                    somethingHappened = true;
                }
                else if( i - 1 >= 0 && board[i][j].getValue() != 0 && board[i-1][j].getValue() == 0 )
                {
                    for( int k = i-1; k >= 0; k-- )
                    {
                        if( board[k][j].getValue() != 0 )
                        {
                            if( board[k][j].getValue() == board[i][j].getValue() && !board[k][j].isCellUpdated() ) 
                            {
                                score.updateScore(board[k][j].getValue()*2);
                                board[i][j].setValue(board[k][j].getValue()*2);
                                board[k][j].setValue(0);
                            
                                somethingHappened = true;
                            }
                            
                            k = -1;
                        }
                    }
                }
            }
            
            // Now move all cells as left as they can go
            for( int i = BOARD_SIZE-1; i >= 0; i-- )
            {
                if( board[i][j].getValue() == 0 )
                {
                    for( int k = i-1; k >= 0; k-- )
                    {
                        if( board[k][j].getValue() != 0 )
                        {
                            board[i][j].setValue(board[k][j].getValue());
                            board[k][j].setValue(0);
                            
                            k = -1;
                            somethingHappened = true;
                        }
                    }
                }
            }
        }
        
        if( somethingHappened ) insertCell();
        
        repaint();
    }

    public void up()
    {
        boolean somethingHappened = false;
        
        for( int i = 0; i < BOARD_SIZE; i++ )
        {
            // Start from the right side of the row and combine all the pairs
            for( int j = 0; j < BOARD_SIZE; j++ )
            {
                if( j + 1 < BOARD_SIZE && board[i][j].getValue() != 0 && board[i][j].getValue() == board[i][j+1].getValue() )
                {
                    score.updateScore(board[i][j].getValue()*2);
                    board[i][j].setValue(board[i][j].getValue()*2);
                    board[i][j+1].setValue(0);
                    
                    somethingHappened = true;
                }
                else if( j + 1 < BOARD_SIZE && board[i][j].getValue() != 0 && board[i][j+1].getValue() == 0 )
                {
                    for( int k = j+1; k < BOARD_SIZE; k++ )
                    {
                        if( board[i][k].getValue() != 0 )
                        {
                            if( board[i][k].getValue() == board[i][j].getValue() ) 
                            {
                                score.updateScore(board[i][k].getValue()*2);
                                board[i][j].setValue(board[i][k].getValue()*2);
                                board[i][k].setValue(0);
                            
                                somethingHappened = true;
                            }
                            
                            k = BOARD_SIZE;
                        }
                    }
                }
            }
            
            // Now move all cells as left as they can go
            for( int j = 0; j < BOARD_SIZE; j++ )
            {
                if( board[i][j].getValue() == 0 )
                {
                    for( int k = j+1; k < BOARD_SIZE; k++ )
                    {
                        if( board[i][k].getValue() != 0 )
                        {
                            board[i][j].setValue(board[i][k].getValue());
                            board[i][k].setValue(0);
                            
                            k = BOARD_SIZE;
                            somethingHappened = true;
                        }
                    }
                }
            }
        }
        
        if( somethingHappened ) insertCell();
        
        repaint();
    }

    public void down()
    {
        boolean somethingHappened = false;
        
        for( int i = 0; i < BOARD_SIZE; i++ )
        {
            // Start from the right side of the row and combine all the pairs
            for( int j = BOARD_SIZE - 1; j >= 0; j-- )
            {
                if( j - 1 >= 0 && board[i][j].getValue() != 0 && board[i][j].getValue() == board[i][j-1].getValue() )
                {
                    score.updateScore(board[i][j].getValue()*2);
                    board[i][j].setValue(board[i][j].getValue()*2);
                    board[i][j-1].setValue(0);
                    
                    somethingHappened = true;
                }
                else if( j - 1 >= 0 && board[i][j].getValue() != 0 && board[i][j-1].getValue() == 0 )
                {
                    for( int k = j-1; k >= 0; k-- )
                    {
                        if( board[i][k].getValue() != 0 )
                        {
                            if( board[i][k].getValue() == board[i][j].getValue() ) 
                            {
                                score.updateScore(board[i][k].getValue()*2);
                                board[i][j].setValue(board[i][k].getValue()*2);
                                board[i][k].setValue(0);
                            
                                somethingHappened = true;
                            }
                            
                            k = -1;
                        }
                    }
                }
            }
            
            // Now move all cells as left as they can go
            for( int j = BOARD_SIZE - 1; j >= 0; j-- )
            {
                if( board[i][j].getValue() == 0 )
                {
                    for( int k = j-1; k >= 0; k-- )
                    {
                        if( board[i][k].getValue() != 0 )
                        {
                            board[i][j].setValue(board[i][k].getValue());
                            board[i][k].setValue(0);
                            
                            k = -1;
                            somethingHappened = true;
                        }
                    }
                }
            }
        }
        
        if( somethingHappened ) insertCell();
        
        repaint();
    }

    public void draw(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g;
        
        // Draw the background
        g2d.setColor(new Color(130, 105, 83));
        g2d.fillRect(0, 0, (Cell2048.CELL_WIDTH * 4)+10, (Cell2048.CELL_WIDTH * 4)+10);

        // Draw all the cells
        for( int i = 0; i < BOARD_SIZE; i++ )
        {
            for( int j = 0; j < BOARD_SIZE; j++ )
            {
                board[i][j].draw(g2d);
            }
        }
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        draw(g);
    }
}
