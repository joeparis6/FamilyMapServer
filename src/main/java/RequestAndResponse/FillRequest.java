package RequestAndResponse;

public class FillRequest {

    private String userName;
    private String generations;

    public FillRequest(String userName, String generations) {
        this.userName = userName;
        this.generations = generations;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setGenerations(String generations) {
        this.generations = generations;
    }

    public String getUserName() {
        return userName;
    }

    public String getGenerations() {
        return generations;
    }
}
