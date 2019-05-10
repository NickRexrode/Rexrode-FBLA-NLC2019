package rexrode.fbla.nlc;
 
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
 /*
  * This class is responsilble for everything SQL
  * 
  * In this class I access and modify the SQLite database linked with the program.
  * The data base is called data.db and is located at "/res/textFile/data.db"
  * 
  * 
  * */
public class FileSystem {
   //All of the methods here are static
   
	//This Method will return a string.  It is used to get the books somebody has checked out.
    public static String doSQLStringSearch(String args) {
    	//This is the connection sequence to the database.
        String url = "jdbc:sqlite:"+new File("").getAbsolutePath() +"/res/textFiles/data.db";
    String result = null;
       
        try (Connection conn = DriverManager.getConnection(url);
                Statement statement = conn.createStatement()){
 
        		//The ResultSet contains the data that matches the search
                ResultSet rs = statement.executeQuery(args);
                while (rs.next()) { //This loop will loop over every item found
                    result = rs.getString("checkedOutBooks"); // this gets the value under checkedOutBooks
                }
 
        } catch (SQLException e) {
            System.out.println(e.getMessage() );
        }
 
        return result;
       
       
       
    }
    //This method will execute and SQL code.  Usefull for updating or deleting
    public static void executeSQL(String args) {
    String url = "jdbc:sqlite:"+new File("").getAbsolutePath() +"/res/textFiles/data.db";        
        try (Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement()) {
            stmt.execute(args);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
   
   
   
 
   
   
   
   
    //This method will make sure the database file exists on startup.
    public static void checkForDBFiles() {
       
       
        boolean DBExists = new File(new File("").getAbsolutePath()+"\\res\\textFiles\\data.db").exists();// Path to database
 
        if (!DBExists) {
           
            createDataBase("data.db");
            initDB();
            //System.out.println("Database \"data\" was created");
        }
       
    }
   
   //THis method will be called if the database does not exist
    public static void createDataBase(String name) {
    	
 
        String url = "jdbc:sqlite:"+new File("").getAbsolutePath() +"/res/textFiles/"+name;
   
        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
 
                System.out.println("A new database, " +name+ " has been created.");
            } else  {
                System.out.println("Connection couldn't be made!");
            }
 
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public static void initDB() {
       
        String url = "jdbc:sqlite:"+new File("").getAbsolutePath() +"/res/textFiles/data.db";
        		//The SQL strings are the SQL code that will initialize the tables
                String sql = "CREATE TABLE \"books\" (\n" +
                "   \"title\"   TEXT NOT NULL,\n" +
                "   \"author\"  TEXT NOT NULL,\n" +
                "   \"topics\"  TEXT NOT NULL,\n" +
                "   \"desc\"    TEXT NOT NULL\n" +
                ");";
       
                String sql1 = "CREATE TABLE \"people\" (\n" +
                "   \"firstname\"   TEXT NOT NULL,\n" +
                "   \"lastname\"    TEXT NOT NULL,\n" +
                "   \"isTeacher\"   TEXT NOT NULL,\n" +
                "   \"checkedOutBooks\" TEXT NOT NULL\n" +
                ");";
                String sql2 = "CREATE TABLE \"logins\" (\n" +
                "   \"username\"    TEXT NOT NULL,\n" +
                "   \"password\"    TEXT NOT NULL\n" +
                ");";
       
 
       
        try (Connection conn = DriverManager.getConnection(url);
                Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
            stmt.execute(sql1);
            stmt.execute(sql2);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
   
   
   //This method is a more primative one, but it will insert a book into the books table
    public static void insertBook(Book book) {
 
        String url = "jdbc:sqlite:"+new File("").getAbsolutePath() +"/res/textFiles/data.db";
        //String sql = "INSERT INTO books(title, author, topics) VALUES(?,?,?)";
        try (Connection conn = DriverManager.getConnection(url);
                Statement statement = conn.createStatement()){
                String topics = Arrays.toString(book.getTopics());
                statement.executeUpdate("INSERT INTO books (title, author, topics, desc) "
                        + "VALUES (" +"\""+book.title+"\",\""+ book.author+"\",\""+topics+"\", \""+book.desc+"\")");
 
 
           // System.out.println("Successfully inserted book!");
        } catch (SQLException e) {
            System.out.println(e.getMessage() );
        }
       
       
    }
   //this method is primative, but it will insert a person into the people table
    public static void insertPerson(Person person) {
        String url = "jdbc:sqlite:"+new File("").getAbsolutePath() +"/res/textFiles/data.db";
        //String sql = "INSERT INTO books(title, author, topics) VALUES(?,?,?)";
        try (Connection conn = DriverManager.getConnection(url);
                Statement statement = conn.createStatement()){
                statement.executeUpdate("INSERT INTO people (lastname, firstname,isTeacher,checkedOutBooks) "
                        + "VALUES (" +"\""+person.getLastName()+"\",\""+ person.getFirstName()+"\",\""+person.isTeacher+"\","+"\"\""+")");
 
 
            //System.out.println("Successfully inserted person!");
        } catch (SQLException e) {
            System.out.println(e.getMessage() );
        }
       
       
    }
   //This method will search for books and return the results as a book[]
    public static Book[] doSQLBookSearch(String args) {
        String url = "jdbc:sqlite:"+new File("").getAbsolutePath() +"/res/textFiles/data.db";
        ArrayList<Book> BookList = new ArrayList<Book>(); //AraayList because the ammount of found books will be an unknown size
       
        try (Connection conn = DriverManager.getConnection(url);
                Statement statement = conn.createStatement()){
 
                ResultSet rs = statement.executeQuery(args);
                while (rs.next()) {
                    String title = rs.getString("title");
                    String author = rs.getString("author");                              
//                  System.out.println("Book "); 
                    String[] topics = rs.getString("topics").replace("[", "").replace("]","").split(",");
                    String desc = rs.getString("desc");
                    //Gets all the data needed to create a book,  then it adds a book to the list            
                    BookList.add(new Book(title, author, topics, desc));
                }
 
        } catch (SQLException e) {
            System.out.println(e.getMessage() );
        }
        //This converts the ArrayList to a regular Book[]
        Book[] books = Book.convertToArray(BookList);
      //This is a null check
            return books;
       
       
    }
    //This method will serach the database for people and then return the results as a Person[]
    //this way both teachers and students can be included because Person is the superclass.
    public static Person[] doSQLPeopleSearch(String args) {
        String url = "jdbc:sqlite:"+new File("").getAbsolutePath() +"/res/textFiles/data.db";
        ArrayList<Person> PeopleList = new ArrayList<Person>();//ARrayList because size is not known
       
        try (Connection conn = DriverManager.getConnection(url);
                Statement statement = conn.createStatement()){
 
                ResultSet rs = statement.executeQuery(args);
               
                while (rs.next()) {
                    String firstname =rs.getString("firstname");
                    String lastname = rs.getString("lastname");
                    String isTeacher = rs.getString("isTeacher");
                    //gets data needed
                    boolean isTeach = isTeacher.equals("true");
                    //If they are a teacher, a teacher is made.  same with student.  THen they are added to the Person List
                    if(!isTeach) {
                                        Teacher teach = new Teacher(firstname, lastname);
                        PeopleList.add(teach);
                                       // System.out.println(teach.toString());
                    } else {
                                    Student stude = new Student(firstname, lastname);
                        PeopleList.add(stude);
                                        //System.out.println(stude.toString());
                    }
                   
                    //String[] topics = rs.getString("topics").replace("[", "").replace("]","").split(",");
                }
 
        } catch (SQLException e) {
            System.out.println(e.getMessage() );
        }
        //Converts to people[]
        Person[] people = Person.convertToArray(PeopleList);
        
        return people;
       
       
    }
    //This method checks if a login is valid
        public static Login doSQLLoginSearch(String args) {
            String url = "jdbc:sqlite:"+new File("").getAbsolutePath() +"/res/textFiles/data.db";
            Login login = null;
           
            try(Connection conn = DriverManager.getConnection(url);
                    Statement statement = conn.createStatement()) {
                ResultSet rs = statement.executeQuery(args);
               
                while (rs.next()) {
                    String username = rs.getString("username");
                    String password = rs.getString("password");
                   
                    login = new Login(username, password); //Finds the login, if one exists
                }
               
            } catch (SQLException e) {
                System.out.println(e.getMessage() );
       
                   
        }
   
            return login;
        }
        //This will get a List of all of the Logins.  This is useful for the Admin page.
        public static Login[] doSQLLoginListSearch(String args) {
            ArrayList<Login> logins = new ArrayList<Login>(); // use arraylist because size is not known
            String url = "jdbc:sqlite:"+new File("").getAbsolutePath() +"/res/textFiles/data.db";
           
            try(Connection conn = DriverManager.getConnection(url);
                    Statement statement = conn.createStatement()) {
                ResultSet rs = statement.executeQuery(args);
               
                while (rs.next()) {
                    String username = rs.getString("username");
                    String password = rs.getString("password");
                   
                    logins.add(new Login(username, password));
                }
               
            } catch (SQLException e) {
                System.out.println(e.getMessage() );
       
                   
        }
           
           
           
            return Login.asArray(logins);  //turns it to a regular login[]
        }
 
}