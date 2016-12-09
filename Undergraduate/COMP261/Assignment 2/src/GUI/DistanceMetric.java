package GUI;
import java.awt.Point;

import AStar.Metric;
import Graph.Node;
import Graph.Segment;
import Location.Location;

public class DistanceMetric implements Metric<Node> {
	public static final DistanceMetric METRIC = new DistanceMetric();
	
	private DistanceMetric() {
		
	}
	
	@Override
	public double estimate(Node n1, Node n2) {
		//return n1.getLocation().distance(n2.getLocation());
		//Location p1 = n1.getLocation();
		//Location p2 = n2.getLocation();
		
		Segment link = n1.segmentLink(n2);
		
		if (link != null) {
			return link.getLength() * 1000;
		}
		
		Point p1 = n1.getPoint();
		Point p2 = n2.getPoint();
		
		//return Math.sqrt(Math.pow(p2.x - p1.x, 2) + Math.pow(p2.y - p1.y, 2));
		return Math.hypot(p1.x - p2.x, p1.y - p2.y);
	}
}
