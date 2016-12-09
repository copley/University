package Shader;

import java.awt.Color;
import java.util.List;

import Lighting.LightSource;
import Polygon.Polygon;
import Vector.Vector3D;

public class PhongShader implements Shader {

	private static final int NUM_INTERPOLATE_VALUES = 3;

	private Polygon polygon;
	private List<LightSource> lightsources;
	private float ambient;
	private float[] ambientI;

	@Override
	public void setData(Polygon p, List<LightSource> l, float a, float[] aI) {
		polygon = p;
		lightsources = l;
		ambient = a;
		ambientI = aI;
	}

	@Override
	public int numValuesToInterpolate() {
		return NUM_INTERPOLATE_VALUES;
	}

	@Override
	public Double[] getInterpolateValues(Vector3D v) {
		// Returns the vector normal as the values to be interpolated
		Double[] vals = new Double[3];
		Vector3D vecNormal = v.vectorNormal();

		vals[0] = (double) vecNormal.x;
		vals[1] = (double) vecNormal.y;
		vals[2] = (double) vecNormal.z;

		return vals;
	}

	@Override
	public Color getColor(double[] values) {
		if (values.length != NUM_INTERPOLATE_VALUES) {
			throw new IllegalArgumentException("Expecting "
					+ NUM_INTERPOLATE_VALUES + " number of values but got "
					+ values.length);
		}

		int newRed = 0;
		int newGreen = 0;
		int newBlue = 0;

		if (lightsources.size() == 0) {
			Color color = polygon.getColor();

			newRed = (int) ((ambient * ambientI[0]) * color.getRed());
			newGreen = (int) ((ambient * ambientI[1]) * color.getGreen());
			newBlue = (int) ((ambient * ambientI[2]) * color.getBlue());
		} else {
			Vector3D normal = new Vector3D((float) values[0],
					(float) values[1], (float) values[2]);

			for (LightSource l : lightsources) {

				Color color = l.computeShading(normal, polygon.getColor(),
						ambient, ambientI);

				newRed = color.getRed();
				newGreen = color.getGreen();
				newBlue = color.getBlue();
			}

			newRed = (int) (newRed / lightsources.size());
			newBlue = (int) (newBlue / lightsources.size());
			newGreen = (int) (newGreen / lightsources.size());
		}

		if (newRed < 0) {
			newRed = 0;
		}
		if (newBlue < 0) {
			newBlue = 0;
		}
		if (newGreen < 0) {
			newGreen = 0;
		}

		return new Color((int) ((newRed <= 255) ? newRed : 255),
				(int) ((newGreen <= 255) ? newGreen : 255),
				(int) ((newBlue <= 255) ? newBlue : 255));
	}

}
