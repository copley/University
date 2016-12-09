package Graph;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

import Location.Location;

public class Segment {
	private Node node1; // start node
	private Node node2; // finish node
	private double length;
	private ArrayList<Point> points;
	private Road road;

	public Segment(Node node1, Node node2, double length, double[][] coords) {
		// Store what nodes the segment is connected to
		this.node1 = node1;
		this.node2 = node2;
		this.length = length;

		// Add the segment to the nodes
		node1.linkSegment(this);
		node2.linkSegment(this);

		// Turn all the coord points into locations
		points = new ArrayList<Point>();
		for (double[] coord : coords) {
			points.add(Location.newFromLatLon(coord[0], coord[1]).asPoint(Location.origin, 1));
		}
	}

	public void setRoad(Road r) {
		road = r;
	}

	public Road getRoad() {
		return road;
	}

	public void draw(Graphics2D g2d) {
		// Make sure that we should be drawing this segment
		boolean draw = false;
		for (Point p : points) {
			if (g2d.getClipBounds().contains(p.x, p.y)) {
				draw = true;
			}
		}

		if (!draw) {
			return;
		}

		for (int i = 0; i < points.size(); i++) {
			// Draw the line connecting the current point to the next one
			// if a next point exsists
			if (i != points.size() - 1) {
				g2d.drawLine((int) points.get(i).x, (int) points.get(i).y, (int) points.get(i + 1).x, (int) points.get(i + 1).y);
			}
		}
	}

	public Point getAvgLocation() {
		return new Point(
				(node1.getPoint().x + node2.getPoint().x) / 2,
				(node1.getPoint().y + node2.getPoint().y) / 2);
	}
	
	public double getLength() {
		return length;
	}
	
	public Node getOtherNode(Node n) {
		if (n.getID() == node1.getID()) {
			return node2;
		}
		
		return node1;
	}
	
	public Node getStartNode() {
		return node1;
	}
	
	public Node getEndNode() {
		return node2;
	}
}
