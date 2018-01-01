package controllers;

import com.google.inject.Inject;
import controllers.form.LoginForm;
import controllers.secure.NoLogin;
import play.Logger;
import play.data.Form;
import play.data.FormFactory;
import play.i18n.Messages;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import play.mvc.Security;
import services.ResultJson;
import services.user.GtUser;
import services.user.form.GtUserSignUpForm;

import java.util.ArrayList;
import java.util.List;

/**
 * サインアップ（本登録）用
 */
public class SignUp extends Controller {

    /**
     * class name
     */
    final static private String className = "Signup";

    @Inject
    private FormFactory formFactory;

    /**
     * サインアップ用API
     */
    @Security.Authenticated(NoLogin.class)
    public Result preRegist() {

        return Results.ok(views.html.login_signup.render());
    }

    /**
     * 仮登録のPOST用コントローラー
     */
    @Security.Authenticated(NoLogin.class)
    public Result doPreRegist() {
        try {

            Form<GtUserSignUpForm> form = formFactory.form(GtUserSignUpForm.class).bindFromRequest();
            if (form.hasErrors()) {

                return Results.badRequest(form.errorsAsJson());
            }
            GtUserSignUpForm sf = form.get();


            //サインアップ
            String messageKey = GtUser.preRegister(
                    sf,
                    request()
            );

            ResultJson resultJson = new ResultJson(
                    OK,
                    Messages.get(lang(), messageKey) // TODO Messages
            );

            return Results.ok(resultJson.toJson());

        } catch (Exception e) {
            e.printStackTrace();

            ResultJson resultJson = new ResultJson(
                    BAD_REQUEST,
                    Messages.get(lang(), e.toString()) // TODO Messages
            );

            return Results.badRequest(resultJson.toJson());
        }
    }

    /**
     * 仮登録確認メールからの登録処理
     *
     * @param hash 確認用のハッシュ文字列
     */
    public Result confirmEmail(String hash) {

        // method name
        final String methodName = "confirmEmail";

        try {
            GtUser.confirmPreRegister(hash, request());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return Results.redirect("/login");
    }
}
