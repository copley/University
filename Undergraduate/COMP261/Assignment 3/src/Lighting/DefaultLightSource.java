package Lighting;

import java.awt.Color;

import Vector.Vector3D;
import GUI.Transform;

/**
 * Represents a light source with a fixed intensity placed at infinity in a
 * specific direction
 * 
 * @author danielbraithwt
 * 
 */
public class DefaultLightSource implements LightSource {
	private Vector3D direction;

	public DefaultLightSource(Vector3D dir) {
		direction = dir;
	}

	public DefaultLightSource(double x, double y, double z) {
		direction = new Vector3D((float) x, (float) y, (float) z);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((direction == null) ? 0 : direction.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		
		DefaultLightSource other = (DefaultLightSource) obj;
		if (direction == null) {
			if (other.direction != null) {
				return false;
			}
		} else if (!direction.equals(other.direction)) {
			return false;
		}
		return true;
	}

	@Override
	public Vector3D getDirection() {
		return direction;
	}

	@Override
	public LightSource transform(Transform t) {
		return new DefaultLightSource(t.multiply(direction));
	}

	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append(direction.toString());

		return b.toString();
	}

	@Override
	public Color computeShading(Vector3D normal, Color c, float a, float[] aI) {

		float costh = normal.dotProduct(direction);
		costh = (costh < 0 ? 0 : costh);

		int newRed = (int) (((a * aI[0]) + 1 * costh) * c.getRed());
		int newGreen = (int) (((a * aI[1]) + 1 * costh) * c.getGreen());
		int newBlue = (int) (((a * aI[2]) + 1 * costh) * c.getBlue());

		if (newRed < 0) {
			newRed = 0;
		}
		if (newBlue < 0) {
			newBlue = 0;
		}
		if (newGreen < 0) {
			newGreen = 0;
		}

		Color shading = new Color((newRed <= 255) ? newRed : 255,
				(newGreen <= 255) ? newGreen : 255, (newBlue <= 255) ? newBlue
						: 255);
		
		return shading;
	}
}
