package Perceptron;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Daniel Braithwaite on 22/03/2016.
 */
public class PerceptronTest {
    public static void main(String[] args) throws IOException {
        String file = FileDialog.open();

        ArrayList<PBMImage> trainingImages = readImages(file);


        Perceptron p = new Perceptron(75, trainingImages.get(0).getWidth(), trainingImages.get(0).getHeight());

        int iterations = 1;
        boolean incorrect = true;
        while (incorrect && iterations < 100) {
            System.out.println("Itteration: " + iterations);

            iterations++;

            incorrect = false;
            int failures = 0;
            for (PBMImage image : trainingImages) {
                boolean res = p.train(image, image.getType());

                if (!res) {
                    failures++;
                    incorrect = true;
                }
            }

            System.out.println("Failures: " + failures + "\n");
        }

        System.out.println("\nTRAINING COMPLETE\n");

        while(true) {
            String testingFile = FileDialog.open();

            if (testingFile == null || testingFile.equals("")) {
                break;
            }

            ArrayList<PBMImage> testingData = readImages(testingFile);
            int correct = 0;
            int f = 0;

            for (PBMImage image : testingData) {
                int classification = p.classify(image);

                if (classification == image.getType()) {
                    correct++;
                } else {
                    System.out.println("Classification Failure: Got " + classification + " : Expected " + image.getType());

                    f++;
                }
            }

            System.out.println(String.format("Perceptron Accuracy: %f", (((double) correct) / ((double) testingData.size()))));
        }
    }

    public static ArrayList<PBMImage> readImages(String file) throws IOException{
        Scanner imageScanner = new Scanner(new File(file));

        ArrayList<PBMImage> images = new ArrayList<>();

        imageScanner.nextLine();
        while (imageScanner.hasNext()) {
            int type = imageScanner.nextLine().replace("#", "").toLowerCase().equals("yes") ? 1 : 0;
            int width = imageScanner.nextInt();
            int height = imageScanner.nextInt();

            imageScanner.nextLine();
            String tmp = "";
            String imageString = imageScanner.nextLine();
            while (imageScanner.hasNext() && !tmp.equals("P1")) {
                tmp = imageScanner.nextLine();
                imageString += tmp;
            }
            int k = 0;
            int[][] pixles = new int[width][height];
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    pixles[i][j] = Integer.parseInt(String.format("%c", imageString.charAt(k)));
                    k++;
                }
            }

            images.add(new PBMImage(pixles, width, height, type));
        }

        return images;
    }
}
