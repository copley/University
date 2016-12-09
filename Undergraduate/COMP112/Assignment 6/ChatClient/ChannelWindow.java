import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Write a description of class ChannelWindow here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class ChannelWindow extends JFrame
{
    private String name;
    private String username;
    private ServerConnection con;

    private JTextField input;
    private JTextArea output;
    //private JTextArea userlist;
    private JList userlist;
    private DefaultListModel list;
    private JLabel userLabel;
    //private JPanel usersList;

    private JButton send;
    private JButton leave;
    private JButton sendprivmsg;
    private JButton whois;
    private JButton kick;

    private ChatClientGUI mainWindow;

    private boolean privmsg;
    private boolean printThreadlock = false;
    private boolean isInitilised = false;

    public ChannelWindow(ChatClientGUI mw, String user, String channelName, ServerConnection c, boolean pm)
    {
        con = c;
        name = channelName;
        username = user;
        privmsg = pm;
        mainWindow = mw;

        setLayout(new BorderLayout());
        JPanel information = new JPanel();
        information.setLayout( new FlowLayout() );
        information.setPreferredSize(new Dimension(800, 800));

        output = new JTextArea();
        output.setEditable(false);
        output.setPreferredSize(new Dimension(700, 800));
        output.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        output.setAutoscrolls(true);

        JScrollPane scroll = new JScrollPane(output);
        scroll.setAutoscrolls(true);
        //scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        information.add(scroll);

        if( !privmsg )
        {

            //userlist = new JTextArea();
            //userlist.setEditable(false);
            //userlist.setPreferredSize(new Dimension(100, 800));
            //userlist.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            //userlist.setText("User List\n\n");

            JPanel usersList = new JPanel();
            usersList.setLayout(new BorderLayout());

            userLabel = new JLabel("Users");
            userLabel.setHorizontalTextPosition(JLabel.CENTER);
            usersList.add(userLabel, BorderLayout.NORTH);

            list = new DefaultListModel();
            userlist = new JList(list);
            userlist.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
            userlist.setLayoutOrientation(JList.VERTICAL);
            //userlist.setPreferredSize(new Dimension(100, 800));
            userlist.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            JScrollPane userscroll = new JScrollPane(userlist);
            userscroll.setPreferredSize(new Dimension(100, 755));

            usersList.add(userscroll, BorderLayout.CENTER);
            //userscroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

            JPanel buttons = new JPanel();
            buttons.setLayout(new FlowLayout());

            sendprivmsg = new JButton("MSG");
            sendprivmsg.addActionListener(new ActionListener()
                {
                    public void actionPerformed( ActionEvent e )
                    {
                        int index = userlist.getSelectedIndex();
                        String message ;
                        //do
                        //{
                        //    message = (String) JOptionPane.showInputDialog("Enter initial message to send", null);
                        //} while( message == null || message.equals("") );

                        message = (String) JOptionPane.showInputDialog("Enter initial message to send", null);

                        if( index != -1 && message != null )
                        {
                            con.send(String.format("PRIVMSG %s :%s", list.get(index), message));
                            mainWindow.addStartPrivateMessageWindow((String) list.get(index), message);
                        }
                        else print("[Server] Message was empty");
                    }
                });
            //sendprivmsg.setPreferredSize(new Dimension(50, 20));

            //usersList.add(sendprivmsg, BorderLayout.SOUTH);
            buttons.add(sendprivmsg);

            whois = new JButton("WHOIS");
            whois.addActionListener(new ActionListener()
                {
                    public void actionPerformed( ActionEvent e )
                    {
                        int index = userlist.getSelectedIndex();

                        System.out.println(String.format("WHOIS Button Pressed: %d", index));

                        if( index != -1 ) con.send(String.format("WHOIS %s", list.get(index)));
                    }
                });

            buttons.add(whois);

            kick = new JButton("Kick");
            kick.addActionListener(new ActionListener()
                {
                    public void actionPerformed( ActionEvent e )
                    {
                        int index = userlist.getSelectedIndex();

                        System.out.println(String.format("Kick Button Pressed: %d", index));

                        String kickMessage = (String) JOptionPane.showInputDialog(null, "Enter the kick message");

                        if( index != -1 ) con.send(String.format("KICK %s %s :%s", name, list.get(index), kickMessage));
                    }
                });

            buttons.add(kick);

            usersList.add(buttons, BorderLayout.SOUTH);

            //information.add(userscroll);
            information.add(usersList);

        }

        add(information, BorderLayout.CENTER);
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());

        input = new JTextField();
        input.setPreferredSize(new Dimension(600, 50));
        input.setFocusTraversalKeysEnabled(false);
        input.addActionListener(new ActionListener()
            {
                public void actionPerformed( ActionEvent e )
                {
                    send(input.getText().trim());
                    input.setText("");
                }
            });
        input.addKeyListener(new KeyListener()
            {
                public void keyTyped(KeyEvent e)
                {

                }

                public void keyPressed(KeyEvent e)
                {
                    // If tab is pressed try auto compleat the name
                    if( e.getKeyCode() == KeyEvent.VK_TAB )
                    {
                        String components[] = input.getText().split(" ");

                        if( !input.getText().equals("") )
                        {

                            String toCompleat = components[components.length-1];
                            System.out.println("Compleaing: " + toCompleat);
                            
                            // Look through names to see if there is one to auto comleat with
                            for( int i = 0; i < list.size(); i++ )
                            {
                                if( ((String) list.get(i) ).startsWith(toCompleat) )
                                {
                                    input.setText(String.format("%s %s", input.getText().substring(0, input.getText().lastIndexOf(" ")), (String)list.get(i)));
                                    break;
                                }
                            }
                        }
                    }
                }

                public void keyReleased(KeyEvent e)
                {

                }
            });

        inputPanel.add(input);
        send = new JButton("Send!");
        send.setPreferredSize(new Dimension(100, 50));
        send.addActionListener(new ActionListener()
            {
                public void actionPerformed( ActionEvent e )
                {
                    send(input.getText().trim());
                    input.setText("");
                }
            });

        inputPanel.add(send);

        if( !privmsg )
        {
            leave = new JButton("Part");
            leave.setPreferredSize(new Dimension(100, 50));
            leave.addActionListener(new ActionListener()
                {
                    public void actionPerformed( ActionEvent e )
                    {
                        con.send(String.format("PART %s \r\n", name));
                    }
                });

            inputPanel.add(leave);

        }
        add(inputPanel, BorderLayout.SOUTH);

        if( privmsg ) setTitle( String.format("Private Message - %s", channelName) );
        else setTitle(String.format("Channel - %s", channelName));
        setSize(1000, 930);
        //setDefaultCloseOperation(HIDE_ON_CLOSE);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        addWindowListener( new WindowAdapter() 
            {
                public void windowClosing(WindowEvent e)
                {
                    if( !privmsg ) con.send(String.format("PART %s", name));
                    else setVisible(false);
                    //parting();
                }
            });

        setVisible(true);
    }

    public String getChannel() { return name; }

    public void addUser(String user)
    {
        if( !username.contains(user) ) 
        {
            list.addElement(user);
            if( isInitilised ) print(String.format("--- %s Joined The Channel ---", user));
        }
    }

    public void removeUser( String user )
    {
        list.removeElement(user);
        print(String.format("--- %s Left The Channel ---", user));
    }

    private void send( String message )
    {
        if( message.startsWith("/") ) 
        {
            con.send(String.format("%s", message.substring(1)));
            print(String.format("[Sending Command] %s", message.substring(1)));
        }
        else 
        {
            con.send(String.format("PRIVMSG %s :%s", name, message));
            print(String.format("[%s] %s", username, message));
        }
    }

    private void print( String message )
    {
        while( printThreadlock );

        printThreadlock = true;

        String curText = output.getText();
        output.setText(curText + message.trim() + "\n");

        printThreadlock = false;
    }

    public void addMessage( IRCMessage m )
    {   
        String message = m.extendedParamater;//m.message.split(":")[2];

        print(String.format("[%s] %s", m.source.split("!")[0], message));
    }

    public void addMessage( String s )
    {
        print(s);
    }

    public boolean isAUser(String u)
    {
        for(int i = 0; i < list.capacity(); i++ )
        {
            if( u.equals( (String) list.get(i) ) ) return true;
        }

        return false;
    }

    public void initilis()
    {
        isInitilised = true;
    }

    public void joining()
    {
        //con.send(String.format("NAMES %s", name));
        setVisible(true);

    }

    public void parting()
    {
        setVisible(false);

        list.removeAllElements();

        //output.setForeground(Color.RED);
        print("-- Left Channel --\n\n--Joined Channel--");
        isInitilised = false;

    }
}
