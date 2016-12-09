import assignment4.shapes.Canvas;
import assignment4.shapes.Color;
import assignment4.shapes.Interpreter;
import assignment4.shapes.Rectangle;
import assignment4.shapes.Shape;


public class Main {
	
	public static void main(String[] args) {
		Interpreter i = new Interpreter("x = [10, 10, 50, 50]\nfill x #00ff00");//("fill [10, 10, 50, 50] #000000");
		Canvas c = i.run();
		c.show();
	}

}
