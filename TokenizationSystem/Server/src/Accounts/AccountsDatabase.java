package Accounts;

import java.util.ArrayList;

public class AccountsDatabase {

    private static ArrayList<User> listOfUsers;

    public AccountsDatabase() {
        User Admin = new BankEmployee(true, "Admin", "Admin");
    }

    boolean addUser(User userToAdd, BankEmployee employee) { //only employees can add new users
        if(employee.isBankEmployee()){
            listOfUsers.add(userToAdd);
            return true;
        }else
            return false;
    }
}
