package sample;


import Client.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
    @FXML
    private Button RegisterButton;


    @FXML
    private Button addCardButton;

    @FXML
    private TextField passwordAddUser;

    @FXML
    private TextField usernameAddUser;

    @FXML
    private RadioButton bankEmployeeRadioButton;

    @FXML
    private TextField usernameAddCard;

    @FXML
    private TextField cardNumberField;

    public void setSendButton(ActionEvent e) throws IOException {
        client.sendMessage("1 " + usernameField.getText() + " " + passwordField.getText());
        String received = client.readMessage();
        if(received.equals("none"))
            incorrect.setVisible(true);
        else{
            if(received.equals("employee")){
                username = usernameField.getText().substring(0);
                password = passwordField.getText().substring(0);
                System.out.println(username);
                System.out.println(password);
                bankEmployee = true;
                showNewWindow(e, "EmployeeWindow.fxml", "Employee UI"); //opens Employee UI
            }
            else{
                username = usernameField.getText();
                password = passwordField.getText();
                bankEmployee = false;
                //open user window
            }
        }

    }

    public void handleRegisterButton(ActionEvent e){
        String bankEmployee = new String();
        if(bankEmployeeRadioButton.isSelected()){
            bankEmployee = "t";
        }
        else bankEmployee = "f";
        client.sendMessage("2 " + username + " " + password + " " + usernameAddUser.getText() + " " + passwordAddUser.getText() +" " + bankEmployee);

        System.out.println(client.readMessage());
    }

    public void handleAddCardButton(ActionEvent e){

        client.sendMessage("3 " + username + " " + password + " " + usernameAddCard.getText() + " " + cardNumberField.getText());

        System.out.println(client.readMessage());
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

