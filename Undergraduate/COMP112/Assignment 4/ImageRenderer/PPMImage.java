import ecs100.*;
import java.awt.Color;

/**
 * Write a description of class PPMImage here.
 */
public class PPMImage extends Image
{
    public PPMImage( int height, int width, int colorDepth, boolean compressed, String data )
    {
        super("P3", height, width, colorDepth, compressed, data);
    }
    
    protected void parseImageData(String data)
    {
        // Take data and load into color table
        String[] elements = data.split(" ");
        
        // Remove pointer to data variable
        data = null;
        
        int a = 0;
        int b = 0;

        for( int i = 0; i < elements.length; i += 3 )
        {
            Trace.println("\nI: " + i);
            Trace.println("Red: " + elements[i]);
            Trace.println("Green: " + elements[i+1]);
            Trace.println("Blue: " + elements[i+2]);
            
            int red = Integer.parseInt(elements[i]);
            int green = Integer.parseInt(elements[i+1]);
            int blue = Integer.parseInt(elements[i+2]);
            
            colors[a][b] = new Color(((int) colorScale * red), ((int) colorScale * green), ((int) colorScale * blue));

            b++;
            if( b == width )
            {
                b = 0;
                a++;
            }
        }
    }
}
