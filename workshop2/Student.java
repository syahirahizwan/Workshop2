package my.edu.utem.ftmk.workshop2;

public class Student
{
    public String fullName, faculty, studentEmail, password, phoneNumber;

    public Student ()
    {

    }

    public Student(String fullName, String faculty, String studentEmail, String password, String phoneNumber)
    {
        this.fullName = fullName;
        this.faculty = faculty;
        this.studentEmail = studentEmail;
        this.password = password;
        this.phoneNumber = phoneNumber;
    }
}
