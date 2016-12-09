import java.awt.*;

/**
 * Write a description of class Atom here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Atom
{
    // instance variables - replace the example below with your own
    private int x;
    private int y;
    private int z;
    private double radius;
    private Color color;

    public Atom(int x, int y, int z, double radius, Color c)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.radius = radius;
        this.color = c;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getZ() { return z; }
    public double getRadius() { return radius; }
    public Color getColor() { return color; }
    
    /**
     * Taken from provided code
     */
    public int further(Atom o, double angle)
    {
        double rad = angle * Math.PI/180;
        double otherDist = o.z * Math.cos(rad) - o.x * Math.sin(rad);
        double thisDist = z * Math.cos(rad) - x * Math.sin(rad);
        return (int)(otherDist-thisDist);
    }

    public void draw(Graphics2D g2d, double panAngle, double tiltAngle, double zoom)
    {
        double panRadian = panAngle * Math.PI / 180;
        double tiltRadian = tiltAngle * Math.PI / 180;
        double left = 0;
        double top = 0;
        double diam =  radius*2;

        // The vertical coordinate on the graphics pane is the y coordinate of the
        // atom
        top = (y*Math.cos(tiltRadian)) + (x)*Math.sin(tiltRadian) - radius;

        // The horizontal coordinate on the graphics pane is given by x, z,
        // and the angle.
        // angle is 0 if we are looking from the front.
        // horiz coordinate = x * cos(radian) + z * sin(radian)
        left = (x* Math.cos(panRadian) + z * Math.sin(panRadian)) - radius;
        
        //double newDiam = 2*( diam - (zoom*diam) );
        //System.out.println(newDiam);
        
        g2d.setColor(color);
        g2d.fillOval((int)(left+400), (int)(top+300), (int)(diam), (int)(diam));
        
        g2d.setColor(Color.BLACK);
        g2d.drawOval((int)(left+400), (int)(top+300), (int)(diam), (int)(diam));
    }
}
