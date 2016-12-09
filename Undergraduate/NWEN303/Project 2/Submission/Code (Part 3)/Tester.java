import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;

/**
 * Created by danielbraithwt on 10/1/16.
 */
public class Tester {
    public static BigInteger MANAGER_START = BigInteger.ZERO;
    public static int MANAGER_KEY_SIZE = 4;
    public static String MANAGER_CYPHER_TEXT = "gGwv3ZSYo0J/1g6yx3aAfr0Nibv/ykgYrSdFQcMXO7fKpC8/S2ym5w==";
    public static String MANAGER_PLAIN_TEXT = "May good flourish; Kia hua ko te pai";

    public static String CLIENT_IP = "localhost";
    public static int CLIENT_PORT = 38369;

    public static int REPITIONS = 30;

    public static void main(String[] args) {
        int clients = Integer.parseInt(args[0]);
        int size = Integer.parseInt(args[1]);
        int delay = Integer.parseInt(args[2]);
        int sdelay = Integer.parseInt(args[3]);

        DataPoint data = runTestInsance(clients, size, delay, sdelay);

        System.out.println("Mean Time: " + data.mean );
        System.out.println("Std Div: " + data.stdDiv);
    }


    /**
     * Runs a test for 30 replications with the given paramaters
     * @param clients the number of clients to create
     * @oaram size chunk size for all the clients
     * @params delay delay between creating the clients
     * @params sdelay delay at the start of the test
     */
    public static DataPoint runTestInsance(int clients, int chunkSize, int delay, int sdelay) {
        long totalTime = 0;
        ArrayList<Long> times = new ArrayList<>();

        for (int i = 0; i < REPITIONS; i++) {
            System.out.println("Running Test: " + i);
            long t = runTest(clients, chunkSize, delay, sdelay);

            totalTime += t;
            times.add(t);
        }

        long mean = totalTime/REPITIONS;

        // Compute the std div
        long SSD = 0;
        for (Long time : times) {
            SSD += Math.pow(mean - time, 2);
        }

        double stdDiv = Math.sqrt(SSD);

        return new DataPoint(mean, stdDiv);
    }

    /**
     * Runs a single paramater with the given paramaters
     *
     * @params clients number of clients to create
     * @params chunkSize the chunk size for each of the clients
     * @params delay delay between creating the clients
     * @params sdelay delay at the start of the test
     */
    public static long runTest(int clients, final int chunkSize, int delay, int sdelay) {
        try {
            Thread.sleep(sdelay);
        } catch (InterruptedException e) {}

        final KeyManager keyManager = new KeyManager(new BigInteger(MANAGER_START.toString()),
                MANAGER_KEY_SIZE, MANAGER_CYPHER_TEXT,
                MANAGER_PLAIN_TEXT);

        // Start the key manager on a new thread
        Thread manager = new Thread(new Runnable() {
            @Override
            public void run() {
                keyManager.run();
            }
        });
        manager.start();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {}


        for (int i = 0; i < clients; i++) {
            try {
                Runtime.getRuntime().exec(String.format("java Client %s %d %d", CLIENT_IP, CLIENT_PORT, chunkSize));
            } catch (IOException e) {
                System.out.println("Failed to create process");
            }

            if (delay != 0) {
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {

                }
            }
        }

        // Join back into the thread running the key manager so when it finishes
        // we continue 
        System.out.println("Joinning into manager thread");
        try {
            manager.join();
        } catch (InterruptedException e) {
            throw new RuntimeException();
        }

        System.out.println("Test Completed");
        return keyManager.clock.getElapsedTimeSecs();
    }

    private static class DataPoint {
        public long mean;
        public double stdDiv;

        public DataPoint(long m, double s) {
            mean = m;
            stdDiv = s;
        }
    }
}
