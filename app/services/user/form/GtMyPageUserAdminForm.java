package services.user.form;

import conf.Definitions;
import models.accounts.User;
import play.Logger;
import play.data.validation.ValidationError;
import services.GtFormAbstract;
import util.StringUtil;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

/**
 * 登録用のフォーム
 */
public class GtMyPageUserAdminForm extends GtFormAbstract {

    /**
     * 公開鍵
     */
    private String publicId;

    /**
     * 登録用メールアドレス（重複は無効）
     */
    private String mail;

    /**
     * パスワード
     */
    private String pass;

    /**
     * パスワードの再入力
     */
    private String rePass;

    /**
     * ユーザーの名前
     */
    private String userName;

    /**
     * 電話番号
     */
    private String mobileNumber;


    /**
     * Form の Validation 用メソッド
     * passwordとrePasswordは一致していること。
     *
     * @return エラー
     */
    public Map<String, List<ValidationError>> validate() {

        if (!StringUtil.isEmailSyntax(mail)) {
            addValidationError("mail", "form.error.mail.syntax");
        }
        if (!(pass.matches(""))) {
            Matcher matcher = Definitions.PASSWORD_PATTERN.matcher(pass);
            if (!matcher.find()) {
                addValidationError("pass", "form.error.password.pattern");
            }
            if (!pass.equals(rePass)) {
                addValidationError("rePass", "form.error.password.notSame");
            }
        }
        if (userName == null || trimSpace(userName).length() == 0) {
            addValidationError("userName", ""); // TODO : 何かエラーメッセージMessagesで
        }
        if (publicId == null || trimSpace(publicId).length() == 0) {
            addValidationError("publicId", ""); // TODO : 何かエラーメッセージMessagesで
        }

        return validationErrorMap;
    }


    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getRePass() {
        return rePass;
    }

    public void setRePass(String rePass) {
        this.rePass = rePass;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPublicId() {
        return publicId;
    }

    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }
}
