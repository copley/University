package Lighting;

import java.awt.Color;

import Vector.Vector3D;
import GUI.Transform;

/**
 * Defines the functions for shader implementations
 * 
 * @author danielbraithwt
 *
 */
public interface LightSource {
	
	/**
	 * Gets the direction of the light source
	 * @return Vector3D - Direction of light source
	 */
	public Vector3D getDirection();
	
	/**
	 * 
	 * @param t - Transform to be applyed
	 * @return A light source with the transform applyed
	 */
	public LightSource transform(Transform t);
	
	/**
	 * Given paramaters will compute the color for thoughs values
	 * 
	 * @param normal - Normal to the surface
	 * @param c - Color of the surface
	 * @param a - Ambient light intencity
	 * @param aI - Ambient light intencity components
	 * @return Color for these values
	 */
	public Color computeShading(Vector3D normal, Color c, float a, float[] aI);
}
