
/**
 * Handles applying a n * m convolution filter to an image
 */
public class ApplyConvolutionFilter
{
    /**
     * Takes an image and a filter and returns the filter applied to the image
     */
    public static float[][][] ApplyConvolutionFilter(float[][][] origonal, float[][] filter)
    {
        float[][][] temp = new float[origonal.length][origonal[0].length][3];

        // Go through all the pixles in the image
        for( int i = 0; i < origonal.length; i++ )
        {
            for( int j = 0; j < origonal[i].length; j++ )
            {
                float newRed = 0.0f;
                float newGreen = 0.0f;
                float newBlue = 0.0f;

                // Loop through all the elements in the convolution filter and find the new color of the current pixle
                for( int k = -1*(filter.length/2); k <= (filter.length/2); k++ )
                {
                    for( int h = -1*(filter[0].length/2); h <= (filter[0].length/2); h++ )
                    {
                        int colPos = j+h;
                        int rowPos = i+k;

                        // Make sure cords are within range, if they arnt use closest to edge
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

                // Make sure the new color values are within the rage we want
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

        return temp;
    }
    
}
