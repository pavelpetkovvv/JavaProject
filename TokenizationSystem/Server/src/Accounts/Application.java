package Accounts;

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
        listOfUsers = new HashMap<>();
        User Admin = new BankEmployee(true, "Admin", "Admin");
        listOfUsers.put("Admin", Admin);
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

    boolean addCard(Card card, User user, BankEmployee employee){
        if(!user.isBankEmployee()){
            return ((BankClient) user).card.add(card);
            //check if card does already exist
        }
        return false;
    }

    public static String handleCommand(String command){
        String[] commandCode = command.split(" "); //to make work fo many spaces

        if(commandCode[COMMAND_POSITION].equals("1")){ //login
            if(authenticateByUsernamePassword(commandCode[USERNAME_POSITION], commandCode[PASSWORD_POSITION])==0) {
                return "employee";
            }else {
                if (authenticateByUsernamePassword(commandCode[USERNAME_POSITION], commandCode[PASSWORD_POSITION])==1)
                    return "client";
            }
            return "none";
        }

        if(commandCode[COMMAND_POSITION].equals("2")){ //register user
            if(authenticateByUsernamePassword(commandCode[USERNAME_POSITION], commandCode[PASSWORD_POSITION])==0){ //first authenticates that a bank employee wants to add new user
                //example command:
                //2 Admin Admin Pavel 123456 f/t

                if(commandCode[ARGUMENT3_POSITION].equals("t"))
                    if(addUser(commandCode[ARGUMENT1_POSITION], commandCode[ARGUMENT2_POSITION], true)==0)
                        return "success"; //added successfully
                if(commandCode[ARGUMENT3_POSITION].equals("f"))
                    if(addUser(commandCode[ARGUMENT1_POSITION], commandCode[ARGUMENT2_POSITION], false)==0)
                        return "success"; //added successfully

                    return "exists"; //username is redundant
            }else return "nbe"; //not bank employee
        }




        return "Error";
    }
}
