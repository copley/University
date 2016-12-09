import ecs100.*;
import java.awt.Color;

/**
 * Zooms in on an area and returns it 
 */
public class CropAndZoom
{
    /**
     * Takes the origonal image and two points, the starting an finishing point, one will be top left and one
     * will be bottom right. Returs the area contained between the two points zoomed in on
     */
    public static float[][][] CropAndZoom( float[][][] origonal, int[] point1, int[] point2 )
    {
        float[][][] temp = new float[origonal.length][origonal[0].length][3];
        
        // Find the dimentions of the area that each zoom pixle maps to. Sometimes you loose out on the
        // pixles at the end so i am adding 20 to each of the distances to account for this
        double cellWidth = ((double) temp[0].length / (double) (Math.abs(point1[0] - point2[0])+20));
        double cellHeight = ((double) temp.length / (double) (Math.abs(point1[1] - point2[1])+20));

        // Find the smallest x and y values
        int origonalX = 0;
        if( point1[0] < point2[0] ) origonalX = point1[0];
        else origonalX = point2[0];

        int origonalY = 0;
        if( point1[1] < point2[1] ) origonalY = point1[1];
        else origonalY = point2[1];
        
        // Go through all the pixles in the temp array and find what they map to 
        for( int i = 0; i < temp.length; i++ )
        {
            for( int j = 0; j < temp[i].length; j++ )
            {   
                temp[i][j] = origonal[(int)(origonalY + (double)i/(cellHeight+1))][(int)(origonalX + (double)j/(cellWidth+1))].clone();
            }
        }

        return temp;
    }
}
