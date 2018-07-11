package shine.com.basicrxjava.entity;

public class User {

    public final String username;
    public final String password;
    public final String verifyCod;
    public final String loginType;

    public User(String username, String password, String verifyCod, String loginType) {
        this.username = username;
        this.password = password;
        this.verifyCod = verifyCod;
        this.loginType = loginType;
    }
}
