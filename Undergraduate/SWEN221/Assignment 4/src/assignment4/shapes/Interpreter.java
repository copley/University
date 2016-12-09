package assignment4.shapes;

import java.util.HashMap;
import java.util.Map;

public class Interpreter {
	private Map<String, Shape> variables;
	private Map<String, Shape> systemVaraibles;
	private String input;
	private int index;

	public Interpreter(String input) {
		variables = new HashMap<>();
		systemVaraibles = new HashMap<>();
		this.input = input;
		this.index = 0;
	}

	/**
	 * This method should return a canvas to which the input commands have been
	 * applied.
	 * 
	 * @return a canvas that shows the result of the input.
	 */
	public Canvas run() {
		Canvas canvas = new Canvas();

		// Repeat until we have parsed the entire string
		while (index < input.length()) {
			// Record where we are currently up to
			int tmpIndex = index;
			
			// If there is a bracket in this current line, then we need to deal with it
			while (input.substring(index, input.indexOf("\n", index)).contains("(")) {
				int i = input.indexOf("(");
				
				// Make sure that the bracket is on the current line
				if (i > input.indexOf("\n", index)) {
					break;
				}

				// Find the closing bracket that matches with this open bracket
				while (input.indexOf("(", i + 1) < input.indexOf(")", i + 1)) {
					if (input.indexOf("(", i + 1) == -1) {
						break;
					}

					i = input.indexOf("(", i + 1);
				}

				// Throw an error if there is no closing bracekt
				if (input.indexOf(")", i) == -1) {
					throw new IllegalArgumentException();
				}

				index = i + 1;

				Shape result = null;

				// Read in the shape
				skipWhiteSpace();
				Shape s1 = readShape();
				skipWhiteSpace();

				// If there is an operator then read the operator and the folowing shape
				// then apply the operator
				if (isNextOperator()) {
					String op = readChar();

					skipWhiteSpace();
					Shape s2 = readShape();

					result = applyOperator(s1, s2, op);

				} else {
					result = s1;
				}

				// Replace the expression contained inside the braceks with a sys
				// generated variable storing the result of the operation
				String key = String.format("#%d#", result.hashCode());
				systemVaraibles.put(key, result);

				input = input.substring(0, i) + key
						+ input.substring(input.indexOf(")", i)+1);

			}
			
			index = tmpIndex;

			// Now we can look at the next command
			evaluateNextCommand(canvas);
		}
		return canvas;
	}

	/**
	 * Evalulates the next command in the input and applys it to the canvas
	 * 
	 * @param canvas
	 */
	private void evaluateNextCommand(Canvas canvas) {
		skipWhiteSpace();
		String cmd = readWord();
		skipWhiteSpace();
		if (cmd.equals("fill")) {
			Shape shape = readShape();
			Color color = readColor();
			fillShape(shape, color, canvas);
		} else if (cmd.equals("draw")) {
			Shape shape = readShape();
			Color color = readColor();
			drawShape(shape, color, canvas);
		} else {
			skipWhiteSpace();

			// If the next is a '=' then we are reading a variable
			if (checkNext("=")) {
				skipPast("=");
				skipWhiteSpace();
				Shape shape = readShape();

				// Then check to see if there is an operation to peform
				skipWhiteSpace();
				if (isNextOperator()) {
					String op = readChar();
					skipWhiteSpace();
					Shape s2 = readShape();

					shape = applyOperator(shape, s2, op);
				}

				variables.put(cmd, shape);
			} else {
				throw new IllegalArgumentException("not valid syntax");
			}
		}
	}

	/**
	 * Applys a given operator to two shapes and returns the result
	 * 
	 * @param s1
	 * @param s2
	 * @param op
	 * @return Result of operation
	 */
	private Shape applyOperator(Shape s1, Shape s2, String op) {
		if (op.equals("+")) {
			return new ShapeUnion(s1, s2);
		} else if (op.equals("-")) {
			return new ShapeDiffrence(s1, s2);
		} else if (op.equals("&")) {
			return new ShapeIntersection(s1, s2);
		} else {
			// Invalid syntax
			throw new IllegalArgumentException(op
					+ " is not a valid shape operator");
		}
	}

	/**
	 * Skips to the first occorance of the stirng passed
	 * 
	 * @param skipTo
	 */
	private void skipPast(String skipTo) {
		index = input.indexOf(skipTo, index) + 1;

	}

	/**
	 * Checks the start of the remaining string
	 * 
	 * @param start
	 * @return Wether the start of the string matches what was passed
	 */
	private boolean checkNext(String start) {
		return (input.substring(index).startsWith(start));
	}

	/**
	 * Checks to see if the next charactor is a shape operator
	 * 
	 * @return Wether the next charactor is a shape operator
	 */
	private boolean isNextOperator() {
		return checkNext("+") || checkNext("-") || checkNext("&");
	}

	/**
	 * Reads the next word in the string
	 * @return Next word in the string
	 */
	private String readWord() {
		int start = index;
		while (index < input.length()
				&& Character.isLetter(input.charAt(index))) {
			index++;
		}
		return input.substring(start, index);
	}

	/**
	 * Peeks at the next word but dosnt advance the current place
	 * it the string
	 * 
	 * @return The next word in the stirng
	 */
	private String peekWord() {
		int start = index;
		while (index < input.length()
				&& Character.isLetter(input.charAt(index))) {
			index++;
		}

		int end = index;
		index = start;
		return input.substring(start, end);
	}

	/**
	 * Reads the next char in the input string
	 * 
	 * @return
	 */
	private String readChar() {
		String c = input.substring(index, index + 1);
		index++;

		return c;
	}

	/**
	 * Reads the next shape in the input string, could be a definition
	 * of a shape or could be a refrence to a varaible
	 * 
	 * @return Shape that was read
	 */
	private Shape readShape() {
		skipWhiteSpace();
		
		// Check to see if its a variable name
		String name = peekWord();

		if (name.length() != 0) {
			// It should be a user set varaible
			if (variables.containsKey(name)) {
				return variables.get(readWord());
			}

			// Otherwise there is no variable with this label
			throw new IllegalArgumentException();
		} else if (checkNext("#")) {
			// Othersie it could be a system varaible, one created when
			// parsing brackets
			name = input.substring(index, input.indexOf("#", index + 1)+1);
			
			if (systemVaraibles.containsKey(name)) {
				index = input.indexOf("#", index + 1)+1;
				
				return systemVaraibles.get(name);
			}
		}
		
		

		int start = input.indexOf("[", index) + 1;
		int end = input.indexOf("]", start);

		String[] shapeInfo = input.substring(start, end).split(",");
		
		if (shapeInfo.length != 4) {
			throw new IllegalArgumentException();
		}

		int x = Integer.parseInt(shapeInfo[0].trim());
		int y = Integer.parseInt(shapeInfo[1].trim());
		int width = Integer.parseInt(shapeInfo[2].trim());
		int height = Integer.parseInt(shapeInfo[3].trim());

		if (width < 0 || height < 0) {
			throw new IllegalArgumentException();
		}

		index = end + 1;

		return new Rectangle(x, y, width, height);
	}
	
	/**
	 * Reads the next color in the input string
	 * 
	 * @return Color read from input string
	 */
	private Color readColor() {
		int start = input.indexOf("#", index);
		int end = start + 7;

		index = end + 1;

		return new Color(input.substring(start, end));
	}

	/**
	 * Skips all the white space folowing the current position
	 */
	private void skipWhiteSpace() {
		while (index < input.length()
				&& (input.charAt(index) == ' ' || input.charAt(index) == '\n')) {
			index = index + 1;
		}
	}

	/**
	 * Fills a shape on the canvas with the given color
	 * 
	 * @param shape
	 * @param color
	 * @param canvas
	 */
	public static void fillShape(Shape shape, Color color, Canvas canvas) {
		Rectangle bounding = shape.boundingBox();

		for (int i = bounding.x(); i < bounding.x() + bounding.width(); i++) {
			for (int j = bounding.y(); j < bounding.y() + bounding.height(); j++) {
				if (shape.contains(i, j)) {
					canvas.draw(i, j, color);
				}
			}
		}
	}

	/**
	 * Draws a given shape to the canvas with the given color
	 * 
	 * @param shape
	 * @param color
	 * @param canvas
	 */
	private void drawShape(Shape shape, Color color, Canvas canvas) {
		Rectangle bounding = shape.boundingBox();

		boolean inside = false;
		for (int i = bounding.x(); i < bounding.x() + bounding.width(); i++) {
			inside = false;
			for (int j = bounding.y(); j < bounding.y() + bounding.height(); j++) {
				if (shape.contains(i, j) && !inside) {
					canvas.draw(i, j, color);
					inside = true;
				} else if (!shape.contains(i, j + 1) && inside) {
					canvas.draw(i, j, color);
					inside = false;
				}
			}
		}

		inside = false;
		for (int j = bounding.y(); j < bounding.y() + bounding.height(); j++) {
			inside = false;
			for (int i = bounding.x(); i < bounding.x() + bounding.width(); i++) {
				if (shape.contains(i, j) && !inside) {
					canvas.draw(i, j, color);
					inside = true;
				} else if (!shape.contains(i + 1, j) && inside) {
					canvas.draw(i, j, color);
					inside = false;
				}
			}
		}
	}
}
