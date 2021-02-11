package Accounts;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

class Card {

    private static Map<String, String> allCardsList = new HashMap<String, String>();

    private String cardNumber;
    private String token;

    public Card() {
        this.cardNumber = new String();
    }

    void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
        allCardsList.put(cardNumber, "0");
    }

    static boolean cardExists(String cardnumber){
        return allCardsList.containsKey(cardnumber);
    }

    String addToken(){
        if(token==null) {
            token = generateToken();
            allCardsList.replace(cardNumber, "0", token);
        }
        return token;
    }

    String getCardNumber() {
        return cardNumber;
    }

    String getToken() {
        if(token!=null)
            return token;
        else return "0";
    }

    private String generateToken(){
        Random random = new Random();

        int sum = 0;

        do {
            token = new String();

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
        }while (sum % 10 == 0 || allCardsList.containsValue(token));
        return token;

        //example result:
        //card number:
        //4563960122019991
        //generated token:
        //8322107731939991
    }
}
