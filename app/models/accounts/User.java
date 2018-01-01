package models.accounts;

import models.GtModel;
import models.TypeInterface;
import org.apache.commons.lang3.RandomStringUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class User extends GtModel {

    /**
     * ユーザーの名前
     */
    @NotNull
    private String userName;

    /**
     * 公開ID（文字列）
     */
    @NotNull
    @Column(unique = true)
    private String publicId;

    /**
     * パスワードハッシュ
     */
    @NotNull
    private String encPassword;

    /**
     * ユーザの権限.
     * UserPrivileges.idを設定します。
     */
    @NotNull
    private Integer userPrivileges;

    /**
     * ユーザの状態.
     * UserStatus.idを設定します。
     */
    @NotNull
    private Integer userStatus;

    /**
     * 誕生日
     */
    @NotNull
    private LocalDate birthDay;

    /**
     * 電話番号
     */
    @NotNull
    private String mobileNumber;

    /**
     * メールアドレス
     */
    @NotNull
    private String mail;

    /**
     * メッセージのリレーション
     */
    @OneToMany
    private List<Message> messageList = new ArrayList<>();

    /**
     * 永続化前の処理
     */
    @PrePersist
    public void init() {
        // デフォルトのユーザ権限
        Integer privileges = UserPrivileges.ADMIN_MANAGER.getId();
        // デフォルトのユーザ状態
        Integer status = UserStatus.REGISTERED.getId();

        // 初期値を設定
        initModel();
        userPrivileges = (userPrivileges == null) ? privileges : userPrivileges;
        userStatus = (userStatus == null) ? status : userStatus;

        // 公開IDの設定
        if (publicId == null) {
            String id;
            while (publicId == null) {
                id = RandomStringUtils.randomAlphabetic(10);
                if (User.getFind().where().eq("publicId", id).findCount() == 0) {
                    publicId = id;
                }
            }
        }
    }

    /* static */
    /**
     * Finder
     */
    final static private Find<Long, User> find = new Find<Long, User>() {
    };

    /**
     * UserのFindを取得します。
     *
     * @return UserのFind
     */
    public static Find<Long, User> getFind() {
        return find;
    }

    /**
     * メールアドレスがアクティブなユーザで登録されていないかどうか確認する
     *
     * @param email 確認するメールアドレス
     * @return 登録されていたら true, 登録されていなかったら false
     */
    static public Boolean userExist(String email) {

        int count = User.getFind()
                .where()
                .eq("mail", email)
                .eq("deleted", false)
                .findCount();

        return count > 0;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEncPassword() {
        return encPassword;
    }

    public void setEncPassword(String encPassword) {
        this.encPassword = encPassword;
    }

    public UserPrivileges getUserPrivileges() {
        return TypeInterface.getType(UserPrivileges.class, userPrivileges);
    }

    public void setUserPrivileges(UserPrivileges privileges) {
        this.userPrivileges = privileges.getId();
    }

    public UserStatus getUserStatus() {
        return TypeInterface.getType(UserStatus.class, userStatus);
    }

    public void setUserStatus(UserStatus status) {
        this.userStatus = status.getId();
    }

    public LocalDate getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(LocalDate birthDay) {
        this.birthDay = birthDay;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPublicId() {
        return publicId;
    }

    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }

    public List<Message> getMessageList() {
        return messageList;
    }
}
