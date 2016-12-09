package Vector;

import java.awt.Color;

/**
 * Extenction of the Vector3D class that also stores a color
 * 
 * @author danielbraithwt
 *
 */
public class ColoredVector3D extends Vector3D {
	public Color color;
	
	public ColoredVector3D(float x, float y, float z) {
		super(x, y, z);
	}
}
