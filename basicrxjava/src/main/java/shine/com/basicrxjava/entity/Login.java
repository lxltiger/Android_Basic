package shine.com.basicrxjava.entity;

/**
 * 登录实体类
 * {"loginName":"18217612547","isValidatjeesiteLogin":true,
 * "resultCode":"0000","icon":"","sessionid":"ef84be26dd9744b388de1be22349c442",
 * "userId":"f96f13c6e52a4b7abe1da9da578add52","resultMsg":"用户登录成功","username":"18217612547"}
 */

public class Login {

    private String loginName;
    private String isValidatjeesiteLogin;
    private String resultCode;
    private String icon;
    private String sessionid;
    private String userId;
    private String resultMsg;
    private String username;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String isValidatjeesiteLogin() {
        return isValidatjeesiteLogin;
    }

    public void setValidatjeesiteLogin(String validatjeesiteLogin) {
        isValidatjeesiteLogin = validatjeesiteLogin;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getSessionid() {
        return sessionid;
    }

    public void setSessionid(String sessionid) {
        this.sessionid = sessionid;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "Login{" +
                "loginName='" + loginName + '\'' +
                ", isValidatjeesiteLogin='" + isValidatjeesiteLogin + '\'' +
                ", resultCode='" + resultCode + '\'' +
                ", icon='" + icon + '\'' +
                ", sessionid='" + sessionid + '\'' +
                ", userId='" + userId + '\'' +
                ", resultMsg='" + resultMsg + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
