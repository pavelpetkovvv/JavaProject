package sample;


import Client.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class Controller {

    Client client = new Client("localHost", 8080);

    @FXML
    private javafx.scene.control.Button sendButton;

    @FXML
    private javafx.scene.control.TextField messageToSend;

    @FXML
    private javafx.scene.control.TextField receivedMessage;

    public void setSendButton(ActionEvent e){
        client.sendMessage(messageToSend.getText());
        receivedMessage.setText(client.readMessage());
    }

}

