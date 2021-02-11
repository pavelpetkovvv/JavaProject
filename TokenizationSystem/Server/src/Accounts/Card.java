package Accounts;

import java.util.ArrayList;

class Card {

    private String cardNumber;
    private String token;

    public Card() {
        this.cardNumber = new String();
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    String addToken(){
        if(token==null) {
            token = new String();
            token = "123456";
            //algorithm for creating the token
        }
        return token;
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
