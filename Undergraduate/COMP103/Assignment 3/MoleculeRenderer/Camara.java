
/**
 * Write a description of class Camara here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Camara
{
    private Point3D from;
    private Point3D to;
    private Point3D up;
    private double angleh;
    private double anglev;
    private double zoom;
    private double front;
    private double back;
    private short projection;
    
    public Camara()
    {
        from = new Point3D(0, -50, 0);
        to = new Point3D(0, 50, 0);
        up = new Point3D(0, 0, 1);
        angleh = 45;
        anglev = 45;
        zoom = 1;
        front = 1;
        back = 200;
        projection = 0;
    }
    
    public Point3D getFrom() { return from; }
}
