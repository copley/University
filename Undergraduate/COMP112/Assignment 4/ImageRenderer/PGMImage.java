import java.awt.Color;

/**
 * Write a description of class PGMImage here.
 */
public class PGMImage extends Image
{
    public PGMImage( int height, int width, int colorDepth, boolean compressed, String data )
    {
        super("P2", height, width, colorDepth, compressed, data);
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
            colors[a][b] = new Color(((int) colorScale * Integer.parseInt(elements[i])), ((int) colorScale * Integer.parseInt(elements[i])), ((int) colorScale * Integer.parseInt(elements[i])) );

            b++;
            if( b == width )
            {
                b = 0;
                a++;
            }
        }
    }
}
