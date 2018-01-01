package models.accounts;

import models.GtModel;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity
public class Message extends GtModel {

    /**
     * メッセージの受信者
     * Userモデルで返す
     */
    @NotNull
    @JoinColumn(name = "user_id")
    @ManyToOne
    private User user;

    /**
     * メッセージの送信者
     * (公開鍵で検索する)
     */
    @NotNull
    private String senderPublicId;

    /**
     * メッセージの送信者名
     * (名前で表示させる用)
     */
    @NotNull
    private String senderName;

    /**
     * メッセージの件名
     */
    @NotNull
    private String subject;

    /**
     * メッセージの内容
     */
    @NotNull
    private String messageText;

    /**
     * メッセージが既読か否か
     */
    @NotNull
    private Boolean read;

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getSenderPublicId() {
        return senderPublicId;
    }

    public void setSenderPublicId(String senderPublicId) {
        this.senderPublicId = senderPublicId;
    }

    public String getMessage() {
        return messageText;
    }

    public void setMessage(String messageText) {
        this.messageText = messageText;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public Boolean getRead() {
        return read;
    }

    public void setRead(Boolean read) {
        this.read = read;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }
}
