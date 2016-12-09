import java.awt.*;
import java.awt.event.*;

import java.util.Collections;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.BorderFactory;
import javax.swing.SwingUtilities;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.*;

import java.util.*;

/**
 * Write a description of class ChatClientGUI here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class ChatClientGUI extends JFrame
{
    private JTextArea serverOutput;
    private JTextArea channellist;
    private JTextField input;

    private JButton connect;
    private JButton disconnect;
    private JButton listChannels;
    private JButton join;
    private JButton create;
    private JButton leave;
    private JButton send;

    private JButton help;
    private JButton info;
    private JButton ison;
    private JButton motd;
    private JButton setName;
    private JButton setNick;
    private JButton time;

    private JList list;
    private JScrollPane listScroll;
    private JLabel channelLabel;
    private DefaultListModel listModel;

    private ServerConnection con;
    private String fullname;
    private String username;

    private ArrayList<String[]> channels;

    private ArrayList<String[]> whoisRouting;

    //private ArrayList<String> toSend;
    private LinkedBlockingQueue<String> toSend;
    //private boolean sendThreadlock = false;
    private boolean printLock = false;

    //private ArrayList<ChannelWindow> windows;
    private ArrayList<ChannelWindow> windows;

    public ChatClientGUI()
    {
        whoisRouting = new ArrayList<String[]>();

        // Setup the text area for the server output
        JPanel outputPanel = new JPanel();
        outputPanel.setLayout(new BorderLayout());
        outputPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Creat the output window
        serverOutput = new JTextArea();
        serverOutput.setEditable(false);
        serverOutput.setLineWrap(false);
        JScrollPane scroll = new JScrollPane(serverOutput);
        scroll.setPreferredSize(new Dimension(500, 500));
        outputPanel.add(scroll, BorderLayout.CENTER);

        // Create the panel to hold the channel list information
        JPanel channelList = new JPanel();
        channelList.setLayout(new BorderLayout());

        // Create the channel label
        channelLabel = new JLabel("Channels");
        channelLabel.setHorizontalTextPosition(JLabel.CENTER);
        channelList.add(channelLabel, BorderLayout.NORTH);

        // Create the list to hold the channel information
        listModel = new DefaultListModel();
        list = new JList(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        list.setLayoutOrientation(JList.VERTICAL);

        // Put the information 
        JScrollPane listScrollable = new JScrollPane(list);
        listScrollable.setPreferredSize(new Dimension(100, 260));

        channelList.add(listScrollable, BorderLayout.CENTER);

        join = new JButton("Join");
        join.addActionListener(new ActionListener()
            {
                public void actionPerformed( ActionEvent e )
                {
                    int index = list.getSelectedIndex();

                    if( index != -1 )
                    {
                        send(String.format("JOIN %s", listModel.get(index)));
                    }
                }
            });
        join.setPreferredSize(new Dimension(100, 20));

        channelList.add(join, BorderLayout.SOUTH);

        outputPanel.add(channelList, BorderLayout.EAST);

        add(outputPanel, BorderLayout.CENTER);

        // Setup the user input are
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        inputPanel.setPreferredSize(new Dimension(600, 200));

        JPanel buttons = new JPanel();
        buttons.setLayout(new FlowLayout());
        buttons.setPreferredSize(new Dimension(700, 50));
        buttons.setMinimumSize(buttons.getPreferredSize());

        // Add button to connect to server
        connect = new JButton("Connect");
        connect.addActionListener(new ActionListener() 
            {
                public void actionPerformed( ActionEvent event )
                {
                    if( con == null )
                    {
                        serverOutput.setText("");
                        listModel.clear();

                        ConnectWindow cw = new ConnectWindow(ChatClientGUI.this);

                        toSend = new LinkedBlockingQueue<String>();
                        windows = new ArrayList<ChannelWindow>();
                    }
                    else print("You are allready connected");

                }
            });
        connect.setPreferredSize(new Dimension(130, 50));
        buttons.add(connect);

        // Add button to disconnect from the server
        disconnect = new JButton("Disconnect");
        disconnect.addActionListener(new ActionListener() 
            {
                public void actionPerformed( ActionEvent event )
                {
                    if( con != null )
                    {
                        // Send the quit command
                        send("QUIT");
                    }
                }
            });
        disconnect.setPreferredSize(new Dimension(130,50));
        buttons.add(disconnect);

        create = new JButton("Create Channel");
        create.addActionListener(new ActionListener()
            {
                public void actionPerformed( ActionEvent event )
                {
                    if( con != null )
                    {
                        // Send the list command
                        String channelName = (String) JOptionPane.showInputDialog("Enter the channel name", null);
                        if( channelName != null ) send(String.format("JOIN %s", channelName));
                        send("LIST");
                    }
                }
            });
        create.setPreferredSize(new Dimension(130,50));
        buttons.add(create);

        inputPanel.add(buttons, BorderLayout.CENTER);

        JPanel commandInput = new JPanel();
        commandInput.setLayout(new FlowLayout());
        commandInput.setPreferredSize(new Dimension(700, 30));

        input = new JTextField();
        input.setPreferredSize(new Dimension(600, 20));
        input.addKeyListener(new KeyListener() 
            { 
                public void keyReleased( KeyEvent event )
                {

                }

                public void keyTyped(KeyEvent event)
                {

                }

                public void keyPressed(KeyEvent event)
                {
                    if( event.getKeyCode() == KeyEvent.VK_ENTER )
                    {
                        if( con != null )
                        {
                            // Send command to server
                            String text = input.getText().trim();
                            //System.out.println(text);

                            print(String.format("[Command] %s", text));
                            send(text);

                            input.setText("");
                            input.setCaretPosition(0);
                        }
                    }
                }

            });
        commandInput.add(input);

        send = new JButton("Send");
        send.setPreferredSize(new Dimension(100, 20));
        send.addActionListener(new ActionListener() 
            {
                public void actionPerformed( ActionEvent e )
                {
                    if( con != null )
                    {
                        print(String.format("[Command] %s", input.getText()));
                        send(input.getText());

                        input.setText("");
                    }
                }
            });
        commandInput.add(send);

        inputPanel.add(commandInput, BorderLayout.NORTH);

        //inputPanel.setBorder(BorderFactory.createEmptyBorder(0, 40, 40, 40));
        add(inputPanel, BorderLayout.SOUTH);

        // Create the toolbar
        JToolBar toolbar = new JToolBar("Commands", JToolBar.HORIZONTAL);

        help = new JButton("Help");
        help.addActionListener(new ActionListener()
            {
                public void actionPerformed( ActionEvent e )
                {
                    if( con != null )
                    {
                        send("HELP");
                    }
                }
            });
        toolbar.add(help);

        info = new JButton("Info");
        info.addActionListener(new ActionListener()
            {
                public void actionPerformed( ActionEvent e )
                {
                    if( con != null )
                    {
                        send("INFO");
                    }
                }
            });
        toolbar.add(info);

        ison = new JButton("ISON");
        ison.addActionListener(new ActionListener()
            {
                public void actionPerformed( ActionEvent e )
                {
                    if( con != null )
                    {
                        String users = (String) JOptionPane.showInputDialog("Comma seperated list of users to see if there online", null);
                        if( users != null ) send(String.format("ISON %s", users));
                    }
                }
            });
        toolbar.add(ison);

        motd = new JButton("MOTD");
        motd.addActionListener(new ActionListener()
            {
                public void actionPerformed( ActionEvent e )
                {
                    if( con != null )
                    {
                        send("MOTD");
                    }
                }
            });
        toolbar.add(motd);

        setName = new JButton("Set Name");
        setName.addActionListener(new ActionListener()
            {
                public void actionPerformed( ActionEvent e )
                {
                    if( con != null )
                    {
                        String name = (String) JOptionPane.showInputDialog("Enter your new name", null);
                        if( name != null ) 
                        {
                            send(String.format("SETNAME %s", name));
                            print(String.format("[Server] Name Set To: %s", name));
                        }
                        else print("[Server] You need to actually enter something");
                    }
                }
            });
        toolbar.add(setName);

        setNick = new JButton("Set Nick");
        setNick.addActionListener(new ActionListener()
            {
                public void actionPerformed( ActionEvent e )
                {
                    if( con != null )
                    {
                        String nick = (String) JOptionPane.showInputDialog("Enter your new nickname", null);
                        if( nick != null ) 
                        {
                            send(String.format("NICK %s", nick));
                            print(String.format("[Server] Nickname Set To: %s", nick));
                        }
                        else print("[Server] You need to actually enter something");
                    }
                }
            });
        toolbar.add(setNick);
        
        time = new JButton("Time");
        time.addActionListener(new ActionListener()
            {
                public void actionPerformed( ActionEvent e )
                {
                    if( con != null )
                    {
                        send("TIME");
                    }
                }
            });
        toolbar.add(time);

        add(toolbar, BorderLayout.NORTH);
        setTitle("IRC Client");
        setSize(780, 780);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void connect(String[] serverInformation)
    {
        try
        {
            con = new ServerConnection(serverInformation[0], Integer.parseInt(serverInformation[1]));
            con.connect();

            // Start read thread
            new Thread( new Runnable() { public void run() { readLoop(); }}).start();

            // Start write thread
            new Thread( new Runnable() { public void run() { sendLoop(); }}).start();

            // Start thread to monotor the connection
            new Thread( new Runnable() { public void run() { monotorConnectionLoop(); }}).start();

            fullname = serverInformation[2];
            username = serverInformation[3];

            login();
        }
        catch( Exception e ) { print("[ERROR!] Connection Failed"); }
    }

    private void login()
    {
        print(String.format("Logging in:\n Realname: %s\n Username: %s", fullname, username));

        // Login to the server
        //con.send(String.format("NICK %s", username));
        //con.send(String.format("USER %s 0 unused :%s", username, fullname));
        send(String.format("NICK %s", username));
        send(String.format("USER %s 0 unused :%s", username, fullname));

    }

    private void send( String message )
    {
        //while( sendThreadlock );
        //sendThreadlock = true;

        toSend.add(message);

        // Look at message being sent and see if something needs to be done
        if( message.contains("PRIVMSG") )
        {
            //processMessage(IRCMessageParser.ParseMessage(message));
            String[] components = message.split(" ");
            String channel = components[1];
            String msg = components[2].substring(1);

            boolean isWindow = false;
            for( int i = 0; i < windows.size(); i++ )
            {
                if( windows.get(i).getChannel().equals(channel) ) 
                {
                    isWindow = true;
                    //windows.get(i).joining();
                    windows.get(i).addMessage(String.format("[%s] %s", username, msg));
                    windows.get(i).toFront();
                }
            }
        }

        //sendThreadlock = false;
    }

    private void monotorConnectionLoop()
    {
        // Do nothing while the connection is active
        while( con.isConnected() );

        con = null;

        // Get rid of all the connections 
        for( int i = 0; i < windows.size(); i++ )
        {
            windows.get(i).setVisible(false);
            windows.get(i).dispose();
        }

        windows.clear();
    }

    private void updateChannelsLoop()
    {
        // Wait untill connection is started
        //while( con == null );

        //while( con.isConnected() )
        try
        {
            while( con.isConnected() )
            {
                // Send the list command
                send("LIST");

                // Sleep the thread for 5 seconds because we dont want to be updating it constantly
                try { Thread.sleep(15000); }
                catch (Exception e) {}
            }
        } catch (Exception e) {}
    }

    private void sendLoop()
    {
        // Wait untill connection is started
        //while( con == null );

        //while( con.isConnected() )

        // Catch the exception if the connection is closed when the thread is running
        try
        {
            while( con.isConnected() )

            //while( con.isConnected() )
            {
                //while( sendThreadlock );
                //sendThreadlock = true;

                String message = toSend.poll();

                if( message != null ) con.send(message);

                //sendThreadlock = false;
            }
        } catch (Exception e) {}
    }

    private void readLoop()
    {
        // Wait untill connection is started
        //while( con == null );

        //while( con.isConnected() )

        // Catch the exception if the connection is closed when the thread is running
        try
        {
            while( con.isConnected() )

            //while( con.isConnected() ) 
            {
                final IRCMessage msg = con.read();

                if( msg != null )
                {
                    System.out.println("To String: " + msg.toString());

                    // Process the recived message on a new thread
                    processMessage(msg);
                }
                //new Thread( new Runnable() { public void run() { processMessage(msg); }}).start();
            }
        } catch (Exception e) {}
    }

    private void processMessage( IRCMessage m )
    {    
        if( m.type == IRCMessageType.CODE )
        {   
            // If user login was scusessful
            if( m.code == Replies.IRC_RPL_MYINFO )
            {
                while( printLock );
                print("Login was scusessful!");

                // Start thread to update the list of channels
                new Thread( new Runnable() { public void run() { updateChannelsLoop(); }}).start();

                //send("NAMES");
            }
            // If the username was allready taken
            else if( m.code == Replies.IRC_ERR_USERONCHANNEL )
            {
                username = (String) JOptionPane.showInputDialog(this, "The username you entered was taken. Choose another one");
                login();
            }
            // If the username contained bad charactors
            else if( m.code == Replies.IRC_ERR_NICKNAMEINUSE )
            {
                //print("The username contained bad charactors");
                username = (String) JOptionPane.showInputDialog(this, "The username you entered contained bad charactors. Choose another one");
                login();
            }
            // We are reciving about to recive a list of channels
            else if( m.code == Replies.IRC_STA_LIST )
            {
                //channels = new ArrayList<String[]>();
                //channellist.setText("Channel List:\n\n");
                listModel.clear();
            }
            // We are reciving channel information
            else if( m.code == Replies.IRC_RPL_LIST )
            {
                // Split out the channel information
                //String[] tmp = m.message.split(" ");
                //String channelName = tmp[3].trim();
                //String numPeople = tmp[4];

                String channelName = m.paramaters.get(1);
                String numPeople = m.paramaters.get(2);

                //channels.add(new String[] {channelName, numPeople});
                //channellist.setText(channellist.getText() + String.format("%s : %s\n", channelName, numPeople));
                listModel.addElement(String.format("%s", channelName));
                list.ensureIndexIsVisible(listModel.capacity()-1);

            }
            // We have recived all the channel information
            else if( m.code == Replies.IRC_RPL_LISTEND )
            {
                //list.updateUI();
                //print("Channel List: Name, # People");
                //for( int i = 0; i < channels.size(); i++ )
                //{
                //    String[] tmp = channels.get(i);
                //    print(String.format("%s, %s", tmp[0], tmp[1]));
                //}
            }
            // If we recive a list of users for a channel
            else if( m.code == 353 )
            {
                //String[] components = m.message.split("= ");
                //String channel = components[1].substring(0, components[1].indexOf(":")).trim();

                String[] usersList = m.extendedParamater.split(" ");
                String channel = m.paramaters.get(2);

                //String[] usersList = m.message.split(":")[2].split(" ");

                ChannelWindow current = null;

                for( int i = 0; i < windows.size(); i++ )
                {
                    if( windows.get(i).getChannel().equals(channel) ) 
                    {
                        current = windows.get(i);
                    }
                }

                if( current != null )
                {
                    for( int i = 0; i < usersList.length; i++ )
                    {
                        current.addUser(usersList[i]);
                    }

                    current.initilis();
                }

            }
            // If the list of names has ended
            else if( m.code == 323 )
            {

            }
            // Start of WHOIS information
            else if( m.code == 319 )
            {
                whoisRouting.add(new String[] {m.paramaters.get(1), m.extendedParamater});
            }
            // Description WHOIS information
            else if( m.code == 312 )
            {
                String u = m.paramaters.get(1);
                String desc = m.extendedParamater;

                String[] routing = null;

                // Get routing information
                for( int i = 0; i < whoisRouting.size(); i++ )
                {
                    if( u.equals(m.paramaters.get(1)) ) routing = whoisRouting.get(i);
                }

                // Get window to send info to
                if( routing != null )
                {
                    for( int i = 0; i < windows.size(); i++ )
                    {
                        if( routing[1].contains(windows.get(i).getChannel()) ) windows.get(i).addMessage(String.format("[WHOIS - %s] Description: %s", u, desc));
                    }
                }
            }
            // IP address WHOIS information
            else if( m.code == 338 )
            {
                String u = m.paramaters.get(1);
                String ip = m.paramaters.get(2);
                String desc = m.extendedParamater;

                String[] routing = null;

                // Get routing information
                for( int i = 0; i < whoisRouting.size(); i++ )
                {
                    if( u.equals(m.paramaters.get(1)) ) routing = whoisRouting.get(i);
                }

                // Get window to send info to
                if( routing != null )
                {
                    for( int i = 0; i < windows.size(); i++ )
                    {
                        if( routing[1].contains(windows.get(i).getChannel()) ) windows.get(i).addMessage(String.format("[WHOIS - %s] IP: %s (%s)", u, ip, desc));
                    }
                }
            }
            // Time WHOIS information
            else if( m.code == 317 )
            {
                String u = m.paramaters.get(1);
                //String ip = m.paramaters.get(2);
                //String desc = m.extendedParamater;
                String idlesec = m.paramaters.get(2);
                String signonsec = m.paramaters.get(3);

                String[] routing = null;

                // Get routing information
                for( int i = 0; i < whoisRouting.size(); i++ )
                {
                    if( u.equals(m.paramaters.get(1)) ) routing = whoisRouting.get(i);
                }

                // Get window to send info to
                if( routing != null )
                {
                    for( int i = 0; i < windows.size(); i++ )
                    {
                        if( routing[1].contains(windows.get(i).getChannel()) ) windows.get(i).addMessage(String.format("[WHOIS - %s] Idle Seconds: %s. Sign On Sec: &s", u, idlesec, signonsec));
                    }
                }
            }
            // End of WHOIS information
            else if( m.code == 318 )
            {
                String u = m.paramaters.get(1);

                // Get routing information
                for( int i = 0; i < whoisRouting.size(); i++ )
                {
                    if( u.equals(m.paramaters.get(1)) ) whoisRouting.remove(i);
                }
            }
            // If you request the channel topic
            else if( m.code == 332 )
            {
                String topic = m.extendedParamater;
                String channel = m.paramaters.get(1);

                for( int i = 0; i < windows.size(); i++ )
                {
                    if( channel.equals(windows.get(i).getChannel()) ) windows.get(i).addMessage(String.format("!-- Topic: %s --!", topic));
                }
            }
            else if( m.code == 323 ) {}
            else if( m.code == 374 ) {}
            // If no whois info
            else if( m.code == 401 )
            {
                String u = m.paramaters.get(1);
                for( int i = 0; i < whoisRouting.size(); i++ )
                {
                    if( u.equals(m.paramaters.get(1)) ) whoisRouting.remove(i);
                }
                print("[Server] No WHOIS Information");
            }
            // If you do not have permition to run a command
            else if( m.code == 482 )
            {
                String channel = m.paramaters.get(1);
                String serverMessage = m.extendedParamater;

                for( int i = 0; i < windows.size(); i++ )
                {
                    if( channel.equals(windows.get(i).getChannel()) ) windows.get(i).addMessage(String.format("[SERVER] %s", serverMessage));
                }
            }
            // If the user has joined to many channels
            else if( m.code == Replies.IRC_ERR_TOOMANYCHANNELS )
            {
                print("[Server] You have joined to many channels");
            }
            else if( m.code == Replies.IRC_ERR_UNKNOWNCOMMAND )
            {
                print("[Serer] The command you sent is unknown");
                toFront();
            }
            else if( m.code == 303 )
            {
                print(String.format("[Server - ISON Reply] %s is online", m.extendedParamater));
            }
            else if( m.code == Replies.IRC_ERR_NOSUCHCHANNEL )
            {
                print("[Server] That channel is invalid");
            }
            else if( m.code == Replies.IRC_ERR_CANNOTSENDTOCHAN )
            {
                print("[Server] Can not send to that channel");
            }
            else if( m.code == Replies.IRC_ERR_NOSUCHSERVICE )
            {
                print("[Server] No such service");
            }
            else print(String.format("[Server] %s", m.extendedParamater));
        }
        else
        {
            // If we get a ping sliently reply to it 
            if( m.command.equals("PING") )
            {
                // Get the data to respond with
                //String data = m.message.split(" ")[2];
                String data = m.paramaters.get(0);
                send(String.format("PONG %s", data));
            }
            //else if( m.command.equals("ERROR") )
            //{
            //    print("You have disconnected");
            //    con.disconnect();
            //    con = null;

            // Distroy all the open chat windows
            //    for( int i = 0; i < windows.size(); i++ )
            //    {
            //        windows.get(i).dispose();
            //    }

            //    windows.clear();

            //}
            else if( m.command.equals("SQUIT") )
            {
                print("Server Disconnect");
                con.disconnect();
            }
            // If we have joined a channel
            else if( m.command.equals("JOIN") )
            {
                // Split out channel name
                //String channel = m.message.split(":")[2];
                //String user = m.message.split("!")[0].substring(1);
                String channel = m.extendedParamater;
                String user = m.source.split("!")[0];

                if( user.equals(username.substring(0, user.length()))  )
                {

                    print(String.format("[Action] Joined: %s", channel));

                    // Check to see if there is an window allready for this chat
                    boolean isWindow = false;
                    for( int i = 0; i < windows.size(); i++ )
                    {
                        if( windows.get(i).getChannel().equals(channel) ) 
                        {
                            isWindow = true;
                            windows.get(i).joining();
                        }
                    }

                    if( !isWindow )
                    {
                        // Open up window for reciving and sending messages consernd with this channel
                        windows.add(new ChannelWindow(ChatClientGUI.this, username, channel, con, false));
                        System.out.println("Window Created");
                    }
                }
                else
                {
                    ChannelWindow current = null;
                    for( int i = 0; i < windows.size(); i++ )
                    {
                        if( windows.get(i).getChannel().equals(channel) ) 
                        {
                            current = windows.get(i);
                        }
                    }

                    if( current != null ) current.addUser(user);
                }
            }
            // If we have joined a channel
            else if( m.command.equals("PART") )
            {
                // Split out channel name
                //String channel = m.message.split(" ")[2];
                //String user = m.message.split("!")[0].substring(1);
                String channel = m.paramaters.get(0);
                String user = m.source.split("!")[0];

                if( user.equals(username.substring(0, user.length()))  )
                {

                    for( int i = 0; i < windows.size(); i++ )
                    {
                        if( windows.get(i).getChannel().equals(channel) ) 
                        {
                            //windows.remove(i).setVisible(false);
                            windows.get(i).parting();
                            //windows.remove(i).dispose();
                        }
                    }

                    print(String.format("[Action] Leaving %s", channel));
                }
                else
                {
                    ChannelWindow current = null;
                    for( int i = 0; i < windows.size(); i++ )
                    {
                        if( windows.get(i).getChannel().equals(channel) ) 
                        {
                            current = windows.get(i);
                        }
                    }

                    if( current != null ) current.removeUser(user);
                }
            }
            // If we recive a message
            else if( m.command.equals("PRIVMSG") )
            {
                // Split out the message information
                //String channel = m.message.split(" ")[2];
                //String message = m.message.substring(m.message.indexOf(":", 1), m.message.length());
                String channel = m.paramaters.get(0);
                String message = m.extendedParamater;

                System.out.println(String.format("MESSAGE RECIVED! [%s:%s]: %s", channel, m.source, message));
                String tmp = username.substring(0, channel.length()-1);
                // If the message was specifily to you then the channel will be your username
                if( username.length() != 0 && channel.equals(username.substring(0, channel.length())) )
                {
                    channel = m.source.split("!")[0];
                }

                boolean windowFound = false;
                // Route message to specific window
                for( int i = 0; i < windows.size(); i++ )
                {
                    if( windows.get(i).getChannel().equals(channel) ) 
                    {
                        windowFound = true;
                        windows.get(i).setVisible(true);
                        //windows.get(i).joining();
                        windows.get(i).addMessage(m);
                    }
                }

                // If window not found then it is a private message
                if( !windowFound )
                {
                    windows.add(new ChannelWindow(ChatClientGUI.this, username, channel, con, true));
                    windows.get(windows.size()-1).addMessage(m);
                }
            }
            else if( m.command.equals("KICK") )
            {
                String channel = m.paramaters.get(0);
                String kicke = m.paramaters.get(1);
                String message = m.extendedParamater;

                // If you where kicked
                if( username.contains(kicke) )
                {
                    for( int i = 0; i < windows.size(); i++ )
                    {
                        if( windows.get(i).getChannel().equals(channel) ) 
                        {
                            //windows.remove(i).setVisible(false);
                            //windows.remove(i).dispose();
                            windows.get(i).parting();
                        }
                    }

                    JOptionPane.showMessageDialog(null, String.format("You where kicked from %s with message: %s", channel, message) );

                }
                else
                {
                    for( int i = 0; i < windows.size(); i++ )
                    {
                        if( windows.get(i).getChannel().equals(channel) ) 
                        {
                            windows.get(i).addMessage(String.format("--- %s Was kicked with message: %s ---", kicke, message));
                        }
                    }
                }
            }
            else if( m.command.equals("TOPIC") )
            {
                String channel = m.paramaters.get(0);
                String topic = m.extendedParamater;

                for( int i = 0; i < windows.size(); i++ )
                {
                    if( channel.equals(windows.get(i).getChannel()) ) windows.get(i).addMessage(String.format("!-- Topic: %s --!", topic));
                }
            }
            else if( m.command.equals("NOTICE") )
            {
                String source = m.source.split("!")[0];

                print(String.format("[NOTICE - %s] %s", source, m.extendedParamater));
                this.toFront();
            }

        }
    }

    public void addStartPrivateMessageWindow(String user, String message)
    {
        boolean windowFound = false;
        // Route message to specific window
        for( int i = 0; i < windows.size(); i++ )
        {
            if( windows.get(i).getChannel().equals(user) ) 
            {
                windowFound = true;
                windows.get(i).setVisible(true);
                //windows.get(i).joining();
                windows.get(i).addMessage(message);
            }
        }

        if( !windowFound )
        {
            windows.add(new ChannelWindow(ChatClientGUI.this, username, user, con, true));
            windows.get(windows.size()-1).addMessage(String.format("[%s] %s", username, message));
        }
    }

    public void print(String str)
    {
        printLock = true;

        String curText = serverOutput.getText();
        serverOutput.setText(curText + str.trim() + "\n");

        printLock = false;
    }

    public void clearOutput()
    {
        serverOutput.setText("");
    }
}
