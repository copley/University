import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 */
public class Board2048 extends JFrame
{
    private JMenuBar menuBar;
    private Panel2048 p;
    private ScoreBar s;

    public Board2048()
    {
        setLayout(new BorderLayout());

        // Set up the menu bar
        menuBar = new JMenuBar();

        JMenu game = new JMenu("Game");
        menuBar.add(game);

        JMenuItem newGame = new JMenuItem("New Game");
        newGame.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    p.resetBoard();
                }
            });
        game.add(newGame);

        setJMenuBar(menuBar);

        s = new ScoreBar();
        add(s, BorderLayout.SOUTH);

        p = new Panel2048(s);
        add(p, BorderLayout.CENTER);

        addKeyListener( new KeyListener()
            {
                public void keyTyped( KeyEvent e )
                {

                }

                public void keyPressed( KeyEvent e )
                {
                    if( !p.hasLost() && !p.hasWon() )
                    {
                        if( e.getKeyCode() == KeyEvent.VK_LEFT )
                        {
                            p.left();
                        }
                        else if( e.getKeyCode() == KeyEvent.VK_RIGHT )
                        {
                            p.right();
                        }
                        else if( e.getKeyCode() == KeyEvent.VK_UP )
                        {
                            p.up();
                        }
                        else if( e.getKeyCode() == KeyEvent.VK_DOWN )
                        {
                            p.down();
                        }
                        
                        // Chceck to see if the user has won or lost
                        if( p.hasLost() ) JOptionPane.showMessageDialog(null, "You have lost :(", "Game Over", JOptionPane.INFORMATION_MESSAGE);
                        else if( p.hasWon() ) JOptionPane.showMessageDialog(null, "You have won :D", "Game Over", JOptionPane.INFORMATION_MESSAGE);
                    }
                }

                public void keyReleased( KeyEvent e )
                {

                }
            });

        setSize(new Dimension((Cell2048.CELL_WIDTH * 4) + 32, (Cell2048.CELL_HEIGHT * 4) + 120));
        setTitle("2048");
        setVisible(true);
    }

    /////////////////////////////////////////
    public static void main(String[] args)
    {
        new Board2048();
    }

}
