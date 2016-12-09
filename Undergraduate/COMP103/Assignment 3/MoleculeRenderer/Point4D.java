
/**
 * Write a description of class Point4D here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Point4D
{
    double x;
    double y;
    double z;
    double h;
    
    public Point4D(double x, double y, double z, double h)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.h = h;
    }
    
    public double getX() { return x; }
    public double getY() { return y; }
    public double getZ() { return z; }
    public double getH() { return h; }
}
