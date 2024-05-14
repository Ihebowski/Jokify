package dev.ihebowski.jokify.models;

public class User {
    public String firstname;
    public String lastname;
    public String email;
    public String phone;

    public User(){
    }

    public User(String firstname,String lastname, String email, String phone){
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.phone = phone;
    }
}
