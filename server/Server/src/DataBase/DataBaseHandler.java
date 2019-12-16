package DataBase;

import auth.Encryption;
import auth.User;
import DataBase.DataBase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.sql.*;
import java.util.ArrayList;

public class DataBaseHandler extends DataBase{
    Connection dbConnection;

    public Connection getDBConnection()
            throws ClassNotFoundException, SQLException {
        String connectionString = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName + "?" + "autoReconnect=true&useSSL=false&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
        Class.forName("com.mysql.cj.jdbc.Driver");

        dbConnection = DriverManager.getConnection(connectionString, dbUser, dbPass);
        return dbConnection;
    }

    public boolean CheckLogin(String login) {
        String SELECT = "SELECT login FROM `users` WHERE login = ?";

        try {
            PreparedStatement prSt = getDBConnection().prepareStatement(SELECT);
            prSt.setString(1, login);
            ResultSet rs = prSt.executeQuery();
            String ChekLog = "";
            while (rs.next()) {
                ChekLog = rs.getString(Const.USERS_LOGIN);
            }

            if (ChekLog.equals(""))
                return true;

        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }

        return false;
    }

    public void signUpUser(User user) {

        String insert = "INSERT INTO " + Const.USERS_TABLE + "(" +
                Const.USERS_LOGIN + "," + Const.USERS_PASSWORD + "," +
                Const.USERS_FIRSTNAME + "," + Const.USERS_LASTNAME + "," +
                Const.USERS_GENDER +","+ Const.USERS_DATEOFBIRTH +","+ Const.USERS_FRIENDS +")" +
                "VALUES(?,?,?,?,?,?,?)";

        try {
            PreparedStatement prSt = getDBConnection().prepareStatement(insert);
            prSt.setString(1, user.getLogin());
            prSt.setString(2, user.getPass());
            prSt.setString(3, user.getFirstname());
            prSt.setString(4, user.getLastname());
            prSt.setString(5, user.getGender());
            prSt.setString(6, user.getDateofBirth());
            prSt.setString(7, "{}");

            prSt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public User  getInfoUserVk(String login, String Firstname, String Lastname){
        String SELECT = "SELECT * FROM `users` WHERE login = ? AND firstname = ? AND lastname = ?";

        User user = new User();
        try {
            PreparedStatement prSt = getDBConnection().prepareStatement(SELECT);

            prSt.setString(1, login);
            prSt.setString(2, Firstname);
            prSt.setString(3, Lastname);

            ResultSet rs = prSt.executeQuery();

            while (rs.next()) {
                user.setId(rs.getInt(Const.USERS_ID));
                user.setLogin(rs.getString(Const.USERS_LOGIN));
                user.setFirstname(rs.getString(Const.USERS_FIRSTNAME));
                user.setLastname(rs.getString(Const.USERS_LASTNAME));
                user.setGender(rs.getString(Const.USERS_GENDER));

                user.setDateofBirth(rs.getString(Const.USERS_DATEOFBIRTH));

                String friends = rs.getString(Const.USERS_FRIENDS);
                if (!friends.equals("{}")) {
                    friends = Encryption.decodeBase64(friends);
                    Type listType = new TypeToken<ArrayList<Integer>>() {
                    }.getType();
                    ArrayList<Integer> list = new Gson().fromJson(friends, listType);
                    user.setFriends(list);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return user;
    }

    public User getInfoUser(String login, String password) {

        String SELECT = "SELECT * FROM `users` WHERE login = ? AND password = ?";

        User user = new User();
        try {
            PreparedStatement prSt = getDBConnection().prepareStatement(SELECT);

            prSt.setString(1, login);
            prSt.setString(2, password);

            ResultSet rs = prSt.executeQuery();

            while (rs.next()) {
                user.setId(rs.getInt(Const.USERS_ID));
                user.setLogin(rs.getString(Const.USERS_LOGIN));
                user.setFirstname(rs.getString(Const.USERS_FIRSTNAME));
                user.setLastname(rs.getString(Const.USERS_LASTNAME));
                user.setGender(rs.getString(Const.USERS_GENDER));

                user.setDateofBirth(rs.getString(Const.USERS_DATEOFBIRTH));

                String friends = rs.getString(Const.USERS_FRIENDS);
                if (!friends.equals("{}")) {
                    friends = Encryption.decodeBase64(friends);
                    Type listType = new TypeToken<ArrayList<Integer>>() {
                    }.getType();
                    ArrayList<Integer> list = new Gson().fromJson(friends, listType);
                    user.setFriends(list);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return user;
    }

    public ArrayList<People> getAllUser() {

        String SELECT = "SELECT id,firstname,lastname,gender,dateofBirth FROM `users`";

        People people;
        ArrayList<People> listUser = null;
        try {
            PreparedStatement prSt = getDBConnection().prepareStatement(SELECT);

            ResultSet rs = prSt.executeQuery();
            listUser = new ArrayList<>();

            while (rs.next()) {
                people = new People();
                people.setId(rs.getInt(Const.USERS_ID));
                people.setFirstName(rs.getString(Const.USERS_FIRSTNAME));
                people.setLastName(rs.getString(Const.USERS_LASTNAME));
                people.setGender(rs.getString(Const.USERS_GENDER));
                people.setRegDateofBirth(rs.getString(Const.USERS_DATEOFBIRTH));

                listUser.add(people);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return listUser;
    }

    public ArrayList<People> FindePeople(User user){
        String SELECT = "SELECT id,firstname,lastname,gender,dateofBirth FROM `users` WHERE firstname = ? AND lastname = ?";

        People people;
        ArrayList<People> listUser = null;
        try {
            PreparedStatement prSt = getDBConnection().prepareStatement(SELECT);

            prSt.setString(1, user.getFirstname());
            prSt.setString(2, user.getLastname());


            ResultSet rs = prSt.executeQuery();
            listUser = new ArrayList<>();

            while (rs.next()) {
                people = new People();
                people.setId(rs.getInt(Const.USERS_ID));
                people.setFirstName(rs.getString(Const.USERS_FIRSTNAME));
                people.setLastName(rs.getString(Const.USERS_LASTNAME));
                people.setGender(rs.getString(Const.USERS_GENDER));
                people.setRegDateofBirth(rs.getString(Const.USERS_DATEOFBIRTH));

                listUser.add(people);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return listUser;
    }
}