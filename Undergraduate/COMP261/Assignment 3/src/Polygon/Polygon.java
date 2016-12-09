package Polygon;

import java.awt.Color;
import java.awt.Rectangle;

import GUI.Transform;
import Lighting.LightSource;
import Shader.Shader;
import Vector.ColoredVector3D;
import Vector.Vector3D;

public interface Polygon extends Iterable<ColoredVector3D> {
	
	/**
	 * Determins wether a polygon is facing away
	 * 
	 * @return True if polygon is facing away, otherwise False
	 */
	public boolean isFacingAway();
	
	/**
	 * Gets the color of a polygon surface
	 * 
	 * @return Color of the surface
	 */
	public Color getColor();
	
	/**
	 * Gets the bounding box of a rectangle
	 * 
	 * @return Bounding Box of the rectangle
	 */
	public Rectangle getBoundingBox();
	
	/**
	 * Calcualtes the unit normal to the surface
	 * 
	 * @return Vector3D Unit Normal
	 */
	public Vector3D getUnitNormalToSurface();
	
	/**
	 * Transforms the polygon
	 * 
	 * @param t - Transform to be applyed
	 * @return Polygon with transformed coords
	 */
	public Polygon transform(Transform t);
	
	/**
	 * Gets the array of coords
	 * 
	 * @return Array of Vector3D
	 */
	public Vector3D[] coords();
	
	/**
	 * Sets the shader to be used by the polygon
	 * 
	 * @param s - Shader
	 */
	public void setShader(Shader s);
	
	/**
	 * Given a vector v it will return the vector object in 
	 * this polygon that matches the vector if one exsists
	 * 
	 * @param v - Vector we want to found
	 * @return Vector3D that was found
	 */
	public Vector3D getEquivlentVector(Vector3D v);
}
