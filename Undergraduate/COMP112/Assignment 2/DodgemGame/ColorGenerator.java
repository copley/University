
/**
 * This class randomly generates colors
 * 
 * @author Daniel Thomas Braithwaite 
 * @version 19/03/2014
 */

import java.util.Random;
import java.awt.Color;

public class ColorGenerator
{
    private static Random r = new Random();

    /**
     * Returns a randomly generated color
     */
    public static Color generateColor()
    {
        int r = ColorGenerator.r.nextInt(255);
        int g = ColorGenerator.r.nextInt(255);
        int b = ColorGenerator.r.nextInt(255);
        
        return new Color(r, g, b);
    }
}
