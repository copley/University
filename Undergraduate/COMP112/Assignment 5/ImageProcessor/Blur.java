
/**
 * Class handles applying a blur to an image
 */
public class Blur
{
    /**
     * Takes an image and applys a simple blur to it 
     */
    public static float[][][] Blur( float[][][] origonal )
    {
        float[][] blur = new float[][] {{0.05f, 0.1f, 0.05f},
                {0.1f, 0.4f, 0.1f},
                {0.05f, 0.1f, 0.05f}};

        // Just apply the blur by using apply convolution filter
        return ApplyConvolutionFilter.ApplyConvolutionFilter(origonal, blur);
    }
}
