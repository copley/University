package Perceptron;

/**
 * Created by Daniel Braithwaite on 22/03/2016.
 */
public class PBMImage {
    int[][] image;
    int width;
    int height;
    int type;

    public PBMImage(int[][] image, int width, int height, int type) {
        this.image = image;
        this.type = type;
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getType() {
        return type;
    }

    public boolean get(int x, int y) {
        return image[x][y] == 1;
    }
}
