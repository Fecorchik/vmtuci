package messenger;

public class User {
    private int id;
    private String login;
    private String pass;
    private String firstname;
    private String lastname;
    private String gender;
    private String DateofBirth;

    public String getDateofBirth() {
        return DateofBirth;
    }

    public void setDateofBirth(String regDateofBirth) {
        this.DateofBirth = regDateofBirth;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", pass='" + pass + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", gender='" + gender + '\'' +
                ", DateofBirth='" + DateofBirth + '\'' +
                '}';
    }

    public User(){}

    public User(String firstname, String lastname){
        this.firstname = firstname;
        this.lastname = lastname;
    }


    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
