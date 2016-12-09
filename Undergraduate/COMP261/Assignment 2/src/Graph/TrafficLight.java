package Graph;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

import Location.Location;

public class TrafficLight {
	private Point point;
	
	public TrafficLight(double lon, double lat) {
		point = Location.newFromLatLon(lat, lon).asPoint(Location.origin, 1);
	}
	
	public Point getPoint() {
		return point;
	}

	public void draw(Graphics2D g2d) {
		g2d.setColor(Color.BLACK);
		
		g2d.fillRect((int) point.x - 2, (int) point.y - 2, 4, 4);
	}
}
