import ecs100.*;
import java.awt.Color;
import java.util.*;

/**
 * The class that handles red eye reduction. NOTE that this dosnt work on all images, it works
 * on some but have some problems with this method. As far as i can tell the problem is with how i
 * identify what pixles are red eye pixles and which ones arnt, the rest of this algorythm would work
 * if i could do that better.
 */
public class RedEyeReduction
{
    private static final float THRESHOLD  = 0.7f;

    /**
     * Takes an image as input and removes any red eyes from it.
     * 
     * NOTE: Sometimes there is some error with this, for example it will
     * think there are red eyes where there are none if the image has a
     * high ammount of red. How ever i tried to design it in a way that wouldent
     * be effected by an image having more red it in.
     */
    public static float[][][] RemoveRedeye(float[][][] image)
    {
        float[][][] sharpenedImage = SharpenImage(image);
        float[][] binaryImage = ToBinaryMask(sharpenedImage);
        ArrayList<ArrayList<int[]>> groups = CollectGroups(binaryImage);
        ApplyGroups(groups, binaryImage);

        // Create temp image and copy the origonal image over, while applying red eye reduction to the pixles that require it
        float[][][] newImage = new float[image.length][image[0].length][3];
        for( int i = 0; i < image.length; i++ )
        {
            for( int j = 0; j < image[0].length; j++ )
            {
                if( binaryImage[i][j] == 1 )
                {
                    newImage[i][j][0] = (image[i][j][1] + image[i][j][2])/2;
                    newImage[i][j][1] = image[i][j][1];
                    newImage[i][j][2] = image[i][j][2];
                }
                else
                {
                    newImage[i][j][0] = image[i][j][0];
                    newImage[i][j][1] = image[i][j][1];
                    newImage[i][j][2] = image[i][j][2];
                }
            }
        }

        return newImage;
    }

    /**
     * Takes an image and applys a sharpering filter to it
     */
    private static float[][][] SharpenImage( float[][][] image )
    {
        float[][] filter = {{0.006f, 0.011f, 0.022f, 0.011f, 0.006f},
                {0.011f, 0.05f, 0.1f, 0.05f, 0.011f},
                {0.022f, 0.1f, 0.2f, 0.01f, 0.022f},
                {0.011f, 0.05f, 0.1f, 0.05f, 0.011f},
                {0.006f, 0.011f, 0.022f, 0.011f, 0.006f}};

        return ApplyConvolutionFilter.ApplyConvolutionFilter(image, filter);
    }

    /**
     * Creates a binary mask from the image ie where there is a red eye pixle
     * in the image, at the same position in the binary mask there will be a 1
     */
    private static float[][] ToBinaryMask(float[][][] image)
    {
        float[][] binaryImage = new float[image.length][image[0].length];

        float bestIntensity = 0;
        float minIntensity = 100;
        float aveIntensity = 0;

        // Find redest pixles
        for( int i = 0; i < image.length; i++ )
        {
            for( int j = 0; j < image[i].length; j++ )
            {
                
                float intensity = (image[i][j][0] / ((image[i][j][1]+image[i][j][2])/2));

                // Test to see if intenstiy is NaN
                if( intensity != intensity ) intensity = 0;

                aveIntensity += intensity;
                if( intensity > bestIntensity )
                {
                    bestIntensity = intensity;
                    
                }
                else if( intensity < minIntensity ) minIntensity = intensity;
                
                
            }
        }

        aveIntensity = aveIntensity/(image.length*image[0].length);

        float threshold = bestIntensity/(minIntensity+aveIntensity);

        for( int i = 0; i < image.length; i++ )
        {
            for( int j = 0; j < image[i].length; j++ )
            {
                float intensityOfRed = (image[i][j][0]/ ((image[i][j][1]+image[i][j][2])/2));

                if( intensityOfRed >= bestIntensity-threshold && intensityOfRed <= bestIntensity+threshold )  binaryImage[i][j] = 1;
            }
        }

        return binaryImage;
    }

    /**
     * Finds all the groups of the red eye pixles in the image and returns them
     * in an array list
     */
    private static ArrayList<ArrayList<int[]>> CollectGroups(float[][] binaryMask)
    {
        // Get the groups of pixles
        ArrayList<ArrayList<int[]>> groups = new ArrayList<ArrayList<int[]>>();

        for( int i = 0; i < binaryMask.length; i++ )
        {
            for( int j = 0; j < binaryMask[i].length; j++ )
            {
                if( binaryMask[i][j] == 1 )
                {
                    ArrayList<int[]> g = new ArrayList<int[]>();
                    ArrayList<int[]> stack = new ArrayList<int[]>();
                    stack.add(new int[] {i,j});

                    // Use while loop recursion to find all the elements in the binarymask array that are 1 and are touching the starting one
                    while( stack.size() > 0 )
                    {
                        int[] current = stack.remove(0);

                        if( binaryMask[current[0]][current[1]] == 1 )
                        {
                            g.add(current);

                            binaryMask[current[0]][current[1]] = 0;

                            // Check up
                            if( current[0]-1 > 0 ) stack.add(new int[] {current[0]-1, current[1]});
                            // Check down
                            if( current[0]+1 < binaryMask.length ) stack.add(new int[] {current[0]+1, current[1]});
                            // Check left
                            if( current[1]-1 > 0 ) stack.add(new int[] {current[0], current[1]-1});
                            // Check right
                            if( current[1]+1 < binaryMask[1].length ) stack.add(new int[] {current[0], current[1]+1});
                        }

                    }

                    // Make sure the group is over a sertian size before adding it
                    if( g.size() > 30 ) groups.add(g);
                }
            }
        }

        return groups;
    }

    /**
     * Takes an arraylist of the groups and applys this to the binary mask if
     * the groups are found to be in a circle
     */
    private static void ApplyGroups( ArrayList<ArrayList<int[]>> groups, float[][] binaryMask )
    {
        for( int k = 0; k < groups.size(); k++ )
        {
            ArrayList<int[]> g = groups.get(k);

            // Get information about the points, determine if its a circle
            float maxX = 0;
            float minX = binaryMask[0].length;
            float width = 0;

            float maxY = 0;
            float minY = binaryMask.length;
            float height = 0;

            float centerX = 0;
            float centerY = 0;

            // Collect information about the points that we can turn into a circle of best fit
            for( int j = 0; j < g.size(); j++ )
            {
                int[] point = g.get(j);

                centerX += point[1];
                centerY += point[0];

                if( point[0] > maxY ) maxY = point[0];
                else if( point[0] < minY ) minY = point[0];

                if( point[1] > maxX ) maxX = point[1];
                else if( point[1] < minX ) minX = point[1];
            }

            centerX = centerX/g.size();
            centerY = centerY/g.size();

            width = maxX - minX;
            height = maxY - minY;

            // Calculate the error of the circle. We want 50% or more of the points in the circle to be in
            // the list g. 

            // If we assume that the circle surounds all of them, then first we need to find the
            // total number of points contiained inside the circle.
            // We are taking the radius of this circle to be (width+height)/4

            double radius = (width+height)/4;
            double pointCount = 0;

            for( int i = (int)minY; i <= maxY; i++ )
            {
                for( int j = (int)minX; j <= maxX; j++ )
                {
                    double distance = Math.sqrt(Math.pow((i-centerY), 2) + Math.pow((j-centerX), 2));

                    // For the point to be in the circle the distance has to be less than or equal to the radius
                    if( distance <= radius ) pointCount++;
                }
            }
            
            // Calculate the error, the number of points in the group devided by the total number of points in the estamated circle
            // will give 1 if the cirvle is perfect and 0 is worst case
            double error = g.size()/pointCount;

            // I chose 65% as the threshold for a good error rate because 50% ment you could have a semi circle that would quallify as a circle
            if( error >= 0.65 )
            {
                // Add 20% of radius to the radius
                radius += (radius*0.2);
                for( int i = (int)minY; i <= maxY; i++ )
                {
                    for( int j = (int)minX; j <= maxX; j++ )
                    {
                        double distance = Math.sqrt(Math.pow((i-centerY), 2) + Math.pow((j-centerX), 2));

                        // For the point to be in the circle the distance has to be less than or equal to the radius
                        if( distance <= radius ) binaryMask[i][j] = 1;
                    }
                }
            }
        }
    }
}
