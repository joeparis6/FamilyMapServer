package model;

public class AuthToken {

    private String userAuthToken;
    private String userName;

    public AuthToken(String userAuthToken, String userName) {
        this.userAuthToken = userAuthToken;
        this.userName = userName;
    }

    public String getUserAuthToken() {
        return userAuthToken;
    }

    public void setUserAuthToken(String userAuthToken) {
        this.userAuthToken = userAuthToken;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        userName = userName;
    }
}
