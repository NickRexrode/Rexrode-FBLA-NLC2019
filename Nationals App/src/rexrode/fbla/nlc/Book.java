package rexrode.fbla.nlc;
 
import java.util.ArrayList;
import java.util.Arrays;
 
	/* This is the book class
	 * The main idea of this class is so that I can store title, author, topics, and description together in one place.
	 * 
	 * 
	 * 
	 * 
	 * */
public class Book {
	//FIELDS - every book will have a title, author, topics, and description.
    public String title;
    public String author;
    public String[] topics;
    public String desc;
   
   //CONSTUCTOR
    public Book(String title, String author, String[] topics, String desc) {
   
   
        this.title = title;
        this.author = author;
        this.topics = topics;
        this.desc = desc;
        //Values are assigned to the fields
}

    public String toString() {
        return this.title + " by "+ this.author+ "\tTopics: "+Arrays.toString(this.topics);
        // This overrides the normal toString().
    }
   
    public static Book[] convertToArray(ArrayList<Book> listBooks) {
       //This method takes an ArrayList<Book> and transforms it into a Book[] with a loop
        Book[] books = new Book[listBooks.size()];
       
        for (int i = 0; i< books.length; i++) {
            books[i] = listBooks.get(i);
        }
        return books;
    }
   
   
   
 
 
    public static String[] getTitlesList(Book[] books) {
    	//This Method takes a Book[] and returns the titles of every book
        String[] titles = new String[books.length];
                if (books.equals(null)) {
                    return new String[0];
                } else {
                    for (int i =0; i < books.length; i++) {
            titles[i] = books[i].getTitle();
                    }
                }
        //System.out.println(Arrays.toString(titles));
        return titles;
    }
   

   //THESE are getters to access the private fields.
    public String getTitle() {
        return this.title;
    }
    public String getAuthor() {
        return this.author;
    }
    public String[] getTopics() {
        return this.topics;
    }
    public String getDesc() {
    	return this.desc;
    }
   
   
 
}