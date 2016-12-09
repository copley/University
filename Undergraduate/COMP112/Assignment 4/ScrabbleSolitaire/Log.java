import ecs100.*;
import java.util.ArrayList;
import java.io.*;

/**
 * The log class handles saving all the information about a commit to a file.
 */
public class Log
{
    // Static Variables
    public static final String COMMIT_DIV = "COMMIT";

    private String filePath;
    private PrintWriter r;

    /**
     * Creates a new Log class and links it to a new log file
     */
    public Log()
    {
        filePath = String.format("log-%d.sl", System.currentTimeMillis());

        open();
    }

    /**
     * Created a new Log class and links it to an exsisting log file
     */
    public Log(String fp)
    {
        this.filePath = fp;

        open();
    }

    /**
     * Returns the path to the log file, useful if you initilised the class
     * and it decided on the logfile name
     */
    public String getFilePath()
    {
        return filePath;
    }

    /**
     * Opens a stream to the log file
     */
    private void open()
    {
        try
        {
            // Found how to append to a file here 
            //http://stackoverflow.com/questions/1625234/how-to-append-text-to-an-existing-file-in-java
            r = new PrintWriter( new BufferedWriter(new FileWriter(filePath, true)));
        }
        catch( IOException e )
        {
            r = null;
            UI.println("An error occored when operning stream to the log file.\n This game wont be logged");
            Trace.println(e);
        }
    }

    /**
     * Closes the connection to the file
     */
    public void close()
    {
        if( r != null )
        {
            r.close();
        }
    }

    /**
     * Writes the header for a new commit to the file
     */
    public void newCommit(int score, int roundScore)
    {
        if( r != null )
        {
            r.append(String.format("\n%s\n", Log.COMMIT_DIV));
            r.append(String.format("Score: [%d, %d];;\n", score, roundScore));
            r.flush();
        }
    }

    /**
     * Adds information about a commited tile to the log
     */
    public void add(int x, int y, String l)
    {
        if( r != null )
        {
            r.append(String.format("Commit @ [%d, %d, %s];", x, y, l));
            r.flush();
        }
    }

    // Static Functions
    ///////////////////

    /**
     * Reads the log at file path fp and returns it as a string
     */
    public static String readLog(String fp)
    {
        try
        {
            BufferedReader r = new BufferedReader( new FileReader(fp));
            StringBuilder b = new StringBuilder();

            String l = r.readLine();
            while( l != null ) 
            {
                b.append(l);
                l = r.readLine();
            }
            
            return b.toString().trim();
        }
        catch( IOException e )
        {
            UI.println("There was an error when operning the log file");
            Trace.println(e);
        }

        return null;
    }
}
