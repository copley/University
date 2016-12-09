import java.awt.Graphics;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.awt.print.*;
import javax.print.attribute.*;
import javax.imageio.ImageIO;
import java.net.URL;

/**
 * Write a description of class DiagramEdititor here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class DiagramEdititor extends JFrame implements SelectedListener
{
    private Graphics g;
    private JToolBar toolbar;

    private JButton newRectangel;
    private JButton newOval;
    private JButton newHexagon;

    // Tool buttons
    private JButton move;
    private JButton scale;
    private JButton rotate;
    private JButton changeColor;
    private JButton setText;
    private JButton removeText;
    private JButton link;
    private JButton alignVerticly;
    private JButton alignHorizontally;
    private JButton bringToFront;
    private JButton sendToBack;

    private JMenuBar menuBar;

    private JMenuItem newMenuItem;
    private JMenuItem loadMenuItem;
    private JMenuItem saveMenuItem;
    private JMenuItem printMenuItem;

    private DiagramPane diagram;

    public DiagramEdititor()
    {
        super("Diagram Editior");
        setLayout(new BorderLayout());

        // Add the canvas to the center of the window
        diagram = new DiagramPane();
        diagram.addKeyListener( new KeyPressListener());
        add(diagram, BorderLayout.CENTER);

        menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);

        newMenuItem = new JMenuItem("New");
        newMenuItem.addActionListener( new ActionListener()
            {
                public void actionPerformed( ActionEvent e )
                {
                    diagram.clear();

                    move.setEnabled(false);
                    scale.setEnabled(false);
                    changeColor.setEnabled(false);
                    setText.setEnabled(false);
                    removeText.setEnabled(false);
                    link.setEnabled(false);
                    alignVerticly.setEnabled(false);
                    alignHorizontally.setEnabled(false);
                    bringToFront.setEnabled(false);
                    sendToBack.setEnabled(false);
                }
            });
        fileMenu.add(newMenuItem);

        loadMenuItem = new JMenuItem("Load - Ctrl L");
        loadMenuItem.addActionListener( new ActionListener()
            {
                public void actionPerformed( ActionEvent e )
                {
                    JFileChooser fc = new JFileChooser();
                    int returnValue = fc.showOpenDialog( DiagramEdititor.this );

                    if( returnValue == JFileChooser.APPROVE_OPTION )
                    {
                        diagram.load(fc.getCurrentDirectory().toString() + "/" + fc.getSelectedFile().getName());
                    }
                }
            });
        fileMenu.add(loadMenuItem);

        saveMenuItem = new JMenuItem("Save - Ctrl S");
        saveMenuItem.addActionListener( new ActionListener()
            {
                public void actionPerformed( ActionEvent e )
                {
                    JFileChooser fc = new JFileChooser();
                    int returnValue = fc.showSaveDialog( DiagramEdititor.this );

                    if( returnValue == JFileChooser.APPROVE_OPTION )
                    {
                        diagram.save(fc.getCurrentDirectory().toString() + "/" + fc.getSelectedFile().getName());
                    }
                }
            });
        fileMenu.add(saveMenuItem);

        printMenuItem = new JMenuItem("Print - Ctrl P");
        printMenuItem.addActionListener( new ActionListener()
            {
                public void actionPerformed( ActionEvent e )
                {       
                    diagram.deselectAll();
                    PrinterJob job = PrinterJob.getPrinterJob();
                    PageFormat pf = job.defaultPage();
                    job.setPrintable(new DiagramPrinter(diagram), pf);

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
                }
            });
        fileMenu.add(printMenuItem);

        setJMenuBar(menuBar);

        // Create the new rectangle button

        // Create the tools toolbar
        JToolBar tools = new JToolBar(null, JToolBar.VERTICAL);
        add(tools, BorderLayout.WEST);

        //newRectangel = new JButton("New Rectangle");
        newRectangel = new JButton();
        newRectangel.setIcon( new ImageIcon("buttons/newRectangle.gif"));
        newRectangel.setToolTipText("Lets you click and drag to create a new rectangle shape ");
        newRectangel.addActionListener( new ActionListener() 
            {
                public void actionPerformed( ActionEvent e )
                {
                   // diagram.deselectAll();
                    diagram.newRectangle();
                }
            });
        newRectangel.setFocusable(false);
        tools.add(newRectangel);

        // Create the new oval button
        //newOval = new JButton("New Oval");
        newOval = new JButton();
        newOval.setIcon( new ImageIcon("buttons/newOval.gif"));
        newOval.setToolTipText("Lets you click and drag to create a new oval shape");
        newOval.addActionListener( new ActionListener()
            {
                public void actionPerformed( ActionEvent e )
                {
                    //diagram.deselectAll();
                    diagram.newOval();
                }
            });
        newOval.setFocusable(false);
        tools.add(newOval);

        //newHexagon = new JButton("New Hexagon");
        newHexagon = new JButton();
        newHexagon.setIcon( new ImageIcon("buttons/newHexagon.gif"));
        newHexagon.setFocusable(false);
        newHexagon.setToolTipText("Lets you click and drag to create a new hexagon");
        newHexagon.addActionListener( new ActionListener()
            {
                public void actionPerformed( ActionEvent e )
                {
                    //diagram.deselectAll();
                    diagram.newHex();
                }
            });
        tools.add(newHexagon);
            
        //move = new JButton("Move");
        move = new JButton();
        move.setIcon( new ImageIcon("buttons/move.gif"));
        move.setToolTipText("Lets you move the selected shape(s) around");
        move.setFocusable(false);
        move.setEnabled(false);
        move.addActionListener( new ActionListener()
            {
                public void actionPerformed( ActionEvent e )
                {
                    diagram.moving();
                }
            });
        tools.add(move);

        //scale = new JButton("Scale");
        scale = new JButton();
        scale.setIcon( new ImageIcon("buttons/scale.gif"));
        scale.setToolTipText("Lets you scale the selected shape");
        scale.setFocusable(false);
        scale.setEnabled(false);
        scale.addActionListener( new ActionListener()
            {
                public void actionPerformed( ActionEvent e )
                {
                    diagram.scaling();
                }
            });
        tools.add(scale);

        //changeColor = new JButton("Change Color");
        changeColor = new JButton();
        changeColor.setIcon( new ImageIcon("buttons/changeColor.gif"));
        changeColor.setToolTipText("Lets you change the color of the selected shape(s)");
        changeColor.setFocusable(false);
        changeColor.setEnabled(false);
        changeColor.addActionListener( new ActionListener()
            {
                public void actionPerformed( ActionEvent e )
                {
                    Color newColor = JColorChooser.showDialog(null, "Pick A New Color For Your Shape(s)", null);
                    if( newColor != null ) diagram.setColor(newColor);
                }
            });
        tools.add(changeColor);

        //setText = new JButton("Set Text");
        setText = new JButton();
        setText.setIcon( new ImageIcon("buttons/setText.gif"));
        setText.setToolTipText("Lets you set the text for the selected shape");
        setText.setFocusable(false);
        setText.setEnabled(false);
        setText.addActionListener( new ActionListener() 
            {
                public void actionPerformed( ActionEvent e )
                {
                    String text = (String) JOptionPane.showInputDialog("Enter text to set", null);
                    if( text != null )
                    {
                        diagram.setText(text);
                    }
                }
            });
        tools.add(setText);

        //removeText = new JButton("Remove Text");
        removeText = new JButton();
        removeText.setIcon( new ImageIcon("buttons/removeText.gif"));
        removeText.setToolTipText("Lets you remove the text on the selected shape");
        removeText.setFocusable(false);
        removeText.setEnabled(false);
        removeText.addActionListener( new ActionListener()
            {
                public void actionPerformed( ActionEvent e )
                {
                    diagram.removeText();
                }
            });
        tools.add(removeText);

        //link = new JButton("Link");
        link = new JButton();
        link.setIcon( new ImageIcon("buttons/link.gif"));
        link.setToolTipText("Links the two selected shapes together with a line");
        link.setFocusable(false);
        link.setEnabled(false);
        link.addActionListener(new ActionListener()
            {
                public void actionPerformed( ActionEvent e )
                {
                    diagram.linkSelected();
                }
            });
        tools.add(link);

        //alignVerticly = new JButton("Align Verticly");
        alignVerticly = new JButton();
        alignVerticly.setIcon( new ImageIcon("buttons/alignVerticly.gif"));
        alignVerticly.setToolTipText("Aligns all the shapes verticly");
        alignVerticly.setFocusable(false);
        alignVerticly.setEnabled(false);
        alignVerticly.addActionListener( new ActionListener()
            {
                public void actionPerformed( ActionEvent e )
                {
                    diagram.alignSelectedVerticly();
                }
            });
        tools.add(alignVerticly);

        //alignHorizontally = new JButton("Align Horizontally");
        alignHorizontally = new JButton();
        alignHorizontally.setIcon( new ImageIcon("buttons/alignHorizontally.gif"));
        alignHorizontally.setToolTipText("Aligns all the shapes horizontally");
        alignHorizontally.setFocusable(false);
        alignHorizontally.setEnabled(false);
        alignHorizontally.addActionListener( new ActionListener()
            {
                public void actionPerformed( ActionEvent e )
                {
                    diagram.alignSelectedHorizontally();
                }
            });
        tools.add(alignHorizontally);

        //bringToFront = new JButton("Bring To Front");
        bringToFront = new JButton();
        bringToFront.setIcon( new ImageIcon("buttons/bringToFront.gif"));
        bringToFront.setToolTipText("Brings the selected shape to the front");
        bringToFront.setFocusable(false);
        bringToFront.setEnabled(false);
        bringToFront.addActionListener( new ActionListener()
            {
                public void actionPerformed( ActionEvent e )
                {
                    diagram.bringSelectedToFront();
                }
            });
        tools.add(bringToFront);

        //sendToBack = new JButton("Send To Back");
        sendToBack = new JButton();
        sendToBack.setIcon( new ImageIcon("buttons/sendToBack.gif"));
        sendToBack.setToolTipText("Sends the selected shape to the back");
        sendToBack.setFocusable(false);
        sendToBack.setEnabled(false);
        sendToBack.addActionListener( new ActionListener()
            {
                public void actionPerformed( ActionEvent e )
                {
                    diagram.sendSelectedToBack();
                }
            });
        tools.add(sendToBack);

        addKeyListener( new KeyPressListener() );
        diagram.addSelectedEventListener(this);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(new Dimension(1000,900));
        setVisible(true);
    }

    public void selected( SelectedEvent e )
    {
        System.out.println(String.format("[*] Select Change: %d", e.getSelected()));

        if( e.getSelected() >= 1 )
        {
            move.setEnabled(true);
            scale.setEnabled(true);
            changeColor.setEnabled(true);
            setText.setEnabled(true);
            removeText.setEnabled(true);
            bringToFront.setEnabled(true);
            sendToBack.setEnabled(true);
        }

        if( e.getSelected() >= 2 )
        {
            link.setEnabled(true);
            alignVerticly.setEnabled(true);
            alignHorizontally.setEnabled(true);
            bringToFront.setEnabled(false);
            sendToBack.setEnabled(false);
        }
        else
        {
            link.setEnabled(false);
            alignVerticly.setEnabled(false);
            alignHorizontally.setEnabled(false);
        }

        if( e.getSelected() == 0 )  
        {
            move.setEnabled(false);
            scale.setEnabled(false);
            changeColor.setEnabled(false);
            setText.setEnabled(false);
            removeText.setEnabled(false);
            link.setEnabled(false);
            alignVerticly.setEnabled(false);
            alignHorizontally.setEnabled(false);
            bringToFront.setEnabled(false);
            sendToBack.setEnabled(false);
        }
    }

    private class KeyPressListener implements KeyListener
    {
        public void keyTyped( KeyEvent e )
        {
            System.out.println(e);
        }

        public void keyPressed( KeyEvent e )
        {
            diagram.keyPressed(e);
        }

        public void keyReleased( KeyEvent e )
        {
            diagram.keyReleased(e);
        }
    }

    /**
     * 
     */
    public static void main( String[] args )
    {
        new DiagramEdititor();
    }
}
