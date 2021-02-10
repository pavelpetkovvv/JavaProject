package Accounts;

public class BankClient extends User{

    Card[] card;

    public BankClient(boolean isBankEmployee, String userName, String password, Card[] card) {
        super(isBankEmployee, userName, password);
        this.card = card;
    }
}
