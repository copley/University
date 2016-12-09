import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JPanel;

import Graph.Node;
import Graph.RoadGraph;
import Polygon.Polygon;
import QuadTree.QuadTree;

public class MapPane extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final Color BACKGROUND_COLOR = new Color(233, 229, 220);
	public static int MAP_PANE_WIDTH = 500;
	public static int MAP_PANE_HEIGHT = 500;

	private RoadGraph graph;
	private Point dragStart;

	private int translateX = 500;
	private int translateY = 500;
	private float scale = 0.1f;

	private QuadTree<Integer> quadTree;

	private ArrayList<Polygon> polygons;

	private ArrayList<String> tmpCompletions;
	private ArrayList<String> completions;

	private String tmpPartial;
	private String partial;

	private int currentNode;

	private transient Vector<IntersectionSelectedListener> listeners;

	public MapPane() {
		graph = null;
		quadTree = null;
		polygons = null;

		currentNode = -1;

		completions = new ArrayList<String>();
		partial = "";

		// Add listener to handle mouse drags, on a mouse drag the
		// MapPane will be translated
		addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseDragged(MouseEvent e) {
				requestFocus();

				int deltaX = e.getX() - dragStart.x;
				int deltaY = e.getY() - dragStart.y;

				dragStart = e.getPoint();
				
				translate(deltaX, deltaY);
			}

			@Override
			public void mouseMoved(MouseEvent e) {
			}

		});

		// Add a mouse listener to handle mouse clicks, on a mouse
		// click the quad tree will be used to access the closest intersection
		// to the MouseEvent
		addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				requestFocus();

				// Get the virtual click coord
				Point click = new Point(
						(int) ((e.getX() - (translateX)) / scale),
						(int) ((e.getY() - (translateY)) / scale));

				Integer id = quadTree != null ? quadTree.get(click) : null;

				if (id != null) {
					fireIntersectionSelectedEvent(graph.getNodeByID(id));
					currentNode = id;
				}

				repaint();
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
				dragStart = e.getPoint();
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				dragStart = null;
			}

		});

		// Use the mouse wheel to controll the scale
		addMouseWheelListener(new MouseWheelListener() {

			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				scale(e.getWheelRotation() * -0.01f);
				//actions.add(new Action(Action.ZOOM_ACTION, new double[] { e
				//		.getWheelRotation() * -0.01f }));
			}

		});

		// Add key lostener so that the keys can also be used to pan around
		addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_PLUS) {
					scale(0.1f);
				}

				if (e.getKeyCode() == KeyEvent.VK_MINUS) {
					scale(-0.1f);
				}

				if (e.getKeyCode() == KeyEvent.VK_UP) {
					translate(5, 0);
				}

				if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					translate(-5, 0);
				}

				if (e.getKeyCode() == KeyEvent.VK_LEFT) {
					translate(0, 5);
				}

				if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
					translate(0, -5);
				}
			}

			@Override
			public void keyTyped(KeyEvent e) {
			}
		});

	}

	/**
	 * Initilises all the information required to draw the map, and then starts
	 * the draw loop
	 * 
	 * @param g
	 * @param q
	 * @param p
	 */
	public void load(RoadGraph g, QuadTree<Integer> q, ArrayList<Polygon> p) {
		graph = g;
		quadTree = q;
		polygons = p;

		repaint();
	}

	// //////////////////////////////////
	// Intersection Selected Listener //
	// //////////////////////////////////

	/**
	 * Used when adding a new intersection listener
	 * 
	 * @param l
	 */
	synchronized public void addIntersectionSelectedListener(
			IntersectionSelectedListener l) {
		if (listeners == null) {
			listeners = new Vector<IntersectionSelectedListener>();
		}

		listeners.add(l);
	}

	/**
	 * Used when removing an intersection listener
	 * 
	 * @param l
	 */
	synchronized public void removeIntersectionSelectedListener(
			IntersectionSelectedListener l) {
		if (listeners == null) {
			listeners = new Vector<IntersectionSelectedListener>();
		}

		listeners.remove(l);
	}

	/**
	 * Used to fire an event when an intersection is selected
	 * 
	 * @param n
	 */
	@SuppressWarnings("unchecked")
	protected void fireIntersectionSelectedEvent(Node n) {
		if (listeners == null) {
			return;
		}

		IntersectionSelectedEvent e = new IntersectionSelectedEvent(this, n);

		Vector<IntersectionSelectedListener> targets;
		synchronized (this) {
			targets = (Vector<IntersectionSelectedListener>) listeners.clone();
		}

		for (IntersectionSelectedListener l : targets) {
			l.onIntersectionSelected(e);
		}
	}

	// /////////////////////
	// Drawing Functions //
	// /////////////////////

	/**
	 * Handles drawing information to the canvas
	 * 
	 * @param g
	 */
	private void doDrawing(Graphics g) {
		if (graph == null) {
			return;
		}

		// Get new completions array if it exsists
		if (tmpCompletions != null) {
			completions = tmpCompletions;
			partial = tmpPartial;

			tmpCompletions = null;
			tmpPartial = null;
		}

		Graphics2D g2d = (Graphics2D) g;

		// Set up the rendering quallity
		RenderingHints antiAliasHint = new RenderingHints(
				RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHints(antiAliasHint);

		// Clear the canvas
		g2d.setColor(MapPane.BACKGROUND_COLOR);
		g2d.fillRect(0, 0, this.getWidth(), this.getHeight());

		// Set clipping rectangle
		g2d.setClip(new Rectangle(0, 0, this.getHeight(), this.getWidth()));
		
		// Translate the canvas as needed
		g2d.translate(translateX, translateY);
		g2d.scale(scale, scale);

		g2d.setStroke(new BasicStroke(10));

		// Draw the polygons
		if (polygons != null) {
			for (Polygon p : polygons) {
				p.draw(g2d);
			}
		}

		// Draw everything to the canvas
		g2d.setColor(Color.BLACK);
		graph.draw(g2d, partial, completions, currentNode);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		doDrawing(g);
	}

	/**
	 * Used to set the partial string and all the word completions so the map
	 * can color thoughs roads
	 * 
	 * @param partial
	 * @param c
	 */
	public void setCompletions(String partial, ArrayList<String> c) {
		tmpCompletions = c;
		tmpPartial = partial;

		repaint();
	}

	// //////////////////////////
	// Updateing Translations //
	// //////////////////////////

	/**
	 * Changes the translate X and Y by the passed deltas
	 * 
	 * @param deltaX
	 * @param deltaY
	 */
	public void translate(int deltaX, int deltaY) {
		translateX += (deltaX);
		translateY += (deltaY);
		
		repaint();
	}

	/**
	 * Updates the scale by delta
	 * 
	 * @param delta
	 */
	public void scale(float delta) {
		if (scale + delta > 0.01) {
			scale += delta;

			if (delta > 0) {
				translateX -= (this.getWidth() / 2) * delta;// scale;
				translateY -= (this.getHeight() / 2) * delta;// scale;
			} else if (delta < 0) {
				translateX -= (this.getWidth() / 2) * delta;// scale;
				translateY -= (this.getHeight() / 2) * delta;// scale;
			}
			
			repaint();
		}
	}
}
