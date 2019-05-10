package rexrode.fbla.nlc;
/*
 * This is the Student class
 * It is the subclass to Person
 * when super is called it will make a person, then it gives the more specific features to the student
 * 
 * */
public class Student extends Person{
 
    public Student(String firstname, String lastname) {
        super(firstname, lastname);
        this.isTeacher = true; //The isTeacher boolean is actually backward in this program 
 
    }
    //If you want a limit on how many books a student can check out you can do it here.
    public boolean canCheckoutBooks() {
        boolean canCheckout = false;
       
        if (this.getBooks().length >= 3) {
            canCheckout = false;
        }
       
        return canCheckout;
    }
 
}