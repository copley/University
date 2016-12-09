import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.Socket;

/**
 * Created by danielbraithwt on 10/1/16.
 */
public class Client {

    public static void main(String[] args) {
        String hostname = args[0];
        int port = Integer.parseInt(args[1]);
        int cs = Integer.parseInt(args[2]);

        Client client = new Client(hostname, port, cs);
        client.run();
    }

    /**
     * Handles requesting a job from a given key manager
     * @param ip ip address of the server
     * @param port port of the server
     * @param cs chunk size
     * @return
     */
    private static String[] requestJob(String ip, int port, int cs) {
        String[] job = new String[5];

        Socket server;
        BufferedReader serverIn;
        PrintWriter serverOut;

        try {
            server = new Socket(ip, port);

            serverIn = new BufferedReader(new InputStreamReader(server.getInputStream()));
            serverOut = new PrintWriter(server.getOutputStream(), true);

            serverOut.write(String.format("%d\n", KeyManager.SESSION_REQUEST));
            serverOut.write(String.format("%d", cs) + "\n");
            serverOut.flush();

            for (int i = 0; i < job.length; i++) {
                job[i] = serverIn.readLine();
            }

            server.close();
            serverIn.close();
            serverOut.close();
        } catch (IOException e) {
            return null;
        }

        return job;
    }

    /**
     * Handles submitting a job to a key manager
     * @param ip ip address of the key manager
     * @param port port of the key manager
     * @param id id of the job
     * @param result true iff we found the correct key
     * @param key key if it was found
     */
    private static void submitJob(String ip, int port, boolean result, String key) {
        Socket server;
        BufferedReader serverIn;
        PrintWriter serverOut;

        try {
            server = new Socket(ip, port);

            serverIn = new BufferedReader(new InputStreamReader(server.getInputStream()));
            serverOut = new PrintWriter(server.getOutputStream(), true);

            serverOut.write(String.format("%d\n", KeyManager.SESSION_SUBMIT));
            serverOut.write(String.format("%b\n", result));
            if (result) {
                serverOut.write(key);
            }
            serverOut.flush();

            server.close();
            serverIn.close();
            serverOut.close();
        } catch (IOException e) {
            throw new RuntimeException("Unable to submit job");
        }
    }

    private String ip;
    private int port;
    private int chunkSize;
    private boolean solutionFound;
    private boolean exausted;

    public Client(String ip, int port, int cs) {
        this.ip = ip;
        this.port = port;
        this.chunkSize = cs;
    }

    /**
     * The main run loop for the client
     */
    public void run() {
        while (!solutionFound && !exausted) {
            String[] job = requestJob(ip, port, chunkSize);

            if (job == null) {
                break;
            }

            byte[] cypherText = Blowfish.fromBase64(job[0]);
            String plainText = job[1];
            BigInteger key = new BigInteger(job[2]);
            int keySize = Integer.parseInt(job[3]);

            // Process Job
            for (int i = 0; i < chunkSize && !solutionFound; i++) {
                // Ensure we havent exceeded the bounds of the key
                if (key.compareTo(BigInteger.valueOf(2).pow(keySize*8)) > 1) {
                    exausted = true;
                    break;
                }

                byte[] bfk = Blowfish.asByteArray(key, keySize);
                Blowfish.setKey(bfk);
                String possiblePlainText = Blowfish.decryptToString(cypherText);

                if (plainText.equals(possiblePlainText)) {
                    solutionFound = true;
                } else {
                    key = key.add(BigInteger.ONE);
                }
            }

            submitJob(ip, port, solutionFound, (solutionFound ? key.toString() : null));
        }
    }
}
