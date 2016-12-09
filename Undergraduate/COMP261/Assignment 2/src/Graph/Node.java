package Graph;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;

import Location.Location;
import UnionFind.UnionFindElement;

public class Node implements UnionFindElement {
	private final int id; 
	private Location location;
	private Point point;
	private double latitude;
	private double longitude;
	private ArrayList<Segment> roadSegments;
	private ArrayList<Restriction> restrictions;
	private ArrayList<TrafficLight> trafficLights;
	
	// Variables for A* Search
	private boolean visited;
	private Node from;
	private double cost;
	private double pathLength;
	
	// Variables for finding articulation points
	private int depth;
	private int reach;
	private boolean isArticulationPoint;
	
	
	public Node(int id, double lat, double lon) {
		this.id = id;
		this.location = Location.newFromLatLon(lat, lon);
		this.point = location.asPoint(Location.origin, 1);
		this.latitude = lat;
		this.longitude = lon;
		
		roadSegments = new ArrayList<Segment>();
		restrictions = new ArrayList<Restriction>();
		trafficLights = new ArrayList<TrafficLight>();
		
		visited = false;
		from = null;
	}
	
	public void linkSegment(Segment s) {
		roadSegments.add(s);
	}
	
	public void addTrafficLight(TrafficLight t) {
		trafficLights.add(t);
	}
	
	public boolean hasTrafficLights() {
		return trafficLights.size() != 0;
	}
	
	public int getID() {
		return id;
	}
	
	public Point getPoint() {
		return point;
	}
	
	public Location getLocation() {
		return location;
	}
	
	public ArrayList<Segment> getSegments() {
		return roadSegments;
	}
	
	public void draw(Graphics2D g2d) {
		if (visited) {
			g2d.setColor(Color.GREEN);
		}
		
		g2d.fillRect((int) point.x - 4, (int) point.y - 4, 8, 8);
		
		// Draw the traffic lights
		for (TrafficLight t : trafficLights) {
			t.draw(g2d);
		}
	}
	
	public String toString() {
		StringBuilder b = new StringBuilder();
		
		b.append("Node: " + id + "\n");
		b.append("Located at: " + latitude + " lat, " + longitude + " lon\n");
		
		// Compile a list of roads this node is a part of
		ArrayList<String> roadNames = new ArrayList<String>();
		for (Segment s : roadSegments) {
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
	
	// Functions For A* Search //
	
	public void setVisisted(boolean v) {
		visited = v;
	}
	
	public boolean isVisited() {
		return visited;
	}
	
	public void setFromNode(Node n) {
		from = n;
	}
	
	public Node getFromNode() {
		return from;
	}
	
	public Segment segmentLink(Node n) {
		for (Segment s : roadSegments) {
			if (s.getOtherNode(this).getID() == n.id) {
				return s;
			}
		}
		
		return null;
	}
	
	public ArrayList<Node> getNeighbour() {
		ArrayList<Node> children = new ArrayList<Node>();
		
		for (Segment s : roadSegments) {
			children.add(s.getOtherNode(this));
		}
		
		return children;
	}
	
	public void addRestriction(Node f, Node t) {
		restrictions.add(new Restriction(f, t));
	}
	
	public boolean routeAllowed(Node f, Node t) {
		for (Restriction r : restrictions) {
			if (!r.isRouteAllowed(f, t)) {
				return false;
			}
		}
		
		return true;
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	public boolean isArticulationPoint() {
		return isArticulationPoint;
	}

	public void setArticulationPoint(boolean isArticulationPoint) {
		this.isArticulationPoint = isArticulationPoint;
	}

	public int getReach() {
		return reach;
	}

	public void setReach(int reach) {
		this.reach = reach;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}
	
	public double getPathLength() {
		return pathLength;
	}

	public void setPathLength(double length) {
		this.pathLength = length;
	}

	@Override
	public Collection<? extends UnionFindElement> getConnectedNodes() {
		return getNeighbour();
	}
}
