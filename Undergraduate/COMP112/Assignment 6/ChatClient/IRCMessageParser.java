/**
 * Write a description of class IRCMessageParser here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class IRCMessageParser
{
    public static IRCMessage ParseMessage( String message )
    {
        IRCMessage m = new IRCMessage();

        m.source = message.substring(1, message.indexOf(" "));

        String[] components = message.split(" :");
        String[] paramaters = components[0].split(" ");
        String command = paramaters[1].trim();
        
        try
        {
            m.code = Integer.parseInt(command);
            m.type = IRCMessageType.CODE;
        }
        catch( Exception e )
        {
            m.command = command.trim();
            m.type = IRCMessageType.COMMAND;
        }

        for( int i = 2; i < paramaters.length; i++ )
        {
            m.paramaters.add(paramaters[i].trim());
        }

        if( components.length == 2 ) m.extendedParamater = components[1];

        return m;

        //message = message.trim();
        //IRCMessage m = new IRCMessage();

        //String[] components = message.split(" ");

        // Determine what sorta message it is
        //try
        //{
        //    // If this scuseeds then the message is a code message
        //    int code = Integer.parseInt( components[1] );

        //    m.type = IRCMessageType.CODE;
        //    m.source = components[0].substring(1).trim();
        //    m.code = code;
        //}
        //catch( Exception e )
        //{
        //    m.type = IRCMessageType.COMMAND;
        //    m.source = components[0].substring(1).trim();
        //    m.command = components[1].trim();

        // If its a message
        //if( m.command.equals("PRIVMSG") )
        //{
        //    m.payload = message.split(":")[2];
        //    m.channel = components[2];
        //}
        // If its a ping
        //else if( m.command.equals("PING") )
        //{
        //    m.payload = components[2];
        //}
    }

    //m.message = message;
    //
    //return m;
}