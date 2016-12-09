import javax.swing.*;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Color;
import java.util.*;
import java.io.*;

/**
 * Write a description of class MoleculeRenderer here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class MoleculeRenderer extends JPanel
{
    private List<Atom> molecule;
    private Map<String, Element> elementTable;
    private boolean isDrawing = false;

    private double currentPanAngle = 0.0;
    private double currentTiltAngle = 0.0;
    private double currentZoom = 1;

    public MoleculeRenderer()
    {
        readElementTable();
    }

    private void draw(Graphics2D g2d)
    {   
        g2d.clearRect(0, 0, 1000, 1000);
        if( molecule == null ) return;
        
        // Enable anti-alising and high rendering quallity
        RenderingHints renderHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        renderHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.addRenderingHints(renderHints);
        
        g2d.translate((int)(0.5*(1000 - (1000*currentZoom))), (int)(0.5*(1000 - (1000*currentZoom))));
        g2d.scale(currentZoom, currentZoom);
        

        for( Atom a : molecule )
        {
            a.draw(g2d, currentPanAngle, currentTiltAngle, currentZoom);
        }

    }

    public void paintComponent(Graphics g)
    {
        isDrawing = true;

        super.paintComponent(g);
        draw((Graphics2D) g);

        isDrawing = false;
    }

    public void readElementTable()
    {
        elementTable = new HashMap<String, Element>();

        try
        {
            Scanner fIn = new Scanner(new File("./element-table.txt"));
            while(fIn.hasNext())
            {
                String type = fIn.next();

                int radius = fIn.nextInt();

                int r = fIn.nextInt();
                int g = fIn.nextInt();
                int b = fIn.nextInt();

                // Remvoe the end of line charactor
                fIn.nextLine();

                // Add the new element to the table
                elementTable.put(type, new Element(type, radius, new Color(r,g,b)));
            }

            System.out.println("Element table was read in");
        } catch(IOException e) {System.out.println("Element table read failed");}
    }

    public void readMoleculeFile(String fileName)
    {
        while(isDrawing);

        molecule = new ArrayList<Atom>();
        try
        {
            Scanner fIn = new Scanner(new File(fileName));
            while(fIn.hasNext())
            {
                String type = fIn.next();
                int x = fIn.nextInt();
                int y = fIn.nextInt();
                int z = fIn.nextInt();

                // Remvoe the end of line charactor
                fIn.nextLine();

                // Add the new atom to the list of atoms (molecule)
                Element e = elementTable.get(type);
                molecule.add(new Atom(x, y, z, e.getRadius(), e.getColor()));
            }

            System.out.println("Molecule from " + fileName + " was read in");
        } catch(IOException e) {}

        repaint();
    }

    public void viewFromFront()
    {
        Collections.sort(molecule, new Comparator<Atom>()
        {
            public int compare(Atom a1, Atom a2)
            {
                if(a1.getZ() - a2.getZ() > 0) return 1;
                else return -1;
            }
        });
        
        currentPanAngle = 0;
        repaint();
    }
    
    public void viewFromBack()
    {
        Collections.sort(molecule, new Comparator<Atom>()
        {
           public int compare(Atom a1, Atom a2)
           {
               if(a2.getZ() - a1.getZ() > 0) return 1;
               else return -1;
           }
        });
        
        currentPanAngle = 180;
        repaint();
    }
    
    public void viewFromLeft()
    {
        Collections.sort(molecule, new Comparator<Atom>()
        {
           public int compare(Atom a1, Atom a2)
           {
               if(a1.getX() - a2.getX() > 0) return 1;
               else return -1;
           }
        });
        
        currentPanAngle = 90;
        repaint();
    }
    
    public void viewFromRight()
    {
        Collections.sort(molecule, new Comparator<Atom>()
        {
            public int compare(Atom a1, Atom a2)
            {
                if(a2.getX() - a1.getX() > 0) return 1;
                else return -1;
            }
        });
        
        currentPanAngle = 270;
        repaint();
    }
    
    public void viewFromTop()
    {
        Collections.sort(molecule, new Comparator<Atom>()
        {
           public int compare(Atom a1, Atom a2)
           {
               if(a1.getY() - a2.getY() > 0) return 1;
               else return -1;
           }
        });
        
        repaint();
    }
    
    public void viewFromBottom()
    {
        Collections.sort(molecule, new Comparator<Atom>()
        {
           public int compare(Atom a1, Atom a2)
           {
               if(a2.getY() - a1.getY() > 0) return 1;
               else return -1;
           }
        });
        
        repaint();
    }
    
    public void pan(double deltaAngle)
    {
        currentPanAngle += deltaAngle;
        
        Collections.sort(molecule, new Comparator<Atom>()
        {
           public int compare(Atom a1, Atom a2)
           {
               return a1.further(a2, currentPanAngle);
           }
        });
        
        repaint();
    }
    
    public void tilt(double deltaAngle)
    {
        currentTiltAngle += deltaAngle;
        
        repaint();
    }
    
    public void zoom(double deltaZoom)
    {
        //if( (currentZoom <= 0.5 && deltaZoom > 0 ) || ( currentZoom >= 1.5 && deltaZoom < 0) ) currentZoom += deltaZoom;
        if( (currentZoom + deltaZoom < 1.6) && (currentZoom + deltaZoom > 0.4) ) currentZoom += deltaZoom;
        repaint();
    }
    

}
