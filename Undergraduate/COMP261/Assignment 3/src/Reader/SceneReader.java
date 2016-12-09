package Reader;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import GUI.Scene;
import Lighting.DefaultLightSource;
import Lighting.LightSource;
import Polygon.Triangle;
import Vector.Vector3D;

public class SceneReader extends Reader<Scene> {

	public SceneReader(File f) {
		super(f);
	}

	@Override
	public Scene read() {

		// Split the input into an array of lines
		String[] lines = content.split("\n");

		// First line is the lightsource
		String[] lightInfo = lines[0].split(" ");
		LightSource l = new DefaultLightSource(Double.parseDouble(lightInfo[0]
				.trim()), Double.parseDouble(lightInfo[1].trim()),
				Double.parseDouble(lightInfo[2].trim()));

		// Now we want to read in the polygons
		ArrayList<Triangle> triangles = new ArrayList<Triangle>();

		for (int i = 1; i < lines.length; i++) {
			String[] polyInfo = lines[i].split(" ");
			Vector3D[] coords = new Vector3D[3];

			// Read in the coordernats
			for (int j = 0; j < polyInfo.length - 3; j += 3) {
				coords[j / 3] = new Vector3D(
						(float) Double.parseDouble(polyInfo[j].trim()),
						(float) Double.parseDouble(polyInfo[j + 1].trim()),
						(float) Double.parseDouble(polyInfo[j + 2].trim()));
			}

			// Read in the color for the polygon
			Color color = new Color(
					Integer.parseInt(polyInfo[polyInfo.length - 3]),
					Integer.parseInt(polyInfo[polyInfo.length - 2]),
					Integer.parseInt(polyInfo[polyInfo.length - 1]));

			Triangle triangle = new Triangle(coords, color);

			triangles.add(triangle);
		}

		return new Scene(l, triangles);
	}

}
