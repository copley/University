// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP112 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP102 Assignment 5
 * Name:
 * Usercode:
 * ID:
 */

import ecs100.*;
import java.util.*;
import java.io.*;
import java.awt.Color;

/** Renders pnm images (pbm, pgm, or ppm) onto the graphics panel
ppm images are the simplest possible colour image format.
 */

public class ImageRenderer implements UIButtonListener
{

    public static final        int top = 20;   // top edge of the image
    public static final int left = 20;  // left edge of the image
    public static final        int pixelSize = 2;  

    public Image[] currentImage;
    public String currentPath;
    boolean imageChanged;

    public ImageRenderer()
    {
        UI.initialise();
        UI.addButton("Render", this);
        UI.addButton("Compress", this);
        UI.addButton("Quit", this);

    }

    /**
     * This function draws the images, should constantly be running so if there are multiple images it will draw them in a loop
     */
    public void draw()
    {
        while( true )
        {
            if( currentImage != null )
            {
                // Catch any exceptions that may occor if i change image while drawing
                try
                {
                    for( int i = 0; i < currentImage.length; i++ )
                    {
                        if( currentImage != null ) currentImage[i].draw();
                        
                        // Sleep 50 so if its an animated image you can actually see the diffrent images
                        UI.sleep(50);
                    }
                }
                catch( Exception e ) {}
            }
            else
            {
                UI.clearGraphics(true);
            }

        }
    }

    /**
     * Handles the button events
     */
    public void buttonPerformed(String b)
    {
        if (b.equals("Render"))
        {
            // Prompt user to select the file
            String fname = UIFileChooser.open("Image file to render");
            
            if (fname != null && ( fname.endsWith(".ppm") || fname.endsWith(".pgm") || fname.endsWith(".pbm") || fname.endsWith(".rle") ))
            {
                // Clear text pain, and clear the graphics pain for the new image
                UI.clearText();
                UI.clearGraphics();
                
                // Render the new image
                this.renderImage(fname);
            }
            else if( fname != null )
            {
                UI.println("The file you selected isnt reconised as a image file this program can handle");
                fname = null;
            }

        }
        else if(b.equals("Compress") )
        {
            if( currentPath != null )
            {
                // Alert the user if they are trying to compress an allready compressd image
                if( currentPath.endsWith(".rle") ) UI.println("WARNING: The file you are trying to compress has allready\nbeen compressed, you are just resaving the file");

                String filepath = UIFileChooser.save("Enter Compressed Image Name");

                if( filepath != null )
                {
                    if( filepath.indexOf(".") == -1 ) filepath += ".rle";

                    StringBuilder compressed = new StringBuilder();

                    for( int i = 0; i < currentImage.length; i++ )
                    {
                        compressed.append(currentImage[i].getCompressedImage());
                    }

                    try
                    {
                        BufferedWriter w = new BufferedWriter( new FileWriter(filepath));
                        w.append(compressed.toString());
                        w.close();

                        UI.println("Compressed Image(s) written to file");

                        // Get the size of the files
                        long origonalSize = new File(currentPath).length();
                        long compressedSize = new File(filepath).length();

                        UI.println(String.format("\nOrigonal File Size: %d bytes", origonalSize));
                        UI.println(String.format("Compressed File Size: %d bytes", compressedSize));

                        if( (origonalSize-compressedSize) >= 0 )
                        {
                            UI.println(String.format("Reduction In Size: %d bytes ( %d%s )", (origonalSize-compressedSize), (int) (( 1 - ((double) compressedSize/origonalSize)) * 100), "%" ));
                        }
                        else UI.println(String.format("Groth In Size: %d bytes", -1*(origonalSize-compressedSize) ));
                    }
                    catch( IOException e )
                    {
                        UI.println("There was an error when writing the file");
                    }
                }

            }
        }
        else if (b.equals("Quit"))
        {
            UI.quit();
        }
    }

    /**
     * Renders a pnm image file.
     * Asks for the name of the file, then renders the image at position (left, top).
     * Each pixel of the image is rendered by a square of size pixelSize
     * The first three tokens (other than comments) are
     *    the magic number (P1, P2, or P3),
     *    number of columns, (integer)
     *    number of rows,  (integer)
     * ppm and pgm files then have 
     *    colour depth  (integer: range of possible color values)
     * The remaining tokens are the pixel values
     *  (0 or 1 for pbm, single integer for pgm; red, green, and blue integers for ppm)
     * There may be comments anywhere in the file, which start with # and go to the end of the line. Comments should be ignored.
     * The image may be "animated", in which case the file contains a sequence of images
     * (ideally, but not necessarily, the same type and size), which should be rendered
     * in sequence.
     * This method should read the magic number then call the appropriate method for rendering the rest of the image
     */                                
    public void renderImage(String fname)
    {
        currentPath = fname;
        currentImage = readFile(fname);
    }

    /**
     * Function reads in an image file and returns an array of images
     */
    private Image[] readFile(String fname)
    {
        UI.println("Loading File: " + fname + "\n");

        Image[] img;
        String content = "";
        // Get wether the file at the file path is compressed
        boolean compressed = ( fname.indexOf(".rle") != -1 );

        try
        {
            BufferedReader in = new BufferedReader( new FileReader(fname));
            StringBuilder b = new StringBuilder();

            String line;
            while( ( line = in.readLine() ) != null )
            {
                String parsed = removeComment(line);

                if( !parsed.equals("") )
                {
                    b.append(parsed + "\n");
                }
            }

            content = b.toString().trim();

            in.close();
        }
        catch( IOException e )
        {
            UI.println("There was an error when reading the file");
            return null;
        }

        // Split into the diffrent images
        String[] imgs = content.split("P");
        img = new Image[imgs.length-1];

        for( int i = 1; i < imgs.length; i++ )
        {
            String[] lines = imgs[i].trim().split("\n");

            String type = "";
            String[] heightWidth = null;
            String colorDepth = "";
            String data = "";
            int startOffset = 2;

            // We know first line will be type
            type = lines[0];

            // We know that second line is height and width
            heightWidth = lines[1].split(" ");

            // We know third line is color depth and we know that if image is
            // black and white it wont have it
            if( !type.equals("1") )
            {
                colorDepth = lines[2];
                startOffset = 3;
            }

            // Read the rest of the data
            StringBuilder d = new StringBuilder();
            for( int j = startOffset; j < lines.length; j++ )
            {
                d.append(lines[j].trim() + " ");
                //data += lines[j].trim() + " ";
            }

            data = d.toString();

            // Return the correct type of image
            switch( type )
            {
                case "1": 
                UI.println("Image Type: Black And White");
                img[i-1] =  new PBMImage(Integer.parseInt(heightWidth[1]), Integer.parseInt(heightWidth[0]), 0, compressed, data);
                break;
                case "2": 
                UI.println("Image Type: Greyscale");
                img[i-1] = new PGMImage(Integer.parseInt(heightWidth[1]), Integer.parseInt(heightWidth[0]), Integer.parseInt(colorDepth), compressed, data);
                break;
                case "3": 
                UI.println("Image Type: Color");
                img[i-1] = new PPMImage(Integer.parseInt(heightWidth[1]), Integer.parseInt(heightWidth[0]), Integer.parseInt(colorDepth), compressed, data);
                break;
            }
        }

        UI.println("\nTo compress the currant image press the compress button");
        
        return img;
    }

    /**
     * Function takes a string and removes any comments from it 
     */
    private String removeComment(String s)
    {
        // Added the replaceAll at the end so if the person or program that made the image used more than 
        // one space and a number seperator it would change them into singluar spaces
        // Found how to use it here: http://stackoverflow.com/questions/2932392/java-how-to-replace-2-or-more-spaces-with-single-space-in-string-and-delete-lead
        if( !s.equals("") && !s.equals("#") ) 
        {
            return s.split("#")[0].trim().replaceAll(" +", " ");
        }

        return "";
    }

    public static void main(String[] args)
    {
        UI.setImmediateRepaint(false);
        ImageRenderer im = new ImageRenderer();
        im.renderImage("image-bee.ppm");   // this is useful for testing.
        im.draw();
    }
} 

