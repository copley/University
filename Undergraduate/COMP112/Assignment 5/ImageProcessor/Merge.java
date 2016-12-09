
/**
 * Class handles the merging of two images based on some value between 0 and 100
 */
public class Merge
{
    /**
     * Takes two images and merges them, if there is an origonal image, image1 should be it
     * 
     * @returns result of merging the two images in a float[][][]
     */
    public static float[][][] Merge( float[][][] image1, float[][][] image2, double sliderVal )
    {
        double weight = sliderVal/100;

        float[][][] tmp;
        int height = 0;
        int width = 0;

        // Create a temp image
        tmp = new float[image1.length][image1[0].length][3];

        // Loop through all the pixles in the temp image and find what the color of them should be
        for( int i = 0; i < tmp.length; i++ )
        {
            for( int j = 0; j < tmp[i].length; j++ )
            {
                if( i >= image1.length || j >= image1[0].length ) tmp[i][j] = image2[i][j].clone();
                else if( i >= image2.length || j >= image2[0].length ) tmp[i][j] = image1[i][j].clone();
                else
                {
                    tmp[i][j][0] = (float) ((weight*image2[i][j][0]) + ((1-weight)*image1[i][j][0]));
                    tmp[i][j][1] = (float) ((weight*image2[i][j][1]) + ((1-weight)*image1[i][j][1]));
                    tmp[i][j][2] = (float) ((weight*image2[i][j][2]) + ((1-weight)*image1[i][j][2]));
                }
            }
        }

        return tmp;
    }
}
