import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Write a description of class ConnectWindow here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class ConnectWindow extends JDialog
{
    private JLabel serverAddressLabel;
    private JTextField serverAddressInput;

    private JLabel serverPortLabel;
    private JTextField serverPortInput;

    private JLabel fullnameLabel;
    private JTextField fullnameInput;

    private JLabel usernameLabel;
    private JTextField usernameInput;

    private JButton connect;
    
    private ChatClientGUI client;

    private boolean submitted;
    
    public ConnectWindow(Frame owner)
    {
        super(owner, "Enter IRC Server Information");
        
        client = (ChatClientGUI) owner;
        
        submitted = false;
        
        setLayout(new FlowLayout());

        JPanel serverAddressPanel = new JPanel();
        serverAddressLabel = new JLabel("Server Address");
        serverAddressPanel.add(serverAddressLabel);
        serverAddressInput = new JTextField();
        serverAddressInput.setPreferredSize(new Dimension(250, 20));
        serverAddressPanel.add(serverAddressInput);

        JPanel serverPortPanel = new JPanel();
        serverPortLabel = new JLabel("Server Port");
        serverPortPanel.add(serverPortLabel);
        serverPortInput = new JTextField();
        serverPortInput.setPreferredSize(new Dimension(250, 20));
        serverPortPanel.add(serverPortInput);

        JPanel fullnamePanel = new JPanel();
        fullnameLabel = new JLabel("Full Name");
        fullnamePanel.add(fullnameLabel);
        fullnameInput = new JTextField();
        fullnameInput.setPreferredSize(new Dimension(250, 20));
        fullnamePanel.add(fullnameInput);

        JPanel usernamePanel = new JPanel();
        usernameLabel = new JLabel("Username");
        usernamePanel.add(usernameLabel);
        usernameInput = new JTextField();
        usernameInput.setPreferredSize(new Dimension(250, 20));
        usernamePanel.add(usernameInput);

        connect = new JButton("Connect!");
        connect.addActionListener(new ActionListener()
            {
                public void actionPerformed( ActionEvent e )
                {
                    String address = serverAddressInput.getText();
                    String port = serverPortInput.getText();
                    String fullname = fullnameInput.getText();
                    String username = usernameInput.getText();
                    
                    if( !address.equals("") && !port.equals("") && !fullname.equals("") && !username.equals("") )
                    {
                        setVisible(false);
                        client.connect( new String[] {serverAddressInput.getText(), serverPortInput.getText(), fullnameInput.getText(), usernameInput.getText()});
                    }
                    else JOptionPane.showMessageDialog(null, "Not all input is valid");
                }
            });

        add(serverAddressPanel);
        add(serverPortPanel);
        add(fullnamePanel);
        add(usernamePanel);
        add(connect);

        //setTitle("Server Select");
        setPreferredSize(new Dimension(400, 250));

        pack();

        setVisible(true);
    }
    
    public String[] getServerInformation()
    {
        //while( !submitted );
        
        return new String[] {serverAddressInput.getText(), serverPortInput.getText(), fullnameInput.getText(), usernameInput.getText()};
    }
}
