package assignment4.shapes;

public class ShapeUnion extends ShapeOperator {

	public ShapeUnion(Shape s1, Shape s2) {
		super(s1, s2);
	}

	@Override
	public boolean contains(int x, int y) {
		return shape1.contains(x, y) || shape2.contains(x, y);
	}

	@Override
	public Rectangle boundingBox() {
		Rectangle b1 = shape1.boundingBox();
		Rectangle b2 = shape2.boundingBox();
		
		int minX = Math.min(b1.x(), b2.x());
		int minY = Math.min(b1.y(), b2.y());
		
		int maxX = Math.max(b1.x(), b2.x());
		int maxY = Math.max(b1.y(), b2.y());
		
		int width = maxX + (maxX == b1.x() ? b1.width() : b2.width());
		int height = maxY + (maxY == b1.y() ? b1.height() : b2.height());
		
		return new Rectangle(minX, minY, width, height);
		
	}

}
