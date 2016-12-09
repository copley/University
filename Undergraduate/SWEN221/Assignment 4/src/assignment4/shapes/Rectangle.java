package assignment4.shapes;

public class Rectangle implements Shape {

	private int x;
	private int y;
	private int width;
	private int height;
	
	public Rectangle(int x, int y, int w, int h) {
		this.x = x;
		this.y = y;
		this.width = w;
		this.height = h;
	}
	
	@Override
	public boolean contains(int x, int y) {
		return (x  >= this.x && y >= this.y && x < (this.x + this.width) && y < (this.y + this.height));
	}

	@Override
	public Rectangle boundingBox() {
		return new Rectangle(x, y, width, height);
	}
	
	/** 
	 * @return to left X
	 */
	public int x() {
		return x;
	}
	
	/**
	 * @return Top left Y
	 */
	public int y() {
		return y;
	}
	
	/**
	 * @return Width of the rectangle
	 */
	public int width() {
		return width;
	}
	
	/**
	 * @return Height of the rectangle
	 */
	public int height() {
		return height;
	}

}
