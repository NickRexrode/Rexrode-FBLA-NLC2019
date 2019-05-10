package rexrode.fbla.nlc;
 
import java.util.ArrayList;
 

/* This is the Person Class
 * This is the superclass for Teacher and Student
 * The important fields such as first and last name are stored here.
 * Also the methods that could be done on either group, like getNamesList() 
 * are here.
 * */
public class Person {
	//Every Person will have first and last.  Then a boolean that determines if theyre a teacher.
    public boolean isTeacher;
    private String lastname;
    private String firstname;
    private String[] books;
   
    public Person(String firstname, String lastname) {
        this.lastname = lastname;
        this.firstname = firstname;
    }
    
    public static Person[] convertToArray(ArrayList<Person> listPeople) {
    //This Method will take a ArrayList<Person> and turn it into a Person[].  Can be used for students and teachers aswell
        Person[] people = new Person[listPeople.size()];
       
        for (int i = 0; i< people.length; i++) {
            people[i] = listPeople.get(i);
        }
        return people;
    }
    
    public static String[] getNameList(Person[] people) {
    	//This Method returns the Names of people in the people list.  Just a loop
        String[] titles = new String[people.length];
        for (int i =0; i < people.length; i++) {
            titles[i] = people[i].getFirstName()+" "+ people[i].getLastName();
        }
        //System.out.println(Arrays.toString(titles));
        return titles;
    }
   
    public String getFirstName() {
        return this.firstname;
    }
    public String getLastName() {
        return this.lastname;
    }
   
    public String[] getBooks() {
        return this.books;
    }
   

    public String toString() {
        return this.firstname + " "+ this.lastname;
    }
}