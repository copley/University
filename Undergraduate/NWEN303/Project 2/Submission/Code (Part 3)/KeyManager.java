import com.sun.org.apache.xpath.internal.operations.Bool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by danielbraithwt on 10/1/16.
 */
public class KeyManager {

    public static void main(String[] args) {
        BigInteger start = new BigInteger(args[0]);
        int ks = Integer.parseInt(args[1]);
        String ct = args[2];
        String pt = args[3];

        KeyManager manager = new KeyManager(start, ks, ct, pt);
        manager.run();
    }

    public static int SESSION_REQUEST = 0;
    public static int SESSION_SUBMIT = 1;

    private ServerSocket socket = null;
    private boolean solutionFound;

    public StopWatch clock;

    private boolean exausted = false;
    private long jobsRequested = 0;
    private long jobsReceived = 0;

    private BigInteger bi;
    private int keySize;
    private String cypherText;
    private String plainText;

    public KeyManager(BigInteger start, int ks, String ct, String pt) {
        bi = start;
        keySize = ks;
        cypherText = ct;
        plainText = pt;

        clock = new StopWatch();

        try {
            socket = new ServerSocket(38369);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create socket");
        }

        System.out.println("Waiting for connections on " + socket.getLocalPort());
    }

    /**
     * Run method is the main event loop for the key manager
     */
    public void run() {
        while (!solutionFound && !(exausted && jobsReceived == jobsRequested)) {
            Socket client = null;
            try {
                client = socket.accept();
                if (solutionFound) {
                    client.close();
                    continue;
                }
            }
            catch (IOException e) {
                System.out.println("Connection Failed");
            }

            if (jobsRequested == 0) {
                clock.start();
            }

            handleConnection(client);

            // Ensure we havent exceeded the bounds of the key
            if (bi.compareTo(BigInteger.valueOf(2).pow(keySize*8)) > 1) {
                exausted = true;
            }
        }

        if (exausted) {
            System.out.println("Key Space Exausted, Key Not Found");
        }

        clock.stop();
        System.out.println("Key Manager Exiting");
        System.out.println("Total search time: " + clock.getElapsedTimeSecs() + "s");

        try {
            socket.close();
        } catch (IOException e) {

        }
    }


    /**
     * Given a connection to a client this handles comuincation with the client, sending
     * and receiving job information.
     * @param client
     */
    private void handleConnection(Socket client) {
        try {
            BufferedReader clientRead = new BufferedReader(new InputStreamReader(client.getInputStream()));
            OutputStreamWriter clientWrite = new OutputStreamWriter(client.getOutputStream());

            int type = Integer.parseInt(clientRead.readLine());
            if (type == SESSION_REQUEST) {

                // Key space exausted so cant hand out more jobs
                if (exausted) {
                    client.close();
                    clientRead.close();
                    clientWrite.close();

                    return;
                }

                String chunksize = clientRead.readLine();

                clientWrite.write(cypherText + "\n");
                clientWrite.write(plainText + "\n");
                clientWrite.write(bi.toString() + "\n");
                clientWrite.write(String.format("%d\n", keySize));
                clientWrite.flush();

                bi = bi.add(new BigInteger(chunksize));

                jobsRequested++;
            } else if (type == SESSION_SUBMIT) {
                boolean s = Boolean.parseBoolean(clientRead.readLine());

                if (s) {
                    String key = clientRead.readLine();
                    System.out.println(key);

                    solutionFound = true;
                }

                jobsReceived++;
            }

            client.close();
            clientRead.close();
            clientWrite.close();
        } catch (IOException e) {}
    }
}
