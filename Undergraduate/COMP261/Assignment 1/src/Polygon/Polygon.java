package Polygon;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;

import Location.Location;

public class Polygon {
	public static final Color DEFAULT_COLOR = new Color(233, 229, 220);
	public static final Color BIG_BUILDINGS_COLOR = new Color(232, 221, 189);
	public static final Color BUILDINGS_COLOR = new Color(241, 239, 232);
	public static final Color OCEAN_COLOR = new Color(51, 153, 255);
	public static final Color LAKE_COLOR = new Color(51, 153, 255);
	public static final Color FORREST_COLOR = new Color(220, 223, 170);
	
	
	private HashMap<String, String> data;
	private ArrayList<Point> coords;
	
	public Polygon(String polygonInfoString) {
		data = new HashMap<String, String>();
		coords = new ArrayList<Point>();
		String[] lines = polygonInfoString.split("\n");
		
		for (String line : lines) {
			String[] keyVal = line.split("=");
			
			if (keyVal[0].startsWith("Data")) {
				String[] coordStrings = keyVal[1].split("\\),\\(");
				
				for (String coord : coordStrings) {
					String[] vals = coord.replace(")", "").replace("(", "").split(",");
					coords.add(Location.newFromLatLon(Double.parseDouble(vals[0]), Double.parseDouble(vals[1])).asPoint(Location.origin, 1));
				}
			} else {
				data.put(keyVal[0], keyVal[1]);
			}
		}
		
		Point start = coords.get(0);
		Point end = coords.get(coords.size()-1);
		
		if (start.x == end.x && start.y == end.y) {
			//coords.add(coords.size()-1, start);//(start);
			coords.remove(coords.size()-1);
		}
	}

	/**
	 * Handles drawing the polygon to the given canvas
	 * @param g2d
	 */
	public void draw(Graphics2D g2d) {
		// Get all the x y points in to arrays
		int[] xData = new int[coords.size()+1];
		int[] yData = new int[coords.size()+1];
		
		int sumX = 0;
		int sumY = 0;
		
		boolean draw = false;
		for (int i = 0; i < coords.size(); i++) {
			xData[i] = (int) coords.get(i).x;
			yData[i] = (int) coords.get(i).y;
			
			sumX += xData[i];
			sumY += yData[i];
			
			if (g2d.getClipBounds().contains(xData[i], yData[i])) {
				draw = true;
			}
		}
		
		if (!draw) {
			return;
		}
		
		xData[xData.length-1] = (int) coords.get(0).x;
		yData[yData.length-1] = (int) coords.get(0).y;
		
		// Get the color of the polygon
		switch (data.get("Type")) {
		// Domestic Airport
		case "0x7":
			g2d.setColor(Color.LIGHT_GRAY);
			break;
			
		// International Airport + Warf + University
		case "0x13":
			g2d.setColor(BIG_BUILDINGS_COLOR);
			break;
			
		// Ocean
		case "0x28":
			g2d.setColor(OCEAN_COLOR);
			break;
			
		// Fields
		case "Ox17":
			g2d.setColor(FORREST_COLOR);
			break;
			
		case "0x1":
			g2d.setColor(new Color(220, 223, 170));
			break;
			
		// Buildings
		case "0x2":
			g2d.setColor(BUILDINGS_COLOR);
			break;
			
		// Lake
		case "0x3c":
			g2d.setColor(LAKE_COLOR);
			break;
			
		// Ocean
		case "0x47":
			g2d.setColor(OCEAN_COLOR);
			break;
			
		// Ocean
		case "0x3e":
			g2d.setColor(OCEAN_COLOR);
			break;
			
		// Ocean
		case "0x48":
			g2d.setColor(OCEAN_COLOR);
			break;
			
		// Fields
		case "0xb":
			g2d.setColor(FORREST_COLOR);
			break;
			
		case "0x18":
			g2d.setColor(new Color(220, 223, 170));
			break;
			
		// Buildings
		case "0x8":
			g2d.setColor(BUILDINGS_COLOR);
			break;
			
		default:
			//System.out.println(data.get("Type"));
			g2d.setColor(Polygon.DEFAULT_COLOR);
			break;
		}
		
		g2d.fillPolygon(xData, yData, xData.length);
		
		if (data.containsKey("Type")) {
			g2d.setColor(Color.PINK);
			g2d.drawString(data.get("Type"), sumX/coords.size(), sumY/coords.size());
		}
	}
}
