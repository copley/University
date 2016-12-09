// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP103 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP103, Assignment 5
 * Name:
 * Usercode:
 * ID:
 */

import ecs100.*;
import java.util.*;

/** Prints out all permuations of a string
 *  The static method permute constructs all the permutations
 *  The main method gets the string, calls recPermute, and prints the result.
 */
public class Permutations {

    /** Return a List of all the permutations of a String. */
    public static List <String> recPermute(String string) 
    {
        List<String> l = new ArrayList<String>();

        if( string.length() == 1 )
        {    
            l.add(string);   
            return l;
        }

        char lastChar = string.charAt(string.length()-1);
        String remaining = string.substring(0, string.length()-1);
        List<String> per = recPermute(remaining);

        for( String p : per ) 
        {
            for( int i = 0; i < p.length()+1; i++ )
            {
                l.add(p.substring(0, i) + lastChar + p.substring(i));
            }	
        }

        return l;
    }

    // Main
    public static void main(String[] arguments){
        String string = "";
        while (! string.equals("#")) {
            string = UI.askString("Enter string to permute - # to exit: ");
            List<String> permutations = recPermute(string);
            for (String p : permutations)
                UI.println(p);
            UI.println("---------");
        }
        UI.quit();
    }    
}
