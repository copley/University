package Graph;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;

public class RoadGraph {
	
	private HashMap<Integer, Node> nodesMap;
	private HashMap<Integer, Road> roadsMap;
	private HashMap<String, Road> roadsNameMap;
	
	public RoadGraph(HashMap<Integer, Node> nodes, HashMap<Integer, Road> roads, HashMap<String, Road> roadsName) {
		// Initilise maps
		nodesMap = nodes;
		roadsMap = roads;
		roadsNameMap = roadsName;
	}
		
	
	public Road getRoadByName(String name) {
		if (roadsNameMap.containsKey(name)) {
			return roadsNameMap.get(name); 
		}
		
		return null;
	}
	
	public Node getNodeByID(Integer id) {
		
		if (nodesMap.containsKey(id)) {
			return nodesMap.get(id); 
		}
		
		return null;		
	}
	
	public void draw(Graphics2D g2d, String partial, ArrayList<String> possibleCompletions, int currentNode) { 
		for (Road r : roadsMap.values()) {	
			g2d.setColor(r.getRoadColor());
			
			if (possibleCompletions.contains(r.getLabel())) {
				g2d.setColor(Color.GREEN);
			
				if (possibleCompletions.size() == 1) {
					g2d.setColor(Color.CYAN);
					}
			}
			
			if (r.getLabel().equals(partial)) {
				g2d.setColor(Color.RED);
			} 
		
			r.draw(g2d);
		}
		
		for (Node n : nodesMap.values()) {
			if (g2d.getClipBounds().contains(n.getLocation().x, n.getLocation().y)) {
				g2d.setColor(Color.BLUE);
			
				if (currentNode == n.getID()) {
					g2d.setColor(Color.YELLOW);
				}
			
				n.draw(g2d);
			}
		}
	}
}
