package GUI;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import Graph.Node;
import Graph.Road;
import Graph.RoadClass;
import Graph.RoadGraph;
import Graph.Segment;
import Graph.TrafficLight;
import JAutoCompleteTextField.AutoCompletionEvent;
import JAutoCompleteTextField.AutoCompletionListener;
import JAutoCompleteTextField.JAutoCompleteTextField;
import Polygon.Polygon;
import QuadTree.QuadTree;

public class GUI extends JFrame implements AutoCompletionListener, IntersectionSelectedListener {

	// Static Filename constatants from original GUI.java
	private static final String NODES_FILENAME = "nodeID-lat-lon.tab";
	private static final String ROADS_FILENAME = "roadID-roadInfo.tab";
	private static final String RESTRICTIONS_FILENAME = "restrictions.tab";
	private static final String SEGS_FILENAME = "roadSeg-roadID-length-nodeID-nodeID-coords.tab";
	private static final String TRAFFIC_LIGHTS_FILENAME = "traffic-lights.tab";
	private static final String POLYS_FILENAME = "polygon-shapes.mp";

	//private final JDialog progressDialog;

	private MapPane map;
	private JAutoCompleteTextField textField;
	
	private JButton closeButton;
	private JTextArea textArea;

	private JPanel buttons;
	private JButton panUp;
	private JButton panDown;
	private JButton panLeft;
	private JButton panRight;
	private JButton zoomIn;
	private JButton zoomOut;
	
	private JPanel routeFinder;
	private JButton chooseRouteStart;
	private JButton chooseRouteFinish;
	private JRadioButton distanceMetric;
	private JRadioButton speedMetric;
	private JRadioButton isCar;
	private JRadioButton isBike;
	private JRadioButton isPerson;

	private JMenuBar menuBar;

	private JMenuItem loadMenuItem;

	public GUI() {
		super("Auckland Map");
		setLayout(new BorderLayout());

		// Add a menu bar
		menuBar = new JMenuBar();

		JMenu fileMenu = new JMenu("File");
		menuBar.add(fileMenu);

		loadMenuItem = new JMenuItem("Load");
		loadMenuItem.addActionListener(new ActionListener() {

			/**
			 * The file loading code is from the GUI.java file provided
			 */
			public void actionPerformed(ActionEvent ev) {
				JFileChooser fileChooser = new JFileChooser();

				File nodes = null, roads = null, segments = null, polygons = null, restrictions = null, trafficLights = null;

				// set up the file chooser
				fileChooser.setCurrentDirectory(new File("."));
				fileChooser.setDialogTitle("Select input directory");
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

				// run the file chooser and check the user didn't hit cancel
				if (fileChooser.showOpenDialog(GUI.this) == JFileChooser.APPROVE_OPTION) {
					// get the files in the selected directory and match them to
					// the files we need.
					File directory = fileChooser.getSelectedFile();
					File[] files = directory.listFiles();

					for (File f : files) {
						if (f.getName().equals(NODES_FILENAME)) {
							nodes = f;
						} else if (f.getName().equals(ROADS_FILENAME)) {
							roads = f;
						} else if (f.getName().equals(SEGS_FILENAME)) {
							segments = f;
						} else if (f.getName().equals(POLYS_FILENAME)) {
							polygons = f;
						} else if (f.getName().equals(RESTRICTIONS_FILENAME)) {
							restrictions = f;
						} else if (f.getName().equals(TRAFFIC_LIGHTS_FILENAME)) {
							trafficLights = f;
						}
					}

					// check none of the files are missing, and call the load
					// method in your code.
					if (nodes == null || roads == null || segments == null) {
						JOptionPane.showMessageDialog(GUI.this,
								"Directory does not contain correct files",
								"Error", JOptionPane.ERROR_MESSAGE);
					} else {
						onLoad(nodes, roads, segments, polygons, restrictions, trafficLights);
						repaint();
					}
				}
			}
		});

		fileMenu.add(loadMenuItem);

		setJMenuBar(menuBar);

		buttons = new JPanel();
		buttons.setSize(new Dimension(400, 80));
		buttons.setLocation(350, 10);
		buttons.setOpaque(false);

		// Create and add all the buttons
		panUp = new JButton(new ImageIcon("icons/up.gif"));
		panUp.setPreferredSize(new Dimension(32, 32));
		panUp.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				map.translate(0, 10);
			}

		});
		buttons.add(panUp);

		panDown = new JButton(new ImageIcon("icons/down.gif"));
		panDown.setPreferredSize(new Dimension(32, 32));
		panDown.addActionListener(new ActionListener() {

			@Override
			public
			void actionPerformed(ActionEvent arg0) {
				map.translate(0, -10);
			}

		});
		buttons.add(panDown);

		panLeft = new JButton(new ImageIcon("icons/left.gif"));
		panLeft.setPreferredSize(new Dimension(32, 32));
		panLeft.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				map.translate(10, 0);
			}

		});
		buttons.add(panLeft);

		panRight = new JButton(new ImageIcon("icons/right.gif"));
		panRight.setPreferredSize(new Dimension(32, 32));
		panRight.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				map.translate(-10, 0);
			}

		});
		buttons.add(panRight);

		zoomIn = new JButton(new ImageIcon("icons/zoomin.gif"));
		zoomIn.setPreferredSize(new Dimension(32, 32));
		zoomIn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				map.scale(0.01f);
			}

		});
		buttons.add(zoomIn);

		zoomOut = new JButton(new ImageIcon("icons/zoomout.gif"));
		zoomOut.setPreferredSize(new Dimension(32, 32));
		zoomOut.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				map.scale(-0.01f);
			}

		});
		buttons.add(zoomOut);
		
		buttons.setVisible(false);
		
		routeFinder = new JPanel();
		routeFinder.setLayout(new BorderLayout());
		routeFinder.setSize(new Dimension(200, 250));
		routeFinder.setLocation(760, 10);
		routeFinder.setVisible(false);
		routeFinder.setOpaque(false);
		
		JPanel routeFinderButtons = new JPanel();
		routeFinderButtons.setOpaque(false);
		
		chooseRouteStart = new JButton("S");
		chooseRouteStart.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				map.toggleStartNodeSelection();
			}
			
		});
		routeFinderButtons.add(chooseRouteStart);
		//buttons.add(chooseRouteStart);
		
		chooseRouteFinish = new JButton("F");
		chooseRouteFinish.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				map.toggleFinishNodeSelection();
			}
			
		});
		routeFinderButtons.add(chooseRouteFinish);
		//buttons.add(chooseRouteFinish);
		
		routeFinder.add(routeFinderButtons, BorderLayout.NORTH);
		
		JPanel routeFinderOptions = new JPanel();
		routeFinderOptions.setOpaque(false);
		
		JPanel routeFinderMetrics = new JPanel();
		routeFinderMetrics.setLayout(new BorderLayout());
		routeFinderMetrics.setOpaque(false);
		
		distanceMetric = new JRadioButton("Distance Metric");
		distanceMetric.setOpaque(false);
		distanceMetric.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				map.setMetric(DistanceMetric.METRIC);
				//speedMetric.setSelected(false);
			}
			
		});
		distanceMetric.setSelected(true);
		routeFinderMetrics.add(distanceMetric, BorderLayout.NORTH);
		
		speedMetric = new JRadioButton("Speed Metric");
		speedMetric.setOpaque(false);
		speedMetric.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				map.setMetric(SpeedMetric.METRIC);
			}
		
		});
		
		// Create a button group for the metrics
		ButtonGroup metricsGroup = new ButtonGroup();
		metricsGroup.add(distanceMetric);
		metricsGroup.add(speedMetric);
		
		routeFinderMetrics.add(speedMetric, BorderLayout.SOUTH);
		
		routeFinderOptions.add(routeFinderMetrics);
		
		JPanel routeFinderType = new JPanel();
		routeFinderType.setOpaque(false);
		
		isCar = new JRadioButton("Car");
		isCar.setOpaque(false);
		isCar.setSelected(true);
		isCar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				map.setTransportType(TransportType.CAR);
			}
			
		});
		routeFinderType.add(isCar);
		
		isBike = new JRadioButton("Bike");
		isBike.setOpaque(false);
		isBike.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				map.setTransportType(TransportType.BIKE);
			}
		});
		routeFinderType.add(isBike);
	
		isPerson = new JRadioButton("Person");
		isPerson.setOpaque(false);
		isPerson.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				map.setTransportType(TransportType.PERSON);
			}
		});
		routeFinderType.add(isPerson);
		
		// Create a button group for the transport type selection
		ButtonGroup transportTypes = new ButtonGroup();
		transportTypes.add(isCar);
		transportTypes.add(isBike);
		transportTypes.add(isPerson);
		
		routeFinderOptions.add(routeFinderType);
		
		routeFinder.add(routeFinderOptions, BorderLayout.CENTER);
		
		JLayeredPane p = new JLayeredPane();
		
		p.add(buttons, new Integer(2));

		textField = new JAutoCompleteTextField();
		textField.addAutoCompletionListener(this);
		textField.setSize(new Dimension(400, 20));
		textField.setLocation(20, 20);
		p.add(textField, new Integer(2));

		textArea = new JTextArea();
		textArea.setRows(4);
		textArea.setEditable(false);
		textArea.setSize(new Dimension(400, 100));
		textArea.setLocation(20, 50);
		textArea.setVisible(false);
		p.add(textArea, new Integer(3));
		
		closeButton = new JButton(new ImageIcon("icons/close.gif"));
		
		closeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				textArea.setVisible(false);
				closeButton.setVisible(false);
			}
		});
		
		closeButton.setSize(new Dimension(16, 16));
		closeButton.setLocation(401, 52);
		closeButton.setBorderPainted(false);
		closeButton.setVisible(false);
		p.add(closeButton, new Integer(4));
		
		// Add the route finder controll panel to the pane
		p.add(routeFinder, new Integer(4));
		
		// Add the map pane to the layout
		map = new MapPane();
		map.setSize(new Dimension(1000, 1000));
		map.setLocation(0, 0);
		add(map, BorderLayout.CENTER);
		map.addIntersectionSelectedListener(this);
		
		p.add(map, new Integer(1));
		add(p, BorderLayout.CENTER);

		// Set the GUI
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(new Dimension(1000, 1000));
		setMaximumSize(new Dimension(1000, 1000));
		setVisible(true);
	}

	// //////////////////////////
	// File Parsing Functions //
	// //////////////////////////

	/**
	 * Used to load map data into the program, gets file objects as input and
	 * it takes the file object and parses the data at there location
	 * 
	 * @param nodesf - nodes data file
	 * @param roadsf - roads data file
	 * @param segmentsf - road segments data file
	 * @param polygonsf - polygon data file
	 */
	private void onLoad(File nodesf, File roadsf, File segmentsf, File polygonsf, File restrictionsf, File trafficLightsf) {
		// Create a dialog to display the progress of loading the data
		final JDialog progressDialog = new JDialog(GUI.this, "Loading...", true);
		progressDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		progressDialog.setSize(new Dimension(200, 100));

		JProgressBar progressBar = new JProgressBar();
		progressDialog.add(progressBar, BorderLayout.NORTH);

		progressBar.setValue(0);

		JLabel progressLabel = new JLabel("Parsing File Information");
		progressDialog.add(BorderLayout.SOUTH, progressLabel);

		new Thread(new Runnable() {
			@Override
			public void run() {
				progressDialog.pack();
				progressDialog.setVisible(true);
			}
		}).start();
		

		// Parse the files into arraylists of information
		ArrayList<String[]> nodes = parseFile(nodesf);
		ArrayList<String[]> roads = parseFile(roadsf);
		ArrayList<String[]> segments = parseFile(segmentsf);
		String polygonFile = polygonsf != null ? getFileText(polygonsf) : null;
		ArrayList<String[]> restrictions = parseFile(restrictionsf);
		ArrayList<String[]> trafficLights = parseFile(trafficLightsf);

		// Create the maps that will hold all the parsed data
		HashMap<Integer, Node> nodesMap = new HashMap<Integer, Node>();
		HashMap<Integer, Road> roadsMap = new HashMap<Integer, Road>();
		HashMap<String, Road> roadsNameMap = new HashMap<String, Road>();

		ArrayList<Polygon> polygonsArray = null;

		// Update the progress
		progressBar.setValue(10);
		progressLabel.setText("Processing Polygon Files");

		if (polygonsf != null) {
			// Read in all the data and then extract the road names
			polygonsArray = new ArrayList<Polygon>();

			String[] sections = polygonFile.split("POLYGON");

			for (int i = 1; i < sections.length; i++) {
				String section = sections[i];
				String parsed = section.split("END")[0].trim();
				String trimmed = parsed.substring(1, parsed.length() - 1);
				polygonsArray.add(new Polygon(trimmed.trim()));
			}
		}

		progressLabel.setText("Loading Road, Node and Segment Files");
		progressBar.setValue(20);

		double minX = Integer.MAX_VALUE;
		double maxX = 0;
		double minY = Integer.MAX_VALUE;
		double maxY = 0;

		// Tell user what we are doing
		progressBar.setValue(20);
		progressLabel.setText("Parsing Node Data");
		// progressBar.setValue(40);

		// Read in all the nodes
		for (String[] nodeInfo : nodes) {
			// Read and parse all the node info
			int id = Integer.parseInt(nodeInfo[0]);
			double lat = Double.parseDouble(nodeInfo[1]);
			double lon = Double.parseDouble(nodeInfo[2]);

			// Create the node object
			Node n = new Node(id, lat, lon);
			Point l = n.getPoint();

			// Use location to calculate area of coordanates
			if (l.x < minX) {
				minX = l.x;
			} else if (l.x > maxX) {
				maxX = l.x;
			}

			if (l.y < minY) {
				minY = l.y;
			} else if (l.y > maxY) {
				maxY = l.y;
			}

			// Store the node in the hash map
			nodesMap.put(id, n);
		}
		
		// Load the restrictions
		for (String[] restrictionInfo : restrictions) {
			// We only need the node ID's
			int fromID = Integer.parseInt(restrictionInfo[0]);
			int intersectionID = Integer.parseInt(restrictionInfo[2]);
			int toID = Integer.parseInt(restrictionInfo[4]);
			
			Node from = nodesMap.get(fromID);
			Node intersection = nodesMap.get(intersectionID);
			Node to = nodesMap.get(toID);

			intersection.addRestriction(from, to);
		}

		// Tell user what we are doing
		progressBar.setValue(40);
		progressLabel.setText("Parsing Road Data");
		// progressBar.setValue(60);

		// Read in all the road information
		for (String[] roadInfo : roads) {
			// Read and parse all the road info
			int id = Integer.parseInt(roadInfo[0]);
			int type = Integer.parseInt(roadInfo[1]);
			String label = roadInfo[2];
			String city = roadInfo[3];
			boolean oneway = Integer.parseInt(roadInfo[4]) == 1 ? true : false;
			int speedClass = Integer.parseInt(roadInfo[5]);
			int speed = 0;
			
			switch(speedClass) {
			case 0:
				speed = 5;
				break;
				
			case 1:
				speed = 20;
				break;
				
			case 2:
				speed = 40;
				break;
			
			case 3:
				speed = 60;
				break;
				
			case 4:
				speed = 80;
				break;
				
			case 5:
				speed = 100;
				break;
				
			case 6:
				speed = 110;
				break;
				
			case 7:
				speed = 200;
				break;
			}

			// Get the road class
			RoadClass roadClass;
			switch (Integer.parseInt(roadInfo[6])) {
			case 1:
				roadClass = RoadClass.Residential;
				break;

			case 2:
				roadClass = RoadClass.Collector;
				break;

			case 3:
				roadClass = RoadClass.Arterial;
				break;

			case 4:
				roadClass = RoadClass.PrincipalHW;
				break;

			case 5:
				roadClass = RoadClass.MajorHW;
				break;

			default:
				roadClass = RoadClass.Unknown;
				break;
			}

			boolean notForCar = Integer.parseInt(roadInfo[7]) == 1 ? true
					: false;
			boolean notForPedestrians = Integer.parseInt(roadInfo[8]) == 1 ? true
					: false;
			boolean notForBicycle = Integer.parseInt(roadInfo[9]) == 1 ? true
					: false;

			// Create the road obect
			Road r = new Road(id, type, label, city, oneway, speed, roadClass,
					notForCar, notForPedestrians, notForBicycle);

			// Insert the road into the map
			roadsMap.put(id, r);

			roadsNameMap.put(label, r);
		}

		// Tell user what we are doing
		progressBar.setValue(60);
		progressLabel.setText("Parsing Segment Data");
		// progressBar.setValue(80);

		// Read in all the segments
		for (String[] segmentInfo : segments) {
			int roadId = Integer.parseInt(segmentInfo[0]);
			double length = Double.parseDouble(segmentInfo[1]);
			int nodeID1 = Integer.parseInt(segmentInfo[2]);
			int nodeID2 = Integer.parseInt(segmentInfo[3]);

			// Collect the coordernates
			double[][] coords = new double[(segmentInfo.length - 4) / 2][2];
			for (int i = 4; i < segmentInfo.length; i += 2) {
				coords[(i - 4) / 2] = new double[] {
						Double.parseDouble(segmentInfo[i]),
						Double.parseDouble(segmentInfo[i + 1]) };
			}

			// Get the required objects from the maps
			Node n1 = nodesMap.get(nodeID1);
			Node n2 = nodesMap.get(nodeID2);
			Road r = roadsMap.get(roadId);

			// Create the segment object
			Segment s = new Segment(n1, n2, length, coords);

			// Add segment to road
			r.addSegment(s);
		}

		// Create a graph with the parsed data
		RoadGraph g = new RoadGraph(nodesMap, roadsMap, roadsNameMap);

		progressBar.setValue(80);
		progressLabel.setText("Creating Quad Tree");
		// progressBar.setValue(90);

		progressDialog.setVisible(false);

		// Create a Quad Tree with the parsed data
		QuadTree<Integer> quadTree = new QuadTree<Integer>((int) minX + (int) (minX * 0.01), (int) minY + (int) (minY * 0.01), (int) (maxY - minY) + (int) ((maxY - minY) * 0.01) , (int) (maxX - minX) + (int) ((maxX - minX) * 0.01));
		for (Node n : nodesMap.values()) {
			quadTree.add(n.getPoint(), new Integer(n.getID()));
		}
		
		// Parse the traffic lights
		for (String[] trafficLight : trafficLights) {
			// Convert the strings to the lon, lat doubles
			double lon = Double.parseDouble(trafficLight[0]);
			double lat = Double.parseDouble(trafficLight[1]);
			
			// Create a traffic light object
			TrafficLight light = new TrafficLight(lon, lat);
			
			// Get the closest node to the traffic light, using a try catch because the
			// data might include traffic lights that arnt on the map data im using
			Integer nodeID = null;
			try {
				nodeID = quadTree.get(light.getPoint());
			} catch (IndexOutOfBoundsException e) {}
			
			// If there is no node close then we dont need to worry
			if (nodeID == null) {
				continue;
			}
			
			// Get the node object and add the traffic light to it
			Node n = nodesMap.get(nodeID);
			n.addTrafficLight(light);
		}

		// Add all the road names into the Trie
		progressBar.setValue(90);
		progressLabel.setText("Adding Road Names To Trie");
		// progressBar.setValue(90);

		textField.clear();
		textField.add(roadsNameMap.keySet());

		map.load(g, quadTree, polygonsArray);

		// Get rid of the progress dialog
		progressBar.setValue(100);
		progressLabel.setText("FINISHED!");
		progressDialog.setVisible(false);
		
		buttons.setVisible(true);
		routeFinder.setVisible(true);
		textField.makeVisible();
		map.requestFocus();
	}

	private ArrayList<String[]> parseFile(File file) {
		ArrayList<String[]> info = new ArrayList<String[]>();

		String fileText = getFileText(file);
		String[] lines = fileText.split("\n");

		for (String line : lines) {
			info.add(line.split("\t"));
		}

		return info;
	}

	private String getFileText(File file) {
		// Create a string builder to hold the file text as its
		// read in
		StringBuilder b = new StringBuilder();

		try (BufferedReader in = Files.newBufferedReader(file.toPath(),
				StandardCharsets.UTF_8)) {
			String line = null;

			while ((line = in.readLine()) != null) {
				b.append(line + "\n");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return b.toString().trim();
	}

	// /////////////////////////////////////////////
	// Implemented From Auto Complete Text Field //
	// /////////////////////////////////////////////

	@Override
	public void availableCompletions(AutoCompletionEvent e) {
		System.out.println("Possible Completions For: " + e.getPartialString());

		map.setCompletions(e.getPartialString(), e.getCompletions());
	}

	// //////////////////////////////////
	// Intersection Selected Listener //
	// //////////////////////////////////

	@Override
	public void onIntersectionSelected(IntersectionSelectedEvent e) {
		Node n = e.getNode();

		Document d = textArea.getDocument();
		try {
			d.remove(0, d.getLength() - 1);
			d.insertString(0, n.toString(), null);
			
			textArea.setVisible(true);
			closeButton.setVisible(true);
		} catch (BadLocationException e1) {
			e1.printStackTrace();
		}

	}

	public static void main(String[] args) {
		new GUI();

	}
}
