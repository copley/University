import java.io.*;
import java.net.Socket;
import java.util.*; 
import java.net.ServerSocket;

/**
 * Write a description of class EchoServer here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class EchoServer
{

    public static final int PORT = 6667; // The port the server will listen on
    
    public static void main(String[] args) 
    {
        try
        {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Waiting for clients to connect...");
            while (true) 
            {
                Socket socket = serverSocket.accept();
                System.out.println("Found client");
                EchoService echoService = new EchoService(socket);
                new Thread(echoService).start();
            }
        }catch(IOException e){}
    }

    private static class EchoService implements Runnable
    {
        private Socket socket;
        private Scanner clientIn;
        private PrintStream clientOut;

        public EchoService( Socket socket )
        {
            this.socket = socket;

            try
            {
                clientIn = new Scanner( socket.getInputStream() );
                clientOut = new PrintStream( socket.getOutputStream() );
            } catch (IOException e) {}
        }

        public void run()
        {
            while( clientIn.hasNext() )
            {
                String message = clientIn.nextLine();
                System.out.println("Recived: " + message);
                
                clientOut.println(message);
                clientOut.flush();

                if( message.equals("QUIT") ) {break;}
            }

            try{ socket.close(); }
            catch(IOException e) {}
            System.out.println("Client disconected");
        }
    }

}
