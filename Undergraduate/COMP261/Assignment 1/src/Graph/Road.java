package Graph;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

import Location.Location;

public class Road {
	public static final Color RESIDENTIAL_COLOR = Color.LIGHT_GRAY;
	public static final Color COLLECTOR_COLOR = Color.WHITE;
	public static final Color HIGHWAY_COLOR = new Color(255, 255, 104);
	
	private int id;
	private int type;
	private String label;
	private String city;
	private boolean oneway;
	private int speed;
	private RoadClass roadClass;
	private boolean notForCar;
	private boolean notForPedestrians;
	private boolean notForBicycle;
	private ArrayList<Segment> segments;
	
	public Road(int id, int type, String label, String city, boolean oneway, int speed, RoadClass roadClass, boolean notForCar, boolean notForPedestrians, boolean notForBicycle) {
		this.id = id;
		this.label = label;
		this.city = city;
		this.oneway = oneway;
		this.speed = speed;
		this.roadClass = roadClass;
		this.notForCar = notForCar;
		this.notForPedestrians = notForPedestrians;
		this.notForBicycle = notForBicycle;
		
		segments = new ArrayList<Segment>();
	}
	
	public void addSegment(Segment s) {
		segments.add(s);
		s.setRoad(this);
	}
	
	public Color getRoadColor() {
		if (roadClass == RoadClass.Residential) {
			return Road.RESIDENTIAL_COLOR;
		} else if (roadClass == RoadClass.Collector) {
			return Road.COLLECTOR_COLOR;
		} else if (roadClass == RoadClass.PrincipalHW) {
			return Road.HIGHWAY_COLOR;
		} else if (roadClass == RoadClass.MajorHW) {
			return Road.HIGHWAY_COLOR;
		}
		
		return Road.RESIDENTIAL_COLOR;
	}
	
	public void draw(Graphics2D g2d) {
		for (Segment s : segments) {
			s.draw(g2d);
		}
	}
	
	public String getLabel() {
		return label;
	}
	
	public Point getAvgLocation() {
		int x = 0;
		int y = 0;
		
		for (Segment s : segments) {
			Point avgSegmentLocation = s.getAvgLocation();
			
			x += avgSegmentLocation.x;
			y += avgSegmentLocation.y;
		}
		
		return new Point(x / segments.size(), y / segments.size());
	}
}
