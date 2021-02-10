package Accounts;

abstract class User {
    private boolean isBankEmployee;
    private String userName;
    private String password;

    User(boolean isBankEmployee, String userName, String password) {
        this.isBankEmployee = isBankEmployee;
        this.userName = userName;
        this.password = password;
    }

    public boolean isBankEmployee() {
        return isBankEmployee;
    }

    public void setBankEmployee(boolean bankEmployee) {
        isBankEmployee = bankEmployee;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
