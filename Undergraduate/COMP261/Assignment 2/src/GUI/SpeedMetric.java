package GUI;
import AStar.Metric;
import Graph.Node;
import Graph.Road;
import Graph.RoadClass;
import Graph.Segment;

public class SpeedMetric implements Metric<Node> {
	
	public static final SpeedMetric METRIC = new SpeedMetric();
	
	private static final int MAX_SPEED_LIMIT = 200;
	
	private SpeedMetric() {
		
	}
	
	@Override
	public double estimate(Node n1, Node n2) {
		//double minTime = minTime(n1, n2);
		
		double time = minTime(n1, n2);
		Segment link = n1.segmentLink(n2);
		
		if (link != null) {
			// Calculate the time required to traverse the segment
			time = ((RoadClass.MajorHW.ordinal() - link.getRoad().getRoadClass().ordinal()) + 1) * (link.getLength() * 1000 / link.getRoad().getSpeed());
		}
		
		return time;
	}
	
	private double minTime(Node n1, Node n2) {
		return DistanceMetric.METRIC.estimate(n1, n2) / MAX_SPEED_LIMIT;
	}
}
