import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class BoardRipper {
	private static final int WIDTH_SQUARES = 24;
	private static final int HEIGHT_SQUARES = 25;
	
	private static final int WIDTH_OFFSET = 0;
	
	public static void main(String[] args) {
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File("../Cludo/board.png"));
		} catch (IOException e) {
			System.out.println("Failed to read image");
			return;
		}
		
		System.out.println("Height: " + img.getHeight());
		System.out.println("Width: " + img.getWidth());
		
		int squareWidth = ((img.getWidth() - WIDTH_OFFSET) / WIDTH_SQUARES);
		int squareHeight = ((img.getHeight()) / HEIGHT_SQUARES);
		
		System.out.println(squareWidth);
		System.out.println(squareHeight);
		
		System.out.println((img.getWidth() - WIDTH_OFFSET) / squareWidth);
		System.out.println(img.getHeight() / squareHeight);
		
		int x = 0;
		int y = 0;
		
		for (int i = (WIDTH_OFFSET/2); i < img.getWidth() - (WIDTH_OFFSET/2); i += squareWidth) {
			for (int j = 0; j < img.getHeight(); j += squareHeight) {
				try {
					BufferedImage square = new BufferedImage(squareWidth, squareHeight, BufferedImage.TYPE_INT_RGB);
					
					for (int w = 0; w < squareWidth; w++) {
						for (int h = 0; h < squareHeight; h++) {
							if (i + w >= img.getWidth() || j+h >= img.getHeight()) {
								square.setRGB(w, h, Color.BLACK.getRGB());
								continue;
							}
							
							square.setRGB(w, h, img.getRGB(i + w, j + h));
						}
					}
					
					BufferedImage biggerSquare = resizeImage(square, BufferedImage.TYPE_INT_RGB, 20, 20);
					ImageIO.write(biggerSquare, "png", new File("../Cludo/board/" + String.format("square_%d_%d.png", x, y)));
				} catch (IOException e) {
					
				}
				
				y++;
			}
			
			x++;
			y = 0;
		}
	}
	
	private static BufferedImage resizeImage(BufferedImage originalImage, int type, int IMG_WIDTH, int IMG_HEIGHT) {
	    BufferedImage resizedImage = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, type);
	    Graphics2D g = resizedImage.createGraphics();
	    g.drawImage(originalImage, 0, 0, IMG_WIDTH, IMG_HEIGHT, null);
	    g.dispose();

	    return resizedImage;
	}
}
