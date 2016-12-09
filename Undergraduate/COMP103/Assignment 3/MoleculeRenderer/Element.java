import java.awt.Color;

/**
 * Write a description of class Element here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Element
{
    private String name;
    private double radius;
    private Color color;
    
    public Element(String name, double radius, Color color)
    {
        this.name = name;
        this.radius = radius;
        this.color = color;
    }
    
    public Color getColor() { return color; }
    public double getRadius() { return radius; }
    public String getName() { return name; }
}
