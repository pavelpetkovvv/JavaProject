package Accounts;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map;

public class BankClient extends User{

    ArrayList<Card> card;

    public BankClient(boolean isBankEmployee, String userName, String password) {
        super(isBankEmployee, userName, password);
        this.card = new ArrayList<Card>();
    }

    private void saveCardsToXML(){
        XStream xStream = new XStream(new DomDriver());
        xStream.alias("card", java.util.ArrayList.class);
        String xml = xStream.toXML(card);
        try (PrintWriter out = new PrintWriter("user"+ getUserName()+ "cards.xml")) {
            out.println(xml);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

     void loadCardsFromXML(){
        Path path = Paths.get("user"+ getUserName()+ "cards.xml");
        String xml = null;
        try {
            xml = Files.readString(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        XStream xStream = new XStream(new DomDriver());
        xStream.alias("card", java.util.ArrayList.class);
        System.out.println(xml);
        card = (ArrayList<Card>) xStream.fromXML(xml);
    }

    String getCardNumberByToken(String token){
        for(Card c : card){
            if(c.getToken().equals(token))
                return c.getCardNumber();
        }
        return "Wrong token";
    }

    boolean addCard(String cardNumber){
        Card newCard = new Card();
        if(newCard.setCardNumber(cardNumber))
        {
            card.add(newCard);
            saveCardsToXML();
            return true;
        }
        return false;
    }


    String addToken(String cardNumber){
        for(Card c : card){
            if(c.getCardNumber().equals(cardNumber)){
                return c.addToken();
            }
        }
        return "Wrong card number";
    }
}
