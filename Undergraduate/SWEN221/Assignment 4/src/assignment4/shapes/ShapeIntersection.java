package assignment4.shapes;

public class ShapeIntersection extends ShapeOperator {

	public ShapeIntersection(Shape s1, Shape s2) {
		super(s1, s2);
	}

	@Override
	public boolean contains(int x, int y) {
		return shape1.contains(x, y) && shape2.contains(x, y);
	}

	@Override
	public Rectangle boundingBox() {
		Rectangle b1 = shape1.boundingBox();
		Rectangle b2 = shape2.boundingBox();

		int maxX = Math.max(b1.x(), b2.x());
		int maxY = Math.max(b1.y(), b2.y());

		int width = 0;
		int height = 0;

		if (maxX != b1.x()) {
			width = Math.abs(b1.width() - maxX); 
		} else {
			width = Math.abs(b2.width() - maxX); 
		}

		if (maxY != b1.y()) {
			height = Math.abs(b1.height() - maxY);
		} else {
			height = Math.abs(b2.height() - maxY);
		}

		return new Rectangle(maxX, maxY, width, height);
	}

}
