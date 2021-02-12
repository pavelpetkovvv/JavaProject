package Accounts;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Application {

    private final static int COMMAND_POSITION = 0;
    private final static int USERNAME_POSITION = 1;
    private final static int PASSWORD_POSITION = 2;
    private final static int ARGUMENT1_POSITION = 3;
    private final static int ARGUMENT2_POSITION = 4;
    private final static int ARGUMENT3_POSITION = 5;


    private static Map<String, User> listOfUsers; //list of all users, mapping Username (key) to User (value)

    public Application() {
        listOfUsers = new HashMap<String, User>();
        User Admin = new BankEmployee(true, "Admin", "Admin");
        listOfUsers.put("Admin", Admin);
        loadListOfUsers();
        Card.loadCardsListFromXML();
        Card.loadTokensListFromXML();
        for(Map.Entry<String, User> entry : listOfUsers.entrySet()){
            if(!entry.getValue().isBankEmployee()){
                User bankClient = entry.getValue();
                ((BankClient) bankClient).loadCardsFromXML();
            }
        }
    }


    //save listOfUsers in XML serialization
    static void saveListOfUsers(){
        XStream xStream = new XStream(new DomDriver());
        xStream.alias("listOfUsers", java.util.Map.class);
        String xml = xStream.toXML(listOfUsers);
        try (PrintWriter out = new PrintWriter("listOfUsers.xml")) {
            out.println(xml);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    //load listOfUsers from XML serialization
    static void loadListOfUsers(){
        Path path = Paths.get("listOfUsers.xml");
        String xml = null;
        try {
            xml = Files.readString(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        XStream xStream = new XStream(new DomDriver());
        xStream.alias("listOfUsers", java.util.Map.class);
        System.out.println(xml);
        listOfUsers = (Map<String, User>) xStream.fromXML(xml);
    }

    //authenticates that the user is registered as BankEmployee
    static boolean authenticate(BankEmployee employee){
            if (searchUser(employee.getUserName())) { //looks for the name in the listOfUsers
                if(listOfUsers.get(employee.getUserName()).isBankEmployee()) { //checks in listOfUsers if user is bank employee
                    if(listOfUsers.get(employee.getUserName()).getPassword().equals(employee.getPassword())) //checks if both passwords are equal
                            return true;
                }
            }
        return false;
    }

    static String generateToken(String username,String cardNumber){
        User user = listOfUsers.get(username);
        if(user.isBankEmployee())
            return "nbc"; //not bank client
        else{
            return (((BankClient) user).addToken(cardNumber));
        }
    }

    static String getCardByToken(String username, String token){
        User user = listOfUsers.get(username);
        if(user.isBankEmployee())
            return "nbc"; //not bank client
        else{
            return (((BankClient) user).getCardNumberByToken(token));
        }
    }

    static int authenticateByUsernamePassword(String username, String password){
        if(searchUser(username)){
            User user = listOfUsers.get(username);
            if(user.getPassword().equals(password))
                if(user.isBankEmployee()) {
                    return 0; //returns 0 if user is bank employee
                } else return 1; //returns 1 if user is bank client
        }
        return 2;//returns 2 if such user does not exist
    }

    //add new user to the list
    static int addUser(String username, String password, boolean bankEmployee) {
        if(!searchUser(username)) {
            if(bankEmployee) {
                    listOfUsers.put(username, new BankEmployee(true, username, password));
                    return 0; //if added successfully
            }else {
                listOfUsers.put(username, new BankClient(false, username, password));
                return 0; //if added successfully
            }
        }
        else return 1;//if username is redundant
    }

    //returns true if such user exists
    static boolean searchUser(String userName){
        return listOfUsers.get(userName)!=null;
    }

    static String addCard(String username, String cardNumber){
        if(listOfUsers.get(username)!=null){
            if(!listOfUsers.get(username).isBankEmployee()){
                BankClient client = (BankClient) listOfUsers.get(username);
                if(client.addCard(cardNumber))
                    return "success"; //card added
                else
                    return "unsuccessful";//card is not valid
            }else return "nbc"; //not bank client
        }
        return "none"; //user does not exist
    }

    public static String handleCommand(String command){
        String[] commandCode = command.split("\\s+");

        if(commandCode[COMMAND_POSITION].equals("1") && commandCode.length == 3){ //login
            if(authenticateByUsernamePassword(commandCode[USERNAME_POSITION], commandCode[PASSWORD_POSITION])==0) {
                return "employee";
            }else {
                if (authenticateByUsernamePassword(commandCode[USERNAME_POSITION], commandCode[PASSWORD_POSITION])==1)
                    return "client";
            }
            return "none";
        }

        if(commandCode[COMMAND_POSITION].equals("2") && commandCode.length == 6){ //register user
            if(authenticateByUsernamePassword(commandCode[USERNAME_POSITION], commandCode[PASSWORD_POSITION])==0){ //first authenticates that a bank employee wants to add new user
                //example command:
                //2 Admin Admin Pavel 123456 f/t

                if(commandCode[ARGUMENT3_POSITION].equals("t"))
                    if(addUser(commandCode[ARGUMENT1_POSITION], commandCode[ARGUMENT2_POSITION], true)==0) {
                        saveListOfUsers();
                        return "success"; //added successfully
                    }
                if(commandCode[ARGUMENT3_POSITION].equals("f"))
                    if(addUser(commandCode[ARGUMENT1_POSITION], commandCode[ARGUMENT2_POSITION], false)==0) {
                        saveListOfUsers();
                        return "success"; //added successfully
                    }

                    return "exists"; //username is redundant
            }else return "nbe"; //not bank employee
        }

        if(commandCode[COMMAND_POSITION].equals("3") && commandCode.length == 5){ //add card
            if(authenticateByUsernamePassword(commandCode[USERNAME_POSITION], commandCode[PASSWORD_POSITION])==0){ //first authenticates that a bank employee wants to add new user
                //example command:
                //3 Admin Admin Pavel 123456789101
                return addCard(commandCode[ARGUMENT1_POSITION], commandCode[ARGUMENT2_POSITION]);
            }else return "nbe"; //not bank employee
        }

        if(commandCode[COMMAND_POSITION].equals("4") && commandCode.length == 4){//tokenize card
            if(authenticateByUsernamePassword(commandCode[USERNAME_POSITION], commandCode[PASSWORD_POSITION])==1){//first authenticates that a bank client wants to tokenize card
                //example command:
                //4 Pavel pass123 123456789101
                return generateToken(commandCode[USERNAME_POSITION], commandCode[ARGUMENT1_POSITION]);
            }
        }

        if(commandCode[COMMAND_POSITION].equals("5") && commandCode.length == 4){//get card by token
            if(authenticateByUsernamePassword(commandCode[USERNAME_POSITION], commandCode[PASSWORD_POSITION])==1){//first authenticates that a bank client wants to tokenize card
                //example command:
                //5 Pavel pass123 101987654321
                return getCardByToken(commandCode[USERNAME_POSITION], commandCode[ARGUMENT1_POSITION]);
            }
        }

        return "Error";
    }
}
