package DataBase;

public class People {
    private Integer Id;
    private String FirstName;
    private String LastName;
    private String Gender;
    private String DateofBirth;

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public String getRegDateofBirth() {
        return DateofBirth;
    }

    public void setRegDateofBirth(String regDateofBirth) {
        this.DateofBirth = regDateofBirth;
    }
}
