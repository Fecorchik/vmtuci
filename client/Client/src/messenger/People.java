package messenger;

public class People {
    public String Id;
    public String FirstName;
    public String LastName;
    public String Gender;
    public String DateofBirth;

    public People(String firstname, String lastname){
        this.FirstName = firstname;
        this.LastName = lastname;
    }
    public People(String id, String firstname, String lastname, String gender, String bithDay){
        this.FirstName = firstname;
        this.LastName = lastname;
        this.Id = id;
        this.Gender = gender;
        this.DateofBirth = bithDay;
    }

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
    public String getId() {
        return Id;
    }
    public void setId(String id) {
        Id = id;
    }

    public String getRegDateofBirth() {
        return DateofBirth;
    }

    public void setRegDateofBirth(String regDateofBirth) {
        this.DateofBirth = regDateofBirth;
    }
}
