import java.net.Socket;
import java.io.*;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue; 

/**
 * Write a description of class ServerConnection here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class ServerConnection
{
    private String server;
    private int port;
    private Socket irc;
    private Scanner InputFromIRC;
    private PrintStream OutputToIRC;
    
    private LinkedBlockingQueue<String> toSend;
    private LinkedBlockingQueue<String> read;

    private boolean sendThreadlock = false;
    private boolean readThreadlock = false;

    public ServerConnection(String s, int p)
    {
        server = s;
        port = p;
    }

    public boolean connect()
    {
        try
        {
            sendThreadlock = false;
            readThreadlock = false;

            toSend = new LinkedBlockingQueue<String>();
            read = new LinkedBlockingQueue<String>();

            irc = new Socket(server, port);
            //UI.println("[*] Connection Established!");

            InputFromIRC = new Scanner(irc.getInputStream());
            OutputToIRC = new PrintStream(irc.getOutputStream(), true);

            // Start read thread
            new Thread( new Runnable() { public void run() { readLoop(); }}).start();

            // Start write thread
            new Thread( new Runnable() { public void run() { sendLoop(); }}).start();
        }
        catch( IOException e )
        {
            //UI.println("[*] Connection to IRC server failed");
            irc = null;
        }

        return ( irc != null );
    }

    public void disconnect()
    {
        try { irc.close(); }
        catch ( IOException e ) {e.printStackTrace();}
        irc = null;
    }

    public boolean isConnected()
    {
        //return ( irc != null );
        return !irc.isClosed();
        //return irc.isOutputShutdown();
    }

    public void send( String message )
    {
        toSend.add(message);
    }

    public IRCMessage read()
    {
        //while( readThreadlock || read.size() == 0 );
        //readThreadlock = true;

        String message = read.poll();

        //readThreadlock = false;

        IRCMessage parsed = null;
        if( message != null ) parsed = IRCMessageParser.ParseMessage(message);

        return parsed;
    }

    private void sendLoop()
    {
        while( true )
        
        //while( this.isConnected() )
        {
            //while( sendThreadlock || toSend.size() == 0 );
            //sendThreadlock = true;

            String message = toSend.poll();

            //sendThreadlock = false;

            if( message != null )
            {

                System.out.println("[*] Sending: " + message.trim());

                OutputToIRC.print(message.trim() + " \r\n");
                OutputToIRC.flush();
            }
        }
    }

    private void readLoop()
    {
        while( true )
        
        //String line = null;
        //while( this.isConnected() )
        {
            if( InputFromIRC.hasNext() )
            {
                //while( readThreadlock );
                //readThreadlock = true;

                String message = InputFromIRC.nextLine().trim();
                System.out.println("[*] Reading: " + message.trim());
                
                if( message.contains("Closing Link") ) 
                {
                    this.disconnect();
                    //try{ irc.close(); }
                    //catch( IOException e ) {}
                }
                else read.add(message);

                //readThreadlock = false;
            }
        }

        //return line;
    }

}
