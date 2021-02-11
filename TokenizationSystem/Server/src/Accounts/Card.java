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
