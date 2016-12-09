package GUI.FileGUI;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import java.io.File;
import java.io.IOException;

@SuppressWarnings("serial")
public class FileGUI extends JFrame{

    private JTextField ipText;
    private JTextField nameText;
    private JButton actionButton;
    private JButton addButton;

    private FileDNSDB dnsDB;

    public FileGUI() throws IOException{
	super("DNS DB with B+ Tree");
	setDefaultCloseOperation(EXIT_ON_CLOSE);

	dnsDB = new FileDNSDB();

	setLayout(new BorderLayout());
	ipText = new JTextField();
	ipText.setColumns(15);
	ipText.addKeyListener(new KeyAdapter() {

		@Override
		public void keyReleased(KeyEvent e) {
		    validateButton();
		}
	    });
	nameText = new JTextField();
	nameText.setColumns(15);
	nameText.addKeyListener(new KeyAdapter() {

		@Override
		public void keyPressed(KeyEvent e) {
		    validateButton();
		}
	    });

	JPanel labels = new JPanel();
	labels.setLayout(new BoxLayout(labels, BoxLayout.Y_AXIS));
	labels.add(new JLabel("IP: "));
	labels.add(Box.createVerticalStrut(10));
	labels.add(new JLabel("Name: "));
	labels.setBorder(new EmptyBorder(5, 5, 5, 5));

	JPanel fields = new JPanel();
	fields.setLayout(new BoxLayout(fields, BoxLayout.Y_AXIS));
	fields.add(ipText);
	fields.add(Box.createVerticalStrut(5));
	fields.add(nameText);
	fields.setBorder(new EmptyBorder(5, 5, 10, 5));

	add(labels, BorderLayout.WEST);
	add(fields, BorderLayout.CENTER);

	actionButton = new JButton();
	actionButton.setMaximumSize(new Dimension(250, 50));
	actionButton.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
		    try {
				handleAction();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	    });

	addButton = new JButton();
	addButton.setMaximumSize(new Dimension(250, 50));
	addButton.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
		    try {
				handleAdd();
			} catch (HeadlessException | IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	    });

	validateButton();

	JPanel buttonPanel = new JPanel();
	buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
	buttonPanel.add(actionButton);
	buttonPanel.add(Box.createVerticalStrut(5));
	buttonPanel.add(addButton);
	add(buttonPanel, BorderLayout.SOUTH);


		
	JButton iterateAll = new JButton("Iterate All Pairs");
	iterateAll.setMaximumSize(new Dimension(250, 50));
	iterateAll.addActionListener(new ActionListener() {
			
		@Override
		public void actionPerformed(ActionEvent e) {
		    dnsDB.iterateAll();
		}
	    });
		
	JButton runTest = new JButton("Run Test");
	runTest.setMaximumSize(new Dimension(250, 50));
	runTest.addActionListener(new ActionListener() {
			
		@Override
		public void actionPerformed(ActionEvent e) {
		    JFileChooser chooser = new JFileChooser(".");//System.getProperty("user.dir"));
		    int res = chooser.showOpenDialog(FileGUI.this);
		    if(res == JFileChooser.APPROVE_OPTION){
			try {
				dnsDB.testAllPairs(chooser.getSelectedFile());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		    }
		}
	    });
		
	buttonPanel.add(Box.createVerticalStrut(5));
	buttonPanel.add(iterateAll);
	buttonPanel.add(Box.createVerticalStrut(5));
	buttonPanel.add(runTest);

	setLocationRelativeTo(null);
	pack();
	setVisible(true);
    }

    private void validateButton(){
	String ip = ipText.getText().trim();
	String name = nameText.getText().trim();

	if(ip.isEmpty() && name.isEmpty()){
	    actionButton.setText("No Input");
	    actionButton.setEnabled(false);
	    addButton.setText("Can't Add");
	    addButton.setEnabled(false);
	} else if(ip.isEmpty()){
	    actionButton.setText("Find IP");
	    actionButton.setEnabled(true);
	    addButton.setText("Can't Add");
	    addButton.setEnabled(false);
	} else if(name.isEmpty()){
	    actionButton.setText("Find Host Name");
	    actionButton.setEnabled(true);
	    addButton.setText("Can't Add");
	    addButton.setEnabled(false);
	} else{
	    actionButton.setText("Test Name-IP Pair");
	    actionButton.setEnabled(true);
	    addButton.setText("Add Name-IP pair");
	    addButton.setEnabled(true);
	}
    }
	
    private void handleAction() throws IOException {
	if(actionButton.getText().equals("Find IP")){
	    Integer ip = dnsDB.findIP(nameText.getText());
	    if(ip == null){
		JOptionPane.showMessageDialog(FileGUI.this, "Could not find an IP address with host name: " + nameText.getText());
	    } else{
		ipText.setText(FileDNSDB.IPToString(ip));
	    }
	} else if(actionButton.getText().equals("Find Host Name")){
	    Integer ip = FileDNSDB.stringToIP(ipText.getText());
	    if(ip == null){
		JOptionPane.showMessageDialog(FileGUI.this, "'" + ipText.getText() + "' is not a valid IP address.");
	    } else{
		String name = dnsDB.findHostName(ip);
		if(name == null){
		    JOptionPane.showMessageDialog(FileGUI.this, "Could not find a host name with IP address: " + ipText.getText());
		} else{
		    nameText.setText(name);
		}
	    }
	} else if(actionButton.getText().equals("Test Name-IP Pair")){
	    Integer ip = FileDNSDB.stringToIP(ipText.getText());
	    if(ip == null){
		JOptionPane.showMessageDialog(FileGUI.this, "'" + ipText.getText() + "' is not a valid IP address.");
	    } else{
		boolean valid = dnsDB.testPair(ip, nameText.getText());
		if(valid){
		    JOptionPane.showMessageDialog(FileGUI.this, "'" + ipText.getText() + "' is correctly mapped to '" + nameText.getText() + "'.");
		} else{
		    JOptionPane.showMessageDialog(FileGUI.this, "'" + ipText.getText() + "' is not the IP address for '" + nameText.getText() + "'.");
		}
	    }
	}
    }

    private void handleAdd() throws HeadlessException, IOException {
	Integer ip = FileDNSDB.stringToIP(ipText.getText());
	if(ip == null){
	    JOptionPane.showMessageDialog(FileGUI.this, "'" + ipText.getText() + "' is not a valid IP address.");
	} else{
	    if (dnsDB.add(nameText.getText(), ip)){
		JOptionPane.showMessageDialog(FileGUI.this, "'" + ipText.getText() + "' is now mapped to '" + nameText.getText() + "'.");
	    } else{
		JOptionPane.showMessageDialog(FileGUI.this, "adding '" + ipText.getText() + "' - '" + nameText.getText() + "' failed.");
	    }
	}
    }
    
    public void load(File f){
	dnsDB.load(f);
    }



    public static void main(String[] args) throws IOException {
	File file = new File("host-list.txt");
	if (args.length>0){
	    File f = new File(args[0]);
	    if (f.exists()) file = f;
	}
	new FileGUI().load(file);
    }

}
