package Accounts;

import java.util.ArrayList;

class Card {

    private String cardNumber;
    private String token;

    public Card(String cardNumber) {
        //add number validation
        this.cardNumber = new String(cardNumber);
    }

    boolean addToken(){
        String token = new String();
        //algorithm for creating the token
        return true;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getToken() {
        if(token!=null)
            return token;
        else return "0";
    }
}
