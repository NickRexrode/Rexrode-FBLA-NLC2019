package rexrode.fbla.nlc;
/*
 * This is the Teacher class
 * It is the subclass to Person
 * when super is called it will make a person, then it gives the more specific features to the teacher
 * 
 * */
public class Teacher extends Person{
 
    public Teacher(String firstname, String lastname) {
        super(firstname, lastname);
        this.isTeacher = false; // all this does is make it so the teacher can check out unlimited books
    }
 
}