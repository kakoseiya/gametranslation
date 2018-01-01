package models.accounts;

import com.google.common.net.HttpHeaders;
import models.GtModel;
import net.arnx.jsonic.JSON;
import play.mvc.Http;
import util.StringUtil;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static util.StringUtil.generatePasswordHash;

/**
 * 仮登録ユーザモデルクラス
 */
@Entity
public class PreRegist extends GtModel {

    /**
     * ハッシュ値
     */
    @Column(unique = true, length = 127)
    private String hash;

    /**
     * メールアドレス
     */
    @NotNull
    private String email;

    /**
     * ユーザーの名前
     */
    @NotNull
    private String userName;

    /**
     * 登録時のパスワードハッシュ文字列
     */
    @NotNull
    private String encPassword;

    /**
     * 有効期限切れかどうか
     */
    @NotNull
    private Boolean expired;

    /**
     * ユーザエージェント情報
     */
    @NotNull
    @Column(length = 2048)
    private String userAgent;

    /**
     * リモートIP
     */
    @NotNull
    @Column(length = 2048)
    private String remoteIp;

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

    @PrePersist
    public void init() {
        initModel();
        expired = (expired == null) ? false : expired;
        encPassword = (encPassword == null) ? "" : encPassword;

        hash = generatePasswordHash(email + UUID.randomUUID().toString());
        while (isExist(hash)) {
            hash = generatePasswordHash(email + UUID.randomUUID().toString());
        }
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEncPassword() {
        return encPassword;
    }

    public void setEncPassword(String encPassword) {
        this.encPassword = encPassword;
    }

    public Boolean getExpired() {
        return expired;
    }

    public void setExpired(Boolean expire) {
        this.expired = expire;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getRemoteIp() {
        return remoteIp;
    }

    public void setRemoteIp(String remoteIp) {
        this.remoteIp = remoteIp;
    }

    public void setUserInformation(Http.Request request) {
        this.userAgent = request.getHeader(HttpHeaders.USER_AGENT);
        this.remoteIp = request.remoteAddress().split(",")[0];
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    /**
     * ハッシュ値が利用されているか確認します。
     *
     * @param hash ハッシュ文字列
     * @return 存在していたら true
     */
    private boolean isExist(String hash) {
        return (find.where().eq("hash", hash).findCount() > 0);
    }

    /**
     * PreRegist の Find
     */
    final static Find<Long, PreRegist> find = new Find<Long, PreRegist>() {
    };

    /**
     * Find を取得します。
     *
     * @return Find
     */
    static public Find<Long, PreRegist> getFind() {
        return find;
    }


}
