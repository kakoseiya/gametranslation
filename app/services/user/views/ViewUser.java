package services.user.views;

import com.fasterxml.jackson.annotation.JsonInclude;
import models.accounts.User;

import java.time.LocalDate;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ViewUser {

    /**
     * 公開ユーザーID（Long型）
     */
    private String publicUserId;

    /**
     * ユーザー名
     */
    private String userName;

    /**
     * パスワード
     */
    private String pass;

    /**
     * ユーザー権限
     */
    private Integer userPrivileges;

    /**
     * ユーザの状態
     */
    private Integer userStatus;

    /**
     * 削除されたかどうか
     */
    protected Boolean deleted;

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

    public ViewUser(User user){
        this.publicUserId=user.getPublicId();
        this.userName=user.getUserName();
        this.userPrivileges=user.getUserPrivileges().getId();
        this.userStatus=user.getUserStatus().getId();
        this.birthDay=user.getBirthDay();
        this.mobileNumber=user.getMobileNumber();
        this.mail=user.getMail();
        this.deleted=user.isDeleted();
        this.pass="●●●●●●●●";
    }

    public String getPublicUserId() {
        return publicUserId;
    }

    public String getUserName() {
        return userName;
    }

    public Integer getUserPrivileges() {
        return userPrivileges;
    }

    public Integer getUserStatus() {
        return userStatus;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public String getPass() {
        return pass;
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
