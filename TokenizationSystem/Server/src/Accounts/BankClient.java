package Accounts;

import java.util.ArrayList;

public class BankClient extends User{

    ArrayList<Card> card;

    public BankClient(boolean isBankEmployee, String userName, String password) {
        super(isBankEmployee, userName, password);
        this.card = new ArrayList<Card>();
    }

    String getCardNumberByToken(String token){
        for(Card c : card){
            if(c.getToken().equals(token))
                return c.getCardNumber();
        }
        return "0";
    }

    boolean addCard(String cardNumber){
        Card newCard = new Card();
        newCard.setCardNumber(cardNumber);
        card.add(newCard);
        return true;
    }
}
