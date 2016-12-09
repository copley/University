import java.util.EventObject;

/**
 * Write a description of class SelectedEvent here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class SelectedEvent extends EventObject
{
    private int numSelected;
    
    public SelectedEvent(Object source, int numSelected)
    {
        super(source);
        this.numSelected = numSelected;
    }
    
    public int getSelected()
    {
        return numSelected;
    }

}
