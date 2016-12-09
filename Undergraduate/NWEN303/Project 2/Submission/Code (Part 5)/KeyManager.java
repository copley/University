

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.net.InterfaceAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by danielbraithwt on 10/1/16.
 */
public class KeyManager {

    // The fixed ammout of time that has to pass before a job is re clamed
    public static final int allowedJobTime = 60000;

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

    private Map<Long, Long> times;
    private Map<Long, Job> jobs;
    private ArrayList<Job> reclamed;

    private boolean exausted = false;

    private long jobNumber = 0;
    private BigInteger bi;
    private int keySize;
    private String cypherText;
    private String plainText;

    private class Job {
        public BigInteger key;
        public int size;
    }

    public KeyManager(BigInteger start, int ks, String ct, String pt) {
        bi = start;
        keySize = ks;
        cypherText = ct;
        plainText = pt;

        reclamed = new ArrayList<>();
        times = new HashMap<>();
        jobs = new HashMap<>();

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
        // Keep the event loop running until a solution has been found or until we have exausted
        // the key space and all jobs have been finished.
        while (!solutionFound && !(exausted && (jobs.size() == 0 && reclamed.size() == 0))) {
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

            // If this is the first job then start the timer
            if (jobNumber == 0) {
                clock.start();
            }

            handleConnection(client);

            // Check to see if we have exausted the key space
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
        reclameJobs();

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
                int cs = Integer.parseInt(chunksize);

                long id = jobNumber;
                jobNumber += 1;

                String key;
                int size = 0;

                // If there is a reclamed job then use that otherwise
                // create a new job to send
                if (reclamed.size() > 0) {
                    Job j = reclamed.remove(0);
                    key = j.key.toString();

                    // If there are more keys then the client rewuested then break
                    // the reclamed job up
                    if (j.size > cs) {
                        size = cs;
                        j.size = j.size - cs;
                        j.key = j.key.add(new BigInteger(String.format("%d", size)));
                        reclamed.add(j);
                    } else {
                        size = j.size;
                    }
                } else {
                    key = bi.toString();
                    size = cs;
                    bi = bi.add(new BigInteger(chunksize));
                }

                // Log the start time of this job and log the job
                times.put(id, System.currentTimeMillis());
                Job j = new Job();
                j.key = new BigInteger(key);
                j.size = Integer.parseInt(chunksize);
                jobs.put(id, j);

                // Send all required information to the client
                clientWrite.write(String.format("%d\n", id));
                clientWrite.write(cypherText + "\n");
                clientWrite.write(plainText + "\n");
                clientWrite.write(key + "\n");
                clientWrite.write(String.format("%d\n", size));
                clientWrite.write(String.format("%d\n", keySize));
                clientWrite.flush();

            } else if (type == SESSION_SUBMIT) {
                long id = Long.parseLong(clientRead.readLine());
                boolean s = Boolean.parseBoolean(clientRead.readLine());

                if (s) {
                    String key = clientRead.readLine();
                    System.out.println(key);

                    solutionFound = true;
                }

                // Remove the job from the times if its still there
                if (times.containsKey(id)) {
                    times.remove(id);
                    jobs.remove(id);
                }
            }

            client.close();
            clientRead.close();
            clientWrite.close();
        } catch (IOException e) {}
    }

    /**
     * Handles reclaming jobs, when called looks through all active jobs and
     * if the time has expired on one then it is reclamed
     */
    private void reclameJobs() {
        long t = System.currentTimeMillis();

        for (Long i : times.keySet()) {
            long j = times.get(i);

            if (t - j > allowedJobTime) {
                reclamed.add(jobs.get(i));

                times.remove(i);
                jobs.remove(i);
            }
        }
    }
}
