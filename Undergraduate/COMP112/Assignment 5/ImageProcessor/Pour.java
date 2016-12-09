
import java.util.*;

/**
 * Class will replace the starting color ( at (x,y)) and any colors touching it within a certian threshold with the new color.
 * Uses while loop recursion because i thought it would work better on larger images, how ever if the image is to big you will
 * probberly get "out of memory" errors
 */
public class Pour
{
    /**
     * Takes the origonal image, the color to replace the old ones with and the x,y coord of the starting position.
     * Returns a temp image containing the moderfyed image.
     */
    public static float[][][] Pour( float[][][] origonal, float[] newColor, int x, int y )
    {
        float[][][] temp = new float[origonal.length][origonal[0].length][3];
        ArrayList<int[]> stack = new ArrayList<int[]>();
        float[] startingColor = origonal[y][x].clone();
        float threshold = 0.09f;

        // Copy origonal image into temp image
        for( int i = 0; i < origonal.length; i++ )
        {
            for( int j = 0; j < origonal[i].length; j++ )
            {
                temp[i][j] = origonal[i][j].clone();
            }
        }

        // Add the starting point to the stack
        stack.add(new int[] {y, x});

        while( stack.size() > 0 )
        {
            // Get the next point in the stack and get the color of it
            int[] currentIndex = stack.remove(0);
            float[] currentColor = temp[currentIndex[0]][currentIndex[1]];

            int count = 0;
            // Check to see if current pos color and new color are the same
            for( int i = 0; i < currentColor.length; i++ )
            {
                if( Math.abs(currentColor[i] - startingColor[i]) <= threshold ) count++;
            }
            // If they arnt then we can just move on
            if( count != 3 ) continue;

            // Set the color of the current position to the new color
            temp[currentIndex[0]][currentIndex[1]] = newColor.clone();

            // Add the suronding pixles to the stack
            if( currentIndex[0]-1 > 0 ) stack.add(new int[] {currentIndex[0]-1, currentIndex[1]});
            if( currentIndex[0]+1 < origonal.length ) stack.add(new int[] {currentIndex[0]+1, currentIndex[1]});
            if( currentIndex[1]-1 > 0 ) stack.add(new int[] {currentIndex[0], currentIndex[1]-1});
            if( currentIndex[1]+1 < origonal[0].length ) stack.add(new int[] {currentIndex[0], currentIndex[1]+1});
        }


        return temp;
    }
}
