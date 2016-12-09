import java.awt.Color;
import ecs100.*;

/**
 * Super class for all the diffrent types of images that can be drawn. Made it
 * abstract because people shouldent be initilising this class
 */
public abstract class Image
{
    // Protected class variables
    protected Color[][] colors;
    protected double colorScale;

    // Meta variables
    private String type;
    private int colorDepth;
    protected int height;
    protected int width;

    public Image(String t, int height, int width, int colorDepth, boolean compressed, String data)
    {
        this.type = t;
        this.height = height;
        this.width = width;
        this.colorDepth = colorDepth;

        // Determine the color scale
        this.colorScale = (255.0/colorDepth);

        // Initilise the colors array
        this.colors = new Color[height][width];

        // Print the information out
        UI.println("Image Data:");
        UI.println("  Compressed: " + compressed);
        UI.println("  Dimentions: " + this.width + " x " + this.height);
        UI.println("  Color Depth: " + this.colorDepth + "( Scaling Factor: " + this.colorScale + " )" );
        UI.println("--------");

        // Call the function to parse the image data
        if( !compressed ) parseImageData(data);
        else parseCompressedImageData(data);
    }

    /**
     * Function that each class extending this one must implement. It handles the parsing of non compressed
     * image data
     */
    protected abstract void parseImageData(String data);

    /**
     * Parses compressed image data. This can be a generic function because 
     */
    private void parseCompressedImageData(String data)
    {
        String[] c = data.split(" ");

        int a = 0;
        int b = 0;
        for( int i = 0; i < c.length; i++ )
        {
            String[] components = c[i].split(",");

            int count = Integer.parseInt(components[0]);
            int color = Integer.parseInt(components[1]);

            for( int k = 0; k < count; k++ )
            {
                colors[a][b] = new Color(color);
                b++;

                // Make sure the indices are within range
                if( b == colors[a].length )
                {
                    b = 0;
                    a++;
                }
            }
        }
    }

    /**
     * Returns the image compressed using run length encoding. the image
     * metadata is included.
     * 
     * Got information on Run Length Encoding from
     * http://en.wikipedia.org/wiki/Run-length_encoding
     */
    public String getCompressedImage()
    {
        UI.println("Compressing Image");
        StringBuilder b = new StringBuilder();

        // Append metadata to the string builder. Type, [width,height], color depth
        b.append(type + "\n");
        b.append(String.format("%d %d\n", width, height));
        b.append(colorDepth + "\n");

        // Append the encoded data, use the single integer representation of the RGB color because it simpler and it
        // will probberly use up less bytes, also this way it works for any any type of image as oposed to just black
        // and white images
        int old = colors[0][0].getRGB();
        int count = 0;
        for( int i = 0; i < colors.length; i++ )
        {
            for( int j = 0; j < colors[i].length; j++ )
            {
                int current = colors[i][j].getRGB();

                if( current == old )
                {
                    count++;
                }
                else
                {
                    b.append(String.format(" %d,%d", count, old));

                    old = current;
                    count = 1;
                }
            }
        }

        return b.toString();
    }

    /**
     * Function draws the image to the screen
     */
    public void draw()
    {
        UI.clearGraphics();

        int x = ImageRenderer.left;
        int y = ImageRenderer.top;

        for( int i = 0; i < colors.length; i++ )
        {
            for( int j = 0; j < colors[i].length; j++ )
            {
                UI.setColor(colors[i][j]);
                UI.fillRect(x, y, ImageRenderer.pixelSize, ImageRenderer.pixelSize);

                x += ImageRenderer.pixelSize;

            }

            x = ImageRenderer.left;
            y += ImageRenderer.pixelSize;

        }

        UI.repaintGraphics();
    }

}
