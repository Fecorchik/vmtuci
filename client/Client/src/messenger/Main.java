package messenger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import messenger.Controllers.ControllerAuth;
import messenger.Controllers.ControllerMainWindow;
import java.io.IOException;

public class Main extends Application {
    private Stage primary;
    private static Main m;

    @Override
    public void start(Stage primaryStage) throws Exception{
        primary=primaryStage;
        m=this;

        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/auth/auth.fxml"));
        Parent root = loader.load();
        ControllerAuth ctrl = loader.getController();

        primaryStage.setTitle("Vmtuci");
        primaryStage.setScene(new Scene(root, 648, 600));

        primaryStage.setMaxHeight(600);primaryStage.setMinHeight(600);
        primaryStage.setMaxWidth(648);primaryStage.setMinWidth(648);
        primaryStage.getScene().getStylesheets().add("css/style-auth.css");
        primaryStage.show();
    }

    public void setMain(User user){
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/mainWindow/MainWindow.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ControllerMainWindow ctrl = loader.getController();
        ctrl.setUser(user);
        ctrl.setMainPage();

        Scene scene = new Scene (root,800,600);
        primary.setScene(scene);
        primary.setMinWidth(800);
        primary.getScene().getStylesheets().add("css/style-main.css");
        primary.show();
    }

    public static Main getMain(){return m;}
    public static void main(String[] args) {launch(args); }

}
