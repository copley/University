package Shader;

import java.awt.Color;
import java.util.List;

import Lighting.LightSource;
import Polygon.Polygon;
import Vector.Vector3D;

public class DefaultShader implements Shader {

	private Color c;

	@Override
	public void setData(Polygon p, List<LightSource> lights, float a, float[] aI) {
		// Calculate the color for the surface
		int newRed = 0;
		int newGreen = 0;
		int newBlue = 0;

		if (lights.size() == 0) {
			Color color = p.getColor();
			
			newRed = (int) ((a * aI[0]) * color.getRed());
			newGreen = (int) ((a * aI[1]) * color.getGreen());
			newBlue = (int) ((a * aI[2]) * color.getBlue());
		} else {
			for (LightSource l : lights) {
				Color color = l.computeShading(p.getUnitNormalToSurface(),
						p.getColor(), a, aI);

				newRed += color.getRed();
				newGreen += color.getGreen();
				newBlue += color.getBlue();
			}

			newRed = (int) (newRed / lights.size());
			newGreen = (int) (newGreen / lights.size());
			newBlue = (int) (newBlue / lights.size());
		}

		// Ensure the color is within bounds
		if (newRed < 0) {
			newRed = 0;
		}
		if (newBlue < 0) {
			newBlue = 0;
		}
		if (newGreen < 0) {
			newGreen = 0;
		}

		c = new Color((newRed <= 255) ? newRed : 255,
				(newGreen <= 255) ? newGreen : 255, (newBlue <= 255) ? newBlue
						: 255);
	}

	@Override
	public int numValuesToInterpolate() {
		return 0;
	}

	@Override
	public Double[] getInterpolateValues(Vector3D v) {
		return new Double[0];
	}

	@Override
	public Color getColor(double[] values) {
		return c;
	}

}
