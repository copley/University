package Polygon;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.Iterator;

import GUI.Transform;
import Lighting.LightSource;
import Shader.Shader;
import Vector.ColoredVector3D;
import Vector.Vector3D;

public class Triangle implements Polygon {

	private Vector3D[] coords;
	private Color color;
	private Shader shader;

	public Triangle(Vector3D[] coords, Color color) {
		this.coords = coords;
		this.color = color;

		for (Vector3D v : coords) {
			v.addConnected(this);
		}
	}

	@Override
	public boolean isFacingAway() {
		return (coords[1].x - coords[0].x) * (coords[2].y - coords[1].y) > (coords[1].y - coords[0].y)
				* (coords[2].x - coords[1].x);
	}

	public Color getColor() {
		return color;
	}

	@Override
	public Vector3D getUnitNormalToSurface() {
		Vector3D a = new Vector3D(coords[1].x - coords[0].x, coords[1].y
				- coords[0].y, coords[1].z - coords[0].z);
		Vector3D b = new Vector3D(coords[2].x - coords[1].x, coords[2].y
				- coords[1].y, coords[2].z - coords[1].z);

		Vector3D normal = new Vector3D(a.y * b.z - a.z * b.y, a.z * b.x - a.z
				* b.x, a.x * b.y - a.y * b.x);

		return new Vector3D(normal.x / normal.mag, normal.y / normal.mag,
				normal.z / normal.mag);
	}

	public void setShader(Shader s) {
		shader = s;
	}

	private double[][] computeEdgeList() {
		// Find the coord with lowest y value
		Vector3D lowestY = coords[0];
		Vector3D biggestY = coords[0];
		for (Vector3D v : coords) {
			if (v.y < lowestY.y) {
				lowestY = v;
			} else if (v.y > biggestY.y) {
				biggestY = v;
			}
		}

		int minY = (int) Math.floor(lowestY.y);
		int maxY = (int) Math.ceil(biggestY.y);
		// int height = (int) Math.round(biggestY.y - lowestY.y);

		int height = (maxY - minY);
		if (height == 0 || height == 1) {
			System.out.println("WTF");
		}

		double[][] edgeList = new double[height][4 + shader
				.numValuesToInterpolate() * 2];
		for (int i = 0; i < edgeList.length; i++) {
			// edgeList[i] = new double[] { Double.POSITIVE_INFINITY,
			// Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY,
			// Double.POSITIVE_INFINITY };
			for (int j = 0; j < edgeList[i].length; j++) {
				if (j == 2 + shader.numValuesToInterpolate()) {
					edgeList[i][j] = Double.NEGATIVE_INFINITY;
				} else {
					edgeList[i][j] = Double.POSITIVE_INFINITY;
				}
			}
		}

		for (int c = 0; c < coords.length; c++) {
			// Get the two vertecys defining the edge
			Vector3D v1 = coords[c];
			Vector3D v2 = (c != coords.length - 1) ? coords[c + 1] : coords[0];

			Vector3D min = (v1.y <= v2.y) ? v1 : v2;
			Vector3D max = (min == v1) ? v2 : v1;

			Double[] minShading = shader.getInterpolateValues(min);
			Double[] maxShading = shader.getInterpolateValues(max);

			// If there is a graydent of 0
			if (min.y == max.y) {
				int y = (int) Math.round(min.y - lowestY.y);

				y = (y > edgeList.length - 1) ? edgeList.length - 1 : y;

				double[] row = edgeList[y];

				edgeList[y][0] = min.x;
				edgeList[y][1] = min.z;

				for (int i = 0; i < minShading.length; i++) {
					edgeList[y][i + 2] = minShading[i];
				}

				edgeList[y][2 + shader.numValuesToInterpolate()] = max.x;
				edgeList[y][3 + shader.numValuesToInterpolate()] = max.z;

				for (int i = 0; i < minShading.length; i++) {
					edgeList[y][i + 4 + shader.numValuesToInterpolate()] = maxShading[i];
				}

				continue;
			}

			double mx = (max.x - min.x) / (max.y - min.y);
			double mz = (max.z - min.z) / (max.y - min.y);

			// Find the step values for the shading values
			double[] shaderM = new double[minShading.length];
			for (int i = 0; i < minShading.length; i++) {
				shaderM[i] = (maxShading[i] - minShading[i]) / (max.y - min.y);
			}

			double x = min.x;
			double z = min.z;

			// Get the initial values for the interpolating
			double[] shaderInterpolation = new double[minShading.length];
			for (int i = 0; i < minShading.length; i++) {
				shaderInterpolation[i] = minShading[i];
			}

			int i = (int) Math.floor(min.y);
			int maxi = (int) Math.floor(max.y);

			while (i < maxi) {

				double[] row = edgeList[i - minY];

				if (x < row[0]) {
					row[0] = x;
					row[1] = z;

					for (int j = 0; j < shader.numValuesToInterpolate(); j++) {
						row[j + 2] = shaderInterpolation[j];
					}
				}

				if (x > row[2 + shader.numValuesToInterpolate()]) {
					row[2 + shader.numValuesToInterpolate()] = x;
					row[3 + shader.numValuesToInterpolate()] = z;

					for (int j = 0; j < shader.numValuesToInterpolate(); j++) {
						row[j + shader.numValuesToInterpolate() + 4] = shaderInterpolation[j];
					}
				}

				i++;
				x += mx;
				z += mz;

				// Increment the shader values
				for (int j = 0; j < shaderInterpolation.length; j++) {
					shaderInterpolation[j] += shaderM[j];
				}
			}
		}

		// TODO: Remove this loop, just for testing
		for (int i = 0; i < edgeList.length; i++) {
			double[] row = edgeList[i];

			for (int j = 0; j < row.length; j++) {
				if (row[j] == Double.POSITIVE_INFINITY) {
					System.out.println("infinity");
				}
			}
		}

		// TODO: Remove this loop, again, just for testing
		for (int i = 0; i < edgeList.length; i++) {
			double[] row = edgeList[i];

			if (row[0] == row[2]) {
				System.out.println("SAME X");
			}
		}

		return edgeList;
	}

	@Override
	public Iterator<ColoredVector3D> iterator() {
		return new TriangleItterator();

	}

	class TriangleItterator implements Iterator<ColoredVector3D> {

		private double[][] edgeList;
		private Vector3D lowestY;
		private int minY;
		private boolean finished;

		private int x;
		private int y;
		private double z;

		private double mz;
		private double[] shaderM;
		private double[] shading;

		private int shadingValues;

		public TriangleItterator() {
			shadingValues = shader.numValuesToInterpolate();
			shading = new double[shadingValues];

			// Get the edge list
			edgeList = computeEdgeList();

			for (int i = 0; i < edgeList.length; i++) {
				double[] row = edgeList[i];

				if (row[0] == row[2]) {
					System.out.println("SAME X");
				}
			}

			// Get the coord with the lowest Y component
			lowestY = coords[0];
			for (Vector3D v : coords) {
				if (v.y < lowestY.y) {
					lowestY = v;
				}
			}

			minY = (int) Math.floor(lowestY.y);

			finished = false;

			// Initilise all the variables we need
			y = 0;

			if (edgeList.length == 0) {
				System.out.println(":(");
			}

			x = (int) Math.floor(edgeList[0][0]);
			z = (int) Math.floor(edgeList[0][1]);

			for (int i = 0; i < shading.length; i++) {
				shading[i] = edgeList[y][i + 2];
			}

			mz = 0;
			shaderM = new double[shadingValues];
		}

		@Override
		public boolean hasNext() {
			return !finished;
		}

		@Override
		public ColoredVector3D next() {
			ColoredVector3D toReturn = new ColoredVector3D((float) x,
					(float) (y + minY), (float) z);

			if (toReturn.y < 0 || toReturn.x < 0) {
				System.out.println("GRRRRR");
			}

			// Collect color values
			Color color = shader.getColor(shading);

			if (x >= Math.round(edgeList[y][shadingValues + 2])) {
				y++;

				// Check to see if there are any more points
				if (y >= edgeList.length) {
					finished = true;
				} else {

					// Catch an off by one error
					if (edgeList[y][0] == Double.POSITIVE_INFINITY) {
						finished = true;
					}

					x = (int) Math.floor(edgeList[y][0]);

					z = edgeList[y][1];

					for (int i = 0; i < shading.length; i++) {
						shading[i] = edgeList[y][i + 2];
					}

					mz = (edgeList[y][shadingValues + 3] - edgeList[y][1])
							/ (edgeList[y][shadingValues + 2] - edgeList[y][0]);

					for (int i = 0; i < shadingValues; i++) {
						shaderM[i] = (edgeList[y][shadingValues + i + 4] - edgeList[y][i + 2])
								/ (edgeList[y][shadingValues + 2] - edgeList[y][0]);
					}
				}
			} else {
				x++;
				z += mz;

				for (int i = 0; i < shading.length; i++) {
					shading[i] += shaderM[i];
				}
			}

			toReturn.color = color;

			return toReturn;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();

		}

	}

	@Override
	public Rectangle getBoundingBox() {
		Vector3D lowestY = coords[0];
		Vector3D lowestX = coords[0];
		Vector3D biggestY = coords[0];
		Vector3D biggestX = coords[0];

		for (Vector3D v : coords) {
			if (v.y < lowestY.y) {
				lowestY = v;
			} else if (v.y > biggestY.y) {
				biggestY = v;
			}

			if (v.x < lowestX.x) {
				lowestX = v;
			} else if (v.x > biggestX.x) {
				biggestX = v;
			}
		}

		int width = (int) (biggestX.x - lowestX.x);
		int height = (int) (biggestY.y - lowestY.y);

		return new Rectangle((int) lowestX.x, (int) lowestY.y, width, height);
	}

	@Override
	public Polygon transform(Transform t) {
		Vector3D[] transformed = new Vector3D[3];

		for (int i = 0; i < coords.length; i++) {
			transformed[i] = t.multiply(coords[i]);
		}

		return new Triangle(transformed, color);
	}

	public String toString() {
		StringBuilder b = new StringBuilder();

		for (Vector3D v : coords) {
			b.append(v.toString());
		}

		return b.toString();
	}

	@Override
	public Vector3D[] coords() {
		return coords;
	}

	@Override
	public Vector3D getEquivlentVector(Vector3D v) {
		for (Vector3D vec : coords) {
			if (v.x == vec.x && v.y == vec.y && v.z == vec.z) {
				return vec;
			}
		}

		return null;
	}
}
