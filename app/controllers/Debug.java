package controllers;

import models.accounts.User;
import models.accounts.UserPrivileges;
import models.accounts.UserStatus;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import services.ResultJson;
import services.debug.DebugUser;
import util.LoggerUtil;

import java.time.LocalDate;

import static util.StringUtil.generatePasswordHash;

public class Debug extends Controller {
    final static String classname="Debug";

    public Result viewLoginTop() {
        return Results.ok(views.html.login_top.render());
    }

    public Result viewLoginNew() {
        return Results.ok(views.html.login_signup.render());
    }

    public Result userCreate() {
        String pass;
        pass = "01234567";
        User user;
        user = new User();
        user.setBirthDay(LocalDate.of(1994, 4, 14));
        user.setMail("kako@gmail.com");
        user.setEncPassword(generatePasswordHash(pass));
        user.setMobileNumber("09000000000");
        user.setUserName("kakoseiya");
        user.setUserPrivileges(UserPrivileges.SYSTEM_MANAGER);
        user.setUserStatus(UserStatus.REGISTERED);
        user.save();
        user.refresh();
        LoggerUtil.info("Debug", "create", LoggerUtil.Type.ACTION, "userCreate");
        LoggerUtil.info("Debug", "create", LoggerUtil.Type.ACTION, user.getEncPassword());

        return Results.redirect("/login");
    }

    /**
     * Userの一覧を取得します。
     *
     * @return ResultJson
     */
    public Result list() {
        final String methodName = "list_user";

        try {
            DebugUser debugUser = new DebugUser();

            ResultJson resultJson = new ResultJson(
                    OK,
                    debugUser.listUser()
            );

            return Results.ok(resultJson.toJson());
        } catch (Exception e) {
            LoggerUtil.error(classname,methodName, LoggerUtil.Type.ERROR,e);
            return Results.badRequest("NG");
        }
    }

    public Result viewSignUpPregisted(String hash) {
        return Results.ok(views.html.debug.debug_login_signup_pregisted.render(hash));
    }


}
