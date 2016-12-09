import java.awt.*;

/**
 * http://www.codeincodeblock.com/2012/03/projecting-3d-world-co-ordinates-into.html
 */
public class Projection
{
    private Point3D origin;
    private Camara camara;
    private Screen screen;
    
    public Projection()
    {
        camara = new Camara();
        screen = new Screen();
        origin = new Point3D();
    }
    
    public void drawCircle(Graphics2D g2d, Point3D p, Color c, double radius)
    {
        Point4D h = Point3Dto4D(p);
        g2d.setColor(c);
        g2d.fillOval( (int)(h.getX()/h.getH()), (int)(h.getY()/h.getH()), (int)radius, (int)radius);
    }
    
    private Point4D Point3Dto4D(Point3D p)
    {
        Point4D newPoint = new Point4D(p.getX(), p.getY(), 0, 1 + (p.getZ()/camara.getFrom().getZ()));
        return newPoint;
    }
}
