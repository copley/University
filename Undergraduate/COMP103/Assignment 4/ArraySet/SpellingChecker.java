// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP103 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP103, Assignment 4
 * Name:
 * Usercode:
 * ID:
 */

import ecs100.*;
import java.awt.Color;
import java.util.*;
import java.io.*;

/** SpellingChecker checks all the words in a file against a dictionary.
 *  It prints out (one per line) any word in the file that is not in the dictionary.
 *  The program can be very simple - it is OK to just have a main method
 *  that contains all the code, but you can use multiple methods if you want.
 *  
 *  The key requirement is that the dictionary should be read from the
 *  dictionary file into a set.
 *  When reading through the document with spelling errors, the program should
 *  check each each word against the dictionary.
 *  Any word that is not in the dictionary is considered to be an error and
 *  should be printed out.
 *
 *  The program should record and print out the total time taken to read
 *  all the words into the dictionary.
 *  It should also record and print out the total time taken to check all the words.
 *
 *  Note that the dictionary and the file to check are assumed to be all
 *  lowercase, with all punctuation removed.
 */

public class SpellingChecker implements UIButtonListener
{

    // Suggested Design:
    // 
    // 1. create a Set of String to hold the words in the dictionary.
    //    (For your first version, use a HashSet; Then use your ArraySet.)
    //
    // 2. 
    //   read each word in the file "dictionary.txt" (use a Scanner)
    //   and add each word to the dictionary Set.
    //   Note that the dictionary contains just over 200,000 words
    //
    // 3. 
    //   Ask the user (UIFileChooser) for the file to check
    //   open the file in a Scanner.
    //   record the start time
    //   loop through file reading each word.
    //   check if the word is in dictionary set
    //   if not, print out the word.
    //   print out total time taken

    Set wordsArraySet;
    Set wordsHashSet;

    public SpellingChecker()
    {
        UI.initialise();
        UI.addButton("Check File", this);

        wordsArraySet = new ArraySet();
        wordsHashSet = new HashSet();
        
        ArrayList<String> documentWords = getDictionary("dictionary.txt");
        UI.println("Have dictionary words");
        
        // Read into Array Set
        UI.println("Reading words into ArraySet");
        long arraySetStart = System.nanoTime();
        for( int i = 0; i < documentWords.size(); i++ )
        {
            wordsArraySet.add(documentWords.get(i));

            if( i % 1000 == 0 ) UI.println(i + " Words Read!");
        }
        double arraySetTime = (System.nanoTime() - arraySetStart)/1000000000;
        UI.println("Words read into ArraySet: " + arraySetTime);
        
        // Read into Hash Set
        UI.println("Reading words into HashSet");
        long hashSetStart = System.nanoTime();
        for( int i = 0; i < documentWords.size(); i++ )
        {
            wordsHashSet.add(documentWords.get(i));

            if( i % 1000 == 0 ) UI.println(i + " Words Read!");
        }
        double hashSetTime = (System.nanoTime() - hashSetStart)/1000000000;
        UI.println("Words read into HashSet: " + hashSetTime);


        UI.println("Dictionary read in");

    }

    public void buttonPerformed(String button)
    {
        if( button.equals("Check File") )
        {
            String file = UIFileChooser.open("Choose a file to spell check");

            ArrayList<String> documentWords = readFile(file);

            UI.println("Testing Array Set");
            // Check file with arraySet
            long arraySetStart = System.nanoTime();
            checkFile(documentWords, wordsArraySet);
            double arraySetTime = (System.nanoTime() - arraySetStart)/1000000000;

            UI.println("Testing Hash Set");
            // Check file with hashSet
            long hashSetStart = System.nanoTime();
            checkFile(documentWords, wordsHashSet);
            double hashSetTime = (System.nanoTime() - hashSetStart)/1000000000;

            // Print out the results
            if( hashSetTime < arraySetTime )
            {
                double percent = (arraySetTime/hashSetTime)*100;
                UI.println(String.format("Hash Set Wins! %.2f%s faster ( HashSet: %.0fs, ArraySet: %.0fs )", percent, "%", hashSetTime, arraySetTime));
            }
            else
            {
                double percent = (hashSetTime/arraySetTime)*100;
                UI.println(String.format("Array Set Wins! %.2f%s faster ( ArraySet: %.0fs, HashSet: %.0fs )", percent, "%", arraySetTime, hashSetTime));
            }

        }
    }

    public ArrayList<String> getDictionary(String file)
    {
        ArrayList<String> words = new ArrayList<String>();
        
        try
        {
            Scanner wordsIn = new Scanner( new File(file) );
            
            while( wordsIn.hasNext() ) words.add(wordsIn.nextLine().trim());
        } catch( IOException e ) { UI.println("An error occored when reading dictonary"); }
        
        return words;
    }
    
    public ArrayList<String> readFile(String file)
    {
        // Use regex to remove puncuation
        // Information From: http://en.wikipedia.org/wiki/Regular_expression#Syntax

        ArrayList<String> words = new ArrayList<String>();
        try
        {

            Scanner wordsIn = new Scanner( new File(file) );
            wordsIn.useDelimiter("[\\s+.]+");
            while( wordsIn.hasNext() )
            {
                String word = wordsIn.next().replaceAll("[^a-zA-Z]", "").toLowerCase();
                if( word != "" ) words.add(word);
            }
        } catch( IOException e ) {UI.println("Error occored when reading document");}
        
        return words;
    }

    public void checkFile(ArrayList<String> documentWords, Set words)
    {
        for( int i = 0; i < documentWords.size(); i++ )
            if( !words.contains(documentWords.get(i)) ) Trace.println("Couldent find: " + documentWords.get(i));

    }

    // Main Function 
    ///////////////////////////////////////////
    public static void main( String[] args )
    {
        new SpellingChecker();
    }
}
