// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP103 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP 103, Assignment 2
 * Name:
 * Usercode:
 * ID:
 */

import ecs100.*;
import java.awt.Color;
import java.util.*;
import java.io.*;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Font;
import java.awt.FontMetrics;

/** This program reads 2 text files and compiles word counts for each.
 *  It then eliminates rare words, and words that only occur in one
 *  document, and displays the remainder as a "word cloud" on a graphics pane,
 *  to allow the user to examine differences between the word usage in
 *  the two documents.
 */ 
public class WordCloud implements UIButtonListener {

    // Fields:
    private int numWordsToRemove = 100;

    // The two maps.
    private Map <String,Double> counts1, counts2;

    // Constructor
    /** Constructs a WordCloud object
     *  Set up the graphical user interface, and call the basic method.
     */ 
    public WordCloud() {
        // Set up the GUI.
        UI.addButton("remove standard common words", this);
        UI.addButton("remove infrequent words", this);
        UI.addButton("remove un-shared words", this);

        String fname1 = UIFileChooser.open("First filename to read text from");
        counts1 = buildHistogram(fname1);
        UI.println("Text read from " + fname1);

        String fname2 = UIFileChooser.open("Second filename to read text from");
        counts2 = buildHistogram(fname2);
        UI.println("Text read from " + fname2);

        displayWords();
    }

    /** Read the contents of a file, counting how often each word occurs.
     *  Put the counts (as Doubles) into a Map, which is returned.
     *  [CORE]
     */
    public Map <String, Double> buildHistogram(String filename)
    {
        if (filename == null) return null;
        Map <String,Double> wordcounts;
        double total = 0.0;
        try {
            // Open the file and get ready to read from it
            Scanner scan = new Scanner(new File(filename));

            // The next line tells Scanner to remove all punctuation
            scan.useDelimiter("[^-a-zA-Z']"); 

            wordcounts = new HashMap <String,Double> ();
            /*# YOUR CODE HERE */
            while( scan.hasNext() )
            {
                String word = scan.next().toLowerCase();

                if( word.length() > 0 )
                {
                    if( !wordcounts.containsKey(word) ) wordcounts.put(word, 0.0);
                    wordcounts.put(word, wordcounts.get(word)+1);
                }
            }

            scan.close(); // closes the scanner 
            return wordcounts;
        }
        catch(IOException ex) {
            UI.println("Could not read the file " + ex.getMessage());
            return null;
        }
    }

    /** Construct and return a Set of all the words that occur in EITHER
     *  document.
     *  [CORE]
     */
    public Set <String> findAllWords() 
    {
        HashSet<String> allWords = new HashSet<String>();

        // Add all the words from the first document
        for( String word : counts1.keySet() )
            allWords.add(word);

        // Add all the words from the second document
        for( String word : counts2.keySet() )
            allWords.add(word);

        return allWords;
    }

    /** Display words that exist in both documents.
     *  
     *  The x-position is essentially random (it just depends on the order in
     *  which an iterator goes through a Set).
     *  
     *  However the y-position reflects how much the word is used in the 1st
     *  document versus the 2nd. That is, a word that is common in the 1st and
     *  uncommon in the second should appear at the top.
     *  
     *  The SIZE of the word as displayed reflects how common the word is
     *  overall, including its count over BOTH documents.
     *  NB! There is UI.setFontSize method that may come in useful!
     *  
     *  [CORE]
     */
    public void displayWords() 
    {
        UI.clearGraphics();
        if ((counts1 == null) || (counts2 == null)) return;

        // Got the syntax from
        // http://docs.oracle.com/javase/tutorial/2d/advanced/quality.html
        Graphics2D g2d = UI.getGraphics();
        RenderingHints hints = new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING, 
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.addRenderingHints(hints);

        Random r = new Random();

        // First we re-normalise the counts.
        normaliseCounts(counts1);
        normaliseCounts(counts2);

        Set<String> allWords = findAllWords();

        double maxCount = 0;
        double maxAdd = 0;

        // Find the max count
        for( String word : allWords )
        {
            if( counts1.containsKey(word) && counts2.containsKey(word) )
            {
                if( counts1.get(word)+counts2.get(word) > maxCount ) maxCount = counts1.get(word)+counts2.get(word);
                if( (counts1.get(word)+counts2.get(word)) > maxAdd ) maxAdd = (counts1.get(word)+counts2.get(word));
            }
        }

        for( String word : allWords )
        {
            double c1 = 0;
            double c2 = 0;

            if( counts1.containsKey(word) ) c1 = counts1.get(word);
            if( counts2.containsKey(word) ) c2 = counts2.get(word);

            double x = UI.getCanvasWidth() * ( 0.9 * c1/(c1+c2));

            ///////////////////////////////////////////////////////////////////////////
            //          Y - Value                                                    //
            // Made the Y value random because i thought it looked the nicest        //
            // I origonally had it as being how much the word occored int total      //
            // but this  dident look very nice so i made the Y random and it gives   //
            // a much nicer picture                                                  //
            ///////////////////////////////////////////////////////////////////////////
            double y = 20 + r.nextInt(UI.getCanvasHeight() - 20);

            // Get the color for this word
            ///////////////////////////////////////////////////////////////////////
            //         Color Generation                                          // 
            // Found a value for saturation and brightness that gave a nice      //
            // range of colors as the hue varies. I then split the colors with   //
            // hue from 0 to 60 ( stopped at 60 to avoid wrapparound ie to stop  //
            // the smallest and largest words both being red )                   //
            ///////////////////////////////////////////////////////////////////////
            Color c = getColor(c1+c2, maxCount);
            UI.setColor(c);

            Font f = new Font("TimesRoman", Font.PLAIN, (int) (100 * ((c1+c2)/maxAdd)));
            g2d.setFont(f);

            // Check that the text will fit on the screen
            FontMetrics met = g2d.getFontMetrics();
            if( y - met.getHeight() < 20 ) y += (20 - (y - met.getHeight()));

            UI.drawString(word, (int) x, (int) y);
        }

        return;
    }

    /** Take a word count Map, and a Set of words. Remove those words from the
     *  Map.
     *  [COMPLETION]
     */
    public void removeWords(Map<String,Double> wc, Set<String> words) 
    {
        for( String word : words )
        {
            wc.remove(word);
            //wc.remove(word.toLowerCase());
        }
    }

    /** Takes a Map from strings to integers, and an integer,
     * limitNumWords. It should leave this Map containing only the
     * limitNumWords most common words in the original.
     * [COMPLETION]
     * 
     * Found example implementation for comparator
     * http://stackoverflow.com/questions/109383/how-to-sort-a-mapkey-value-on-the-values-in-java
     */
    public void removeInfrequentWords (Map<String,Double> c, int limitNumWords) 
    {
        // Check to make sure the map isnt allready within constraints
        if( c.size() <= limitNumWords ) return;

        // Sort the words using a custom comparator
        TreeMap<String, Double> sorted = new TreeMap<String, Double>(new Comparator<String>()
                {
                    public int compare( String a, String b )
                    {
                        if( c.get(a) >= c.get(b) ) return -1;
                        else return 1;
                    }
                });
        sorted.putAll(c);
        c.clear();

        Iterator it = sorted.entrySet().iterator();
        while( it.hasNext() )
        {
            Map.Entry e = (Map.Entry) it.next();
            c.put((String) e.getKey(), (Double) e.getValue());
            if( c.size() == limitNumWords ) return;
        }

    }

    /** Take a Map from words to counts, and "normalise" the counts,
     *  so that they are fractions of the total: they should sum to one.
     */
    public void normaliseCounts(Map <String, Double> counts) 
    {
        // Figure out the total in the current Map
        if (counts == null) return;
        double total = 0.0;
        for (String wd : counts.keySet()) 
            total += counts.get(wd);

        // Divide all values by the total, so they will sum to one.
        for (String wd : counts.keySet()) {
            double count = counts.get(wd)/total;
            counts.put(wd,count);
        }
    }

    /** Print the words and their counts to standard out.
     *  Not necessary to the program, but might be useful for debugging
     */
    public void printcounts(Map <String,Double> counts ) {
        if (counts == null) {
            UI.println("The Map is empty");
            return;
        }
        for (String s : counts.keySet()) 
            UI.printf("%15s \t : \t %.3f \n",s,counts.get(s));
        UI.println("----------------------------------");
    }

    //-- GUI stuff --------------------------------------------------------
    /** Respond to button presses */
    public void buttonPerformed(String button) {

        if (button.equals("remove standard common words")) {
            String fname = "some-common-words.txt"; // More general form: UIFileChooser.open("filename to read common words from");
            if (fname == null) return;
            UI.println("Getting ignorable words from " + fname);

            // Set the elements of the toRemove Set to be the words in file
            try {
                Set <String> toRemove = new HashSet <String> ();
                Scanner scan = new Scanner(new File(fname));
                while (scan.hasNext()) {
                    String str = scan.next().toLowerCase().trim(); 
                    toRemove.add(str);
                }
                scan.close();

                // Remove the words
                removeWords(counts1, toRemove);
                removeWords(counts2, toRemove);
            }
            catch(IOException ex) {   // what to do if there is an io error.
                UI.println("Could not read the file " + ex.getMessage());
            }
        }

        else if (button.equals("remove infrequent words") ) {
            UI.println("Keeping only the most common " + numWordsToRemove 
                + " words");
            removeInfrequentWords(counts1,numWordsToRemove);
            removeInfrequentWords(counts2,numWordsToRemove);

            printcounts(counts1);

            numWordsToRemove = numWordsToRemove/2; // It halves each time.
        }

        else if (button.equals("remove un-shared words") ) {
            UI.println("Keeping only words that occur in BOTH docs ");
            Set <String> wordsToBeRemoved = new HashSet <String> ();
            for (String wd : counts1.keySet()) 
                if (!counts2.keySet().contains(wd)) wordsToBeRemoved.add(wd);
            for (String wd : counts2.keySet()) 
                if (!counts1.keySet().contains(wd)) wordsToBeRemoved.add(wd);
            // Notice you do need to do both!
            // Now actually remove them.
            removeWords(counts1, wordsToBeRemoved);
            removeWords(counts2, wordsToBeRemoved);
        }

        // printcounts(counts1);
        // printcounts(counts2);

        // Now redo everything on the screen
        displayWords();
    }

    private static Color getColor(double num, double total)
    {
        float h = ((float) ((60.0/total) * num) ) / (float) 100.0;

        return Color.getHSBColor(h, (float) 0.68, (float) 0.66);
    }

    //================================================================
    // Main
    public static void main(String[] args) {
        new WordCloud();
    }
}
