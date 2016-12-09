
/**
 * Write a description of class Point3D here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Point3D
{
    // instance variables - replace the example below with your own
    private double x;
    private double y;
    private double z;
    
    public Point3D(double x, double y, double z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public Point3D()
    {
        x = 0;
        y = 0;
        z = 0;
    }
    
    public double getX() { return x; }
    public double getY() { return y; }
    public double getZ() { return z; }
}
