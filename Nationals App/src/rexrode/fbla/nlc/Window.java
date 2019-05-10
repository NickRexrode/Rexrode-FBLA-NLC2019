package rexrode.fbla.nlc;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.BorderFactory;

import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

@SuppressWarnings("serial")
public class Window extends JFrame{
    private Font font = new Font("Comic Sans MS", Font.PLAIN, 24);
    private JPanel displayPane =new JPanel();
    private JPanel buttonPane = new JPanel();
    private JPanel logos = new JPanel();
    private JPanel buttons= new JPanel();
    private JPanel login = new JPanel();
    GridBagLayout layout = new GridBagLayout();
	private JTable table = new JTable();  
    private boolean librarian = false;
    private Color background = new Color(245,245,205);


    public Window() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        FileSystem.checkForDBFiles();
        initMenu();
    }
	
    public void clear() {
    	//This clears the Frame
    	login.removeAll();
    	buttons.removeAll();
    	logos.removeAll();
    	getContentPane().removeAll();
    	revalidate();
    	repaint();	
    }
	
    public void adminScreen() {
    	/* ADMINSCREEN METHOD -
    	 * This Method will trigger when the Admin Button is pressed and the User is loged in.*/
    	displayPane.removeAll();
    	buttonPane.removeAll();
    	displayPane.setBackground(background);
        clear(); //Removes ALL JComponents from the window
        
        GridBagLayout adminLayout = new GridBagLayout(); //Creates the overall layout for the admin screen.  I used GridBag because I can easily size the areas.
        getContentPane().setLayout(adminLayout); //sets the layout
        getContentPane().setBackground(background);
        getContentPane().revalidate();
        
        
        buttonPane.setLayout(new GridLayout(3,1)); // Sets the Layout to 3by1.  3 Tall 1 wide.
        
  
        
        

        
        
        //Initializing the three buttons on the buttonPanel.  They will be put on in the order Back,Logins,SQL
        
        JButton back = new JButton("back");
        back.setFont(new Font("Comic Sans MS", Font.PLAIN, 60));
        back.setBackground(background);
        JButton logins = new JButton("Logins");
        logins.setFont(new Font("Comic Sans MS", Font.PLAIN, 60));
        logins.setBackground(background);
        JButton SQL = new JButton("SQL");
        SQL.setFont(new Font("Comic Sans MS", Font.PLAIN, 60));
        SQL.setBackground(background);
 
        
        //This is the action Listener for the SQL Button.  When it is pressed, the SQL page will be shown
        SQL.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		displayPane.removeAll();
        		initSQL();
        	}
        });
 
        //This is the action Listener for the Logins Button. When it is press the Login page will be shown.
        logins.addActionListener(new ActionListener() {
 
            public void actionPerformed(ActionEvent e) {
            	LoginsScreen();
            } 
        });
        
        
        //this is the back button.  When its pressed this will trigger
        back.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	//Goes back to the first screen.
                initMenu();
            }
        }) ;
        
            

        
        //the back SQL and logins buttons are always added.
        buttonPane.add(back);
        buttonPane.add(SQL);
        buttonPane.add(logins);
        
        //sets constraints for the adminLayout.
        makeConstraints(adminLayout, buttonPane, 1,1,1,0,.25,1.0);
        makeConstraints(adminLayout, displayPane, 1,1,0,0,1.0,1.0);
        
        
        
        getContentPane().add(buttonPane);
        getContentPane().add(displayPane);
        getContentPane().revalidate();
    }
    public void initSQL() {
    	//This method is where the SQL database can be displayed
    	//Most of this is just formating
    	displayPane.removeAll();
    	buttonPane.removeAll();
    	displayPane.revalidate();
    	buttonPane.revalidate();
    	
    	JButton back = new JButton("Back");
    	JButton help = new JButton("Help");
    	help.addActionListener(new ActionListener() {


			public void actionPerformed(ActionEvent arg0) {
				new HelpMenu();
				
			}
    		
    	});
    	back.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			adminScreen();
    		}
    	});
    	back.setFont(new Font("Comic Sans MS", Font.PLAIN, 60));
    	back.setBackground(background);
    	help.setFont(new Font("Comic Sans MS", Font.PLAIN, 60));
    	help.setBackground(background);
    	buttonPane.setBackground(background);
    	buttonPane.add(back);
    	buttonPane.add(help);
  	
    	table.setBackground(background);
    	table.setFont(font);
    	JTextField input = new JTextField("SQL Code");
    	input.setFont(font);
    	GridBagLayout layouttt = new GridBagLayout();
    	
    	makeConstraints(layouttt, input,1,1,0,0,1.0,.1);
    	makeConstraints(layouttt, table,1,1,0,1,1.0,3);
    	
    	displayPane.setLayout(layouttt);
    	
    	displayPane.add(input);
	    displayPane.add(table);
    	//This is the input listener.  When you press enter it will interpret what you put in.
    	input.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			String sql = input.getText();
    			//makes sure there is SELECT and FROM in the serach
    			if (!(sql.contains("SELECT") && sql.contains("FROM"))) {
    				return;
    			}
    			//gets positions to get the column names
    			String col = sql.substring(sql.indexOf("SELECT")+6, sql.indexOf("FROM")).trim();

    			String[] colnames = col.split(", ");
    			String[][] data = new String[30][colnames.length];
    			//^^ Creates array with the column names and then one for the data.  It is 30 long just to show that it works.
    			table.revalidate();
    			table.updateUI();
    			//This is all connecting to SQL database
    		    String url = "jdbc:sqlite:"+new File("").getAbsolutePath() +"/res/textFiles/data.db";	
    	       
    	        try (Connection conn = DriverManager.getConnection(url);
    	                Statement statement = conn.createStatement()){
    	 
    	                ResultSet rs = statement.executeQuery(sql);  //I use the Result set to get the values
    	               int j = 0; //This is to help loop through 2d array
    	                while (rs.next()) {
    	                	for (int i = 0; i < colnames.length; i++) {
    	                		data[j][i] = rs.getString(colnames[i]); //Fills the array the correct way so that it can be put in the JTable
    	                	}
    	                	j++;
    	                }

    	        } catch (SQLException e1) {
    	        	
    	        	//This will display any errors that the database has
    	        	JOptionPane.showMessageDialog(displayPane,
    	        		    ""+e1.getMessage(),
    	        		    "Inane error",
    	        		    JOptionPane.ERROR_MESSAGE);
    	        }

   
    	    	table.revalidate();
    	    	displayPane.remove(table);
    	    	displayPane.setBackground(background);
    	        table = new JTable(data,colnames);
    	        table.setFont(font);
    	        table.setBackground(background);
    	    	makeConstraints(layouttt, table,1,1,0,1,1.0,3);
    	    	table.setRowHeight(30);
    	    	displayPane.add(table);
    	    	displayPane.revalidate();
    	        table.revalidate();
    	        

    	       
    	       
    		}
    	});
    }
    
    
    
    
    
    
    
    
    
    
    
    
	public void LoginsScreen() {
    	//Removes everything that is currenly on the screen
        displayPane.removeAll();
        buttonPane.removeAll();
        
        //Creates a new Button for creating Logins.  This will replace the Login button on the buttonPanel because you are already on the screen.
        JButton SQL = new JButton("SQL");
        SQL.setFont(new Font("Comic Sans MS", Font.PLAIN, 60));
        SQL.setBackground(background);
        
        JButton back = new JButton("back");
        back.setFont(new Font("Comic Sans MS", Font.PLAIN, 60));
        back.setBackground(background);
        
        back.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	//Goes back to the first screen.
                adminScreen();
            }
        }) ;
        
        SQL.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		displayPane.removeAll();
        		initSQL();
        	}
        });
        
        JButton newLogin = new JButton("New Login");
        newLogin.setFont(new Font("Comic Sans MS", Font.PLAIN, 60));
        newLogin.setBackground(background);
        
        
        //Because I removed everything, I had to add them back.  The New order will be Back, New Login, SQL
        buttonPane.add(back);
        buttonPane.add(newLogin);
        buttonPane.add(SQL);
        
        GridBagLayout loginListLayout = new GridBagLayout(); //This is the layout that will be used on the displayPane.  GridBag allows me to changes sizes of boxes
        displayPane.setLayout(loginListLayout); //Sets the Layout 
        
        Login[] logins = FileSystem.doSQLLoginListSearch("SELECT * FROM logins");  //Gets a list of all the logins in the database
        
        JTextField searchBar = new JTextField("Search Logins"); //This initializes the Search bar
        searchBar.setFont(font);
        searchBar.setBackground(background);
        JList<String> list = new JList<String>(Login.getLoginsAsText(logins)); //This creates the JList which will display the Usernames.  
        																	   //The static method Login.getLoginsAsText() returns a String[] of usernames
        
        
        JPanel info = new JPanel(); // initializes a info JPanel so I can group the logins details together.
        
        
        //Sets the background of these two fields to the background color (245,245,205)
        info.setBackground(background);         
        list.setBackground(background);
        //Sets the Font of the list, which is (Comic Sans MS, Font.Plain, 16) 
        list.setFont(font); 
        
        

        /* @param loginListLayout - this is the Layout that the Components Constraints will be added to
         * @param list, searchBar, info - these are the JComponents I want to create constraints for
         * @param width - size
         * @param height
         * @param x - location on the grid
         * @param y 
         * @param xweight
         * @param yweight
         * */
        
        makeConstraints(loginListLayout, list,1,1,0,1,.5,5.0);
        makeConstraints(loginListLayout, searchBar,1,1,0,0,.5,.1);
        makeConstraints(loginListLayout, info, 1,2,1,0,1.0,1.0);
        
        //Adds everything to the displayPane
        displayPane.add(searchBar);
        displayPane.add(list);
        displayPane.add(info);
        //updates the display of the displayPane
        displayPane.revalidate();
        
        
        //this is the ActionListener for the new Login button.
        newLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	//removes everything from the screen
            	info.removeAll();
            	//creates a new Layout for the info area
            	GridBagLayout infoLay = new GridBagLayout();
            	info.setLayout(infoLay);
            	
            	
                JLabel image = new JLabel("Create Login", JLabel.CENTER); //creates a JPanel for the image that is on top

                image.setBackground(background);
                image.setFont(new Font("Comic Sans MS", Font.PLAIN, 60));
                //These are the textBoxs and text that you put your new information in
                JLabel usernameText = new JLabel("Username:"); 

                usernameText.setFont(font);
                JTextField usernameBox = new JTextField("Username");
                usernameBox.setFont(font);
                usernameBox.setBackground(background);
                
                
                JLabel passwordText = new JLabel("Password:");
                JTextField passwordBox = new JTextField("Password");
                passwordBox.setBackground(background);
                passwordText.setFont(font);
                passwordBox.setFont(font);
                //this is the submit button.  
                JButton create = new JButton("Create");
                create.setBackground(background);
                create.setFont(new Font("Comic Sans MS", Font.PLAIN, 60));
                
                


                /* @param GridBagLayout - this is the Layout that the Components Constraints will be added to
                 * @param JComponent - these are the JComponents I want to create constraints for
                 * @param width - size
                 * @param height
                 * @param x - location on the grid
                 * @param y 
                 * @param xweight
                 * @param yweight
                 * */
                
                makeConstraints(infoLay, image,2,1,0,0,1.0,1.0);
                makeConstraints(infoLay, usernameText,1,1,0,1,1.0,1.0);
                makeConstraints(infoLay, usernameBox,1,1,1,1,1.0,1.0);
                makeConstraints(infoLay, passwordText,1,1,0,2,1.0,1.0);
                makeConstraints(infoLay, passwordBox,1,1,1,2,1.0,1.0);
                makeConstraints(infoLay, create,2,1,0,3,1.0,1.0);
                
                
                //adds everything to info
                info.add(create);
                info.add(usernameBox);
                info.add(usernameText);
                info.add(passwordText);
                info.add(passwordBox);
                info.add(image);
                info.revalidate();
                
                //When the create button is clicked, this method will trigger.
                create.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                    	
                        boolean isUserValid = usernameBox.getText().matches("^[a-zA-Z0-9 .,]+$") && !usernameBox.getText().trim().equals("");
                        boolean isPassValid = passwordBox.getText().matches("^[a-zA-Z0-9 .,]+$") && !passwordBox.getText().trim().equals("");
                        if (isUserValid && isPassValid) {
                            FileSystem.executeSQL("INSERT INTO logins (username, password) VALUES (\""+usernameBox.getText()+"\", \""+passwordBox.getText()+"\")");
                            LoginsScreen();
                            JOptionPane.showMessageDialog(displayPane,"Login Created!");  
                        } else {
                        	String combo = "";
                        	if (!isUserValid) combo += "username is not (a-z)(0-9)\n";
                        	if (!isPassValid) combo += "password is not (a-z)(0-9)\n";
                        	JOptionPane.showMessageDialog(displayPane, "Login not created because: \n "+ combo);
                        	
                        }
                        //System.out.println("INSERT INTO logins (username, password) VALUES (\""+usernameBox.getText()+"\", \""+passwordBox.getText()+"\")");

                        //^^ This statement will put a new login into data.db
                        
                       //goes back to main admin screen.
                    }
                     
                });
                
                
            }
        });
        //This will trigger whenever the user hits enter in the
        searchBar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Login[] loginSearched = FileSystem.doSQLLoginListSearch("SELECT * FROM logins WHERE username LIKE \"%"+searchBar.getText()+"%\"");
                //This will get a list of the logins that contain the characters that are searched in the box
                list.setListData(Login.getLoginsAsText(loginSearched));
                //Gets the usernames of the remaining logins
                list.updateUI(); //updates the list to display the new results
            }
        });
        //When an item in the list is selected, this method will trigger.
        list.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                info.removeAll(); // I remove everything from the info panel so it is empty.
                
                
                GridBagLayout infoLayout = new GridBagLayout();// I make a new layout that will control how the info Panel will look
                info.setLayout(infoLayout);
//              System.out.println("SELECT * FROM logins WHERE username is \""+list.getSelectedValue()+"\"");
                
                
                Login selectedLogin= FileSystem.doSQLLoginSearch("SELECT * FROM logins WHERE username is \""+list.getSelectedValue()+"\"");
                //^^ this code gets the Login from the Value you have Selected

                
                JLabel image = new JLabel("Login View", JLabel.CENTER); //This is the image at the top
                image.setBackground(background);
                image.setFont(new Font("Comic Sans MS", Font.PLAIN, 60));
                
                //These are the username and password of the selected login
                JLabel usernameText = new JLabel("Username: ");
                JLabel username = new JLabel(selectedLogin.getUser());
                
                usernameText.setFont(font);
                username.setFont(font);
                
                
                JLabel passwordText = new JLabel("Password: ");
                JLabel password = new JLabel(selectedLogin.getPass());
                passwordText.setFont(font);
                password.setFont(font);
                
                //the Buttons edit and remove are at the bottum of the info pane
                JButton edit = new JButton("Edit");
                JButton remove = new JButton("Remove");
                
                edit.setBackground(background);
                edit.setFont(font);
                remove.setBackground(background);
                remove.setFont(new Font("Comic Sans MS", Font.PLAIN, 60));
                
                
                /* @param loginListLayout - this is the Layout that the Components Constraints will be added to
                 * @param list, searchBar, info - these are the JComponents I want to create constraints for
                 * @param width - size
                 * @param height
                 * @param x - location on the grid
                 * @param y 
                 * @param xweight
                 * @param yweight
                 * */
                makeConstraints(infoLayout, image, 2,1,0,0,1.0,1.0);
                
                makeConstraints(infoLayout, username, 1,1,1,1,1.0,1.0);
                makeConstraints(infoLayout, password, 1,1,1,2,1.0,1.0);
                
                makeConstraints(infoLayout, usernameText, 1,1,0,1,1.0,1.0);
                makeConstraints(infoLayout, passwordText, 1,1,0,2,1.0,1.0);
                
                makeConstraints(infoLayout, edit, 1,1,0,3,1.0,1.0);
                makeConstraints(infoLayout, remove, 1,1,1,3,1.0,1.0);

                
                //this will fire whenever remove button is clicked
                remove.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                    	String name = list.getSelectedValue();
                    	//This SQL statement will remove the Login from data.db
                        FileSystem.executeSQL("DELETE FROM logins WHERE username=\""+list.getSelectedValue()+"\"");
                        //I return to adminScreen() immediatly so there is no possible NullPointerExcetion.
                        JOptionPane.showMessageDialog(displayPane, name + " has been removed!");
                        LoginsScreen();
                    }
                });
                JTextField username1 = new JTextField();
                JTextField password1= new JTextField();
                username1.setText(selectedLogin.getUser());
                password1.setText(selectedLogin.getPass());
                //When the edit button is clicked, this will fire.
                edit.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                    	// I initialized the two TextField here so that their scope is higher.

                        
                        if (edit.getText().equals("Edit")) { //This is a basic toggle so that the button can be used to edit and submit edit.
                            
                            edit.setText("Submit Edit"); // changes text
                            
                            info.removeAll(); //clears info so its empty.
                            

                            
                            
                            username1.setText(selectedLogin.getUser());
                            password1.setText(selectedLogin.getPass());
                            
                            username1.setBackground(background);
                            username1.setFont(font);
                            password1.setBackground(background);
                            password1.setFont(font);
                            //sets the already initialized JTextField text to the username and password.  These are where you would put new values.

                            
                            /* @param loginListLayout - this is the Layout that the Components Constraints will be added to
                             * @param list, searchBar, info - these are the JComponents I want to create constraints for
                             * @param width - size
                             * @param height
                             * @param x - location on the grid
                             * @param y 
                             * @param xweight
                             * @param yweight
                             * */
                            makeConstraints(infoLayout, image, 2,1,0,0,1.0,1.0);
                            
                            
                            makeConstraints(infoLayout, usernameText, 1,1,0,1,1.0,1.0);
                            makeConstraints(infoLayout, username1, 1,1,1,1,1.0,1.0);
                            
                            makeConstraints(infoLayout, passwordText, 1,1,0,2,1.0,1.0);
                            makeConstraints(infoLayout, password1, 1,1,1,2,1.0,1.0);
                            
                            makeConstraints(infoLayout, edit, 1,1,0,3,1.0,1.0);
                            makeConstraints(infoLayout, remove, 1,1,1,3,1.0,1.0);
                            
                            info.add(image);
                            info.add(username1);
                            info.add(password1);
                            info.add(usernameText);
                            info.add(passwordText);
                            info.add(edit);
                            info.add(remove);
                            
                            info.revalidate();
                        
                    } else {
                            
                    	
                    	
                        boolean isUserValid = username1.getText().matches("^[a-zA-Z0-9 .,]+$") && !username1.getText().trim().equals("");
                        boolean isPassValid = password1.getText().matches("^[a-zA-Z0-9 .,]+$") && !password1.getText().trim().equals("");
                        if (isUserValid && isPassValid) {
                        	FileSystem.executeSQL("UPDATE logins SET username = \""+username1.getText()+"\", password= \""+password1.getText()+"\" WHERE username is \""+selectedLogin.getUser()+"\"");
                            JOptionPane.showMessageDialog(displayPane,"Login Updated!");  
                        } else {
                        	String combo = "";
                        	if (!isUserValid) combo += "username is not (a-z)(0-9)\n";
                        	if (!isPassValid) combo += "password is not (a-z)(0-9)\n";
                        	JOptionPane.showMessageDialog(displayPane, "Login not updated because: \n "+ combo);
                        	
                        }
//                    		System.out.println(username1.getText() + " "+ password1.getText());
//                            System.out.println("UPDATE logins SET username = \""+selectedLogin.getUser()+"\", password= \""+selectedLogin.getPass()+"\" WHERE username is \""+selectedLogin.getUser()+"\"");
//                            
                            //This will trigger when the button is ready to submit. The SQL replaces the old data with the new data in data.db
                            LoginsScreen(); // returns to admin screen to avoid NullPointerExeption.
        
                    	}
                    }
                });
                
                
                //No matter what the button is, these will be added as a fallback.
                info.add(image);
                info.add(username);
                info.add(password);
                info.add(usernameText);
                info.add(passwordText);
                info.add(edit);
                info.add(remove);
                info.revalidate();

                
                
                
                
                
            }
        });
	}
    public void personScreen() {
    	clear();
		//This screen will trigger when the users button is pressed
    	GridBagLayout bookLayout = new GridBagLayout();
    	getContentPane().setLayout(bookLayout);
	
    	JPanel peopleAndSearch = new JPanel();
    	JPanel peopleButtons = new JPanel();
    	JPanel displayInfo = new JPanel();
    	displayInfo.setBackground(background);
    	peopleAndSearch.setBackground(background);
    	peopleButtons.setBackground(background);
    	
    	peopleAndSearch.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 5, new Color(64,64,64)));
    	displayInfo.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 5, new Color(64,64,64)));


    	makeConstraints(bookLayout, peopleButtons,   1, 3, 4, 0, .5, 1.0);
    	makeConstraints(bookLayout, displayInfo,   2, 3, 2, 0, 3.0, 1.0);
    	makeConstraints(bookLayout, peopleAndSearch,2, 3, 0, 0, 1.5, 1.0);
		//This makes the back button redirect to the main screen.
    	JButton backButton = new JButton();	
    	ActionListener al = new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			if (e.getSource().equals(backButton)) {
    				initMenu();     
    			}
    		}
   		};
		
   		//setting up the pictures.
        backButton.setBackground(background);
        Icon backIcon = new ImageIcon("res/img/backButton.png");
        backButton.setIcon(backIcon);
        backButton.setBorder(null);

        JButton addPersonButton = new JButton();
        Icon helpIcon = new ImageIcon("res/img/helpIcon.png");
        JButton helppbutton = new JButton(helpIcon);
        helppbutton.setBorder(null);
        helppbutton.setBackground(background);
        helppbutton.addActionListener(new ActionListener( ) {
        	public void actionPerformed(ActionEvent e) {
        		new HelpMenu();
        	}
        });
                
        //sets up the buttons on the right side.
        backButton.addActionListener(al);
        GridBagLayout peopleButtonsLayout = new GridBagLayout();
        peopleButtons.setLayout(peopleButtonsLayout);
        makeConstraints(peopleButtonsLayout, backButton,1,1,0,0,1.0,0.15);
        makeConstraints(peopleButtonsLayout, addPersonButton,1,1,0,1,1.0,0.5);
        makeConstraints(peopleButtonsLayout, helppbutton,1,1,0,2,1.0,0.5);
                
                
        peopleButtons.add(backButton);
		peopleButtons.add(addPersonButton);
        peopleButtons.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, Color.BLACK));
                
                
        addPersonButton.setBackground(background);
        Icon personIcon = new ImageIcon("res/img/newPersonIcon.png");
        addPersonButton.setIcon(personIcon);
        addPersonButton.setBorder(null);
               
        peopleButtons.add(helppbutton);
        //This fires when you add a person        
        addPersonButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                        
                displayInfo.removeAll();
                   
                
                //creating the proper fields
                GridBagLayout dil = new GridBagLayout();
                JTextField firstname = new JTextField("firstname");
                JTextField lastname = new JTextField("lastname");
                firstname.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, new Color(64,64,64)));
                lastname.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, new Color(64,64,64)));
                firstname.setBackground(background);
                firstname.setFont(font);
                lastname.setBackground(background);
                lastname.setFont(font);
                            
                JLabel firstnameText = new JLabel("First Name: ");
                JLabel lastnameText = new JLabel("Last Name: ");
                JLabel studentOrTeacher = new JLabel("Student or Teacher?");
                firstnameText.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, new Color(64,64,64)));
                lastnameText.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, new Color(64,64,64)));
                firstnameText.setFont(font);
                lastnameText.setFont(font);
                studentOrTeacher.setFont(font);
                studentOrTeacher.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, new Color(64,64,64)));
                
                JRadioButton teachButton = new JRadioButton("Teacher");
                teachButton.setBackground(background);
                teachButton.setFont(font);
                
                JRadioButton studentButton = new JRadioButton("Student");
                studentButton.setBackground(background);
                studentButton.setFont(font);
                ButtonGroup bg = new ButtonGroup();
                bg.add(teachButton);
                bg.add(studentButton);
                JPanel radioPanel = new JPanel();
                radioPanel.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, new Color(64,64,64)));
                radioPanel.setBackground(background);
                radioPanel.add(teachButton);
                radioPanel.add(studentButton);
                            
                JButton submit = new JButton("Submit");
                submit.setFont(new Font("Comic Sans MS", Font.PLAIN, 60));
                submit.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, new Color(64,64,64)));
                submit.setBackground(background);
                JLabel addABookLogo = new JLabel("New Person", JLabel.CENTER);
                addABookLogo.setFont(new Font("Comic Sans MS", Font.PLAIN, 60));
                addABookLogo.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, new Color(64,64,64)));
                            
                makeConstraints(dil,addABookLogo,2,1, 0,0,1.0,1.0);
                            
                makeConstraints(dil,firstname,1,1, 1,1,1.0,1.0);
                makeConstraints(dil,lastname,1,1, 1,2,1.0,1.0);
                            
                makeConstraints(dil,firstnameText,1,1, 0,1,1.0,1.0);
                makeConstraints(dil,lastnameText,1,1, 0,2,1.0,1.0);
                            
                makeConstraints(dil,studentOrTeacher,1,1, 0,3,1.0,1.0);
                makeConstraints(dil,radioPanel,1,1, 1,3,1.0,1.0);
                            
                makeConstraints(dil,submit,2,1, 0,5,1.0,1.0);
                            
                displayInfo.setLayout(dil);
                displayInfo.add(addABookLogo);
                displayInfo.add(firstname);
                displayInfo.add(lastname);
                displayInfo.add(studentOrTeacher);
                displayInfo.add(radioPanel);
                displayInfo.add(firstnameText);
                displayInfo.add(lastnameText);
                displayInfo.add(submit);
                displayInfo.revalidate();
                          //This will fire when you try to submit something
                submit.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {

                                    //THis checks to make sure the names are valid names and not like numbers or special characters.
                        boolean isTitleValid = firstname.getText().matches("^[a-zA-Z .,]+$") && !firstname.getText().trim().equals("");
                        boolean isAuthorValid = lastname.getText().matches("^[a-zA-Z .,]+$") && !lastname.getText().trim().equals("");
                                    
                                    
                        if ((isTitleValid && isAuthorValid) && (teachButton.isSelected()|| studentButton.isSelected())) {
                            Person a =  null;
                            if (teachButton.isSelected()) {
                                a = new Teacher(firstname.getText(), lastname.getText());     
                            } else if (studentButton.isSelected()) {
                                a = new Student(firstname.getText(), lastname.getText());
                                            
                            }
                            FileSystem.insertPerson(a);
                            personScreen();

                        } else {// an error will be displayed if there is a problem
                            String str = "";
                            if (!isTitleValid) str += "first name can only be (a-z),\n";
                            if (!isAuthorValid) str += "last name can only be (a-z),\n";
                            JOptionPane.showMessageDialog(displayInfo, "Person " + " could not be created because: \n"+str);                                        
                                        
                        }                                  
                    }
                });
            }                
        });
                

                
                  
                
        //Creates the list to display the people        
        JList<String> list = new JList<String>(Person.getNameList(FileSystem.doSQLPeopleSearch("SELECT * FROM people")));
        list.setBackground(background);
        list.setFont(font);

        list.addListSelectionListener(new ListSelectionListener() {
        	public void valueChanged(ListSelectionEvent e) { 
        		//This will trigger whenever you select a Person
                Person[] selectedPersons = FileSystem.doSQLPeopleSearch("SELECT * FROM people WHERE firstname is \""+list.getSelectedValue().split(" ")[0]+"\""); //finds the selected person
                if (selectedPersons.length == 0) return; // if they cant be found this diverts a NullPointerException
                Person selectedPerson = selectedPersons[0];
                displayInfo.removeAll();

                JLabel infoText = new JLabel();
                infoText.setBorder(new EmptyBorder(0,0,0,0));
                //Initializes all of the fields
                infoText.setText("Selected: "+selectedPerson.getFirstName() + " "+ selectedPerson.getLastName());
                infoText.setFont(font);
                JButton edit = new JButton("Edit");
                edit.setBackground(background);
                edit.setFont(font);
                JButton remove = new JButton("Remove");
                remove.setBackground(background);
                remove.setFont(font);
                JLabel bookDetailLogo = new JLabel("User Details", JLabel.CENTER);
                bookDetailLogo.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, new Color(64,64,64)));
                bookDetailLogo.setFont(new Font("Comic Sans MS", Font.PLAIN, 60));
                
                edit.setBorder(BorderFactory.createMatteBorder(5, 5, 0, 5, new Color(64,64,64)));
                remove.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, new Color(64,64,64)));
                
                GridBagLayout displayLayout = new GridBagLayout();
                displayInfo.setLayout(displayLayout);
                
                
                JPanel info = new JPanel();
                info.setBackground(background);
                GridBagLayout infoLayout = new GridBagLayout();

                info.setLayout(infoLayout);

                            
                JButton assignBook = new JButton("Assign Book");
                JButton returnBook = new JButton("Return Book");
                assignBook.setBackground(background);
                returnBook.setBackground(background);
                assignBook.setFont(font);
                returnBook.setFont(font);
                
                assignBook.setBorder(BorderFactory.createMatteBorder(5, 5, 0, 5, new Color(64,64,64)));
                returnBook.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, new Color(64,64,64)));
                makeConstraints(displayLayout,bookDetailLogo,2,1,0,0,1.0,1.0);
                makeConstraints(displayLayout,info,2,1,0,1,1.0,1.5);

                            
                makeConstraints(displayLayout,assignBook,1,1,1,2,1.0,0.75);
                makeConstraints(displayLayout,returnBook,1,1,1,3,1.0,0.75);
                makeConstraints(displayLayout,edit,1,1,0,2,1.0,0.75);
                makeConstraints(displayLayout,remove,1,1,0,3,1.0,0.75);
                
                
                bookDetailLogo.setBackground(background);
                
                displayInfo.add(assignBook);
                displayInfo.add(returnBook);
                assignBook.setEnabled(false);
                returnBook.setEnabled(false);
                

                //this gets the books which the person has checked out.
                String result = FileSystem.doSQLStringSearch("SELECT checkedOutBooks FROM people WHERE firstname is \""+list.getSelectedValue().split(" ")[0]+"\" AND lastname is \""+list.getSelectedValue().split(" ")[1]+"\"");
                
                String[] titles = result.split(",");
                JList<String> checkedOutBooks;
            	Book[] books = new Book[titles.length];
            	
                if (titles[0].equals("") && titles.length ==1) {
                	checkedOutBooks = new JList<String>();
                } else {
                	 books= new Book[titles.length];
                	for (int i = 0; i < titles.length; i++) {
//                		System.out.println("SELECT * from books WHERE title = \""+titles[i]+"\"");
//                    
                		Book book = FileSystem.doSQLBookSearch("SELECT * from books WHERE title = \""+titles[i].trim()+"\"")[0];
                		books[i] = book;
                		
                	}
                	checkedOutBooks = new JList<String>(Book.getTitlesList(books));
                }
                //^ this code gets the titles ready to be displayed and also prevents NullPointers
                
            
        
  
                //This will get a list of all of the books they could check out
                JList<String> bookList = new JList<String>(Book.getTitlesList(FileSystem.doSQLBookSearch("SELECT * FROM books")));

                ListSelectionListener userBookListener = new ListSelectionListener() {
                	public void valueChanged(ListSelectionEvent e) {
                		if (e.getSource().equals(checkedOutBooks)) {
                			bookList.clearSelection();
                            returnBook.setEnabled(true);
                            assignBook.setEnabled(false);
                		}
                		if (e.getSource().equals(bookList)) {
                			checkedOutBooks.clearSelection();
                            assignBook.setEnabled(true);
                            returnBook.setEnabled(false);
                		}
                	}
                };
                
                checkedOutBooks.addListSelectionListener(userBookListener);
                bookList.addListSelectionListener(userBookListener);
                
                checkedOutBooks.setFont(font);
                bookList.setFont(font);
                
                checkedOutBooks.setBackground(background);
                bookList.setBackground(background);
                
                JTextField searchBookBox = new JTextField("Search for Book");
                JTextField searchCheckedBox = new JTextField("Search for Book");
                searchCheckedBox.setFont(font);
                searchBookBox.setFont(font);
                searchCheckedBox.setBackground(background);
                searchBookBox.setBackground(background);
                //I grouped a few action Listeners together so it was easier to find them.
                ActionListener infoSearchBoxListener = new ActionListener() {
                	public void actionPerformed(ActionEvent e) {
                		if (e.getSource().equals(searchBookBox)) { // This searches ALL books
                			if (searchBookBox.getText().equals("")) {
                            	Book[] books = FileSystem.doSQLBookSearch("SELECT * from books");
                            	bookList.setListData(Book.getTitlesList(books));
                            } else {
                            	Book[] books = FileSystem.doSQLBookSearch("SELECT * from books WHERE title is \""+searchBookBox.getText()+"\" OR author is \""+searchBookBox.getText()+"\"");
                            	bookList.setListData(Book.getTitlesList(books));
                            }
                            bookList.updateUI();
                			
                		}
                		if (e.getSource().equals(searchCheckedBox)) { // This searchs only checked out books
                            ArrayList<Book> books = new ArrayList<Book>();
                			if (searchCheckedBox.getText().equals("")) {
                            	checkedOutBooks.setListData(Book.getTitlesList(Book.convertToArray(books)));
                            	for (int i = 0; i < titles.length; i++) {
//                                    System.out.println("SELECT * from books WHERE title = \""+titles[i]+"\"");
                                    Book book = FileSystem.doSQLBookSearch("SELECT * from books WHERE title = \""+titles[i].trim()+"\"")[0];
                                    if (book.title.equals(searchCheckedBox.getText())) {
                                        books.add(book);
                                    }
                                }
                            } else {
                                String result = FileSystem.doSQLStringSearch("SELECT checkedOutBooks FROM people WHERE firstname is \""+list.getSelectedValue().split(" ")[0]+"\" AND lastname is \""+list.getSelectedValue().split(" ")[1]+"\"");
                                String[] titles = result.split(",");

                                for (int i = 0; i < titles.length; i++) {
//                                    System.out.println("SELECT * from books WHERE title = \""+titles[i]+"\"");
                                    Book book = FileSystem.doSQLBookSearch("SELECT * from books WHERE title = \""+titles[i].trim()+"\"")[0];
                                    if (book.title.equals(searchCheckedBox.getText())) {
                                        books.add(book);
                                    }
                                }
                                checkedOutBooks.setListData(Book.getTitlesList(Book.convertToArray(books)));
                            }
                            bookList.updateUI();
                			
                		}
                 	}
                };
                
                

                searchCheckedBox.addActionListener(infoSearchBoxListener);
                searchBookBox.addActionListener(infoSearchBoxListener);
                
                
                
                
                
                info.add(infoText);
                info.add(searchCheckedBox);
                info.add(searchBookBox);
                info.add(bookList);
                info.add(checkedOutBooks);
                
                makeConstraints(infoLayout,infoText,2,1,0,0,1.0,1.0);
                makeConstraints(infoLayout,searchCheckedBox,1,1,0,1,1.0,.15);
                makeConstraints(infoLayout,checkedOutBooks,1,1,0,2,1.0,1.0);
                makeConstraints(infoLayout,searchBookBox,1,1,1,1,1.0,.15);
                makeConstraints(infoLayout,bookList,1,1,1,2,1.0,1.0);
                
                
                info.revalidate();
                   

                displayInfo.add(bookDetailLogo);
                displayInfo.add(info); 
                displayInfo.add(edit);
                displayInfo.add(remove);
                            
                displayInfo.revalidate();
                            
                JTextField firstnameBox = new JTextField();
                JTextField lastnameBox = new JTextField();
                JRadioButton teacherButton = new JRadioButton("Teacher");
                JRadioButton studentButton = new JRadioButton("Student");
                JPanel buttonPanel = new JPanel();
                ButtonGroup bg = new ButtonGroup();
                bg.add(teacherButton);
                bg.add(studentButton);
                buttonPanel.add(teacherButton);
                buttonPanel.add(studentButton);
                ActionListener editRemoveAssignReturn = new ActionListener() { // I also group these 4 button listeners together.
                	
                	public void actionPerformed(ActionEvent e) {
                		if (e.getSource().equals(edit)) { // if theres edit it will display the proper fields
                			if (edit.getText().equals("Edit")) {
                				info.removeAll();
                				info.setLayout(new GridLayout(3,1));
                				firstnameBox.setText(selectedPerson.getFirstName());
                				lastnameBox.setText(selectedPerson.getLastName());
                				
                				firstnameBox.setFont(font);
                				firstnameBox.setBackground(background);
                				lastnameBox.setFont(font);
                				lastnameBox.setBackground(background);
                				buttonPanel.setBackground(background);
                				info.add(firstnameBox);
                				info.add(lastnameBox);
                				info.add(buttonPanel);
                				
                				teacherButton.setBackground(background);
                				studentButton.setBackground(background);
                				teacherButton.setFont(font);
                				studentButton.setFont(font);
                				
                				
                				edit.setText("Submit Edit");
                				info.revalidate();             
                			} else {
                				String firstname = selectedPerson.getFirstName();
                				String lastname = selectedPerson.getLastName();
                				boolean teach = teacherButton.isSelected();
                				
                                //checks to make sure the new fields are valid
                    boolean isTitleValid = firstnameBox.getText().matches("^[a-zA-Z .,]+$") && !firstname.trim().equals("");
                    boolean isAuthorValid = lastnameBox.getText().matches("^[a-zA-Z .,]+$") && !lastname.trim().equals("");
                                
                                
                    if ((isTitleValid && isAuthorValid)) {
                        @SuppressWarnings("unused")
						Person a =  null;
                        if (teach) {
                            a = new Teacher(firstname, lastname);  
                            
                        } else if (!teach) {
                            a = new Student(firstname, lastname);
                                        
                        }
                        
        				FileSystem.executeSQL("UPDATE people SET firstname = \""+ firstnameBox.getText()+ "\",lastname = \""+ lastnameBox.getText() + "\",isTeacher = \""+teach+ "\" WHERE firstname = \""+firstname+"\";");
        				personScreen();


                    } else {
                        String str = "";
                        if (!isTitleValid) str += "first name can only be (a-z),\n";
                        if (!isAuthorValid) str += "last name can only be (a-z),\n";
                        JOptionPane.showMessageDialog(displayInfo, "Person " + " could not be edited because: \n"+str);                                        
                                    
                    }  				


                            
                			}
                        }
                		if (e.getSource().equals(assignBook)) { // Adds the book to the person
//                			System.out.println("SELECT checkedOutBooks FROM people WHERE firstname is \""+list.getSelectedValue().split(" ")[0]+"\" and lastname is \""+list.getSelectedValue().split(" ")[1]+"\"");
                            String alreadyCheckedOut= FileSystem.doSQLStringSearch("SELECT checkedOutBooks FROM people WHERE firstname is \""+list.getSelectedValue().split(" ")[0]+"\" and lastname is \""+list.getSelectedValue().split(" ")[1]+"\"");
                            alreadyCheckedOut +=   bookList.getSelectedValue()+",";
//                            System.out.println("UPDATE people SET checkedOutBooks = \""+alreadyCheckedOut+"\"\" WHERE firstname is \""+list.getSelectedValue().split(" ")[0]+"\" and lastname is \""+list.getSelectedValue().split(" ")[1]+"\"");
                            FileSystem.executeSQL("UPDATE people SET checkedOutBooks = \""+alreadyCheckedOut+"\" WHERE firstname is \""+list.getSelectedValue().split(" ")[0]+"\" and lastname is \""+list.getSelectedValue().split(" ")[1]+"\"");
                            JOptionPane.showMessageDialog(displayInfo, "Book: " +bookList.getSelectedValue() + " has been checked out to " + list.getSelectedValue()); 
                            info.revalidate();

                            personScreen();
                		}
                		if (e.getSource().equals(returnBook)) { // removes the book from the person
                           // System.out.println("SELECT checkedOutBooks FROM people WHERE firstname is \""+list.getSelectedValue().split(" ")[0]+"\" and lastname is \""+list.getSelectedValue().split(" ")[1]+"\"");
                            String alreadyCheckedOut= FileSystem.doSQLStringSearch("SELECT checkedOutBooks FROM people WHERE firstname is \""+list.getSelectedValue().split(" ")[0]+"\" and lastname is \""+list.getSelectedValue().split(" ")[1]+"\"");
                            alreadyCheckedOut = alreadyCheckedOut.replace(checkedOutBooks.getSelectedValue()+",","");
                           // System.out.println("UPDATE people SET checkedOutBooks = \""+alreadyCheckedOut+"\"\" WHERE firstname is \""+list.getSelectedValue().split(" ")[0]+"\" and lastname is \""+list.getSelectedValue().split(" ")[1]+"\"");
                            FileSystem.executeSQL("UPDATE people SET checkedOutBooks = \""+alreadyCheckedOut+"\" WHERE firstname is \""+list.getSelectedValue().split(" ")[0]+"\" and lastname is \""+list.getSelectedValue().split(" ")[1]+"\"");
                            JOptionPane.showMessageDialog(displayInfo, "Book: " +checkedOutBooks.getSelectedValue() + " has been returned from " + list.getSelectedValue()); 
                            info.revalidate();
                            
                            personScreen();
                			
                		}
                		if (e.getSource().equals(remove)) { //removes the person
                			String name = selectedPerson.getFirstName() + " "+ selectedPerson.getLastName();
                			FileSystem.executeSQL("DELETE FROM people WHERE firstname=\""+selectedPerson.getFirstName()+"\" AND lastname =\""+selectedPerson.getLastName()+"\"");
                			JOptionPane.showMessageDialog(displayInfo, "Removed: "+ name); 
                			personScreen();
                			
                		}
                	}
                };
                
                edit.addActionListener(editRemoveAssignReturn);
                returnBook.addActionListener(editRemoveAssignReturn);
                remove.addActionListener(editRemoveAssignReturn);
                assignBook.addActionListener(editRemoveAssignReturn);
			
				
            }
			
        });
        JRadioButton teachers = new JRadioButton("Students");
        JRadioButton students = new JRadioButton("Teachers");
        JRadioButton all = new JRadioButton("All");
        teachers.setBackground(background);
        teachers.setFont(font);
        students.setBackground(background);
        students.setFont(font);
        all.setBackground(background);
        all.setFont(font);
        
        JTextField searchBox = new JTextField("Search for People");
        searchBox.setFont(font);
        searchBox.setBackground(background);
	searchBox.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent arg0) { //This will narrow down searchs.  If you select a Radio Button, it will narrow down searches
	        if (searchBox.getText().equals("")) {
	        	String addon = "";
			    if (teachers.isSelected()) addon = " WHERE isTeacher is \"true\"";
			    if (students.isSelected()) addon = " WHERE isTeacher is \"false\"";
	            Person[] people = FileSystem.doSQLPeopleSearch("SELECT firstname, lastname, isTeacher FROM people"+ addon);
		    list.setListData(Person.getNameList(people));
		} else {
		    String addon = "";
		    if (teachers.isSelected()) addon = " isTeacher is \"true\" and ";
		    if (students.isSelected()) addon = " isTeacher is \"false\" and ";
		    //System.out.println("SELECT firstname, lastname, isTeacher FROM people WHERE"+ addon + " (firstname LIKE \"%"+searchBox.getText()+"%\" OR lastname LIKE \"%"+searchBox.getText()+"%\")");
					
		    Person[] people = FileSystem.doSQLPeopleSearch("SELECT firstname, lastname, isTeacher FROM people WHERE"+ addon + " (firstname LIKE \"%"+searchBox.getText()+"%\" OR lastname LIKE \"%"+searchBox.getText()+"\")");
		    list.setListData(Person.getNameList(people));
		}
				
				
	    }
			
	});
		
		

		
	ButtonGroup group = new ButtonGroup();
	//Button group so only one button can be selected at a time
	group.add(teachers);
	group.add(students);
	group.add(all);
	JPanel radioPanel = new JPanel();
        radioPanel.setBackground(background);
	radioPanel.add(teachers);
	radioPanel.add(students);
	radioPanel.add(all);
	radioPanel.setVisible(true);

		
	getContentPane().add(displayInfo);
	getContentPane().add(peopleAndSearch);
	getContentPane().add(peopleButtons);
	GridBagLayout peopleAndSearchLayout = new GridBagLayout();
	peopleAndSearch.setLayout(peopleAndSearchLayout);

	makeConstraints(peopleAndSearchLayout, radioPanel, 1,1,1,0,0.25,0.1);
	makeConstraints(peopleAndSearchLayout, searchBox, 1,1,0,0,0.75,0.1);
	makeConstraints(peopleAndSearchLayout, list, 2,2,0,1,1.0,10);
	peopleAndSearch.add(radioPanel);
	peopleAndSearch.add(searchBox);
	peopleAndSearch.add(list);

	peopleAndSearch.setVisible(true);

	peopleAndSearch.revalidate();
	peopleAndSearch.repaint();

		
		
	}
	public void bookScreen() {
	    clear();
		//Mostly just initializing Book screen
	    
	    GridBagLayout bookLayout = new GridBagLayout();
	    getContentPane().setLayout(bookLayout);
		
	    JPanel booksAndSearch = new JPanel();
	    JPanel bookButtons = new JPanel();
	    JPanel displayInfo = new JPanel();
                
	    displayInfo.setBackground(background);
	    booksAndSearch.setBackground(background);
	    booksAndSearch.setBorder(BorderFactory.createMatteBorder(0,0,0,5, new Color(64,64,64)));
	    bookButtons.setBackground(background);

	    makeConstraints(bookLayout, bookButtons,   1, 3, 4, 0, 0.5, 1.0);
	    makeConstraints(bookLayout, displayInfo,   2, 3, 2, 0, 3.0, 1.0);
	    makeConstraints(bookLayout, booksAndSearch,2, 3, 0, 0, 3.0, 1.0);
		
	    JButton backButton = new JButton();
            Icon backIcon = new ImageIcon("res/img/backButton.png");                
            backButton.setIcon(backIcon);
            backButton.setMargin(null);
            backButton.setBorder(null);
            backButton.setBackground(background);




	    JButton addBookButton = new JButton();
            Icon addIcon = new ImageIcon("res/img/newBookicon.png");
            addBookButton.setIcon(addIcon);
            addBookButton.setBackground(background);
            addBookButton.setMargin(null);
            addBookButton.setBorder(null);
	    JButton HelpMenu = new JButton();
                
            Icon HelpMenuIcon = new ImageIcon("res/img/helpIcon.png");
            HelpMenu.setIcon(HelpMenuIcon);
            HelpMenu.setBackground(background);
            HelpMenu.setMargin(null);
            HelpMenu.setBorder(null);
                //This action listener handles the buttons on the right
            ActionListener al = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (e.getSource().equals(backButton)) {
                        initMenu();
                            
                    }
                    if (e.getSource().equals(addBookButton)) { // this will prepare for the fields
                        displayInfo.removeAll();
                            
                        GridBagLayout dil = new GridBagLayout();
                        JTextField title = new JTextField("title");
                        JTextField author = new JTextField("Author");
                        JTextField topics = new JTextField("Topics");
                        JTextArea desc = new JTextArea("Description",5, 5);
                        title.setBorder(BorderFactory.createMatteBorder(0,5,0,0, new Color(64,64,64)));
                        author.setBorder(BorderFactory.createMatteBorder(5,5,0,0, new Color(64,64,64)));
                        topics.setBorder(BorderFactory.createMatteBorder(5,5,0,0, new Color(64,64,64)));
                        desc.setBorder(BorderFactory.createMatteBorder(5,5,0,0, new Color(64,64,64)));
                        desc.setWrapStyleWord(true);
                        desc.setLineWrap(true);
                        JScrollPane jsp = new JScrollPane(desc);
                        jsp.setBorder(null);
                        
                        
                        
                        title.setFont(font);
                        author.setFont(font);
                        topics.setFont(font);
                        desc.setFont(font);
                        title.setBackground(background);
                        author.setBackground(background);
                        topics.setBackground(background);
                        desc.setBackground(background);

                        
                        //desc.setWrapStyleWord(true);
                        JLabel titleText = new JLabel("Title: ");
                        JLabel authorText = new JLabel("Author: ");
                        JLabel topicsText = new JLabel("Topic: ");
                        JLabel descText = new JLabel("Description: ");
                        titleText.setBorder(BorderFactory.createMatteBorder(0,0,5,0, new Color(64,64,64)));
                        authorText.setBorder(BorderFactory.createMatteBorder(0,0,5,0, new Color(64,64,64)));
                        topicsText.setBorder(BorderFactory.createMatteBorder(0,0,5,0, new Color(64,64,64)));
                        descText.setBorder(BorderFactory.createMatteBorder(0,0,0,0, new Color(64,64,64)));
                        titleText.setBackground(background);
                        authorText.setBackground(background);
                        topicsText.setBackground(background);
                        descText.setBackground(background);
                        titleText.setFont(font);
                        authorText.setFont(font);
                        topicsText.setFont(font);
                        descText.setFont(font);
                        
                         
                        JButton submit = new JButton("Sumbit");
                        submit.setBackground(background);
                        submit.setFont(new Font("Comic Sans MS", Font.PLAIN, 60));
                        submit.setBorder(BorderFactory.createMatteBorder(5,0,0,0, new Color(64,64,64)));
                        JLabel createBook = new JLabel("Create Book");
                        createBook.setFont(new Font("Comic Sans MS", Font.PLAIN, 60));
                        createBook.setBackground(background);
                        createBook.setVerticalAlignment(JLabel.CENTER);
                        createBook.setHorizontalAlignment(JLabel.CENTER);
                        createBook.setBorder(BorderFactory.createMatteBorder(0,0,5,0, new Color(64,64,64)));
                            
                        makeConstraints(dil,createBook,2,1, 0,0,1.0,1.0);  //MAKE LOGO
                        makeConstraints(dil,title,1,1, 1,1,1.0,1.0);
                        makeConstraints(dil,author,1,1, 1,2,1.0,1.0);
                        makeConstraints(dil,topics,1,1, 1,3,1.0,1.0);
                        makeConstraints(dil,jsp,1,1, 1,4,1.0,1.0);
                        makeConstraints(dil,titleText,1,1, 0,1,1.0,1.0);
                        makeConstraints(dil,authorText,1,1, 0,2,1.0,1.0);
                        makeConstraints(dil,topicsText,1,1, 0,3,1.0,1.0);
                        makeConstraints(dil,submit,2,1, 0,5,1.0,1.0);
                        makeConstraints(dil,descText,1,1, 0,4,1.0,1.0);
                           
                        displayInfo.setLayout(dil);
                        displayInfo.add(createBook);
                        displayInfo.add(title);
                        displayInfo.add(author);
                        displayInfo.add(topics);
                        displayInfo.add(jsp);
                        displayInfo.add(titleText);
                        displayInfo.add(authorText);
                        displayInfo.add(topicsText);
                        displayInfo.add(descText);
                        displayInfo.add(submit);
                        displayInfo.revalidate();
                         
                        submit.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                            	//this checks to make sure the titles and authors and such are valid and no special characters
                                    
                                boolean isTitleValid = title.getText().matches("^[a-zA-Z0-9 .,]+$") && !title.getText().trim().equals("");
                                boolean isAuthorValid = author.getText().matches("^[a-zA-Z0-9 .,]+$") && !author.getText().trim().equals("");
                                boolean isTopicValid = topics.getText().matches("^[a-zA-Z0-9 .,]+$") && !topics.getText().trim().equals("");
                                boolean isDescValid = desc.getText().matches("^[a-zA-Z0-9 .,]+$") && !desc.getText().trim().equals("");
                                Book a = new Book(title.getText(), author.getText(), topics.getText().split(","), desc.getText());                                   
                                if (isTitleValid && isAuthorValid &&isTopicValid && isDescValid) {

                                    FileSystem.insertBook(a);
                                    bookScreen();
                                } else {
                                    String str = ""; // displays dynamic error messages
                                    if (!isTitleValid) str += "Title can only be (a-z),(0-9)\n";
                                    if (!isAuthorValid) str += "Author can only be (a-z),(0-9)\n";
                                    if (!isTopicValid) str += "Topics can only be (a-z),(0-9)\n";
                                    if (!isDescValid) str += "Desc can only be (a-z),(0-9)\n";
                                    JOptionPane.showMessageDialog(displayInfo, "Book "+ a + " could not be created because: \n"+str);                                        
                                      
                                }
                                    
                            }
                        });
                            
                            
                            
                    }
                    if (e.getSource().equals(HelpMenu)) {
                    	new HelpMenu();
                        
                            

                            
                    }
                }
            };
            HelpMenu.addActionListener(al);
            backButton.addActionListener(al);
            addBookButton.addActionListener(al);
	    GridBagLayout asd = new GridBagLayout();
		
            bookButtons.setLayout(asd);

	    makeConstraints(asd,backButton,1,1,0,0,1.0,0.15);
	    makeConstraints(asd,addBookButton,1,1,0,1,1.0,0.5);
	    makeConstraints(asd,HelpMenu,1,1,0,2,1.0,0.5);
		bookButtons.setBorder(BorderFactory.createMatteBorder(0,5,0,0, new Color(64,64,64)));
	    bookButtons.add(backButton);
	    bookButtons.add(addBookButton);
	    bookButtons.add(HelpMenu);
	    Book[] boooks = FileSystem.doSQLBookSearch("SELECT * FROM books");
	    JList<String> list;
	    //System.out.println(boooks.length);
	    if (boooks.length ==0) {
	    	 list = new JList<String>();
	    } else {
	    	list= new JList<String>(Book.getTitlesList(boooks));
	    }
	    
            list.setFont(this.font);
            list.setBackground(background);

            list.addListSelectionListener(new ListSelectionListener() {
            	public void valueChanged(ListSelectionEvent ea) throws ArrayIndexOutOfBoundsException {
                    displayInfo.removeAll();
                    
                    //getse selected book
                    Book book = FileSystem.doSQLBookSearch("SELECT * FROM books WHERE title is \""+list.getSelectedValue()+"\"")[0];


                    JLabel title = new JLabel();
                    JLabel author = new JLabel();
                    JLabel topics = new JLabel();
                    JTextArea desc = new JTextArea();
                    title.setBackground(background);
                    author.setBackground(background);
                    topics.setBackground(background);
                    desc.setBackground(background);
                    title.setFont(font);
                    author.setFont(font);
                    topics.setFont(font);
                    desc.setFont(font);
                    //Then displayes the selected Book
                
                    JButton edit = new JButton("Edit      ");
                    edit.setBorder(BorderFactory.createMatteBorder(5,0,5,5, new Color(64,64,64)));
                    edit.setBackground(background);
                    edit.setFont(new Font("Comic Sans MS", Font.PLAIN, 60));
                    JButton remove = new JButton("Remove");
                    remove.setBackground(background);
                    remove.setFont(new Font("Comic Sans MS", Font.PLAIN, 60));
                    remove.setBorder(BorderFactory.createMatteBorder(5,5,5,0, new Color(64,64,64)));
                    JPanel info = new JPanel();
                    info.setBackground(background);
                    info.setLayout(new GridLayout(4,1));
                    
                    JTextField titleBox = new JTextField();
                    JTextField authorBox = new JTextField();
                    JTextField topicsBox = new JTextField();
                    JTextField descBox = new JTextField();
                    
                    titleBox.setBackground(background);
                    authorBox.setBackground(background);
                    topicsBox.setBackground(background);
                    descBox.setBackground(background);
                    
                    titleBox.setFont(font);
                    authorBox.setFont(font);
                    topicsBox.setFont(font);
                    descBox.setFont(font);
                    
                    
                    edit.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            info.removeAll();

                            if (edit.getText().equals("Edit      ")) {
                            
                                info.removeAll();
                                titleBox.setText(book.title);
                                authorBox.setText(book.author);
                                topicsBox.setText(Arrays.toString(book.topics));
                                descBox.setText(book.desc);
                                descBox.setMaximumSize(descBox.getSize());
                                info.add(titleBox);
                                info.add(authorBox);
                                info.add(topicsBox);
                                info.add(descBox);
                                edit.setText("Submit Edit");
                                info.revalidate();
                            
                            } else {
                                String title = book.title;

                                //Checks to make sure edit has no special characters
                                boolean isTitleValid = titleBox.getText().matches("^[a-zA-Z0-9 .,]+$") && !titleBox.getText().trim().equals("");
                                boolean isAuthorValid = authorBox.getText().matches("^[a-zA-Z0-9 .,]+$") && !authorBox.getText().trim().equals("");
                                boolean isTopicsValid = !topicsBox.getText().trim().equals(""); //topicsBox.getText().matches("^[a-zA-Z0-9 .,]+$") && 
                                boolean isDescValid = descBox.getText().matches("^[a-zA-Z0-9 .,]+$") && !descBox.getText().trim().equals("");
                                
                                
                                if (isTitleValid && isAuthorValid &&isTopicsValid && isDescValid) {

                                	//System.out.println("UPDATE books SET title = \""+ titleBox.getText()+ "\",author = \""+ authorBox.getText() + "\", topics = \""+ topicsBox.getText() + "\", desc = \""+ descBox.getText()+"\" WHERE title = \""+title+"\";");
                                    
                                    FileSystem.executeSQL("UPDATE books SET title = \""+ titleBox.getText()+ "\",author = \""+ authorBox.getText() + "\", topics = \""+ topicsBox.getText() + "\", desc = \""+ descBox.getText()+"\" WHERE title = \""+title+"\";");
                                    bookScreen();
                                } else {
                                    String str = "";
                                    if (!isTitleValid) str += "Title can only be (a-z),(0-9)\n";
                                    if (!isAuthorValid) str += "Author can only be (a-z),(0-9)\n";
                                    if (!isTopicsValid) str += "Topics can only be (a-z),(0-9)\n";
                                    if (!isDescValid) str += "Desc can only be (a-z),(0-9)\n";
                                    JOptionPane.showMessageDialog(displayInfo, "Book could not be edited because: \n"+str);      
                                    bookScreen();
                                      
                                }
                                
                               //TODO: JOPTIONPANE


                            
                            
                            }
                        }
                    });
                
               
                
                    title.setText("Title: "+book.title);
                    author.setText("Author: "+book.author);
                    topics.setText("Topics: "+Arrays.toString(book.topics));
                    desc.setText("Description: "+book.desc);
                    desc.setWrapStyleWord(true);
                    desc.setLineWrap(true);
                    JScrollPane jsp = new JScrollPane(desc);
                    jsp.setBorder(null);
                    info.add(title);
                    info.add(author);
                    info.add(topics);
                    info.add(jsp);                        

                    Font font1 = new Font("Comic Sans MS",Font.PLAIN, 60);
                    JLabel bookDetailLogo = new JLabel("Book Display", SwingConstants.CENTER);
                    bookDetailLogo.setFont(font1);
                    //This remove listener will remove a book from the system  It also checks every user to make sure it gets removed from there book collection
                    remove.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            //System.out.println("DELETE FROM books WHERE title =\""+list.getSelectedValue()+"\"");
                            
                            
                            Person[] people = FileSystem.doSQLPeopleSearch("SELECT * FROM people WHERE checkedOutBooks LIKE \"%"+list.getSelectedValue()+"%\"");
                            for (int i = 0; i < people.length; i++) {

                                String a = FileSystem.doSQLStringSearch("SELECT * FROM people WHERE firstname is \""+people[i].getFirstName()+"\"");
                                if (a.contains(list.getSelectedValue())) {
                                    a = a.replace(list.getSelectedValue()+",", "");
                                    FileSystem.executeSQL("UPDATE people SET checkedOutBooks = \""+a+"\"");
                                }
                                
                                
                            }
                            FileSystem.executeSQL("DELETE FROM books WHERE title =\""+list.getSelectedValue()+"\"");
                            bookScreen();
                        }
                    });
                
                    GridBagLayout displayLayout = new GridBagLayout();
                    displayInfo.setLayout(displayLayout);
                
                


                    makeConstraints(displayLayout,bookDetailLogo,2,1,0,0,1.0,1.0);
                    makeConstraints(displayLayout,info,2,1,0,1,1.0,1.0);
                    makeConstraints(displayLayout,edit,1,1,0,2,1.0,1.0);
                    makeConstraints(displayLayout,remove,1,1,1,2,1.0,1.0);
                
                    bookDetailLogo.setBackground(background);
                

                
                    displayInfo.add(bookDetailLogo);
                    displayInfo.add(info); 
                    displayInfo.add(edit);
                    displayInfo.add(remove);
                    displayInfo.revalidate();
                                        
                

				
	        }
			
	    });
		//This is the serach bar
	    JTextField searchBox = new JTextField("Search for Book");
	    searchBox.setFont(font);
	    searchBox.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent arg0) {
	    		if (searchBox.getText().equals("")) {
	    			Book[] books = FileSystem.doSQLBookSearch("SELECT * from books");
	    			list.setListData(Book.getTitlesList(books));
	    		} else {
	    			Book[] books = FileSystem.doSQLBookSearch("SELECT * from books WHERE title LIKE \"%"+searchBox.getText()+"%\" OR author LIKE \"%"+searchBox.getText()+"%\""); // Uses a LIKE keyword for character comparing
	    			list.setListData(Book.getTitlesList(books));
	    		}
	    		
				
	        }
			
	    });
		
		


	    getContentPane().add(displayInfo);
	    getContentPane().add(booksAndSearch);
	    getContentPane().add(bookButtons);
	    booksAndSearch.setLayout(new BorderLayout());
	    booksAndSearch.add(searchBox, BorderLayout.NORTH);
	    booksAndSearch.add(list, BorderLayout.CENTER);
	    booksAndSearch.setVisible(true);
	    booksAndSearch.revalidate();
            booksAndSearch.repaint();	
		
	}
	public void initMenu() {
	    login.removeAll();
	    buttons.removeAll();
	    logos.removeAll();
	    getContentPane().removeAll();

		//This is the main screen
	    setLayout(layout);
//	    setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
            
            
            
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            int width = screenSize.width;
            int height = screenSize.height;
            setSize(width, height);



	    getContentPane().setBackground(background);
	    setTitle("FBLA Library");

	    ImageIcon img = new ImageIcon("res/img/bookIcon.png");
	    setIconImage(img.getImage());
		
	    
	    logos.setBorder(BorderFactory.createMatteBorder(0, 0, 10, 0, new Color(64,64,64)));
	    login.setBorder(BorderFactory.createMatteBorder(0, 10, 0, 0, new Color(64,64,64)));
		
	    makeConstraints(layout, logos, 2,2,0,0,1.0,5);
	    makeConstraints(layout, buttons, 2,1,0,2,0.25,0.25);
	    makeConstraints(layout, login,1,3,2,0,6,1.0);
	    //this basically just sets up the main boundaries

	    initLoginPanel();
	    initButtonsPanel();
            initLogoPanel();
	    setVisible(true);
 	    revalidate();
	    repaint();
	}
	public void clearBoard() {
	    getContentPane().removeAll();
	    revalidate();
	}

	public void initButtonsPanel() {
		//this sets up the buttons on the bottum
	    GridBagLayout buttonsLayout = new GridBagLayout();
	    JButton a = new JButton();
	    JButton b = new JButton();
	    JButton c = new JButton();
		
	    ActionListener buttonListener = new ActionListener() {
	    	//action listeners will tell the buttons what to do
	        public void actionPerformed(ActionEvent e) {
   		    if (e.getSource().equals(a)) {
			bookScreen();
                        
		    }
		    if (e.getSource().equals(b)) {

			personScreen();
		    }
		    if (e.getSource().equals(c)) {
					//C CLICKED

                        adminScreen();
		    }
				
		}
			
	    };
            a.addActionListener(buttonListener);
            b.addActionListener(buttonListener);
            c.addActionListener(buttonListener);
		
            //adds and sets the pictures to the buttons.
            a.setBackground(background);
            b.setBackground(background);
            c.setBackground(background);
		
            JPanel space1 = new JPanel();
            JPanel space2 = new JPanel();
            space1.setBackground(background);
            space2.setBackground(background);
            makeConstraints(buttonsLayout, a,1,1,0,0,1.0,0.5);
            makeConstraints(buttonsLayout, b,1,1,2,0,1.0,0.5);
            makeConstraints(buttonsLayout, c,1,1,4,0,1.0,0.5);
            makeConstraints(buttonsLayout, space1,1,1,1,0,0.15,0.5);
            makeConstraints(buttonsLayout, space2,1,1,3,0,0.15,0.5);
            buttons.add(space1);
            buttons.add(space2);
            buttons.add(a);
            buttons.add(b);
            buttons.add(c);
            buttons.setLayout(buttonsLayout);
            Icon booksIcon = new ImageIcon("res/img/booksIcon.png");
            Icon userIcon = new ImageIcon("res/img/usersIcon.png");
            Icon adminIcon = new ImageIcon("res/img/adminIcon.png");
            a.setIcon(booksIcon);
            b.setIcon(userIcon);
            c.setIcon(adminIcon);
            a.setBorder(null);
            c.setBorder(null);
            b.setBorder(null);
            if (!this.librarian) {
                b.setEnabled(false);
		c.setEnabled(false);
            }
            this.add(buttons);
	}
	public void initLogoPanel() {
		
		Icon ico = new ImageIcon("res/img/logo.png");
		JLabel logo = new JLabel(ico);
		
		logos.add(logo);
		logos.setBackground(background);

		//Adds the one main logo
		

		
		
		

		this.add(logos);
		
	}
	public void initLoginPanel() {

            GridBagLayout loginLayout = new GridBagLayout();
            login.setLayout(loginLayout);
		
            JButton submit = new JButton("Login");
            submit.setFont(new Font("Comic Sans MS", Font.PLAIN, 60));
            submit.setBackground(background);
            submit.setBorder(null);
            Font font = new Font("Comic Sans MS", Font.PLAIN, 48);            
            JTextField userInput = new JTextField("Username");
            JPasswordField passInput = new JPasswordField("Password");
            
            userInput.setBackground(background);
            passInput.setBackground(background);
            userInput.setFont(font);
            passInput.setFont(font);
            userInput.setBorder(null);
            passInput.setBorder(null);
            
            

            File currentDir = new File("");
            Icon icon = new ImageIcon("res/img/loginTextImage.png"); 
            JPanel asdf = new JPanel();
            JLabel image = new JLabel(icon);
            asdf.add(image);
            asdf.setBackground(background);
            asdf.setLayout(null);
            image.setBounds(0, 0, 625, 555);
		
		

		
            JLabel userText = new JLabel("  Username: ");
            JLabel passText = new JLabel("  Password:");

            userText.setFont(font);
            passText.setFont(font);
            userText.setBackground(background);
            userText.setBackground(background);
            login.setBackground(background);
	    login.revalidate();
		//asdf.setBackground(Color.pink);

            makeConstraints(loginLayout, asdf ,2,1,0,0,1.0,4.0);

            makeConstraints(loginLayout, userInput, 1,1,1,1,2.0,1.0);
            makeConstraints(loginLayout, passInput, 1,1,1,2,2.0,1.0);
            makeConstraints(loginLayout, submit, 2,1,0,3,1.0,1.0);
		
            makeConstraints(loginLayout, userText, 1, 1, 0, 1, 1.0,1.0);
            makeConstraints(loginLayout, passText, 1, 1, 0, 2, 1.0,1.0);
            if (librarian) {
            	userText.setVisible(false);
            	passText.setVisible(false);
            	userInput.setVisible(false);
            	passInput.setVisible(false);
            	
            	submit.setText("Log Out");
            }

		


            submit.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
			if (submit.getText().equals("Login")) {
                    Login newLogin = new Login(userInput.getText(), String.valueOf(passInput.getPassword())); //Char[] to String
                   
                    Login next = FileSystem.doSQLLoginSearch("SELECT * FROM logins WHERE username is \"" +newLogin.getUser()+"\" and password is \""+newLogin.getPass()+"\"");

                    if (newLogin.equals(next)) {

                    	setLibrarian(true);				
                    	initMenu();
                    
                    	
                    	
					//VALID LOGIN
                    } else  {
                    	JOptionPane.showMessageDialog(login,"Invalid User / Pass","Alert",JOptionPane.WARNING_MESSAGE);    
                    }
			
				//INVALID LOGIN
                    
			} else if (submit.getText().equals("Log Out")) {
				setLibrarian(false);
				initMenu();
			}
		}
           });
		
		//login.add(userInput);
		
            login.add(userText);
            login.add(passText);
            login.add(asdf);
            login.add(passInput);
            login.add(userInput);
            login.add(submit);

            this.add(login);
		
	}
	//This method allows for easy constraint making for gridBagLayouts
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
	
	public void setLibrarian(boolean tf) {
	    this.librarian = tf;
		
	}
	public static void main(String[] a) {
	    new Window();
	}
}