import javax.swing.*;

/**
 * Write a description of class ScoreBar here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class ScoreBar extends JPanel
{
    private int score;
    private JLabel scoreLabel;
    
    public ScoreBar()
    {
        score = 0;
        
        scoreLabel = new JLabel("Score: 0");
        add(scoreLabel);
    }
    
    public void updateScore( int scoreChange )
    {
        score += scoreChange;
        
        scoreLabel.setText("Score: " + score);
    }
    
    public int getScore()
    {
        return score;
    }
}
