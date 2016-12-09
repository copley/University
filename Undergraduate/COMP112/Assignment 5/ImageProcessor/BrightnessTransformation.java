
import java.awt.Color;

/**
 * Handles applying a brightness transformation to an image. I used HLS here because its a better color scheme for adjusting the brightness
 * There is a slight problem here cant quite figure out where it is, it adjusts the brightness but it makes some of the pixles black.
 */
public class BrightnessTransformation
{
    /**
     * Takes an image as input and adjusts the brightness based on the value passed as ammount, which should be between -100 and 100 (inclusive)
     */
    public static float[][][] BrightnessTransformation( float[][][] origonal, double ammount )
    {
        float[][][] temp = new float[origonal.length][origonal[0].length][3];

        for( int i = 0; i < origonal.length; i++ )
        {
            for( int j = 0; j < origonal[i].length; j++ )
            {
                float[] hsl = RGBtoHSL(origonal[i][j]);
                
                float normSliderVal = (float) ammount/100;
                
                if( normSliderVal < 0 ) hsl[2] = hsl[2] + ( ( hsl[2] ) * normSliderVal );
                else if( normSliderVal > 0 ) hsl[2] = hsl[2] + ( ( 1 - hsl[2] ) * normSliderVal );
                
                float[] rgb = HSLtoRGB(hsl);
                
                if( rgb[0] > 1 ) rgb[0] = 1;
                else if( rgb[0] < 0 ) rgb[0] = 0;
                
                if( rgb[1] > 1 ) rgb[1] = 1;
                else if( rgb[1] < 0 ) rgb[1] = 0;
                
                if( rgb[2] > 1 ) rgb[2] = 1;
                else if( rgb[2] < 0 ) rgb[2] = 0;
                
                temp[i][j] = rgb;
            }
        }

        return temp;
    }

    /**
     * Converts RGB to HSL and returns it
     */
    private static float[] RGBtoHSL( float[] rgb )
    {
        float max = 0;
        float min = 0;

        if( rgb[0] > rgb[1] && rgb[0] > rgb[2] ) 
        {
            max = rgb[0];

            if( rgb[1] < rgb[2] ) min = rgb[1];
            else min = rgb[2];
        }
        else if( rgb[1] > rgb[0] && rgb[1] > rgb[2] )
        {
            max = rgb[1];

            if( rgb[0] < rgb[2] ) min = rgb[0];
            else min = rgb[2];
        }
        else if( rgb[2] > rgb[0] && rgb[2] > rgb[1] )
        {
            max = rgb[2];

            if( rgb[0] < rgb[1] ) min = rgb[0];
            else min = rgb[1];
        }

        float c = max - min;

        float h = 0;
        float s = 0;
        float l = 0;

        if( max == rgb[0] ) h = ((rgb[1] - rgb[2])/c) % 6;
        else if( max == rgb[1] ) h = (( rgb[2] - rgb[0] ) / c ) + 2;
        else if( max == rgb[2] ) h = (( rgb[0] - rgb[1] ) / c ) + 4;

        l = 0.5f * (max + min);

        if( c == 0 ) s = 0;
        else s = c/(1 - Math.abs((2*l) - 1));

        return new float[] {h, s, l};

    }
    
    /**
     * Converts HSL to RGB and returns it
     */
    private static float[] HSLtoRGB( float[] hsl )
    {
        float c = ( 1 - Math.abs((2 * hsl[2]) - 1)) * hsl[1];
        float x = c * ( 1 - Math.abs((hsl[0] % 2) - 1));

        float r = 0;
        float g = 0;
        float b = 0;

        if( hsl[0] >= 0 && hsl[0] < 1 ) { r = c; g = x; b = 0; }
        if( hsl[0] >= 1 && hsl[0] < 2 ) { r = x; g = c; b = 0; }
        if( hsl[0] >= 2 && hsl[0] < 3 ) { r = 0; g = c; b = x; }
        if( hsl[0] >= 3 && hsl[0] < 4 ) { r = 0; g = x; b = c; }
        if( hsl[0] >= 4 && hsl[0] < 5 ) { r = x; g = 0; b = c; }
        if( hsl[0] >= 5 && hsl[0] < 6 ) { r = c; g = 0; b = x; }
        
        float m = hsl[2] - (0.5f * c);
        
        return new float[] {r+m, g+m, b+m};
    }
}
