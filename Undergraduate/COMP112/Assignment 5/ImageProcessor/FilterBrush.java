import java.util.*;
import java.awt.Color;
import ecs100.*;

/**
 * Handles the filter brush, 
 */
public class FilterBrush
{
    /**
     * Applys a convolution filter to an area defined by mouse positions and the size of a brush
     */
    public static float[][][] ApplyFilterToArea( float[][][] origonal, float[][] filter, ArrayList<double[]> mousePositions, int brushSize )
    {
        float[][][] temp = new float[origonal.length][origonal[0].length][3];

        for( int i = 0; i < origonal.length; i++ )
        {
            for( int j = 0; j < origonal[i].length; j++ )
            {
                temp[i][j] = origonal[i][j].clone();
            }
        }
        
        for( int c = 0; c < mousePositions.size(); c++ )
        {

            for( int i = (int) mousePositions.get(c)[1]-(brushSize/2); i <= mousePositions.get(c)[1]+(brushSize/2); i++ )
            {
                if( i < 0 || i >= origonal.length ) continue;
                
                for( int j = (int) mousePositions.get(c)[0]-(brushSize/2); j <= mousePositions.get(c)[0]+(brushSize/2); j++ )
                {
                    if( j < 0 || j >= origonal[0].length ) continue;
                    
                    float newRed = 0.0f;
                    float newGreen = 0.0f;
                    float newBlue = 0.0f;

                    // apply blur
                    for( int k = -1*(filter.length/2); k <= (filter.length/2); k++ )
                    {
                        for( int h = -1*(filter[0].length/2); h <= (filter[0].length/2); h++ )
                        {
                            int colPos = j+h;
                            int rowPos = i+k;

                            if(i+k < 0)
                            {
                                rowPos = 0;
                            }
                            else if( i+k >= origonal.length )
                            {
                                rowPos = origonal.length-1;
                            }

                            if(j+h < 0)
                            {
                                colPos = 0;
                            }
                            else if(j+h >= origonal[i].length)
                            {
                                colPos = origonal[i].length-1;
                            }

                            newRed += filter[k+(filter.length/2)][h+(filter[0].length/2)] * origonal[rowPos][colPos][0];
                            newGreen += filter[k+(filter.length/2)][h+(filter[0].length/2)] * origonal[rowPos][colPos][1];
                            newBlue += filter[k+(filter.length/2)][h+(filter[0].length/2)] * origonal[rowPos][colPos][2];
                        }
                    }

                    if( newRed < 0 ) newRed = 0;
                    else if( newRed > 1 ) newRed = 1; 

                    if( newGreen < 0) newGreen = 0;
                    else if( newGreen > 1 ) newGreen = 1;

                    if( newBlue < 0) newBlue = 0;
                    else if( newBlue > 1 ) newBlue = 1;

                    temp[i][j][0] = newRed;
                    temp[i][j][1] = newGreen;
                    temp[i][j][2] = newBlue;
                }
            }
        }

        return temp;
    }
}
