package QuadTree;

import java.awt.Point;
import java.awt.Rectangle;

public class Circle {
	private Point center;
	private int radius;
	
	public Circle(Point c, int r) {
		center = c;
		radius = r;
	}

	public boolean intersects(Point p) {
		return Math.pow(center.x - p.x, 2) + Math.pow(center.y - p.y, 2) <= Math.pow(radius, 2);
	}
	
	public boolean intersects(Rectangle r) {
		return r.intersects(new Rectangle(center.x-radius, center.y-radius, radius * 2, radius * 2));
	}
}
