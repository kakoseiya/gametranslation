package services.mail;

import models.accounts.User;
import play.i18n.Lang;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MailUser {

    /**
     * 宛先名
     */
    private String name;

    /**
     * 宛先メールアドレス
     */
    private String mail;


    /**
     * メール送信者（送信先）オブジェクトを作成します。
     * @param name 名前
     * @param mail メールアドレス
     */
    public MailUser(String name, String mail){
        this.name = name;
        this.mail = mail;

    }


    /**
     * 名前を取得します。
     * @return 名前
     */
    public String getName() {
        return name;
    }

    /**
     * 名前を設定します。
     * @param name 設定する名前
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * メールアドレスを取得します。
     * @return メールアドレス
     */
    public String getMail() {
        return mail;
    }

    /**
     * メールアドレスを設定します。
     * @param mail 設定するメールアドレス。
     */
    public void setMail(String mail) {
        this.mail = mail;
    }


    /**
     * Email用の宛先文字列を取得します。
     */
    @Override
    public String toString(){
        return getName() + " <" + getMail() + ">";
    }

    /**
     * HashSet用のクラス
     * @param obj 比較するクラス
     * @return
     */
    @Override
    public boolean equals(Object obj){
        if(obj instanceof MailUser) {
            MailUser user = (MailUser) obj;
            return (this.name + this.mail).equals(user.getName() + user.getMail());
        }
        return false;
    }

    @Override
    public int hashCode(){
        return name.hashCode() + mail.hashCode();
    }

}
