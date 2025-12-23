package models;

/**
 * Patient model class.
 */
public class Patient {
    private int id;
    private String name;
    private int age;
    private String gender;
    private String contact;
    private String address;

    public Patient(int id, String name, int age, String gender, String contact, String address) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.contact = contact;
        this.address = address;
    }

    public Patient(String name, int age, String gender, String contact, String address) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.contact = contact;
        this.address = address;
    }

    // Getters and setters
    public int getId() { return id; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getGender() { return gender; }
    public String getContact() { return contact; }
    public String getAddress() { return address; }

    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setAge(int age) { this.age = age; }
    public void setGender(String gender) { this.gender = gender; }
    public void setContact(String contact) { this.contact = contact; }
    public void setAddress(String address) { this.address = address; }
}
