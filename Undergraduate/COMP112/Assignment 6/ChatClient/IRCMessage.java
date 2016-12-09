import java.util.*;

/**
 */
public class IRCMessage
{
    public ArrayList<String> paramaters = new ArrayList<String>();
    
    public String extendedParamater = "";
    
    // Will tell the program what type of message we are dealing with
    public IRCMessageType type;
    
    public String source = "";
    
    // Will tell the program what to do with the payload
    public String command;
    public int code;
    
    public String toString()
    {
        String s = "";
        
        if( type == IRCMessageType.CODE ) s += String.format("CODE[%d]", code);
        else s += String.format("COMMAND[%s]", command);
        
        s += "PARAMATERS[";
        for( int i = 0; i < paramaters.size(); i++ )
        {
            s += String.format("%s, ", paramaters.get(i));
        }
        s += "]";
        
        s += String.format("EXTENDED[%s]", extendedParamater);

        //s += String.format("[%s] ", source);

        //if( type == IRCMessageType.CODE )
        //{
        //    s += String.format("CODE[%d] ", code);
        //}
        //else 
        //{
        //    s += String.format("COMMAND[%s] ", command);
        //}
        
        
        //s += String.format("PARAMATERS[");
        //for( int i = 0; i < paramaters.size(); i++ )
        //{
        //    if( i != 0 ) s += ",";
        //    s += String.format("%s", paramaters.get(i));
        //}
        
        //s += "]";
        
        //s += String.format(" FINAL PARAMATER[%s]", extendedParamater);
        
        //s += String.format(" : %s", message);
        
        return s;
    }
}
