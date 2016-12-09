package JAutoCompleteTextField;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import Trie.Trie;

public class JAutoCompleteTextField extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8329338069894331812L;
	private static final String PLACEHOLDER_TEXT = "Street Name Search";

	private transient Vector<AutoCompletionListener> listeners;

	private Trie trie;

	private JTextField textField;

	private JComboBox<String> comboBox;

	public JAutoCompleteTextField() {
		super();

		init();
	}

	public JAutoCompleteTextField(Collection<String> strings) {
		super();

		init();

		// Add collection of strings to trie
		add(strings);
	}

	/**
	 * Contolls initilising the the JAutoCompleteTextField, it created a new
	 * Trie and it adds a listener so that AutoCompletionEvents can be fired
	 * when the text is changed
	 */
	private void init() {
		setLayout(new BorderLayout());

		trie = new Trie();

		// Initilise the textField
		textField = new JTextField();
		textField.setText(JAutoCompleteTextField.PLACEHOLDER_TEXT);

		// Handle adding and removing placeholder text
		textField.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent arg0) {
				if (textField.getText().equals(JAutoCompleteTextField.PLACEHOLDER_TEXT)) {
					textField.setText("");
				}
				
			}

			@Override
			public void focusLost(FocusEvent arg0) {
				if (textField.getText().equals("")) {
					textField.setText(JAutoCompleteTextField.PLACEHOLDER_TEXT);
				}	
			}
		});

		// Listen for changes in the text
		textField.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void changedUpdate(DocumentEvent arg0) {
				if (textField.getText() != "") {
					updateCompletionList();
				}
			}

			@Override
			public void insertUpdate(DocumentEvent arg0) {
				if (textField.getText() != "") {
					updateCompletionList();
				}
			}

			@Override
			public void removeUpdate(DocumentEvent arg0) {
				if (textField.getText() != "") {
					updateCompletionList();
				}
			}

		});

		// Add a keyboard listener to allow the user to traverse the sugestion list
		// with the arrow keys
		textField.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {}

			@Override
			public void keyReleased(KeyEvent e) {
				DefaultComboBoxModel<String> model = (DefaultComboBoxModel<String>) comboBox.getModel();

				if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_KP_UP) {
					String selected = (String) model.getSelectedItem();

					if (selected != null) {
						int index = model.getIndexOf(selected);

						if (index - 1 >= 0) {
							String newSelected = model.getElementAt(index - 1);
							model.setSelectedItem(newSelected);
						}
					}
				} else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_KP_DOWN) {
					String selected = (String) model.getSelectedItem();

					if (selected != null) {
						int index = model.getIndexOf(selected);

						if (index + 1 < model.getSize()) {
							String newSelected = model.getElementAt(index + 1);
							model.setSelectedItem(newSelected);
						}
					}
				} else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					String selected = (String) model.getSelectedItem();
					
					if (selected != null) {
						textField.setText(selected);
						updateCompletionList();
					}
				}

			}

			@Override
			public void keyTyped(KeyEvent e) {}

		});

		textField.setVisible(false);
		add(textField, BorderLayout.CENTER);

		comboBox = new JComboBox<String>();
		comboBox.setPreferredSize(new Dimension(JAutoCompleteTextField.this.getWidth(), 0));
		comboBox.setVisible(false);
		add(comboBox, BorderLayout.SOUTH);
		// autoCompletePanel = new JPanel();
	}
	
	/**
	 * Makes the text box and the combo box visible
	 * 
	 * NOTE: Making the combo box visible dosnt mean it will show up, it will only
	 * show up when there are sugestions to display
	 */
	public void makeVisible() {
		textField.setVisible(true);
		comboBox.setVisible(true);
	}

	/**
	 * Takes a string and passes it to the Trie add function
	 * 
	 * @param string
	 * @return wether string was inserted
	 */
	public boolean add(String string) {
		return trie.add(string);
	}

	/**
	 * Takes a collection of strings and passes it to the add collection
	 * function of the Trie
	 * 
	 * @param strings
	 */
	public void add(Collection<String> strings) {
		trie.addCollection(strings);
	}

	/**
	 * Takes a string and passes it to the remove function of the Trie
	 * 
	 * @param string
	 *            ,
	 * @return
	 */
	public boolean remove(String string) {
		return trie.remove(string);
	}

	/**
	 * Takes a string and passes it to the contains method of the Trie
	 * 
	 * @param string
	 * @return
	 */
	public boolean contains(String string) {
		return trie.contains(string);
	}

	/**
	 * Creates a new Trie
	 */
	public void clear() {
		trie = new Trie();
	}

	/**
	 * Handles getting the possible completions from the Trie, putting these 
	 * sugestions into the combo box popup. It then fires an event to alert listeners
	 * that there are new possible completions
	 */
	private void updateCompletionList() {
		String str = textField.getText();
		ArrayList<String> completions = trie.findPossibleCompletions(str);
		
		// We want to limit the output of sugestions to at most 10
		// so get the shortest ones
		ArrayList<String> shortest = new ArrayList<String>();

		if (completions.size() <= 10) {
			shortest.addAll(completions);
		} else {
			Collections.sort(completions, new Comparator<String>() {

				@Override
				public int compare(String arg0, String arg1) {
					return arg0.length() - arg1.length();
				}
			});

			for (int i = 0; i < 10; i++) {
				shortest.add(completions.get(i));
			}
		}

		// If there are no completions then hide the option popup, otherwise
		// show it with all the sugestions
		if (shortest.size() == 0) {
			comboBox.setPopupVisible(false);
		} else {
			DefaultComboBoxModel<String> model = (DefaultComboBoxModel<String>) comboBox.getModel();
			model.removeAllElements();

			for (String s : shortest) {
				model.addElement(s);
			}

			comboBox.setPopupVisible(true);
		}

		// Fire an event to update the completions elsewhere
		fireAutoCompletionEvent(str, shortest);
	}

	/**
	 * Handles adding a new AutoCompletionListener to the list of Listeners
	 * 
	 * @param l
	 */
	synchronized public void addAutoCompletionListener(AutoCompletionListener l) {
		if (listeners == null) {
			listeners = new Vector<AutoCompletionListener>();
		}

		listeners.add(l);
	}

	/**
	 * Handles removeing a listener from the list
	 * 
	 * @param l
	 */
	synchronized public void removeAutoCompletionListener(
			AutoCompletionListener l) {
		if (listeners == null) {
			listeners = new Vector<AutoCompletionListener>();
		}

		listeners.remove(l);
	}

	/**
	 * Creates a AutoCompletionEvent object with the currently entered text and
	 * current possible completions and sends it to all the listeners
	 */
	protected void fireAutoCompletionEvent(String partial,
			ArrayList<String> completions) {
		AutoCompletionEvent event = new AutoCompletionEvent(this, partial,
				completions);

		Vector<AutoCompletionListener> targets;
		synchronized (this) {
			targets = (Vector<AutoCompletionListener>) listeners.clone();
		}

		Enumeration<AutoCompletionListener> e = targets.elements();
		while (e.hasMoreElements()) {
			AutoCompletionListener l = e.nextElement();
			l.availableCompletions(event);
		}
	}
}
