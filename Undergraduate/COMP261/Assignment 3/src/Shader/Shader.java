package Shader;

import java.awt.Color;
import java.util.List;

import Lighting.LightSource;
import Polygon.Polygon;
import Vector.Vector3D;

/**
 * Defines the functons for a shader
 * 
 * @author danielbraithwt
 *
 */
public interface Shader {

	/**
	 * Sets the data to be currently used to for shading
	 * 
	 * @param p - Polygon currently in use
	 * @param l - List of lightsources
	 * @param a - Ambient light intencity
	 * @param aI - Ambient light intencity for each of the color components
	 */
	public void setData(Polygon p, List<LightSource> l, float a, float[] aI);
	
	/**
	 * Returns the number of values that are to be interpolated
	 * 
	 * @return The number of values that are being interpolated
	 */
	public int numValuesToInterpolate();
	
	/**
	 * Given a Vector it will return an array of values to be interpolated
	 * 
	 * @param v - Current vector
	 * @return Values to be interpolated for the current vector
	 */
	public Double[] getInterpolateValues(Vector3D v);
	
	/**
	 * Given a set of interpolated values it will return a color
	 * 
	 * @param values
	 * @return Color for the current values
	 */
	public Color getColor(double[] values);
}
