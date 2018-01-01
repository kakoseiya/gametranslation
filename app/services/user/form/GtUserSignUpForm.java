package services.user.form;

import conf.Definitions;
import models.accounts.User;
import play.Logger;
import play.data.validation.ValidationError;
import play.i18n.Lang;
import services.GtFormAbstract;
import util.StringUtil;

import javax.swing.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

/**
 * 登録用のフォーム
 */
public class GtUserSignUpForm extends GtFormAbstract {

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
     * 誕生日
     */
    private String birthday;

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
        } else {
            if (User.userExist(mail)) {
                addValidationError("mail", "form.error.mail.userExist");
            }
        }
        if (pass == null || trimSpace(pass).length() == 0) {
            addValidationError("pass", "form.error.password.require");
        } else {
            Matcher matcher = Definitions.PASSWORD_PATTERN.matcher(pass);
            if (!matcher.find()) {
                addValidationError("pass", "form.error.password.pattern");
            }
        }
        if (!pass.equals(rePass)) {
            addValidationError("rePass", "form.error.password.notSame");
        }
        if (userName == null || trimSpace(userName).length() == 0) {
            addValidationError("userName", ""); // TODO : 何かエラーメッセージMessagesで
        }
        try {
            String[] birth = birthday.split("-");
            Integer intYear, intMonth, intDay;
            intYear = Integer.valueOf(birth[0]);
            intMonth = Integer.valueOf(birth[1]);
            intDay = Integer.valueOf(birth[2]);
            LocalDate.of(intYear, intMonth, intDay);

        } catch (DateTimeParseException | NumberFormatException | NullPointerException e) {
            Logger.warn(e.getMessage());
            addValidationError("year", "error.form.user.register.birth");
        }
        return validationErrorMap;
    }

    /**
     * 誕生日を取得します。
     *
     * @return 誕生日
     */
    public LocalDate getBirthDay() {
        try {
            String[] birth = birthday.split("-");
            Integer intYear, intMonth, intDay;
            intYear = Integer.valueOf(birth[0]);
            intMonth = Integer.valueOf(birth[1]);
            intDay = Integer.valueOf(birth[2]);
            LocalDate.of(intYear, intMonth, intDay);

            return LocalDate.of(intYear, intMonth, intDay);

        } catch (DateTimeParseException | NumberFormatException | NullPointerException e) {
            return null;
        }
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

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
}
