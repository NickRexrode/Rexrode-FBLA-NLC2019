package rexrode.fbla.nlc;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

@SuppressWarnings("serial")
public class HelpMenu extends JFrame{
	private JPanel display = new JPanel();	
	private JList<String> list = new JList<String>();
	private String[] topics = {
			"Adding a Book",
			"Editing a Book",
			"Logging in",
			"Adding a person",
			"Editing a person",
			"Assigning and Returning Books",
			"Adding a New Login",
			"Editing a Login",
			"Search Boxes",
			"SQL Searching",
			"SQL Searching cont."
			
			
	};
	private String[] data = {
			"To add a book to the library, Starting on the first, main page, click the \"Books\" button.  \n Then you can click the \"New Book\" button \n Finally, you can fill in the data boxes appropiatly, when you are done, click \"Submit\"",
			"To edit a Book, Starting on the first, main page, click the \"Books\" button \n Then you can select the book you wish to edit on the left hand side list.  \n Click the \"Edit\" button.  Change the Values that you wish to change, then click \"Submit Edit\"",
			"To log into the System, Starting on the first, main page , type in your username and password.  \n If you do not have a username or password, refer to your Library to create one for you \n  Once you type in your information, click login. \n If your login is valid, you will be logged in. \n to Log out, click the \"Log Out\" button",
			"To add a person to the library, Stating on the first, main page, log in, then, click the \"Users\" button \n Then you can click the \"New Person\"Button. \n Once you fill out their information, click \"Submit\".",
			"To edit a Person, Starting on the first, main page, log in, then, click the \"Users\" button.  \n  Then you can select the person you wish to edit from the list on the left. \n  Click \"Edit\" then change the data you wish to change.  Then click \"Submit Edit\"",
			"To Assign or Return a book, to to the Users section.  \n Then, Select the User you wish to assign a book to. \n  You will see two lists.  One with their checked out books, the Other with books they could check out. \n To give them a book, select the book and click \"Assign Book\". \n To return a book, Select the book you wish to return and click \"Return Book\"",
			"To Add a new Login, starting on the main screen, log in and click \"Admin\". \n  Select \"Logins\" on the right hand side. \n  Click \"New Login\" and fill out their username and password. \n Then click \"Create\"",
			"To Edit a Login, starting on the main screen, log in and click \"Admin\". \n  Select \"Logins\" on the right hand side. \n Then select the Login you want to edit. \n Click \"Edit\" and fill out the information, then click \"Submit Edit\"",
			"This Application contains many search boxes.  To use a search box, click inside of it \n then type what you would like to search for.  Then hit \"Enter on your keyboard\"",
			"This program uses a sql database to store information.  This is located under \"res//txtFiles//data.db\". \n  You can do custom SQL searches with any database viewing program.  \n To do basic searches go to the admin page and click \"SQL\"",
			"To Search, you must follow the Syntax of SELECT (COLS) FROM table,  example: SELECT firstname, lastname FROM people \n The * or ALL is not supported.  If this syntax is not followed results will not be found.  \n The table names and Columns are books, people, logins, and respectively (title, author, topics, desc), (firstname, lastname, isTeacher, checkedOutBooks), (username, password)" ,
	};
	
	
	public HelpMenu() {

		//When new HelpMenu() is called init() will run 
		init();
	}
	
	
	public void init() {
		
		//This makes a window and it has a list and text area.  The text corresponds to the list index.  The data is above.
		setTitle("Help Menu!");
		setSize(800,800);
		setVisible(true);
		Color background = new Color(245,245,205);
		Font font = new Font("Comic Sans MS", Font.PLAIN, 16);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		getContentPane().setBackground(background);
		
		setBackground(background);
		setLocationRelativeTo(null);
		list.setListData(topics);
		GridBagLayout gbl = new GridBagLayout();
		getContentPane().setLayout(gbl);
		makeConstraints(gbl, list, 1,1,0,0,1.0,1.0);
		makeConstraints(gbl, display, 1,1,1,0,3.0,1.0);
		JTextArea text = new JTextArea(10,50);
		display.add(text);
		getContentPane().add(list);
		getContentPane().add(display);
		list.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				text.setText(data[list.getSelectedIndex()]);
				text.updateUI();
				text.revalidate();
				text.setLineWrap(true);
				text.setWrapStyleWord(true);
				display.revalidate();
				display.updateUI();
				
			}
		});
		list.setBackground(background);
		list.setFont(font);
		text.setFont(font);
		text.setBackground(background);
		display.setBackground(background);
	}
	
	
	public void makeConstraints(GridBagLayout gbl, JComponent comp, int w, int h, int x, int y, double weightx, double weighty) {
	    GridBagConstraints constraints = new GridBagConstraints();
	    constraints.fill = GridBagConstraints.BOTH;
		
	    constraints.gridwidth = w;
	    constraints.gridheight = h;
            constraints.gridx = x;
	    constraints.gridy = y;
	    constraints.weightx = weightx;
            constraints.weighty = weighty;
	    gbl.setConstraints(comp, constraints);
	}
}
