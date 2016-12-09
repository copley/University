package Shader;

import java.awt.Color;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Lighting.LightSource;
import Polygon.Polygon;
import Vector.Vector3D;

public class GouraudShader implements Shader {

	private static final int NUM_INTERPOLATE_VALUES = 3;

	Map<Vector3D, Double[]> lightingVertexMapping;

	@Override
	public void setData(Polygon p, List<LightSource> lights, float a, float[] aI) {
		lightingVertexMapping = new HashMap<Vector3D, Double[]>();

		// Calculate the light intensity at each of the vertex
		// normals
		for (Vector3D v : p.coords()) {
			int newRed = 0;
			int newGreen = 0;
			int newBlue = 0;

			if (lights.size() == 0) {
				Color color = p.getColor();

				newRed = (int) ((a * aI[0]) * color.getRed());
				newGreen = (int) ((a * aI[1]) * color.getGreen());
				newBlue = (int) ((a * aI[2]) * color.getBlue());
			} else {
				Vector3D vector = v.vectorNormal();

				for (LightSource l : lights) {

					Color color = l.computeShading(vector, p.getColor(), a, aI);

					newRed += color.getRed();
					newGreen += color.getGreen();
					newBlue += color.getBlue();
				}

				newRed = (int) (newRed / lights.size());
				newGreen = (int) (newGreen / lights.size());
				newBlue = (int) (newBlue / lights.size());
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

			lightingVertexMapping.put(v, new Double[] {
					(double) ((newRed <= 255) ? newRed : 255),
					(double) ((newGreen <= 255) ? newGreen : 255),
					(double) ((newBlue <= 255) ? newBlue : 255) });
		}
	}

	public Double[] getInterpolateValues(Vector3D v) {
		return lightingVertexMapping.get(v);
	}

	public int numValuesToInterpolate() {
		return NUM_INTERPOLATE_VALUES;
	}

	@Override
	public Color getColor(double[] values) {
		if (values.length != NUM_INTERPOLATE_VALUES) {
			throw new IllegalArgumentException("Expecting "
					+ NUM_INTERPOLATE_VALUES + " number of values but got "
					+ values.length);
		}

		int red = (int) (values[0] <= 255 ? values[0] : 255);
		int green = (int) (values[1] <= 255 ? values[1] : 255);
		int blue = (int) (values[2] <= 255 ? values[2] : 255);

		if (red < 0) {
			red = 0;
		}
		if (blue < 0) {
			blue = 0;
		}
		if (green < 0) {
			green = 0;
		}

		return new Color(red, green, blue);
	}

}
