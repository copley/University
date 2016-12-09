package Renderer;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import GUI.Scene;
import GUI.Transform;
import Lighting.LightSource;
import Polygon.Polygon;
import Reader.Reader;
import Shader.DefaultShader;
import Shader.Shader;
import Vector.ColoredVector3D;
import Vector.Vector3D;

public class Renderer {
	private Scene s;
	private Shader shader;
	private float ambientLight = 0.5f;
	private float[] ambientLightI;

	private int height;
	private int width;

	private double rotateX;
	private double rotateY;

	public Renderer(Reader<Scene> reader, int w, int h) {
		s = reader.read();
		shader = new DefaultShader();

		width = w;
		height = h;

		rotateX = 0;
		rotateY = 0;
	}

	/**
	 * Returns a buffered image with the polygons rendered to it
	 * 
	 * @return BufferedImage
	 */
	public BufferedImage render() {
		// Get the lightsource
		List<LightSource> lightSources = s.getLightSources();
		
		// Rotate the polygons
		Transform rotateXTransform = Transform.newXRotation((float) rotateX);
		Transform rotateYTransform = Transform.newYRotation((float) rotateY);

		// Rotate the lights
		List<LightSource> rotatedLights = new ArrayList<>();
		for (LightSource l : lightSources) {
			rotatedLights.add(l.transform(rotateXTransform).transform(rotateYTransform));
		}

		// Rotate the polygons
		List<Polygon> rotated = new ArrayList<>();
		for (Polygon g : s.getPolygons()) {
			rotated.add(g.transform(rotateXTransform).transform(
					rotateYTransform));
		}

		// Create the transform to center and fit the polygons on
		// the screen
//		Transform initialTranslate = getInitialTranslate(rotated);
//
//		// Apply this transform to the polygons
//		List<Polygon> translated = new ArrayList<>();
//		for (Polygon g : rotated) {
//			translated.add(g.transform(initialTranslate));
//		}

		Transform initialScale = getInitialScale(rotated);
		//lightSource = lightSource.transform(initialScale);

		List<Polygon> scaled = new ArrayList<>();
		for (Polygon g : rotated) {
			scaled.add(g.transform(initialScale));
		}
		
		Transform initialTranslate = getInitialTranslate(scaled);

		// Apply this transform to the polygons
		List<Polygon> translated = new ArrayList<>();
		for (Polygon g : scaled) {
			translated.add(g.transform(initialTranslate));
		}

		List<Polygon> visible = getVisiblePolygons(translated);

		// Collect the connected polygons
		for (int i = 0; i < visible.size(); i++) {
			Polygon polygon = visible.get(i);

			for (Vector3D v : polygon.coords()) {
				for (int j = i + 1; j < visible.size(); j++) {
					Polygon p = visible.get(j);

					// Make sure we arnt linking a triangle to its self
					if (polygon != p) {
						Vector3D e = p.getEquivlentVector(v);

						if (e != null) {
							e.addConnected(polygon);
							v.addConnected(p);
						}
					}
				}
			}
		}

		ZBuffer buffer = new ZBuffer(width, height);

		// Itterate through all the polygons, and the points in thoughs
		// polygons. Insert them into the z buffer
		for (Polygon p : visible) {
			shader.setData(p, rotatedLights, ambientLight, ambientLightI);
			p.setShader(shader);

			for (ColoredVector3D cv : p) {
				buffer.put((int) cv.x, (int) cv.y, cv.z,
						cv.color);
			}
		}

		// Write the Z-Buffer to the buffered image
		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		for (int i = 0; i < image.getHeight(); i++) {
			for (int j = 0; j < image.getWidth(); j++) {
				image.setRGB(j, i, buffer.get(j, i).getRGB());
			}
		}

		return image;
	}

	/**
	 * Gets the initial translate for the polygons
	 * 
	 * @param polygons
	 * @return Translation to center the polygons
	 */
	private Transform getInitialTranslate(List<? extends Polygon> polygons) {
		// Calculate the bounding box
		Rectangle boundingBox = polygons.get(0).getBoundingBox();
		double minZ = Double.POSITIVE_INFINITY;
		double maxZ = Double.NEGATIVE_INFINITY;
		
		for (int i = 1; i < polygons.size(); i++) {
			Polygon p = polygons.get(i);
			
			// Inspect the min and max z value
			for (Vector3D v : p.coords()) {
				if (v.z > maxZ) {
					maxZ = v.z;
				} else if (v.z < minZ) {
					minZ = v.z;
				}
			}

			// Union the bounding boxes
			boundingBox = boundingBox.union(polygons.get(i).getBoundingBox());
		}

		// Calculate the center of the bounding box
		int boundingTopLeftX = boundingBox.x;
		int boundingTopLeftY = boundingBox.y;

		// Calculate diffrence from center of canvas
		int deltaX = 20 - boundingTopLeftX;
		int deltaY = 20 - boundingTopLeftY;
		//int deltaZ = (int) -(maxZ - minZ);

		// Add the transformation
		return Transform.newTranslation(deltaX, deltaY, 0);
	}

	/**
	 * Gets the scale transformation to fit the polygons to the screen
	 * 
	 * @param polygons
	 * @return Scale transformation to apply to the polygons
	 */
	private Transform getInitialScale(List<? extends Polygon> polygons) {
		// Calculate the bounding box
		Rectangle boundingBox = polygons.get(0).getBoundingBox();
		for (int i = 1; i < polygons.size(); i++) {
			boundingBox = boundingBox.union(polygons.get(i).getBoundingBox());
		}

		double scaleX = (double) (width - 40) / (double) boundingBox.width;
		double scaleY = (double) (height - 40) / (double) boundingBox.height;

		float scale = 1f;

		// If we are scaling down then we want the maximum of the two
		// otherwise we want the min
		if (scaleX < 1 && scaleY < 1) {
			scale = (float) Math.max(scaleX, scaleY) - 0.03f;
		} else {
			scale = (float) Math.min(scaleX, scaleY) - 0.03f;
		}

		return Transform.newScale(scale, scale, scale);
	}

	/**
	 * Takes a list of polygons and determins which ones are
	 * facing towards you
	 * 
	 * @param polygons
	 * @return List of polygons facing towards
	 */
	private List<Polygon> getVisiblePolygons(List<Polygon> polygons) {
		List<Polygon> visible = new ArrayList<>();

		for (Polygon p : polygons) {
			if (!p.isFacingAway()) {
				visible.add(p);
			}
		}

		return visible;
	}

	/**
	 * Updates the ambient light
	 * 
	 * @param delta
	 */
	public void updateAmbientLight(float delta) {
		if (ambientLight + delta > 0 && ambientLight + delta < 0.9) {
			ambientLight += delta;
		}
	}

	/**
	 * Adjust the rotation of the objects
	 * 
	 * @param deltaX
	 * @param deltaY
	 */
	public void rotate(double deltaX, double deltaY) {
		rotateX += deltaX;
		rotateY += deltaY;
	}
	
	/**
	 * 
	 * @return
	 */
	public Scene getScene() {
		return s;
	}
	
	/**
	 * 
	 * @param s
	 */
	public void setShader(Shader s) {
		shader = s;
	}

	/**
	 * 
	 * @param fs
	 */
	public void setAmbientLightComponents(float[] fs) {
		ambientLightI = fs;
	}
}
