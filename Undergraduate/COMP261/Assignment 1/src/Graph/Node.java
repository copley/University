package Graph;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

import Location.Location;

public class Node {
	private final int id; 
	//private Location location;
	private Point location;
	private double latitude;
	private double longitude;
	private ArrayList<Segment> RoadSegments;
	
	public Node(int id, double lat, double lon) {
		this.id = id;
		this.location = Location.newFromLatLon(lat, lon).asPoint(Location.origin, 1);
		this.latitude = lat;
		this.longitude = lon;
		
		RoadSegments = new ArrayList<Segment>();
	}
	
	public void linkSegment(Segment s) {
		RoadSegments.add(s);
	}
	
	public int getID() {
		return id;
	}
	
	public Point getLocation() {
		return location;
	}
	
	public void draw(Graphics2D g2d) {
		g2d.fillRect((int) location.x - 4, (int) location.y - 4, 8, 8);
	}
	
	public String toString() {
		StringBuilder b = new StringBuilder();
		
		b.append("Node: " + id + "\n");
		b.append("Located at: " + latitude + " lat, " + longitude + " lon\n");
		
		// Compile a list of roads this node is a part of
		ArrayList<String> roadNames = new ArrayList<String>();
		for (Segment s : RoadSegments) {
			if (!roadNames.contains(s.getRoad().getLabel())) {
				roadNames.add(s.getRoad().getLabel());
			}
		}
		
		if (roadNames.size() == 1) {
			b.append("Node is part of the road " + roadNames.get(0));
		} else if (roadNames.size() > 0) {
			b.append("The node is an intersection between the folowing road\n");
			
			for (String name : roadNames) {
				b.append("\t" + name + "\n");
			}
		}
		
		return b.toString();
	}
}
