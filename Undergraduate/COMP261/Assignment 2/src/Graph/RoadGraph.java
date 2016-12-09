package Graph;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Stack;

import UnionFind.UnionFind;
import UnionFind.UnionFindElement;
import AStar.Metric;
import GUI.TransportType;

public class RoadGraph {

	private HashMap<Integer, Node> nodesMap;
	private HashMap<Integer, Road> roadsMap;
	private HashMap<String, Road> roadsNameMap;
	private ArrayList<Node> articulationPoints;
	private UnionFind disjointSets;

	public RoadGraph(HashMap<Integer, Node> nodes,
			HashMap<Integer, Road> roads, HashMap<String, Road> roadsName) {
		// Initilise maps
		nodesMap = nodes;
		roadsMap = roads;
		roadsNameMap = roadsName;

		disjointSets = new UnionFind(nodes.values());

		articulationPoints = findArticulationPoints();

		System.out.println("Number of articulation points found (ITTER): "
				+ articulationPoints.size());
		//System.out.println("Number of articulation points found (REC) : "
		//		+ findArticulationPointsRec().size());
	}

	public Road getRoadByName(String name) {
		if (roadsNameMap.containsKey(name)) {
			return roadsNameMap.get(name);
		}

		return null;
	}

	public Node getNodeByID(Integer id) {

		if (nodesMap.containsKey(id)) {
			return nodesMap.get(id);
		}

		return null;
	}

	public void draw(Graphics2D g2d, String partial,
			ArrayList<String> possibleCompletions, int currentNode) {
		for (Road r : roadsMap.values()) {
			g2d.setColor(r.getRoadColor());

			if (possibleCompletions.contains(r.getLabel())) {
				g2d.setColor(Color.GREEN);

				if (possibleCompletions.size() == 1) {
					g2d.setColor(Color.CYAN);
				}
			}

			if (r.getLabel().equals(partial)) {
				g2d.setColor(Color.RED);
			}

			r.draw(g2d);
		}

		for (Node n : nodesMap.values()) {
			if (g2d.getClipBounds().contains(n.getPoint().x, n.getPoint().y)) {
				if (n.isArticulationPoint()) {
					g2d.setColor(Color.PINK);
				} else {
					g2d.setColor(Color.BLUE);
				}

				if (currentNode == n.getID()) {
					g2d.setColor(Color.YELLOW);
				}

				n.draw(g2d);
			}
		}
	}

	public ArrayList<Segment> getRoute(Node start, Node finish, Metric<Node> m, TransportType type) {
		double bestToGoal = Double.POSITIVE_INFINITY;

		// Go through all the nodes and reset the visited flag
		for (Node n : nodesMap.values()) {
			//n.setVisisted(false);
			n.setCost(Double.POSITIVE_INFINITY);
			//n.setPathLength(Double.POSITIVE_INFINITY);
		}

		// Create a priority queue for the queue nodes with a comparator
		// that means the one with the best total cost to goal will be popped
		// off first, if two have the same cost then it will return the one with
		// less traffic lights
		PriorityQueue<AStarQueueNode> fringe = new PriorityQueue<AStarQueueNode>(1, new Comparator<AStarQueueNode>() {
					private static final double THRESHOLD = 200;
			
					@Override
					public int compare(AStarQueueNode arg0, AStarQueueNode arg1) {
						// If the total costs are similar enough then base decision off traffic lights
						if (Math.abs(arg0.totalCostToGoal - arg1.totalCostToGoal) < THRESHOLD) {
							return arg0.trafficLightCount - arg1.trafficLightCount;
						}
						
						// return (int) (arg1.totalCostToGoal -
						// arg0.totalCostToGoal);
						if (arg1.totalCostToGoal < arg0.totalCostToGoal) {
							return 1;
						} else if (arg1.totalCostToGoal > arg0.totalCostToGoal) {
							return -1;
						} else {
							// Shouldent be reached;
							return 0;
						}
					}

				});

		// Add the start node to the fringes
		AStarQueueNode root = new AStarQueueNode();
		root.node = start;
		root.trafficLightCount = start.hasTrafficLights() ? 1 : 0;
		root.costToHere = 0;
		root.totalCostToGoal = m.estimate(start, finish);

		fringe.add(root);

		while (fringe.size() > 0) {
			AStarQueueNode n = fringe.poll();

			// If we have visited the node then just go to the next one
			//if (n.node.isVisited()) {
			//	continue;
			//}

			if (n.costToHere < n.node.getCost()) {

				//n.node.setVisisted(true);
				n.node.setFromNode(n.from);
				n.node.setCost(n.costToHere);

				if (n.node.getID() == finish.getID()) {
					break;
				}

				// Go through all the nabours of the node n and add them
				// to the fringe
				for (Segment s : n.node.getSegments()) {
					// Set the neighbours from node
					Node neighbour = s.getOtherNode(n.node);

					// Make sure that the transport type can travle down the segment, and
					// make sure that
					// if its a one way road then we are travling the wrong way
					// down a road
					if (type == TransportType.CAR && !s.getRoad().carsAllowed()) {
						continue;
					} else if (type == TransportType.BIKE && !s.getRoad().bikesAllowed()) {
						continue;
					} else if (type == TransportType.PERSON && !s.getRoad().personsAllowed()) {
						continue;
					} else if (s.getRoad().isOneWay()
							&& s.getStartNode().getID() != n.node.getID()) {
						continue;
					} else if (!n.node.routeAllowed(n.from, neighbour)) {
						continue;
					}

					// neighbour.setFromNode(n.node);
					// Calculate the costs
					double costToNeighbour = n.costToHere + m.estimate(n.node, neighbour);//s.getLength();

					if (costToNeighbour < neighbour.getCost()) {

						double estimatedTotalCost = costToNeighbour
								+ m.estimate(neighbour, finish);

						if (estimatedTotalCost < bestToGoal) {
							// Add the neighbour to the fringe
							AStarQueueNode neighbourNode = new AStarQueueNode();
							neighbourNode.node = neighbour;
							neighbourNode.from = n.node;
							neighbourNode.trafficLightCount = n.trafficLightCount + (neighbour.hasTrafficLights() ? 1 : 0);
							neighbourNode.costToHere = costToNeighbour;
							neighbourNode.totalCostToGoal = estimatedTotalCost;

							fringe.add(neighbourNode);

							if (neighbour.getID() == finish.getID()) {
								bestToGoal = costToNeighbour;
							}
						}
					}

					// if (!neighbour.isVisited()) {
					// // Calculate the costs
					// double costToNeighbour = n.costToHere + s.getLength();
					// double estimatedTotalCost = costToNeighbour
					// + m.estimate(neighbour, finish);
					//
					// // Add the neighbour to the fringe
					// AStarQueueNode neighbourNode = new AStarQueueNode();
					// neighbourNode.node = neighbour;
					// neighbourNode.from = n.node;
					// neighbourNode.costToHere = costToNeighbour;
					// neighbourNode.totalCostToGoal = estimatedTotalCost;
					//
					// fringe.add(neighbourNode);
					// }
				}
			}
		}

		// Construct the route as a sequence of segments
		ArrayList<Segment> route = new ArrayList<Segment>();

		Node current = finish;
		Node next = current.getFromNode();
		while (next != null) {
			route.add(0, current.segmentLink(next));
			current = next;
			next = current.getFromNode();
		}

		return route;
	}

	private ArrayList<Node> findArticulationPointsRec() {
		for (Node n : nodesMap.values()) {
			n.setDepth(Integer.MAX_VALUE);
		}

		ArrayList<Node> points = new ArrayList<Node>();

		for (UnionFindElement e : disjointSets) {
			int numSubtrees = 0;

			// Get the root node, dosnt matter what we choose
			Node root = (Node) nodesMap.get(e.getID());
			root.setDepth(0);

			for (Node n : root.getNeighbour()) {
				if (n.getDepth() == Integer.MAX_VALUE) {
					findArticulationPointsRec(n, 1, root, points);
					numSubtrees++;
				}
			}

			if (numSubtrees > 1) {
				points.add(root);
			}
		}

		return points;
	}

	private int findArticulationPointsRec(Node n, int depth, Node f,
			ArrayList<Node> points) {
		n.setDepth(depth);
		int reachBack = depth;

		for (Node neigh : n.getNeighbour()) {
			if (neigh.getID() != f.getID()) {
				if (neigh.getDepth() < Integer.MAX_VALUE) {
					reachBack = Math.min(neigh.getDepth(), reachBack);
				} else {
					int childReach = findArticulationPointsRec(neigh,
							depth + 1, n, points);

					if (childReach >= depth) {
						points.add(n);
						n.setArticulationPoint(true);
					}

					reachBack = Math.min(childReach, reachBack);
				}
			}
		}

		return reachBack;
	}

	private ArrayList<Node> findArticulationPoints() {
		ArrayList<Node> points = new ArrayList<Node>();

		// Get the root node, dosnt matter what we choose
		// Node root = (Node) nodesMap.values().toArray()[0];

		// Set all the depths to infinity
		for (Node n : nodesMap.values()) {
			n.setDepth(Integer.MAX_VALUE);
		}

		for (UnionFindElement e : disjointSets) {
			// Get the root element
			Node root = nodesMap.get(e.getID());

			int subtrees = 0;
			root.setDepth(0);

			for (Node n : root.getNeighbour()) {
				if (n.getDepth() == Integer.MAX_VALUE) {
					subtrees++;
					findArticulationPoints(n, root, points);
				}
			}

			if (subtrees > 1) {
				points.add(root);
				root.setArticulationPoint(true);
			}
		}

		return points;
	}

	private void findArticulationPoints(Node start, Node root,
			ArrayList<Node> points) {
		Stack<ArticulationPointQueueNode> stack = new Stack<ArticulationPointQueueNode>();

		// Add the root to the stack
		// start.setDepth(1);
		ArticulationPointQueueNode rootElem = new ArticulationPointQueueNode();
		rootElem.node = start;
		rootElem.parent = root;
		rootElem.neighbours = null;
		rootElem.depth = 1;

		stack.push(rootElem);

		while (stack.size() > 0) {
			ArticulationPointQueueNode current = stack.peek();

			if (current.neighbours == null) {
				current.node.setDepth(current.depth);
				current.node.setReach(current.depth);

				current.neighbours = new ArrayList<Node>();
				for (Node n : current.node.getNeighbour()) {
					if (n.getID() != current.parent.getID()) {
						current.neighbours.add(n);
					}
				}

			} else if (current.neighbours != null
					&& current.neighbours.size() != 0) {
				Node n = current.neighbours.remove(0);

				if (n.getDepth() < Integer.MAX_VALUE) {
					current.node.setReach(Math.min(n.getDepth(),
							current.node.getReach()));
				} else {
					ArticulationPointQueueNode neighbourElem = new ArticulationPointQueueNode();
					n.setDepth(current.node.getDepth() + 1);

					neighbourElem.node = n;
					neighbourElem.parent = current.node;
					neighbourElem.neighbours = null;
					neighbourElem.depth = current.node.getDepth() + 1;

					stack.push(neighbourElem);

				}
			} else {
				if (current.node != start) {
					if (current.node.getReach() >= current.parent.getDepth()) {
						points.add(current.parent);
						current.parent.setArticulationPoint(true);
					}

					current.parent.setReach(Math.min(current.parent.getReach(),
							current.node.getReach()));
				}

				stack.pop();
			}
		}
	}

	private class AStarQueueNode {
		public Node node;
		public Node from;
		public int trafficLightCount;
		public double costToHere;
		public double totalCostToGoal;
	}

	private class ArticulationPointQueueNode {
		public Node node;
		public Node parent;
		public int depth;
		public ArrayList<Node> neighbours;
	}
}
