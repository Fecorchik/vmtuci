package messenger;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.text.Text;

import java.io.IOException;

public class UserObservableList extends ListCell<People> {
    @FXML
    private Text friendsFirstName;
    @FXML
    private Text friendsLastName;
    @FXML
    private Parent layout;

    @FXML
    private Label peopleFirstName;
    @FXML
    private Label peopleLastName;
    @FXML
    private Label peopleGender;
    @FXML
    private Label peopleID;
    @FXML
    private Label peopleBithDay;

    private FXMLLoader mLLoader;

    public UserObservableList(Label peopleID, Label peopleFirstName, Label peopleLastName,Label peopleGender, Label peopleBithDay) {
        this.peopleID = peopleID;
        this.peopleFirstName = peopleFirstName;
        this.peopleLastName = peopleLastName;
        this.peopleGender = peopleGender;
        this.peopleBithDay = peopleBithDay;
    }

    @FXML
    public void initialize(People people) {}

    @Override
    protected void updateItem(People people, boolean empty){
        super.updateItem(people,empty);

        if (empty || people == null) {
            setPrefHeight(80);
            setText(null);
            setGraphic(null);
        } else {
            mLLoader = new FXMLLoader(getClass().getResource("/fxml/people.fxml"));
            mLLoader.setController(this);
            try {
                mLLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            friendsFirstName.setText(people.getFirstName());
            friendsLastName.setText(people.getLastName());
            setText(null);
            setGraphic(layout);

            layout.setOnMouseClicked(event -> {
                Platform.runLater(() -> {
                    peopleFirstName.setText(people.getFirstName());
                    peopleLastName.setText(people.getLastName());
                    peopleID.setText("ID = " + people.getId());
                    peopleGender.setText(people.getGender());
                   peopleBithDay.setText("Дата рождения: "+ people.DateofBirth);
                });
            });
        }
    }
}
