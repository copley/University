package Graph;

public class Restriction {
	
	private Node from;
	private Node to;
	
	public Restriction(Node f, Node t) {
		from = f;
		to = t;
	}
	
	public boolean isRouteAllowed(Node f, Node t) {
		if (f == null || t == null) {
			return true;
		}
		
		return !(f.getID() == from.getID() && t.getID() == to.getID());
	}

}
