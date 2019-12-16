package messenger.Controllers;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.neovisionaries.ws.client.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import messenger.*;

import javax.swing.text.html.ImageView;
import java.io.IOException;
import java.lang.reflect.Type;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class ControllerAuth {

    @FXML
    private TabPane tabPane;
    //авторизация - логин
    @FXML
    private TextField login_field;
    //авторизация - пароль
    @FXML
    private TextField password_field;
    //авторизация - кнопка
    @FXML
    private Button authSigInButton;
    //авторизация - HBox логина
    @FXML
    private HBox hboxlogin;
    //авторизация - HBox пароля
    @FXML
    private HBox hboxpassword;

    //авторизация - кнопка сохранения
    @FXML
    private RadioButton rememberMe;
    //авторизация - окно вывода информации
    @FXML
    private TextField loginfo;
    //авторизацяи - вконтакте
    @FXML
    private Button vkbutton;


    //регистрация - логин
    @FXML
    private TextField regLogin;
    //регистрация - пароль
    @FXML
    private TextField regPass;
    //регистрация - подтверждение пароля
    @FXML
    private TextField regPassConf;
    //регистрация - Имя
    @FXML
    private TextField regFirstName;
    //регистрация - Фамилия
    @FXML
    private TextField regLastName;
    //регистрация - пол:мужчина
    @FXML
    private CheckBox regMale;
    //регистрация - пол:женщина
    @FXML
    private CheckBox regFemale;
    //регистрация - кнопка создать аккаунт
    @FXML
    private Button regBtn;
    //информация
    @FXML
    private TextField regInfo;
    @FXML
    private MaskField dateofBirth;
    @FXML
    private CheckBox chekLocal;

    @FXML
    private WebView view;

    public static final String REDIRECT_URL = "https://oauth.vk.com/blank.html";
    public static final String VK_AUTH_URL = "https://oauth.vk.com/authorize?client_id=7147360&display=page&redirect_uri=https://oauth.vk.com/blank.html&scope=friends&response_type=token&v=5.103";
    public static String tokenUrl;

    @FXML
    void initialize() {
        try {
            ConnectServer();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (WebSocketException e) {
            e.printStackTrace();
        }


        //===============================================VK=============================================================
        final WebEngine engine = view.getEngine();
        engine.load(VK_AUTH_URL);
        engine.locationProperty().addListener(new ChangeListener<String>(){
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue.startsWith(REDIRECT_URL)){
                    tokenUrl=newValue;
                    String[] mas = tokenUrl.split("&");
                    for(int i = 0; i <mas.length;i++){
                        String[] temp = mas[i].split("=");
                        switch(i){
                            case 0:{Global.setAccess_token(temp[1]);break; }
                            case 2:{Global.setUser_id(temp[1]);break;}
                        }
                    }
                    if(!Global.getAccess_token().equals("") && !Global.getUser_id().equals("")) {
                        try {
                            String page = "https://api.vk.com/method/account.getProfileInfo?user_ids=" + Global.getUser_id() + "&fields=bdate&access_token=" + Global.getAccess_token() + "&v=5.103";
                            Document doc = Jsoup.connect(page).ignoreContentType(true).get();
                            String vk = doc.body().text();
                            int indexEnd = vk.indexOf("}");
                            vk = vk.substring(12,indexEnd+1);
                            System.out.println(vk);
                            UserVK user_vk = new Gson().fromJson(vk, UserVK.class);
                            user_vk.setPassword(Global.getAccess_token());
                            loginUserVK(user_vk);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        //===============================================VK=============================================================

        //кнопка авторизации
        authSigInButton.setOnAction(e -> {
            User user = new User();
            user.setLogin(login_field.getText().trim());


            try {
                user.setPass(Encryption.toSha256(password_field.getText().trim()));
            } catch (NoSuchAlgorithmException ex) {
                ex.printStackTrace();
            }

            hboxpassword.setStyle("-fx-border-color: #efef94");
            hboxpassword.setStyle("-fx-border-color: #efef94");

            if (!user.getLogin().equals("") && !user.getPass().equals("")) {

                hboxpassword.setStyle("-fx-border-color: #efef94");
                hboxpassword.setStyle("-fx-border-color: #efef94");

//                try {
                //WebSocket ws =  ConnectServer();
                loginUser(user);//, ws);

//                } catch (IOException ex) {
//                    ex.printStackTrace();
//                } catch (WebSocketException ex) {
//                    ex.printStackTrace();
//                }
            } else if (!user.getLogin().equals("") && user.getPass().equals("")) {
                loginfo.setText("Поле пароля пустое!");
                hboxpassword.setStyle("-fx-border-color: red");
            } else if (user.getLogin().equals("") && !user.getPass().equals("")) {
                loginfo.setText("Поле логина пустое!");
                hboxlogin.setStyle("-fx-border-color: red");
                return;
            } else {
                loginfo.setText("Поля авторизации пустые");
                hboxlogin.setStyle("-fx-border-color: red");
                hboxpassword.setStyle("-fx-border-color: red");
                return;
            }
        });

        // Ограничение ввода символов в логине
        Pattern patternLogin = Pattern.compile(".{0,30}");
        TextFormatter formatterLogin = new TextFormatter((UnaryOperator<TextFormatter.Change>) change -> {
            return patternLogin.matcher(change.getControlNewText()).matches() ? change : null;
        });
        login_field.setTextFormatter(formatterLogin);

        // Ограничение ввода символов в пароле
        Pattern patternLPassword = Pattern.compile(".{0,30}");
        TextFormatter formatterPassword = new TextFormatter((UnaryOperator<TextFormatter.Change>) change -> {
            return patternLPassword.matcher(change.getControlNewText()).matches() ? change : null;
        });
        password_field.setTextFormatter(formatterPassword);

        //Выключить другой пол
        regFemale.setOnAction(e -> {
            if (regFemale.isSelected()) regMale.setSelected(false);
        });

        //Выключить другой пол
        regMale.setOnAction(e -> {
            if (regMale.isSelected()) regFemale.setSelected(false);
        });

    }


    WebSocketListener listenner = new WebSocketAdapter() {
        @Override
        public void onTextMessage(WebSocket websocket, String text) throws Exception {
            String[] mas = Global.get(text);
            switch (mas[0]) {
                case "UserIsFound": {//пользователь - найден
                    System.out.println("Получен ответ UserIsFound");
                    User user = new Gson().fromJson(mas[1], User.class);
                    System.out.println(user);

                    Platform.runLater(() -> nextFXML(user));
                    break;
                }
                case "UserNotFound": {//пользователь - не найден
                    System.out.println("Получен ответ UserNotFound");
                    loginfo.setText("Пользователь не найден");
                    regInfo.setText("Аккаунт создан успешно");
                    break;
                }
                case "LoginUnavailable": {//логин - занят
                    regInfo.setText("Данный логин занят");
                    regLogin.setStyle("-fx-border-color: red");
                    break;
                }
                case "LoginAvailable": {//логин - свободен
                    break;
                }
                case "AllUsers": {
                    Type listType = new TypeToken<ArrayList<People>>() {
                    }.getType();
                    ArrayList<People> listUsers = new Gson().fromJson(mas[1], listType);
                    break;
                }
                default:
            }
        }
    };

    //авторизация. Отправка данных на сервер
    private void loginUserVK(UserVK vk){
        User user = new User();
        user.setLogin(vk.getScreen_name());
        try {
            user.setPass(Encryption.toSha256(vk.getPassword()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        user.setFirstname(vk.getFirst_name());
        user.setLastname(vk.getLast_name());
        user.setDateofBirth(vk.getBdate());
        switch (vk.getSex())
        {
            case 0:{
                user.setGender("none");
                break;
            }
            case 1:{
                user.setGender("female");
                break;
            }
            case 2:{
                user.setGender("male");
                break;
            }
        }
        Global.getWs().addListener(listenner);
        Global.getWs().sendText(Global.send(TypeMessage.AuthVk, new Gson().toJson(user)));
    }

    private void loginUser(User user) {//, WebSocket ws) {
        loginfo.clear();
        Global.getWs().addListener(listenner);
//        String str  = Global.send(TypeMessage.Auth, new Gson().toJson(user));
        Global.getWs().sendText(Global.send(TypeMessage.Auth, new Gson().toJson(user)));
    }

    private void registerUser(User user, WebSocket ws) {//TODO регистрация
//        ws.addListener(listenner);
//        ws.sendText(Global.send(TypeMessage.Reg, new Gson().toJson(user)));
        Global.getWs().addListener(listenner);
        Global.getWs().sendText(Global.send(TypeMessage.Reg, new Gson().toJson(user)));
    }

    private void ConnectServer() throws IOException, WebSocketException {
        WebSocketFactory wbF = new WebSocketFactory().setConnectionTimeout(10000);
        //TODO ip - пк - где сервак

//        if (chekLocal.isSelected())
//            serv = "ws://192.168.1.68:9000";
//        else
//            serv = "ws://91.76.182.215:9000";

//        WebSocket ws = wbF.createSocket("ws://192.168.1.68:9000");
        WebSocket ws = wbF.createSocket("ws://192.168.43.212:9000");
        ws.connect();
        Global.setWs(ws);
    }

    public void VkButtonClicked(MouseEvent mouseEvent) {

        //Main.getMain().setMain("Вк");
        //nextFXML();
    }

    private void nextFXML(User user) {
        Main.getMain().setMain(user);
    }

    public void ClickedRegistration(MouseEvent mouseEvent) {
        regLogin.setStyle("-fx-border-color: #efef94");
        regPass.setStyle("-fx-border-color: #efef94");
        regPassConf.setStyle("-fx-border-color: #efef94");

        System.out.println(dateofBirth.getText());


        if (!loginfo.equals("") && !regPass.equals("")
                && !regPassConf.equals("") && !regFirstName.equals("")
                && !regLastName.equals("") && !dateofBirth.getPlainText().equals("") &&
                regMale.isSelected() || regFemale.isSelected()) {

            boolean chekDB = chekdate(dateofBirth.getText());

            if (!chekDB) {
                regInfo.setText("Дата указана неправильно.");
                return;
            }

            if (regPass.getText().equals(regPassConf.getText())) {
                WebSocket ws = null;
                User user = new User();
                try {
                    ws = Global.getWs();
                    //   ws.addListener(listenner);

                    if (regMale.isSelected()) user.setGender("male");
                    else user.setGender("female");

                    user.setLogin(regLogin.getText());
                    user.setPass(Encryption.toSha256(regPass.getText()));
                    user.setFirstname(regFirstName.getText());
                    user.setLastname(regLastName.getText());
                    user.setDateofBirth(dateofBirth.getText());

                    System.out.println(user);
                    registerUser(user, ws);
                } catch (NoSuchAlgorithmException ex) {
                    ex.printStackTrace();
                }
            } else {
                regPass.setStyle("-fx-border-color: red");
                regPassConf.setStyle("-fx-border-color: red");

                regInfo.clear();
                regInfo.setText("Пароли не совпадают");
            }
        } else {
            regInfo.clear();
            regInfo.setText("Поля не заполнены");
        }
    }

    public boolean chekdate(String string) {
        String[] mas = string.split("\\.");
        boolean chekDate = false;
        boolean chekMounth = false;
        boolean cheakYear = false;

        int year = 0;
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        if (Integer.parseInt(mas[0]) <= 31 && Integer.parseInt(mas[0]) >= 1)
            chekDate = true;
        if (Integer.parseInt(mas[1]) <= 12 && Integer.parseInt(mas[1]) >= 1)
            chekMounth = true;
        if (Integer.parseInt(mas[2]) <= year && Integer.parseInt(mas[2]) >= 1970)
            cheakYear = true;

        if (chekDate && chekMounth && cheakYear)
            return true;
        return false;
    }
}


