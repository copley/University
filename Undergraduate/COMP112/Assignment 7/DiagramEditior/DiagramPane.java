import java.util.ArrayList;
import javax.swing.*;
import java.awt.*;
import java.awt.Color;
import java.awt.event.*;
import java.util.Vector;
import java.util.Enumeration;
import java.util.concurrent.*;
import java.io.*;
import java.awt.print.*;
import javax.print.attribute.*;

/**
 * Write a description of class DiagramPane here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class DiagramPane extends JPanel
{
    private transient Vector listeners;

    private ArrayList<Shape> shapes;
    private ArrayList<Shape> selectedShapes;
    private ArrayList<Link> links;

    private boolean newRect = false;
    private boolean newOval = false;
    private boolean newHex = false;

    private int[] shapeStartCoords;
    private int[] shapeEndCoords;

    private int[] startMoveCoords;

    private boolean moving = true;
    private boolean scaling = false;
    private boolean rotating = false;

    private int objectCount = 1;

    private CopyOnWriteArrayList<KeyEvent> pressedKeys;

    private ArrayList<Shape[]> undoStack;

    /**
     * Takes pointer to array list of shapes
     */
    public DiagramPane()
    {
        pressedKeys = new CopyOnWriteArrayList<KeyEvent>();
        shapes = new ArrayList<Shape>();
        selectedShapes = new ArrayList<Shape>();
        links = new ArrayList<Link>();
        undoStack = new ArrayList<Shape[]>();

        addMouseListener( new MouseListener()
            {
                public void mousePressed( MouseEvent e )
                {
                    if( newRect || newOval || newHex ) shapeStartCoords = new int[] {e.getX(), e.getY()};
                    else if( selectedShapes.size() != 0 ) 
                    {
                        if( moving ) startMoveCoords = new int[] {e.getX(), e.getY()}; 
                        else if( scaling ) 
                        {
                            selectedShapes.get(0).startScale(e.getX(), e.getY());
                            repaint();
                            startMoveCoords = new int[] {e.getX(), e.getY()}; 
                        }
                    }
                }

                public void mouseReleased( MouseEvent e )
                {
                    if( newRect || newOval || newHex ) 
                    {
                        int newx = 0;
                        int newy = 0;
                        int newh = 0;
                        int neww = 0;

                        if( shapeStartCoords[0] < e.getX() ) newx = shapeStartCoords[0];
                        else newx = e.getX();

                        if( shapeStartCoords[1] < e.getY() ) newy = shapeStartCoords[1];
                        else newy = e.getY();

                        if( isKeyPressed(KeyEvent.VK_SHIFT) )
                        {
                            neww = newh = (Math.abs(e.getX() - shapeStartCoords[0]) + Math.abs(e.getY() - shapeStartCoords[1]) ) /2;
                        }
                        else
                        {
                            neww = Math.abs(e.getX() - shapeStartCoords[0]);

                            newh = Math.abs(e.getY() - shapeStartCoords[1]);
                        }

                        for( int i = 0; i < shapes.size(); i++ ) shapes.get(i).setOrderValue(shapes.get(i).getOrderValue() + 1);

                        Shape toAdd = null;
                        if( newRect ) toAdd = new Rectangle(newx, newy, newh, neww, objectCount, 0);
                        else if( newOval ) toAdd = new Oval(newx, newy, newh, neww, objectCount, 0);
                        else if( newHex ) toAdd = new Hexagon(newx, newy, newh, neww, objectCount, 0);

                        shapes.add(toAdd);
                        objectCount++;

                        undoStack.add(new Shape[] {toAdd});

                        newRect = newOval = newHex = false;

                        shapeStartCoords = null;
                        shapeEndCoords = null;

                        repaint();
                    }
                    else if( startMoveCoords != null && shapeEndCoords != null )
                    {
                        Shape[] s = new Shape[selectedShapes.size()];
                        for( int i = 0; i < selectedShapes.size(); i++ )
                        {
                            selectedShapes.get(i).commit();

                            s[i] = selectedShapes.get(i);
                        }

                        undoStack.add(s);

                        startMoveCoords = null;
                        shapeEndCoords = null;
                        repaint();
                    }
                }

                public void mouseEntered( MouseEvent e ) {}

                public void mouseExited( MouseEvent e ) {}

                public void mouseClicked( MouseEvent e ) 
                {
                    System.out.println("Mouse Clicked");

                    // Check to see if click was on a shape, if so add to selected shapes
                    //for( int i = 0; i < shapes.size(); i++ )
                    //{
                    //    if( shapes.get(i).isWithinBounds(e.getX(), e.getY()) )
                    //    {
                    //        if( selectedShapes.size() >= 1 && isKeyPressed(KeyEvent.VK_SHIFT) ) selectedShapes.add(shapes.get(i));
                    //        else
                    //        {
                    //            deselectAll();
                    //            selectedShapes.add(shapes.get(i));
                    //        }

                    //        shapes.get(i).select();
                    //        fireSelectedEvent(selectedShapes.size());
                    //        repaint();
                    //        return;
                    //    }        
                    //}
                    for( int i = 0; i < objectCount-1; i++ )
                    {
                        // Find the shape with this order value
                        for( int j = 0; j < shapes.size(); j++ )
                        {
                            if( shapes.get(j).getOrderValue() == i )
                            {
                                if( shapes.get(j).isWithinBounds(e.getX(), e.getY()) )
                                {
                                    if( selectedShapes.size() >= 1 && isKeyPressed(KeyEvent.VK_SHIFT) ) selectedShapes.add(shapes.get(j));
                                    else
                                    {
                                        deselectAll();
                                        selectedShapes.add(shapes.get(j));
                                    }

                                    shapes.get(j).select();
                                    fireSelectedEvent(selectedShapes.size());
                                    repaint();
                                    return;
                                }

                                break;
                            }
                        }
                    }

                    // Else check if its on a link
                    for( int i = 0; i < links.size(); i++ )
                    {
                        if( links.get(i).isWithinBounds(e.getX(), e.getY()) )
                        {
                            links.get(i).select();
                            selectedShapes.add(links.get(i));
                            repaint();
                            return;
                        }
                    }

                    // Else click was on nothing so if the shift key isnt pressed clear selected
                    if ( !isKeyPressed(KeyEvent.VK_SHIFT) )
                    {
                        for( int i = 0; i < selectedShapes.size(); i++ ) selectedShapes.get(i).deSelect();
                        selectedShapes.clear();
                        fireSelectedEvent(selectedShapes.size());
                    }

                    repaint();
                }

            });

        addMouseMotionListener( new MouseMotionListener()
            {
                public void mouseMoved( MouseEvent e )
                {

                }

                public void mouseDragged( MouseEvent e )
                {
                    if( ( newRect || newOval || newHex ) && shapeStartCoords != null ) shapeEndCoords = new int[] {e.getX(), e.getY()};
                    else if( selectedShapes.size() > 0 )
                    {
                        shapeEndCoords = new int[] {e.getX(), e.getY()};

                        if( moving && startMoveCoords != null )
                        {
                            // First make sure the mouse is over one of the selected
                            boolean isValidMove = false;
                            for( int i = 0; i < selectedShapes.size(); i++ )
                            {
                                if( selectedShapes.get(i).isWithinBounds(e.getX(), e.getY()) ) isValidMove = true;
                            }

                            if( !isValidMove )
                            {
                                deselectAll();
                                repaint();
                                return;
                            }

                            for( int i = 0; i < selectedShapes.size(); i++ )
                            {
                                selectedShapes.get(i).move(e.getX() - startMoveCoords[0], e.getY() - startMoveCoords[1]);
                            }
                        }
                        else if( scaling && startMoveCoords != null ) 
                        {
                            selectedShapes.get(0).scale(e.getX() - startMoveCoords[0], e.getY() - startMoveCoords[1]);
                        }
                        else if( rotating && startMoveCoords != null )
                        {

                        }
                    }

                    repaint();
                }

            });

        // Start the thread to act on key presses
        new Thread( new Runnable() { public void run() { keyEventListenerThread(); }}).start();

        // Start the thread to remove all deleted items
        //new Thread( new Runnable() { public void run() { removeDeletedThread(); }}).start();
    }

    private void keyEventListenerThread()
    {
        while( true )
        {
            // Check for specific keys being pressed and act accordlingley

            // Check for delete command
            if( isKeyPressed( KeyEvent.VK_DELETE ) )
            {
                ArrayList<Shape> deletedLinks = new ArrayList<Shape>();

                for( int i = 0; i < selectedShapes.size(); i++ )
                {
                    int id = selectedShapes.get(i).getID();

                    // Remove all links asocated with this shape
                    for( int j = 0; j < links.size(); j++ )
                    {
                        if( links.get(i).isConnectedTo(id) ) 
                        {
                            links.get(i).delete();
                            deletedLinks.add(links.get(i));
                        }
                    }

                    selectedShapes.get(i).delete();

                    deselectAll();
                    repaint();
                }

                Shape[] s = new Shape[selectedShapes.size() + deletedLinks.size()];
                for( int j = 0; j < selectedShapes.size(); j++ )
                {
                    s[j] = selectedShapes.get(j);
                }
                for( int j = 0; j < deletedLinks.size(); j++ )
                {
                    s[selectedShapes.size() + j] = deletedLinks.get(j);
                }

                undoStack.add(s);
            }
            // Check for save command
            else if( isKeyPressed(KeyEvent.VK_CONTROL) && isKeyPressed(KeyEvent.VK_S) )
            {
                JFileChooser fc = new JFileChooser();
                int returnValue = fc.showSaveDialog( null );

                if( returnValue == JFileChooser.APPROVE_OPTION )
                {
                    this.save(fc.getCurrentDirectory().toString() + "/" + fc.getSelectedFile().getName());
                }
            }
            // Chek for load command
            else if( isKeyPressed(KeyEvent.VK_CONTROL) && isKeyPressed(KeyEvent.VK_L) )
            {
                JFileChooser fc = new JFileChooser();
                int returnValue = fc.showOpenDialog( null );

                if( returnValue == JFileChooser.APPROVE_OPTION )
                {
                    this.load(fc.getCurrentDirectory().toString() + "/" + fc.getSelectedFile().getName());
                }
            }
            // Check for print command
            else if( isKeyPressed(KeyEvent.VK_CONTROL) && isKeyPressed(KeyEvent.VK_P) )
            {
                this.deselectAll();
                PrinterJob job = PrinterJob.getPrinterJob();
                PageFormat pf = job.defaultPage();
                job.setPrintable(new DiagramPrinter(this), pf);

                boolean ok = job.printDialog();
                if( ok )
                {
                    try
                    {
                        job.print();
                        JOptionPane.showMessageDialog(null, "Print Job Sent To Printer!");
                    }
                    catch( PrinterException pe ) { JOptionPane.showMessageDialog(null, "Print Job Failed :("); }
                }
                
                keyReleased(KeyEvent.VK_P);
            }
            // Check for undo command
            else if( isKeyPressed(KeyEvent.VK_CONTROL) && isKeyPressed(KeyEvent.VK_Z) )
            {
                deselectAll();
                if( undoStack.size() == 0 ) return;

                Shape[] lastCommited = undoStack.remove(undoStack.size()-1);

                for( int i = 0; i < lastCommited.length; i++ )
                {
                    if( lastCommited[i].isDeleted() ) lastCommited[i].undoDelete();
                    else 
                    {
                        boolean canUndo = lastCommited[i].undo();
                        if( !canUndo ) shapes.get(i).delete();
                    }
                }

                //for( int i = 0; i < shapes.size(); i++ )
                //{
                //    boolean canUndo = shapes.get(i).undo();
                //    if( !canUndo ) shapes.get(i).delete();
                //}

                repaint();

                keyReleased(KeyEvent.VK_Z);

                try{ Thread.sleep(300); }
                catch( Exception e ) {}
            }
            // Check for the up key being pressed
            else if( isKeyPressed(KeyEvent.VK_UP) )
            {
                for( int i = 0; i < selectedShapes.size(); i++ )
                {
                    selectedShapes.get(i).move(0, -5);
                    selectedShapes.get(i).commit();
                }

                Shape[] s = new Shape[selectedShapes.size()];
                for( int i = 0; i < selectedShapes.size(); i++ )
                {
                    selectedShapes.get(i).commit();

                    s[i] = selectedShapes.get(i);
                }

                undoStack.add(s);

                keyReleased(KeyEvent.VK_UP);
            }
            // Check for the up key being pressed
            else if( isKeyPressed(KeyEvent.VK_DOWN) )
            {
                for( int i = 0; i < selectedShapes.size(); i++ )
                {
                    selectedShapes.get(i).move(0, 5);
                    selectedShapes.get(i).commit();
                }

                Shape[] s = new Shape[selectedShapes.size()];
                for( int i = 0; i < selectedShapes.size(); i++ )
                {
                    selectedShapes.get(i).commit();

                    s[i] = selectedShapes.get(i);
                }

                undoStack.add(s);

                keyReleased(KeyEvent.VK_DOWN);
            }
            // Check for the up key being pressed
            else if( isKeyPressed(KeyEvent.VK_LEFT) )
            {
                for( int i = 0; i < selectedShapes.size(); i++ )
                {
                    selectedShapes.get(i).move(-5, 0);
                    selectedShapes.get(i).commit();
                }

                Shape[] s = new Shape[selectedShapes.size()];
                for( int i = 0; i < selectedShapes.size(); i++ )
                {
                    selectedShapes.get(i).commit();

                    s[i] = selectedShapes.get(i);
                }

                undoStack.add(s);

                keyReleased(KeyEvent.VK_LEFT);
            }
            // Check for the up key being pressed
            else if( isKeyPressed(KeyEvent.VK_RIGHT) )
            {
                for( int i = 0; i < selectedShapes.size(); i++ )
                {
                    selectedShapes.get(i).move(5, 0);
                    selectedShapes.get(i).commit();
                }

                Shape[] s = new Shape[selectedShapes.size()];
                for( int i = 0; i < selectedShapes.size(); i++ )
                {
                    selectedShapes.get(i).commit();

                    s[i] = selectedShapes.get(i);
                }

                undoStack.add(s);

                keyReleased(KeyEvent.VK_RIGHT);
            }
        }
    }

    private void removeDeletedThread()
    {
        while( true )
        {
            System.out.println("[B] Removing deleted shapes");

            for( int i = 0; i < links.size(); i++ )
            {
                if( links.get(i).isDeleted() ) links.remove(i);
            }

            for( int i = 0; i < shapes.size(); i++ )
            {
                if( shapes.get(i).isDeleted() ) shapes.remove(i);
            }

            try{ Thread.sleep(5000); }
            catch( Exception e ) {}
        }
    }

    synchronized public void addSelectedEventListener( SelectedListener l )
    {
        if( listeners == null ) listeners = new Vector();
        listeners.addElement(l);
    }

    synchronized public void removeSelectedEventListener( SelectedListener l )
    {
        if( listeners == null ) listeners = new Vector();
        listeners.removeElement(l);
    }

    protected void fireSelectedEvent( int numSelected )
    {
        System.out.println("Selected Event Fireing");

        if( listeners != null && !listeners.isEmpty() )
        {
            System.out.println("Selected Event Fireing");

            SelectedEvent event = new SelectedEvent( this, numSelected );

            Vector targets;
            synchronized ( this )
            {
                targets = (Vector) listeners.clone();
            }

            Enumeration e = targets.elements();
            while( e.hasMoreElements() )
            {
                SelectedListener l = (SelectedListener) e.nextElement();
                l.selected(event);
            }
        }
    }

    private void doDrawing( Graphics g )
    {

        Graphics2D g2d = ( Graphics2D ) g;

        // Clear the screen 
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, 5000, 5000);

        // Draw the links between the various shapes
        for( int i = 0; i < links.size(); i++ )
        {
            links.get(i).draw(g, false);
        }

        // Draw all the shapes in the shapes arraylist
        //for( int i = 0; i < shapes.size(); i++ ) 
        //{
        //    if( scaling ) shapes.get(i).draw(g, true);
        //    else shapes.get(i).draw(g, false);
        //}
        for( int i = objectCount-1; i >= 0; i-- )
        {
            // Find the shape with this order value
            for( int j = 0; j < shapes.size(); j++ )
            {
                if( shapes.get(j).getOrderValue() == i )
                {
                    if( scaling ) shapes.get(j).draw(g, true);
                    else shapes.get(j).draw(g, false);
                    break;
                }
            }
        }

        // Display live drawing if a new rectangle or oval is being made
        if( (newRect || newOval || newHex) && shapeStartCoords != null && shapeEndCoords != null )
        {
            g2d.setColor(Color.GRAY);

            int newx = 0;
            int newy = 0;
            int newh = 0;
            int neww = 0;

            if( shapeStartCoords[0] < shapeEndCoords[0] ) newx = shapeStartCoords[0];
            else newx = shapeEndCoords[0];

            if( shapeStartCoords[1] < shapeEndCoords[1] ) newy = shapeStartCoords[1];
            else newy = shapeEndCoords[1];

            if( isKeyPressed(KeyEvent.VK_SHIFT) )
            {
                neww = newh = (Math.abs(shapeEndCoords[0] - shapeStartCoords[0]) + Math.abs(shapeEndCoords[1] - shapeStartCoords[1]) ) /2;
            }
            else
            {
                neww = Math.abs(shapeEndCoords[0] - shapeStartCoords[0]);

                newh = Math.abs(shapeEndCoords[1] - shapeStartCoords[1]);
            }

            if( newRect ) g2d.drawRect(newx, newy, neww, newh);
            else if( newOval ) g2d.drawOval(newx, newy, neww, newh);
            else if( newHex )
            {
                // Determine the points for the hexagon
                int xPoints[] = new int[] {newx + neww/2,
                        newx + neww,
                        newx + neww,
                        newx + neww/2,
                        newx,
                        newx};

                int yPoints[] = new int[] {newy,
                        newy + newh/3,
                        newy + (2 * newh)/3,
                        newy + newh,
                        newy + (2 * newh)/3,
                        newy + newh/3};

                g.drawPolygon(xPoints, yPoints, 6);
            }
        }

        g2d.dispose();
    }

    public void getPrintableGraphics( Graphics g)
    {

        // Draw the links between the various shapes
        for( int i = 0; i < links.size(); i++ )
        {
            links.get(i).draw(g, false);
        }

        for( int i = objectCount-1; i >= 0; i-- )
        {
            // Find the shape with this order value
            for( int j = 0; j < shapes.size(); j++ )
            {
                if( shapes.get(j).getOrderValue() == i ) shapes.get(j).draw(g, false);
            }
        }
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        doDrawing(g);
    }

    public void newRectangle()
    {
        newRect = newOval = newHex = false;

        if( !newOval && !newHex )
        {
            newRect = true;

            deselectAll();
        }

        repaint();
    }

    public void newOval()
    {
        newRect = newOval = newHex = false;

        if( !newRect && !newHex )
        {
            newOval = true;

            deselectAll();
        }

        repaint();
    }

    public void newHex()
    {
        newRect = newOval = newHex = false;

        if( !newOval && !newRect )
        {
            newHex = true;

            deselectAll();
        }

        repaint();
    }

    public void clear()
    {
        shapes.clear();
        selectedShapes.clear();
        links.clear();
        undoStack.clear();
        objectCount = 1;
        repaint();
    }

    public void keyPressed( KeyEvent e )
    {
        if( !isKeyPressed( e.getKeyCode() ) ) 
        {
            System.out.println("[*] Adding Pressed Key: " + e.getKeyCode());
            pressedKeys.add(e);
            repaint();
        }
    }

    public void keyReleased( KeyEvent e )
    {
        for( int i = 0; i < pressedKeys.size(); i++ )
        {
            if( pressedKeys.get(i).getKeyCode() == e.getKeyCode() ) 
            {
                pressedKeys.remove(i);
                System.out.println("[*] Removing Pressed Key: " + e.getKeyCode());
                repaint();
                break;
            }
        }
    }

    public void keyReleased( int code )
    {
        for( int i = 0; i < pressedKeys.size(); i++ )
        {
            if( pressedKeys.get(i).getKeyCode() == code)
            {
                pressedKeys.remove(i);
                System.out.println("[*] Removing Pressed Key: " + code);
                repaint();
                break;
            }
        }
    }

    public boolean isKeyPressed( int code )
    {
        if( pressedKeys.isEmpty() ) return false;

        for( int i = 0; i < pressedKeys.size(); i++ )
        {
            if( pressedKeys.get(i).getKeyCode() == code ) return true;
        }

        return false;
    }

    public void deselectAll()
    {
        for( int i = 0; i < selectedShapes.size(); i++ )
        {
            //selectedShapes.get(i).commit();
            selectedShapes.get(i).deSelect();
        }

        selectedShapes.clear();

        fireSelectedEvent(selectedShapes.size());
    }

    public void resetAllFlags()
    {
        moving = true;
        scaling = rotating = false;
    }

    public void setColor( Color c )
    {
        Shape[] s = new Shape[selectedShapes.size()];

        undoStack.add(s);
        for( int i = 0; i < selectedShapes.size(); i++ ) 
        {
            selectedShapes.get(i).setColor(c);
            selectedShapes.get(i).commit();
            s[i] = selectedShapes.get(i);
        }

        undoStack.add(s);
        repaint();
    }

    public void setText( String text )
    {
        selectedShapes.get(0).setText(text);
        selectedShapes.get(0).commit();
        undoStack.add(new Shape[] {selectedShapes.get(0)});

        repaint();
    }

    public void removeText()
    {
        selectedShapes.get(0).removeText();
        selectedShapes.get(0).commit();
        undoStack.add(new Shape[] {selectedShapes.get(0)});

        repaint();
    }

    public void moving()
    {
        resetAllFlags();

        moving = true;
        repaint();
    }

    public void scaling()
    {
        resetAllFlags();
        moving = false;

        if( selectedShapes.size() == 1 ) 
        {
            scaling = true;
            repaint();
            System.out.println("[!] Scaling :D");
        }
    }

    public void rotate()
    {
        resetAllFlags();

        rotating = true;
    }

    public void linkSelected()
    {
        links.add(new Link(selectedShapes.get(0), selectedShapes.get(1)));
        repaint();
    }

    public void alignSelectedVerticly()
    {
        int xSum = 0;
        int count = 0;
        for( int i = 0; i < selectedShapes.size(); i++ )
        {
            if( selectedShapes.get(i).getCenter() != null ) 
            {
                count++;
                xSum += selectedShapes.get(i).getCenter()[0];
            }
        }

        int xAve = xSum / count;
        Shape[] s = new Shape[selectedShapes.size()];
        for( int i = 0; i < selectedShapes.size(); i++ )
        {
            selectedShapes.get(i).move(xAve - selectedShapes.get(i).getCenter()[0], 0);
            selectedShapes.get(i).commit();
            s[i] = selectedShapes.get(i);
        }

        undoStack.add(s);
        repaint();
    }

    public void alignSelectedHorizontally()
    {
        int ySum = 0;
        int count = 0;
        for( int i = 0; i < selectedShapes.size(); i++ )
        {
            if( selectedShapes.get(i).getCenter() != null ) 
            {
                count++;
                ySum += selectedShapes.get(i).getCenter()[1];
            }
        }

        int yAve = ySum / count;
        Shape[] s = new Shape[selectedShapes.size()];
        for( int i = 0; i < selectedShapes.size(); i++ )
        {
            selectedShapes.get(i).move(0, yAve - selectedShapes.get(i).getCenter()[1]);
            selectedShapes.get(i).commit();
            s[i] = selectedShapes.get(i);
        }

        undoStack.add(s);
        repaint();
    }

    public void bringSelectedToFront()
    {
        if( selectedShapes.get(0).getOrderValue() == 0 ) return;

        for( int i = 0; i < shapes.size(); i++ ) shapes.get(i).setOrderValue(shapes.get(i).getOrderValue() + 1);
        selectedShapes.get(0).setOrderValue(0);
        selectedShapes.get(0).commit();
        undoStack.add(new Shape[] {selectedShapes.get(0)});

        repaint();
    }

    public void sendSelectedToBack()
    {
        if( selectedShapes.get(0).getOrderValue() == (objectCount - 1) ) return;

        for( int i = 0; i < shapes.size(); i++ ) shapes.get(i).setOrderValue(shapes.get(i).getOrderValue() - 1);
        selectedShapes.get(0).setOrderValue(objectCount-1);
        selectedShapes.get(0).commit();
        undoStack.add(new Shape[] {selectedShapes.get(0)});

        repaint();
    }

    public void save(String filePath)
    {
        if( !filePath.endsWith(".dg") ) filePath += ".dg";

        System.out.println("[*] Saving diagram to: " + filePath);

        try
        {
            BufferedWriter toWrite = new BufferedWriter( new FileWriter( new File( filePath ) ) );

            // Write all the shapes to file
            for( int i = 0; i < shapes.size(); i++ ) toWrite.append(shapes.get(i).toString());

            toWrite.append(";-;\n");

            // Write all the links to file
            for( int i = 0; i < links.size(); i++ ) toWrite.append(links.get(i).toString());//toWrite.append(String.format("LINK[%d:%d]", links.get(i)[0].getID(), links.get(i)[1].getID()));

            toWrite.flush();
            toWrite.close();
        }
        catch( IOException e ) { System.out.println("[E] An error occored while saving"); }
    }

    public void load(String filePath)
    {
        try
        {
            BufferedReader toRead = new BufferedReader( new FileReader( new File( filePath ) ) );
            String tmp = toRead.readLine();
            String fileText = "";

            while( tmp != null )
            {
                fileText += tmp;
                tmp = toRead.readLine();
            }

            clear();

            String[] diagramComponents = fileText.split(";-;");

            // First read in the shapes
            String[] s = diagramComponents[0].split(";;");
            for( int i = 0; i < s.length; i++ )
            {
                String shapeType = s[i].split(":")[0];

                String[] data = s[i].substring(s[i].indexOf("[")+1, s[i].lastIndexOf("]")).split(";");

                String[] dimension = data[0].split(",");
                int x = Integer.parseInt(dimension[0].substring(dimension[0].indexOf("[")+1, dimension[0].indexOf("]")).trim());
                int y = Integer.parseInt(dimension[1].substring(dimension[1].indexOf("[")+1, dimension[1].indexOf("]")).trim());
                int h = Integer.parseInt(dimension[2].substring(dimension[2].indexOf("[")+1, dimension[2].indexOf("]")).trim());
                int w = Integer.parseInt(dimension[3].substring(dimension[3].indexOf("[")+1, dimension[3].indexOf("]")).trim());
                int o = Integer.parseInt(dimension[4].substring(dimension[4].indexOf("[")+1, dimension[4].indexOf("]")).trim());

                int color = Integer.parseInt(data[1].substring(data[1].indexOf("[")+1, data[1].indexOf("]")).trim());

                int id = Integer.parseInt(data[2].substring(data[2].indexOf("[")+1, data[2].indexOf("]")).trim());
                
                String text = data[3].substring(data[3].indexOf("[")+1, data[3].indexOf("]")).trim();

                Shape newshape = null;
                if( shapeType.equals("RECTANGLE") ) 
                {
                    newshape = new Rectangle(x, y, h, w, id, o);

                }
                else if( shapeType.equals("OVAL") )
                {
                    newshape = new Oval(x, y, h, w, id, o);

                }
                else if( shapeType.equals("HEXAGON") )
                {
                    newshape = new Hexagon(x, y, h, w, id, o);

                }

                newshape.setColor(new Color(color));
                newshape.setText(text);
                shapes.add( newshape );

                objectCount++;
            }

            // Now read in the links between the shapes
            if( diagramComponents.length != 1 )
            {
                String[] l = diagramComponents[1].split(";");
                for( int i = 0; i < l.length; i++ )
                {
                    int id1 = Integer.parseInt(l[i].substring(l[i].indexOf("[")+1, l[i].indexOf(":")));
                    int id2 = Integer.parseInt(l[i].substring(l[i].indexOf(":")+1, l[i].indexOf("]")));

                    Shape[] shapelink = new Shape[2];

                    // Find the two objects
                    for( int j = 0; j < shapes.size(); j++ )
                    {
                        if( shapes.get(j).getID() == id1 )
                        {
                            shapelink[0] = shapes.get(j);
                            break;
                        }
                    }

                    for( int j = 0; j < shapes.size(); j++ )
                    {
                        if( shapes.get(j).getID() == id2 )
                        {
                            shapelink[1] = shapes.get(j);
                            break;
                        }
                    }

                    links.add(new Link(shapelink[0], shapelink[1]));
                }
            }
        }
        catch( IOException e ) { System.out.println("[E] There was an error when loading the file"); }
        
        repaint();
    }
}
