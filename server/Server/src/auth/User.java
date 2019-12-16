package auth;

import io.netty.channel.ChannelHandlerContext;

import java.util.ArrayList;


public class User {
    private int id;
    private String login;
    private String pass;
    private String firstname;
    private String lastname;
    private String gender;
    private String DateofBirth;
    private transient ChannelHandlerContext ctx;
    private ArrayList<Integer>friends = new ArrayList<Integer>();


    //========================================================================
    public ArrayList<Integer> getFriends() {
        return friends;
    }
    public void setFriends(ArrayList<Integer> friends) {
        this.friends = friends;
    }


    public ChannelHandlerContext getCtx() {
        return ctx;
    }
    public void setCtx(ChannelHandlerContext ctx) {
        this.ctx = ctx;
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
                ", ctx=" + ctx +
                ", friends=" + friends +
                '}';
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

    public String getDateofBirth() {
        return DateofBirth;
    }

    public void setDateofBirth(String dateofBirth) {
        DateofBirth = dateofBirth;
    }

}
