package model;

public abstract class Person {

    private String name;
    private String email;
    private String phone;

    public Person(String name , String email , String phone){
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    //Getters
    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    //Setters
    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public abstract String getDetails();
}
