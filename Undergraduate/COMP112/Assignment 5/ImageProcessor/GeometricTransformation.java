
/**
 * Handles diffrent geometric transformations
 */
public class GeometricTransformation
{
    /**
     * Takes an image and flips it horizonally
     */
    public static float[][][] HorizontalFilp( float[][][] origonal )
    {
        float[][][] temp = new float[origonal.length][origonal[0].length][3];

        for( int i = 0; i < origonal.length; i++ )
        {
            for( int j = 0; j < origonal[i].length; j++ )
            {
                temp[i][(origonal[i].length-1)-j] = origonal[i][j].clone();
            }
        }

        return temp;
    }
    
    /**
     * Takes an image and rotates it by the specified angle, by default the center of the rotation is the middle of the image so that can be changed by passing non zero values
     * for DX and DY
     */
    public static float[][][] Rotate( float[][][] origonal, double angle, int DX, int DY )
    {
        int centerX = (int) (origonal[0].length/2) + DX;
        int centerY = (int) (origonal.length/2) + DY;
        float[][][] temp = new float[origonal.length][origonal[0].length][3];

        for( int i = 0; i < origonal.length; i++ )
        {
            for( int j = 0; j < origonal[i].length; j++ )
            {
                // Calculate the x and y of the color at this position
                int x = (int) ( centerX + ( j - centerX ) * Math.cos((-1*angle)*(Math.PI/180)) - ( i - centerY ) * Math.sin((-1*angle)*(Math.PI/180) ) );
                int y = (int) ( centerY + ( j - centerX ) * Math.sin((-1*angle)*(Math.PI/180)) + ( i - centerY ) * Math.cos((-1*angle)*(Math.PI/180) ) );

                // Make sure the x and y components are within range, if they are then copy color into new array
                if( ( x >= 0 && x < origonal[0].length ) && ( y >= 0 && y < origonal.length ) )
                {
                    temp[i][j] = origonal[y][x].clone();
                }
                else temp[i][j] = new float[] {1,1,1};
            }
        }

        return temp;
    }
}
