package sample;


import Client.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.*;

import java.io.IOException;

public class Controller {

    Client client = new Client("localHost", 8080);
    static String username = new String();
    static String password = new String();
    static boolean bankEmployee = false;

    //login screen elements:
    @FXML
    private javafx.scene.control.Button sendButton;

    @FXML
    private javafx.scene.control.TextField usernameField;

    @FXML
    private javafx.scene.control.TextField passwordField;

    @FXML
    private Text incorrect;


    //employee screen elements:
    //register new user
    @FXML
    private TextField passwordAddUser;
    @FXML
    private TextField usernameAddUser;
    @FXML
    private RadioButton bankEmployeeRadioButton;
    @FXML
    private Button RegisterButton;
    @FXML
    private Text usernameTakenWarning;
    @FXML
    private Text registrationSuccessful;
    //add new card
    @FXML
    private TextField usernameAddCard;
    @FXML
    private TextField cardNumberField;
    @FXML
    private Button addCardButton;
    @FXML
    private Text invalidUsernameWarning;
    @FXML
    private Text invalidCardNumber;
    @FXML
    private Text cardAdded;

    //client screen elements:
    //generate token:
    @FXML
    private TextField tokenField;
    @FXML
    private TextField cardNumberTokenizeField;
    @FXML
    private Button generateTokenButton;
    //get card number by token
    @FXML
    private TextField tokenGetCardField;
    @FXML
    private TextField cardNumberGetCardField;
    @FXML
    private Button getCardButton;

    public void handleLoginButton(ActionEvent e) throws IOException {
        client.sendMessage("1 " + usernameField.getText() + " " + passwordField.getText());
        String received = client.readMessage();
        if(received.equals("none"))
            incorrect.setVisible(true);
        else{
            if(received.equals("employee")){
                username = usernameField.getText().substring(0);
                password = passwordField.getText().substring(0);
                bankEmployee = true;
                showNewWindow(e, "EmployeeWindow.fxml", "Employee UI"); //opens Employee UI
            }
            else
                if(received.equals("client")) {
                    username = usernameField.getText().substring(0);
                    password = passwordField.getText().substring(0);
                    bankEmployee = false;
                    showNewWindow(e, "ClientWindow.fxml", "Client UI"); //opens Employee UI
                }
                else
                    incorrect.setVisible(true);
        }

    }

    public void setFeedbackInvisible(MouseEvent e){
        registrationSuccessful.setVisible(false);
        usernameTakenWarning.setVisible(false);
        invalidUsernameWarning.setVisible(false);
        invalidCardNumber.setVisible(false);
        cardAdded.setVisible(false);
    }

    public void handleRegisterButton(ActionEvent e){
        String bankEmployee = new String();
        if(bankEmployeeRadioButton.isSelected()){
            bankEmployee = "t";
        }
        else bankEmployee = "f";
        client.sendMessage("2 " + username + " " + password + " " + usernameAddUser.getText() + " " + passwordAddUser.getText() +" " + bankEmployee);

        String received = client.readMessage();
        if(received.equals("success"))
            registrationSuccessful.setVisible(true);
        else
            if(received.equals("exists"))
                usernameTakenWarning.setVisible(true);
        System.out.println(received);
    }

    public void handleAddCardButton(ActionEvent e){

        client.sendMessage("3 " + username + " " + password + " " + usernameAddCard.getText() + " " + cardNumberField.getText());

        String received = client.readMessage();

        if(received.equals("unsuccessful"))
            invalidCardNumber.setVisible(true);
        else
            if(received.equals("none"))
                invalidUsernameWarning.setVisible(true);
            else
                if(received.equals("success"))
                    cardAdded.setVisible(true);
        System.out.println(received);
    }

    public void handleTokenizeButton(ActionEvent e){

        client.sendMessage("4 " + username + " " + password + " " + cardNumberTokenizeField.getText());

        tokenField.setText(client.readMessage());
    }

    public void handleGetCardButton(ActionEvent e){

        client.sendMessage("5 " + username + " " + password + " " + tokenGetCardField.getText());

        cardNumberGetCardField.setText(client.readMessage());
    }

    private void showNewWindow(ActionEvent event, String name, String windowName) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(name));
        Scene scene = new Scene(root);

        Stage window = (Stage) (((Node)event.getSource()).getScene().getWindow());
        window.setTitle(windowName);
        window.setScene(scene);
        window.show();
    }

}

