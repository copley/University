package assignment4.shapes;

public abstract class ShapeOperator implements Shape {

	protected Shape shape1;
	protected Shape shape2;
	
	public ShapeOperator(Shape s1, Shape s2) {
		shape1 = s1;
		shape2 = s2;
	}
}
