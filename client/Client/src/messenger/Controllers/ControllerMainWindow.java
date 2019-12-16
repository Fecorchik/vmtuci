package messenger.Controllers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.neovisionaries.ws.client.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import messenger.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;


public class ControllerMainWindow implements Initializable {
    //=============================================================================
    //Общий чат
    @FXML
    private Label label1;
    @FXML
    private Label label2;
    @FXML
    private ListView<Msg> MessageAllUsers;
    @FXML
    public TextField MessageAllUsersTextField;
    private ObservableList<Msg> MsgAllUsersObservableList = FXCollections.observableArrayList();
    //=============================================================================
    //Главная страница
    @FXML
    public Label myID;
    @FXML
    public Label myFirstName;
    @FXML
    public Label myLastName;
    @FXML
    public Label myGender;
    @FXML
    public Label myDateOfBirth;
    //=============================================================================
    //Вкладка Люди
    @FXML
    public TextField PeopleFindFirstName;//Имя
    @FXML
    public TextField PeopleFindLastName;//Фамиля
    @FXML
    public Button PeopleFindButton;//Кнопки

    public Label peopleLastName;
    public Label peopleFirstName;
    public Label peopleID;
    public Label peopleGender;
    public Label peopleBithDay;
    @FXML
    private ListView<People> peopleListView;
    private ObservableList<People> userObservableList = FXCollections.observableArrayList();
    //=============================================================================
    public static User client;

    public static User getUser() {
        return client;
    }
    public static void setUser(User user) {
        ControllerMainWindow.client = user;
    }




    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // userObservableList.add(new People("Hello","World"));
        peopleListView.setItems(userObservableList);
        peopleListView.setCellFactory(plw -> new UserObservableList(peopleID, peopleFirstName, peopleLastName, peopleGender, peopleBithDay));

        MessageAllUsers.setItems(MsgAllUsersObservableList);
        MessageAllUsers.setCellFactory(mau -> new MsgAllUsersObservableList(client.getLogin()));

        Global.getWs().addListener(MainFormlistenner);
    }

    public void setMainPage() {
        myID.setText("ID = " + String.valueOf(client.getId()));
        myFirstName.setText("Имя: " + client.getFirstname());
        myLastName.setText("Фамилия: " + client.getLastname());
        myGender.setText("Пол: " + client.getGender());
        myDateOfBirth.setText("Дата рождения: " + client.getDateofBirth());
    }

    public void sendMessage(MouseEvent mouseEvent) {
        System.out.println(getUser());
    }

    //Люди- добавить в друзья
    public void peopleAddfriends(MouseEvent mouseEvent) {//TODO потом исправлю
        Global.getWs().addListener(MainFormlistenner);
        Global.getWs().sendText(Global.send(TypeMessage.AllUser, "0"));
    }

    WebSocketListener MainFormlistenner = new WebSocketAdapter() {
        @Override
        public void onTextMessage(WebSocket websocket, String text) throws Exception {
            String[] mas = Global.get(text);
            switch (mas[0]) {
                case "AllUsers":
                case "findePeople": {
                    Platform.runLater(() -> userObservableList.clear());
                    Type listType = new TypeToken<ArrayList<People>>() {
                    }.getType();
                    ArrayList<People> listUsers = new Gson().fromJson(mas[1], listType);
                    listUsers.stream().<Runnable>map(listUser -> () -> userObservableList.add(new People(listUser.Id, listUser.FirstName, listUser.LastName, listUser.Gender, listUser.DateofBirth))).forEach(Platform::runLater);
                    break;
                }
                case "msgAllUsers": {
                    Msg message = new Gson().fromJson(mas[1], Msg.class);
                    System.out.println(MsgAllUsersObservableList.size());
                    MsgAllUsersObservableList.add(new Msg(message.getFrom(),message.getMessage()));
                    break;
                }
            }
        }
    };

    public void PeopleFind(MouseEvent mouseEvent) {
        User user = new User();
        user.setFirstname(PeopleFindFirstName.getText());
        user.setLastname(PeopleFindLastName.getText());
        Global.getWs().addListener(MainFormlistenner);
        Global.getWs().sendText(Global.send(TypeMessage.FindPeople, new Gson().toJson(user)));
    }

    //Отправить сообщение всем пользователям. В окно общего чата
    public void sendMessageAllUsers(MouseEvent mouseEvent) {
        Msg message = new Msg(client.getLogin(), MessageAllUsersTextField.getText());
        Global.getWs().sendText(Global.send(TypeMessage.MessageAllUsers, new Gson().toJson(message)));

    }
}
