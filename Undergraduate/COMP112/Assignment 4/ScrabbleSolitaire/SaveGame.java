import ecs100.*;
import java.io.*;

/**
 * This class is for saving and loading ScrabbleSolitaire save games. It has been designed so that
 * it should be easy to add extra data to the save file. Sinse everything is stored under keys you can
 * add as many keys as you want
 */
public class SaveGame
{
    //Static variables
    private static final String SaveGameHeader = "## Scrabble Solitaire Save Game ##\n\n";

    private String saveGame;

    /**
     * If nothing is passed to the constructor then it must be a new saveGame
     */
    public SaveGame()
    {
        saveGame = "";
    }

    /**
     * If a file path is passed to the constructor then a save game is being loaded
     */
    public SaveGame(String filePath)
    {   
        saveGame = "";
        
        // Try and load the contence from the file
        try
        {
            StringBuilder b = new StringBuilder();
            BufferedReader r = new BufferedReader( new FileReader(filePath));

            String l = r.readLine();
            while( l != null )
            {
                b.append(l);
                l = r.readLine();
            }

            saveGame = b.toString();
        }
        catch( IOException e )
        {
            UI.println("There was an error when operning the file: " + filePath);
            Trace.println(e);
        }
    }
    
    /**
     * Adds the passed data to the save game class under the key passed to it
     */
    public void add(String key, String data)
    {
        saveGame += String.format("[%s]\n%s\n[/%s]\n\n", key, data.trim(), key);
    }

    /**
     * Retreves the data stored under the key passed to it. Assumes that the key is actually
     * in the save game
     */
    public String get(String key)
    {
        int start = saveGame.indexOf(String.format("[%s]", key)) + (2 + key.length());
        int end = saveGame.indexOf(String.format("[/%s]", key));

        return saveGame.substring(start, end).trim();
    }
    
    /**
     * Returns true if the save file contains the passed key
     */
    public boolean containsKey(String key)
    {
        return ( saveGame.indexOf(key) != -1 );
    }

    /**
     * Writes all the information stored in the save game class to a file
     */
    public void save(String fileName)
    {
        try
        {
            if( fileName.indexOf(".ssg") == -1 ) fileName += ".ssg";
            // Got from lecure notes
            BufferedWriter w = new BufferedWriter( new FileWriter(fileName));
            w.write(SaveGame.SaveGameHeader);
            w.write(saveGame);
            w.close();
        }
        catch( IOException c )
        {
            UI.println("An exception occored when writing the file");
        }
    }
}
