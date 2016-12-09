package GUI;

import java.util.ArrayList;
import java.util.List;

import Lighting.LightSource;
import Polygon.Polygon;

public class Scene {
	private List<LightSource> lights;
	private List<? extends Polygon> polygons;
	
	/**
	 * 
	 * 
	 * @param Light source for the scene
	 * @param List of polygons to be drawn
	 */
	public Scene(LightSource l, List<? extends Polygon> p) {
		lights = new ArrayList<>();
		lights.add(l);
		
		polygons = p;
	}
	
	/**
	 * Return the list of light sources
	 * 
	 * @return List of light sources
	 */
	public List<LightSource> getLightSources() {
		return lights;
	}
	
	/**
	 * Returns the list of polgons
	 * 
	 * @return List of polygons
	 */
	public List<? extends Polygon> getPolygons() {
		return polygons;
	}
}
