package messenger;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;

import java.io.IOException;

public class MsgAllUsersObservableList extends ListCell<Msg> {
    @FXML
    private Label label1;
    @FXML
    private Label label2;
    @FXML
    private Parent layout;

    private FXMLLoader mLLoader;
    public String MyLogin;

    public MsgAllUsersObservableList(String Mylogin) {
        this.MyLogin = Mylogin;
    }
    @FXML
    public void initialize(Msg message) {}
    @Override
    protected void updateItem(Msg message, boolean empty){
        super.updateItem(message,empty);

        if (empty || message == null) {
            setPrefHeight(80);
            setText(null);
            setGraphic(null);
        } else {
            if (message.getFrom().equals(MyLogin)) {
                mLLoader = new FXMLLoader(getClass().getResource("/fxml/mainWindow/ListCellRight.fxml"));
            } else {
                mLLoader = new FXMLLoader(getClass().getResource("/fxml/mainWindow/ListCellLeft.fxml"));
            }

            mLLoader.setController(this);
            try {
                mLLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            label1.setText(message.getFrom());
            label2.setText(message.getMessage());
            setText(null);
            setGraphic(layout);
        }
    }
}

