package assignment4.shapes;

public class ShapeDiffrence extends ShapeOperator {

	public ShapeDiffrence(Shape s1, Shape s2) {
		super(s1, s2);
	}

	@Override
	public boolean contains(int x, int y) {
		return shape1.contains(x, y) && !shape2.contains(x, y);
	}

	@Override
	public Rectangle boundingBox() {
		return shape1.boundingBox();
	}

}
