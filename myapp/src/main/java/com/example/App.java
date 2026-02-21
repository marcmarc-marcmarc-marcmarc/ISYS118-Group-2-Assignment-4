package com.example;
import DigitalID.Person;

public class App {
    public static void main(String[] args) {
        // Instantiate person with valid fields
        var person1 = new Person("56s_d%&fAB", "Robert", "Johnson", 
            "32|Highland Street|Melbourne|Victoria|Australia", "15-11-1990"); 
        person1.addPerson();

        // Update birthdate and first name
        person1.setBirthdate("15-11-1988");
        person1.setFirstName("Rob");

        // Fails because you cannot update birthdate and other fields simultaneously
        System.out.println(person1.updatePersonalDetails());

        // Instatiate person under 18 years old with valid fields
        var person2 = new Person("58s_d%&fAZ", "John", "Doe", 
            "49|Highland Street|Melbourne|Victoria|Australia", "15-11-2010"); 
        person2.addPerson();

        // Update address
        person2.setAddress("33|Huckleberry Street|Melbourne|Victoria|Australia");
        System.out.println(person2.updatePersonalDetails());

        // Instatiate person with valid fields
        var person3 = new Person("58s_d%&fAZ", "Mary", "Sue", 
            "49|Redford Street|Melbourne|Victoria|Australia", "15-11-2000"); 
        person3.addPerson();

        // Update all fields except birthdate
        person3.setFirstName("Mariah");
        person3.setLastName("Jones");
        person3.setPersonID("33s_d%&fAZ");
        person3.setAddress("67|Redford Street|Melbourne|Victoria|Australia");
        System.out.println(person3.updatePersonalDetails());

        // Success: valid date format and demeritPoints passed
        System.out.println(person3.addDemeritPoints("19-01-2025", 6));
        System.out.println(person3.getIsSuspended());

        // Fails because invalid demerit points passed
        System.out.println(person3.addDemeritPoints("23-06-2025", 7));
        System.out.println(person3.addDemeritPoints("23-06-2025", 0));

        // Fails because invalid offenseDate
        System.out.println(person3.addDemeritPoints("2000-06-26", 3));

        System.out.println(person3.addDemeritPoints("20-12-2025", 6));
        System.out.println(person3.addDemeritPoints("20-02-2026", 1));
        System.out.println(person3.getIsSuspended());

        // Instantiate person with invalid personID
        var person4 = new Person("5s_d%&fAZ", "Jane", "Do", 
            "66|Redford Street|Melbourne|Victoria|Australia", "15-11-2000");
        
        // Fails because personID is less than 10 characters
        person4.addPerson();

        // Fails because personID's last two chars are not uppercase
        person4.setPersonID("56@#abcdab");
        person4.addPerson();

        person4.setPersonID("56@#!acdAB");
        person4.addPerson();
        
    }
}
