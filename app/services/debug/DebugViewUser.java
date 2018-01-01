package services.debug;

import models.accounts.User;
import models.contents.Game;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class DebugViewUser {

    /**
     * ユーザーの名前
     */
    private String userName;

    /**
     * パスワードハッシュ
     */
    private String encPassword;

    /**
     * ユーザの権限.
     * UserPrivileges.idを設定します。
     */
    private Integer userPrivileges;

    /**
     * ユーザの状態.
     * UserStatus.idを設定します。
     */
    private Integer userStatus;

    /**
     * 誕生日
     */
    private LocalDate birthDay;

    /**
     * 電話番号
     */
    private String mobileNumber;

    /**
     * メールアドレス
     */
    private String mail;

    public DebugViewUser(User user) {
        this.userName = user.getUserName();
        this.encPassword = user.getEncPassword();
        this.birthDay = user.getBirthDay();
        this.mail = user.getMail();
        this.mobileNumber = user.getMobileNumber();
        this.userPrivileges = user.getUserPrivileges().getId();
        this.userStatus = user.getUserStatus().getId();
    }

    public String getUserName() {
        return userName;
    }

    public String getEncPassword() {
        return encPassword;
    }

    public Integer getUserPrivileges() {
        return userPrivileges;
    }

    public Integer getUserStatus() {
        return userStatus;
    }

    public LocalDate getBirthDay() {
        return birthDay;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public String getMail() {
        return mail;
    }

}
