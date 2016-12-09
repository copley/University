// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP112 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP112 Assignment 1
 * Name: Daniel Thomas Braithwaite
 * Usercode: danielbraithwt
 * ID: 300313770
 */

import ecs100.*;
import java.awt.Color;


/**
 * Checks dates, prints out in long format and draws calendars.
 * The processDates method
 * Reads a date from the user as three integers, and then
 * (a) checks that the date is valid (ie, represents a real date,
 *     taking into account leap years), reporting if it is not valid.
 *     (It may assume the standard modern calendar, and isn't required
 *      to give correct answers for dates before the 1600's when the
 *      calendar was different.
 * (b) If the date is valid, it prints out the date in a long form: eg
 *     Monday 3rd March, 2014. This requires working out which day
 *     of the week the date is.
 *     (Core only needs dates this year)
 * (c) It draws a one week calendar, highlighting the date:
 *     It shows the seven days of the week as a row of rectangles, highlighting
 *      the day corresponding to the date. It doesn't need to show the dates
 *      for each day.
 * (d) (Completion) It draws a monthly calendar for the month containing the date:
 *     It should draw a title containing the month and the year, and a
 *     grid of rectangles for each day of the month, giving the day of the month
 *     in each rectangle. This will be between 4 rows and 6 rows of 7 rectangles.
 *     The ISO standard for calendars specifies that the first day of each week
 *     should be a Monday.
 *     Ideally, the calendar should include the last few days of the previous
 *     month when the month doesn't start on a Monday and the first few days
 *     of the next month when the month doesn't end on a Sunday.
 *
 * Reasonable design would have a number of methods, for example:
 *  isValidDate  which would return a boolean (true or false)
 *  isLeapYear   which would return a boolean (true or false)
 *  findDay      which would return the day of the week as an int (0 to 6)
 *  drawWeek     which would draw the weekly "calendar"
 *  drawMonth    which would draw the monthly calendar
 * You might choose to design it differently, but doing it all in one huge method
 *  would not be good design.
*/

public class DateChecker 
{

    // Date info tables
    int[] monthLengths = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
    String[] monthNames = { "January", "Febuary", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" };
    String[] dayNames = { "Monday", "Tuesday", "Wensday", "Thursday", "Friday", "Saturday", "Sunday" };
    
    // Day finding tables
    int[] monthTable = { 0, 3, 3, 6, 1, 4, 6, 2, 5, 0, 3, 5 };
    int[] gregorianCenturyTable = { 6, 4, 2, 0 };
    int[] julianCenturyTable = { 4, 3, 2, 1, 0, 6, 5 };

    /**
     * Loop to repeatedly process a date.
     * Asks the user if they want to enter another date each time.
     */
    public void processDates()
    {
        UI.setImmediateRepaint(false);
        
        do  
        {
            UI.clearText();
            UI.clearGraphics();
            processADate();
        }
        while (UI.askBoolean("Enter another date?")); 
    }

    /**
     * Asks user for a date, then produces the required output.
     * 
     * As this program needs to account for the julian callender, after looking up when
     * diffrent countries made the switch sinse in the brif it said to do it from 1582 i looked
     * at the french callender which showed me the exzact dates that where missing and when the
     * callenders where switeched out.
     * 
     * Resource: http://www.timeanddate.com/calendar/julian-gregorian-switch.html
     */
    public void processADate()
    {
        String dateString = UI.askString("Enter a date ( e.g. DD/MM/YYYY ): ");
        String[] dateSections = dateString.split("/");
         
        if( dateSections.length == 3 )
        {
            // Will store the date in the format DD/MM/YYYY e.g. first
            // index will be the day and so on
            int[] date = new int[3];
            for( int i = 0; i < 3; i++ ) date[i] = Integer.parseInt(dateSections[i]);
            
            if( !isValidDate(date) ) UI.println("Date not valid!");
            else drawMonth(date);
        }
        else UI.println("Date entered in wrong format!");
    
    }
    
    /**
     * Checks to see if the date is valid
     */
    public boolean isValidDate(int[] date)
    {   
        // Check if year is a leap year
        boolean leapYear = isLeapYear(date[2]);
        
        // Check if month is valid
        if( date[1] < 1 || date[1] > 12 ) return false;
        
        // Check if day is valid
        if( leapYear && date[1] == 2 && date[0] <= ( monthLengths[date[1] - 1] + 1 ) ) return true;
        else if( date[0] > monthLengths[date[1] - 1] ) return false;
        
        // Make sure the date entered isnt one of the missing days
        if( date[2] == 1582 && date[1] == 12 && ( date[0] > 9 && date[0] < 20 ) )
        {
            UI.println("The date you entered is missing");
            return false;
        }
        
        return true;
    }
    
    /**
     * Wasnt sure how to do this so i used this as a resource
     * http://www.icsejava.com/programs/leap-year
     */
    
    public boolean isLeapYear(int year)
    {
        if( year % 100 == 0 && year % 400 == 0 ) return true;
        else if( year % 4 == 0 ) return true;
        
        return false;
    }
    
    /**
     * My resource for this information was...
     * http://en.wikipedia.org/wiki/Determination_of_the_day_of_the_week#Basic_method_for_mental_calculation
     */
    public int dayOfWeek(int[] date)
    {   
        int m = 0;
        int y = 0;
        int c = 0;
        
        // Get m from the month tabe
        if( date[1] >= 3 || !isLeapYear(date[2]) ) m = monthTable[date[1]-1];
        else m = monthTable[date[1] - 1] - 1;
        
        // Get the last two digits of the year
        String yearString = String.format("%d", date[2]);
        y = Integer.parseInt(yearString.substring(yearString.length()-2));
        
        // Get the century number
        if( date[2] <= 1582 ) c = julianCenturyTable[Integer.parseInt(yearString.substring(0, 2)) % 7];
        else if( date[2] == 1582 && date[0] >= 20 ) c = gregorianCenturyTable[Integer.parseInt(yearString.substring(0, 2)) % 4];
        else c = gregorianCenturyTable[Integer.parseInt(yearString.substring(0, 2)) % 4];
        
        int dateNumber = ( date[0] + m + y + ((int) Math.floor(y/4)) + c ) % 7;
        
        // Transform the returned date number beacuse this algorythm gives
        // a number from 1 - 7 starting from sunday and i want it to go from
        // 0 - 6 starting from monday
        
        switch( dateNumber )
        {
            // Monday
            case 1: return 0;
            // Tuesday
            case 2: return 1;
            // Wensday
            case 3: return 2;
            // Thrusday
            case 4: return 3;
            // Friday
            case 5: return 4;
            // Saturday
            case 6: return 5;
            // Sunday
            case 0: return 6;
        }
        
        // Should never reach this but return -1 as that isnt a date number
        return -1;
    }
    
    /**
     * Draws the current week highlighting the current day in green
     * 
     */
    public void drawWeek(int date[])
    {
        // Position variables
        int x = 100;
        int y = 100;
        
        // Callender box size varables
        int boxWidth = 100;
        int boxHeight = 50;
        
        // Gets the number of the day of the week
        int day = dayOfWeek(date);
        
        // Loop draws out the current week and highlights the current day in green
        UI.setColor(Color.BLACK);
        for( int i = 0; i < 7; i++ )
        {
            if( i == day )
            {
                UI.setColor(Color.GREEN);
                UI.fillRect(x, y, boxWidth, boxHeight);
                UI.setColor(Color.BLACK);
                UI.drawRect(x, y, boxWidth, boxHeight);
                
                
            }
            else UI.drawRect(x, y, boxWidth, boxHeight);
            
            UI.drawString(dayNames[i], x + 10, y + 25);
            x += boxWidth;
        }
    }
    
    /**
     * Draws the monthly callender, with days leading up to the month on the top row
     * and days after the month on the bottom row(s)
     */
    public void drawMonth(int date[])
    {
        // Position tracking variables
        int x = 100;
        int y = 200;
        int colCount = 0;
        
        // Calender Box Size Variables
        int boxWidth = 100;
        int boxHeight = 50;
        
        // Reset the color to black
        UI.setColor(Color.BLACK);
        
        // Draw the title for the callender
        UI.setFontSize(50);
        UI.drawString(String.format("%s %d", monthNames[date[1]-1], date[2]), 100, 60);
        
        // Change the font size for callender numbers
        UI.setFontSize(20);
        
        // Draw the current week
        drawWeek(date);
        
        // Find what day the first of the month is
        int firstOfMonthDay = dayOfWeek( new int[] {1, date[1], date[2]} );
        
        // Get the length of the prevous month, if the month is january the
        // calenender should go back to the prevous year december
        int prevMonthLength = 0;
        if( date[1] == 1 ) prevMonthLength = monthLengths[11];
        else 
        {
            // Check to if month is feb and thus if we need to account for leap years
            if( date[1] == 3 )
            {
                if( isLeapYear(date[2]) ) prevMonthLength = 29;
                else prevMonthLength = 28;
            }
                
            else prevMonthLength = monthLengths[date[1] - 2];
        }
        
        // Draw the last days of the previous month, drawing in grey to
        // show that its the prevous month
        UI.setColor(Color.GRAY);
        for( int i = ( prevMonthLength - ( firstOfMonthDay - 1 ) ); i <= prevMonthLength; i++ )
        {
            UI.drawRect(x, y, boxWidth, boxHeight);
            UI.drawString(String.format("%d", i) , x + 10, y + 25);
            
            x += boxWidth;
            colCount++;
        }
        
        // Draw the current month, in black so it stands out agains the days that
        // arnt this month
        int curMonthLength = monthLengths[date[1] - 1];
        if( date[1] == 2 && isLeapYear(date[2]) ) curMonthLength++;
        
        UI.setColor(Color.BLACK);
        for( int i = 0; i < curMonthLength; i++ )
        {
            if( ( i + 1 ) == date[0] ) 
            {
                UI.setColor(Color.GREEN);
                UI.fillRect(x, y, boxWidth, boxHeight);
                
                UI.setColor(Color.BLACK);
                UI.drawRect(x, y, boxWidth, boxHeight);
            }
            else UI.drawRect(x, y, boxWidth, boxHeight);
            
            UI.drawString(String.format("%d", i+1) , x + 10, y + 25);
            
            colCount++;
            
            if( colCount >= 7 )
            {
                x = 100;
                y += boxHeight;
                colCount = 0;
            }
            else x += boxWidth;
            
            // Skip the missing days in december 1582
            if( date[2] == 1582 && date[1] == 12 && (i + 1) == 9 ) i = 18;
        }
    
        // Fill the remaining spaces with the dates from the next month
        // draw in the color grey to show not part of current month
        UI.setColor(Color.GRAY);
        for( int i = 0; colCount < 7; i++ )
        {
            UI.drawRect(x, y, boxWidth, boxHeight);
            UI.drawString(String.format("%d", i + 1) , x + 10, y + 25);
            
            colCount++;
            x  += boxWidth;
        }
        
        UI.repaintGraphics();
    }
    
    // Main
    /** Create a new DateChecker object and call processDates */
    public static void main(String[] arguments){
        DateChecker dc = new DateChecker();
        dc.processDates();
    }        

}
