import java.awt.Color;

/**
 * Write a description of class PBMImage here.
 */
public class PBMImage extends Image
{
    public PBMImage(int height, int width, int colorDepth, boolean compressed, String data)
    {
        super("P1", height, width, colorDepth, compressed, data);
    }
    
    protected void parseImageData(String data)
    {
        // Take data and load into colors
        String[] elements = data.split(" ");

        int a = 0;
        int b = 0;

        for( int i = 0; i < elements.length; i++ )
        {
            // Sinse its grey scale all the R, G and B values are equal to color
            if( Integer.parseInt(elements[i]) == 0 ) colors[a][b] = new Color(255, 255, 255);
            else colors[a][b] = new Color(0, 0, 0);

            b++;
            if( b == width )
            {
                b = 0;
                a++;
            }
        }
    }
}
