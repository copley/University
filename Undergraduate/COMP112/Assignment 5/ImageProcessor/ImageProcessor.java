// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP112 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP 102 Assignment 9 
 * Name:
 * Usercode:
 * ID:
 */

import ecs100.*;
import java.util.*;
import java.io.*;
import java.awt.Color;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import javax.swing.JColorChooser;

/** ImageProcessor allows the user to load, display, modify, and save an image in a number of ways.
The program should include
- Load, commit, save. (Core)
- Brightness adjustment (Core)
- Horizontal flip and 90 degree rotation. (Core)
- Merge  (Core)
- Crop&Zoom  (Core)
- Blur (3x3 filter)  (Core)

- Rotate arbitrary angle (Completion)
- Pour (spread-fill)  (Completion)
- General Convolution Filter  (Completion)

- Red-eye detection and removal (Challenge)
- Filter brush (Challenge)
 */
public class ImageProcessor implements UIButtonListener, UIMouseListener,  UISliderListener, UITextFieldListener
{
    public static int XStart = 50;
    public static int YStart = 50;

    private float[][][] image;
    private float[][][] tempImage;
    private float[][][] mergeImage;

    private float[][] brushFilter;
    private ArrayList<double[]> brushPositions;
    private int brushSize = 40;

    private int pixleSize = 1;

    private double currentMergeVal = 50;

    private double mouseX = 0;
    private double mouseY = 0;

    private boolean pouring = false;
    private Color pourColor;

    private boolean cropandzoom = false;
    private double cropStartX = -1;
    private double cropStartY = -1;

    private boolean updating = false;
    private boolean drawing = false;

    private boolean merge = false;

    private float width;
    private float height;

    public ImageProcessor()
    {
        UI.initialise();
        UI.setImmediateRepaint(false);
        UI.setMouseMotionListener(this);

        UI.addButton("Load Image", this);
        UI.addButton("Save Image", this);
        UI.addButton("Discard", this);

        UI.addButton("Commit", this);

        UI.addSlider("Brightness", -100, 100, 0, this);
        UI.addButton("Horizontal Flip", this);
        UI.addButton("Rotate 90 Degrese", this);
        UI.addButton("Merge", this);
        UI.addSlider("Merge Weight", 0, 100, 50, this);
        UI.addButton("Crop And Zoom", this);
        UI.addButton("Blur", this);
        UI.addButton("Pour", this);
        UI.addButton("Change Pour Color", this);
        UI.addButton("Stop Pouring", this);
        UI.addButton("Rotate", this);
        UI.addButton("Apply Convoltuion Matrix", this);
        UI.addButton("Filter Brush", this);
        UI.addButton("Stop Filter Brush", this);
        UI.addButton("Red Eye Reduction", this);

    }

    public void buttonPerformed(String b)
    {
        while(drawing);
        updating = true;

        if( b.equals("Commit") )
        {
            UI.clearText();

            // Stop various minulaptions just incase the stop buttons wernt pressed
            mergeImage = null;
            brushFilter = null;
            pouring = false;

            image = tempImage;
        }
        else if( b.equals("Discard") ) 
        {
            tempImage = image;
            UI.clearText();
        }
        else if( b.equals("Stop Filter Brush") && brushFilter != null )
        {
            brushFilter = null;
        }
        else if( b.equals("Change Pour Color") && pouring )
        {
            if( pouring ) pourColor = JColorChooser.showDialog(null, "Pick A Color", pourColor);
            else UI.println("You must be poring to do this");
        }
        else if( b.equals("Stop Pouring") && pouring )
        {
            pouring = false;
        }
        else if( image != tempImage ) 
        {
            UI.println("Changes have been made. You MUST commit them or\ndiscard them before doing something else");
        }  
        else if(b.equals("Load Image") )
        {
            String filepath = UIFileChooser.open();

            if( filepath != null )
            {
                image = tempImage = loadImage(filepath);

                width = image.length;
                height = image[0].length;
                
                UI.clearText();
            }
        }
        else if( image == null ) UI.println("You must load an image first");
        else if( b.equals("Save Image") )
        {
            if( image != null )
            {
                String saveFile = UIFileChooser.save("Image Save File");

                if( saveFile != null )
                {
                    try
                    {

                        if( !saveFile.contains(".") ) saveFile += ".png";
                        File output = new File(saveFile);
                        String type = saveFile.substring(saveFile.indexOf(".")+1);

                        BufferedImage toSave = new BufferedImage(image[0].length, image.length, BufferedImage.TYPE_INT_RGB);

                        for( int i = 0; i < image.length; i++ )
                        {
                            for( int j = 0; j < image[0].length; j++ )
                            {
                                int red = (int) (image[i][j][0] * 255f);
                                int blue = (int) (image[i][j][1] * 255f);
                                int green = (int) (image[i][j][2] * 255f);

                                Color c = new Color(red, blue, green);

                                toSave.setRGB(j, i, c.getRGB());
                            }
                        }

                        ImageIO.write(toSave, type, output);
                        UI.println("Image has been saved to: " + saveFile);
                    }
                    catch( IOException e ) 
                    {
                        UI.println("Coundent save the image");
                        e.printStackTrace(); 
                    }
                }
            } else UI.println("You must have an image loaded to be able to save it");
        }
        else if( b.equals("Horizontal Flip") )
        {
            tempImage = GeometricTransformation.HorizontalFilp(image);
        }
        else if( b.equals("Rotate 90 Degrese") )
        {
            tempImage = GeometricTransformation.Rotate(image, 90, 0, 0);
        }
        else if( b.equals("Merge") )
        {
            UI.println("Merges the current image with another, on the merge slider\n0 is compleatly this image and 1 is compleatly the other\nimage");

            // Get user to choose file
            String filepath = UIFileChooser.open();

            if( filepath != null )
            {
                mergeImage = loadImage(filepath);

                tempImage = Merge.Merge(image, mergeImage, currentMergeVal);

                merge = true;
            }
            else UI.println("Please enter a file name");
        }
        else if( b.equals("Crop And Zoom") )
        {
            UI.println("Click and drag to create an area to zoom on");
            cropandzoom = true;
        }
        else if( b.equals("Blur") )
        {
            tempImage = Blur.Blur(image);
        }
        else if( b.equals("Pour") )
        {
            UI.println("Replaces the color under where you click and all\nthe ones that are close to that color which\na touching with the new color you select");
            //pouring = true;

            pourColor = JColorChooser.showDialog(null, "Pick A Color", null);
            if( pourColor != null ) pouring = true;

        }
        else if( b.equals("Rotate") )
        {
            UI.println("Rotates the image by an angle that you provide\naround a center point that you also provide\n");
            int angle = UI.askInt("Angle To Rotate By: ");
            UI.println("The center is dfaulty the middle of the image");
            int DX = UI.askInt("Enter ammount to adjust the X center( + = right, - = left ): ");
            int DY = UI.askInt("Enter ammount to adjust the Y center( + = down, - = up ): ");

            tempImage = GeometricTransformation.Rotate(image, angle, DX, DY);
        }
        else if( b.equals("Apply Convoltuion Matrix") )
        {
            String file = UIFileChooser.open();

            if( file != null )
            {
                float[][] convolutionMatrix = ImageProcessor.ParseFilterFile(file);

                if( convolutionMatrix != null ) tempImage = ApplyConvolutionFilter.ApplyConvolutionFilter(image, convolutionMatrix);
            } else UI.println("Please enter a file name");
        }
        else if( b.equals("Filter Brush") )
        {
            UI.println("Paint with the brush over an image to\napply the selected filter to thoughs areas\nNOTE if you release the mouse and then go\nover a place where the filter has allready\nbeen applyed it will intensify the filter\npress the stop filter brush button to stop");

            String file = UIFileChooser.open();

            if( file != null )
            {
                brushFilter = ImageProcessor.ParseFilterFile(file);
                brushPositions = new ArrayList<double[]>();
            } else UI.println("Please enter a file name");
        }
        else if( b.equals("Red Eye Reduction") )
        {
            UI.println("Removes red eyes from the picture\nNOTE: sometimes dosnt work, see comments for detales on why");
            tempImage = RedEyeReduction.RemoveRedeye(image);
        }

        updating = false;
    }

    public void mousePerformed( String t, double x, double y ) 
    {

        if( t.equals("clicked") )
        {
            while(drawing);
            updating = true;
            if( pouring )
            {
                int row = (int) ((x - ImageProcessor.XStart));
                int col = (int) ((y - ImageProcessor.YStart));

                if( ( row >= 0 && row < image[0].length ) && ( col >= 0 && col < image.length ) ) tempImage = Pour.Pour(tempImage, new float[] {pourColor.getRed()/255.0f, pourColor.getGreen()/255.0f, pourColor.getBlue()/255.0f}, row, col);
                else UI.println("The click was outside the image");

            }

            updating = false;
        }
        else if( t.equals("pressed") )
        {
            if( cropandzoom )
            {
                UI.println("[*] Starting crop and zoom");
                cropStartX = x;
                cropStartY = y;

            }
        }
        else if( t.equals("released") )
        {
            if( cropandzoom )
            {
                while(drawing);
                updating = true;

                tempImage = CropAndZoom.CropAndZoom(image, new int[] {(int) cropStartX - ImageProcessor.XStart, (int) cropStartY - ImageProcessor.YStart}, new int[] {(int) x - ImageProcessor.XStart, (int) y - ImageProcessor.YStart});

                cropandzoom = false;

                cropStartX = -1;
                cropStartX = -1;

                updating = false;
            }

            if( brushFilter != null )
            {
                while(drawing);
                updating = true;

                tempImage = FilterBrush.ApplyFilterToArea(tempImage, brushFilter, brushPositions, brushSize);
                brushPositions = new ArrayList<double[]>();

                updating = false;
            }
        }
        else if( t.equals("moved") )
        {
            mouseX = x;
            mouseY = y;
        }
        else if ( t.equals("dragged") )
        {
            mouseX = x;
            mouseY = y;

            if( brushFilter != null )
            {
                int xpos = (int) (x - ImageProcessor.XStart);
                int ypos = (int) (y - ImageProcessor.YStart);

                brushPositions.add(new double[] {xpos, ypos});
            }
        }

    }

    public void sliderPerformed( String s, double val ) 
    {
        while(drawing);
        updating = true;

        if( s.equals("Merge Weight") && mergeImage != null )
        {
            currentMergeVal = val;
            if( mergeImage != null )
            {
                //updating = true;
                //tempImage = ImageTransformation.Merge(image, mergeImage, val);
                tempImage = Merge.Merge(image, mergeImage, val);
                //updating = false;
            }
            //draw();
        }
        else if( image != tempImage ) 
        {
            UI.println("Changes have been made. You MUST commit them or discard them before doing something else");
            return;
        }
        else if( s.equals("Brightness") )
        {
            //tempImage = ImageTransformation.BrightnessTransformation(image,val);
            tempImage = BrightnessTransformation.BrightnessTransformation(image, val);
            //draw();
        }

        updating = false;
    }

    public void textFieldPerformed( String t, String r ) {}

    private float[][][] loadImage( String filepath )
    {
        float[][][] temp = null;

        try
        {
            BufferedImage img = ImageIO.read( new File(filepath));
            //image = new float[img.getHeight()][img.getWidth()][3];
            //tempImage = new float[img.getHeight()][img.getWidth()][3];
            temp = new float[img.getHeight()][img.getWidth()][3];

            for( int i = 0; i < temp.length; i++ )
            {
                for( int j = 0; j < temp[i].length; j++ )
                {
                    // Read RGB from buffered image and insert it into the image array
                    int rgb = img.getRGB(j, i);

                    //image[i][j][0] = ((rgb>>16) & 255)/255.0f;
                    //image[i][j][1] = ((rgb>>8) & 255)/255.0f;
                    //image[i][j][2] = (rgb & 255)/255.0f;

                    //tempImage[i][j][0] = ((rgb>>16) & 255)/255.0f;
                    //tempImage[i][j][1] = ((rgb>>8) & 255)/255.0f;
                    //tempImage[i][j][2] = (rgb & 255)/255.0f;

                    temp[i][j][0] = ((rgb>>16) & 255)/255.0f;
                    temp[i][j][1] = ((rgb>>8) & 255)/255.0f;
                    temp[i][j][2] = (rgb & 255)/255.0f;
                }
            }

            //height = img.getHeight();
            //width = img.getWidth();
        }
        catch ( IOException e )
        {
            UI.println("Failed to load image");
        }

        return temp;

        //draw();
    }

    public void draw()
    {
        while( true )
        {
            if( image != null && tempImage != null )
            {
                while(updating);// UI.sleep(10);
                drawing = true;

                UI.clearGraphics();

                //UI.drawRect(mouseX-10, mouseY-10, 20, 20);
                // Draw temp image
                for( int i = 0; i < tempImage.length; i++ )
                {
                    for( int j = 0; j < tempImage[i].length; j++ )
                    {
                        UI.setColor(new Color((int)(tempImage[i][j][0]*255), (int)(tempImage[i][j][1]*255), (int)(tempImage[i][j][2]*255)));
                        UI.fillRect((XStart+(j*pixleSize)), (YStart+(i*pixleSize)), pixleSize, pixleSize);
                    }
                }

                // Draw origonal iamge
                for( int i = 0; i < image.length; i++ )
                {
                    for( int j = 0; j < image[i].length; j++ )
                    {
                        UI.setColor(new Color((int)(image[i][j][0]*255), (int)(image[i][j][1]*255), (int)(image[i][j][2]*255)));
                        UI.fillRect((XStart+(image[0].length*pixleSize)+(j*pixleSize) + 10), (YStart+(i*pixleSize)), pixleSize, pixleSize);

                    }
                }

                if( cropandzoom && ( cropStartX != -1 && cropStartY != -1 ) ) 
                {
                    UI.setColor(Color.RED);
                    UI.drawPolygon(new double[] {cropStartX, mouseX, mouseX, cropStartX}, new double[] {cropStartY, cropStartY, mouseY, mouseY}, 4);
                }

                if( brushFilter != null )
                {
                    UI.setColor(Color.RED);
                    UI.drawRect(mouseX-(brushSize/2), mouseY-(brushSize/2), 40, 40);
                }

                UI.repaintGraphics();

                drawing = false;

                UI.sleep(20);
            }
            else UI.sleep(300);
        }
    }

    public static float[][] ParseFilterFile(String fp)
    {
        StringBuilder b = new StringBuilder();
        try
        {
            BufferedReader in = new BufferedReader(new FileReader(new File(fp)));

            String line = in.readLine();
            while( line != null )
            {
                b.append(line + "\n");
                line = in.readLine();
            }

        }
        catch( IOException e ) {}

        try
        {
            String fileText = b.toString();
            String[] fileTextArray = fileText.split("\n");
            float[][] temp = new float[fileTextArray.length][0];

            for( int i = 0; i < fileTextArray.length; i++ )
            {
                String[] row = fileTextArray[i].split(" ");
                temp[i] = new float[row.length];
                for( int j = 0; j < row.length; j++ )
                {
                    temp[i][j] = Float.parseFloat(row[j]);
                }
            }

            return temp;
        }
        catch( Exception e ) { UI.println("There was a an error when reading the the filter file"); return null; }

    }

    public static void main( String[] args )
    {
        ImageProcessor ip = new ImageProcessor();
        ip.draw();
    }
}
