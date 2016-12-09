package Renderer;

import java.awt.Color;
import java.util.Iterator;

/**
 * 
 * 
 * @author danielbraithwt
 *
 */
public class ZBuffer {
	private float[][] depth;
	private Color[][] color;
	
	public ZBuffer(int height, int width) {
		depth = new float[width][height];
		color = new Color[width][height];
		
		// Init all depths to Infinity
		for (int i = 0; i < depth.length; i++) {
			for (int j = 0; j < depth[i].length; j++) {
				depth[i][j] = Float.POSITIVE_INFINITY;
				color[i][j] = Color.GRAY;
			}
		}
	}
	
	/**
	 * Will attempt to insert a point into the Z-Buffer
	 * 
	 * @param x - X position of the point
	 * @param y - Y position of the point
	 * @param d - depth of the point
	 * @param c - Color at the point
	 * @return Wether the point was inserted
	 */
	public boolean put(int x, int y, float d, Color c) {
		
		// If outside bounds return false
		if (x >= depth.length || x < 0 || y >= depth[0].length || y < 0) {
			return false;
		}
		
		// If the depth of point is closer than the currently
		// stored depth, then insert the point
		if (depth[x][y] > d) {
			depth[x][y] = d;
			color[x][y] = c;
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Gets the color at a given point
	 * 
	 * @param x 
	 * @param y
	 * @return The color
	 */
	public Color get(int x, int y) {
		return color[x][y];
	}
}
