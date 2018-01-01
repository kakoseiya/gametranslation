package controllers.form;

import play.data.validation.Constraints;
import util.StringUtil;

public class LoginForm {
    @Constraints.Required
    public String mail;

    @Constraints.Required
    public String pass;

    public String validate(){
        if(StringUtil.isEmailSyntax(this.mail))
            return null;
        return "メールアドレスが間違っています。";
    }
    public String getMail() {
        return mail;
    }
    public void setMail(String mail) {
        this.mail = mail;
    }
    public String getPass() {
        return pass;
    }
    public void setPass(String password) {
        this.pass = password;
    }
}
