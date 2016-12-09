import java.awt.print.*;
import java.awt.*;

/**
 * Write a description of class DiagramPrinter here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class DiagramPrinter implements Printable
{  
    private DiagramPane paneToPrint; 
    
    public DiagramPrinter(DiagramPane dp)
    {
        paneToPrint = dp;
    }
    
    public int print( Graphics g, PageFormat pf, int page ) throws PrinterException
    {
        if( page > 0 ) return NO_SUCH_PAGE;
        
        Graphics2D g2d = ( Graphics2D ) g;
        
        // Transform graphics object to fit printable area
        g2d.translate((int) pf.getImageableX(), (int) pf.getImageableY());
        
        // Get window size
        Dimension size = paneToPrint.getSize();
        
        // Get the page size
        double pageWidth = pf.getImageableWidth();
        double pageHeight = pf.getImageableHeight();
        
        // Scale down the width if we need
        if( size.getWidth() > pageWidth )
        {
            double scalingFactor = pageWidth / size.getWidth();
            g2d.scale(scalingFactor, scalingFactor);
            
            // Adjust the height and width
            pageWidth /= scalingFactor;
            pageHeight /= scalingFactor;
        }
        
        // Scale down the height if we need
        if( size.getHeight() > pageHeight )
        {
            double scalingFactor = pageHeight / size.getHeight();
            g2d.scale(scalingFactor, scalingFactor);
            
            // Adjust the height and width
            pageWidth /= scalingFactor;
            pageHeight /= scalingFactor;
        }
        
        paneToPrint.getPrintableGraphics(g2d);
        
        return PAGE_EXISTS;
    }
}
