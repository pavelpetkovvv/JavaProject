package Accounts;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

class Card {

    private static Map<String, Boolean> allCardsList = new HashMap<String, Boolean>();
    private static Map<String, Boolean> allTokensList = new HashMap<String, Boolean>();

    private String cardNumber;
    private String token;

    public Card() {
        this.cardNumber = new String();
    }

    boolean setCardNumber(String cardNumber) {
        if(validateCard(cardNumber)) {
            this.cardNumber = cardNumber;
            allCardsList.put(cardNumber, false);
            saveCardsListToXML();
            return true;
        }
        return false;
    }


    private void saveCardsListToXML(){
        XStream xStream = new XStream(new DomDriver());
        xStream.alias("allCardsList", java.util.Map.class);
        String xml = xStream.toXML(allCardsList);
        try (PrintWriter out = new PrintWriter("cardsList.xml")) {
            out.println(xml);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    static void loadCardsListFromXML(){
        Path path = Paths.get("cardsList.xml");
        String xml = null;
        try {
            xml = Files.readString(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        XStream xStream = new XStream(new DomDriver());
        xStream.alias("allCardsList", java.util.Map.class);
        System.out.println(xml);
        allCardsList = (Map<String, Boolean>) xStream.fromXML(xml);
    }

    private void saveTokensListToXML(){
        XStream xStream = new XStream(new DomDriver());
        xStream.alias("allTokensList", java.util.Map.class);
        String xml = xStream.toXML(allTokensList);
        try (PrintWriter out = new PrintWriter("tokensList.xml")) {
            out.println(xml);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    static void loadTokensListFromXML(){
        Path path = Paths.get("tokensList.xml");
        String xml = null;
        try {
            xml = Files.readString(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        XStream xStream = new XStream(new DomDriver());
        xStream.alias("allTokensList", java.util.Map.class);
        System.out.println(xml);
        allTokensList = (Map<String, Boolean>) xStream.fromXML(xml);
    }

    private boolean validateCard(String cardNumber){
        if(cardNumber.length() == 16 && !allCardsList.containsKey(cardNumber)){
            if(((int)cardNumber.charAt(0) - 48) > 2 && ((int)cardNumber.charAt(0) - 48) < 7){
                int sum = 0;
                int number;
                for(int i = 15; i >= 0; i --){

                    if(i%2 == 1)
                        number = ((int)cardNumber.charAt(i) - 48);
                    else
                        number = ((int)cardNumber.charAt(i) - 48) * 2;

                    if(number > 9)
                        number = number - 9;
                    sum+=number;
                }
                return sum % 10 == 0;
            }
        }
        return false;
    }

    static boolean cardExists(String cardnumber){
        return allCardsList.containsKey(cardnumber);
    }

    String addToken(){
        if(token==null) {
            token = generateToken();
            allCardsList.replace(cardNumber, false, true);
            saveTokensListToXML();
        }
        return token;
    }

    String getCardNumber() {
        return cardNumber;
    }

    String getToken() {
        if(token!=null)
            return token;
        else return "none";
    }

    private String generateToken(){
        Random random = new Random();

        int sum = 0;

        do {
            token = new String();
            sum = 0;

            int firstNumber;
            int cardFirstNumber = cardNumber.charAt(0) - 48;

            System.out.println("Token: " + token);

            do {//arrives at number between 1 and 9, that is different from the first number of the card and the numbers 3, 4, 5, 6
                firstNumber = random.nextInt(9) + 1;
            } while (firstNumber == cardFirstNumber || firstNumber == 3 || firstNumber == 4 || firstNumber == 5 || firstNumber == 6);

            System.out.println("First number: " + firstNumber);
            System.out.println("First number as char: " + (char)(firstNumber + 48));


            sum += firstNumber;

            token = token.concat(String.valueOf((char)(firstNumber + 48)));
            int number;

            for (int i = 1; i < 12; i++) {
                do {
                    number = random.nextInt(10);
                } while (number == cardNumber.charAt(i) - 48);

                System.out.println("Number #" + i + " : " + number);

                token = token.concat(String.valueOf((char) (number + 48)));
                sum += number;
            }
            for (int i = 12; i < 16; i++) {
                token = token.concat(String.valueOf(cardNumber.charAt(i)));
                sum += cardNumber.charAt(i) - 48;
            }
            System.out.println("Sum: " + sum);
            System.out.println("Complete token: " + token);
        }while (sum % 10 == 0 || allTokensList.containsKey(token));
        allTokensList.put(token, true);
        return token;

        //example result:
        //card number:
        //4563960122019991
        //generated token:
        //8322107731939991
    }
}
