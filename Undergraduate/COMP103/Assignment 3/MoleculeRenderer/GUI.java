import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Write a description of class GUI here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class GUI extends JFrame
{
    private JToolBar toolBar;
    private JButton loadMolecule;
    private JButton front;
    private JButton back;
    private JButton left;
    private JButton right;
    private JButton top;
    private JButton bottom;
    private MoleculeRenderer mr;
    //private ProjectedMoleculeRenderer mr;

    public GUI()
    {
        setLayout(new BorderLayout());

        addKeyListener( new KeyListener()
            {
                public void keyTyped(KeyEvent e) {}

                public void keyPressed(KeyEvent e) 
                { 
                    System.out.println(e);
                    
                    if( e.getKeyCode() == KeyEvent.VK_RIGHT ) mr.pan(-5);
                    else if( e.getKeyCode() == KeyEvent.VK_LEFT ) mr.pan(5);
                    else if( e.getKeyCode() == KeyEvent.VK_UP ) mr.tilt(5);
                    else if( e.getKeyCode() == KeyEvent.VK_DOWN ) mr.tilt(-5);
                    else if( e.getKeyCode() == 61 ) mr.zoom(0.1);
                    else if( e.getKeyCode() == 45 ) mr.zoom(-0.1);
                }

                public void keyReleased(KeyEvent e) {}
            });

        mr = new MoleculeRenderer();
        //mr = new ProjectedMoleculeRenderer();
        add(mr, BorderLayout.CENTER);

        // Create the toolbar
        toolBar = new JToolBar(null, JToolBar.HORIZONTAL);
        add(toolBar, BorderLayout.NORTH);

        loadMolecule = new JButton("Load Molecule");
        loadMolecule.setFocusable(false);
        loadMolecule.addActionListener( new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    JFileChooser fc = new JFileChooser();
                    int returnValue = fc.showOpenDialog( GUI.this );

                    if( returnValue == JFileChooser.APPROVE_OPTION )
                    {
                        mr.readMoleculeFile(fc.getCurrentDirectory().toString() + "/" + fc.getSelectedFile().getName());
                    }

                }
            });
        toolBar.add(loadMolecule);

        front = new JButton("Front");
        front.setFocusable(false);
        front.addActionListener( new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    mr.viewFromFront();
                }
            });
        toolBar.add(front);

        back = new JButton("Back");
        back.setFocusable(false);
        back.addActionListener( new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    mr.viewFromBack();
                }
            });
        toolBar.add(back);

        left = new JButton("Left");
        left.setFocusable(false);
        left.addActionListener( new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    mr.viewFromLeft();
                }
            });
        toolBar.add(left);

        right = new JButton("Right");
        right.setFocusable(false);
        right.addActionListener( new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    mr.viewFromRight();
                }
            });
        toolBar.add(right);

        top = new JButton("Top");
        top.setFocusable(false);
        top.addActionListener( new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    mr.viewFromTop();
                }
            });
        toolBar.add(top);

        bottom = new JButton("Bottom");
        bottom.setFocusable(false);
        bottom.addActionListener( new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    mr.viewFromBottom();
                }
            });
        toolBar.add(bottom);

        setTitle("Molecule Renderer");
        setSize(new Dimension(1000, 1000));
        setVisible(true);
    }

    public static void main(String[] args)
    {
        new GUI();
    }
}
