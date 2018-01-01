package controllers;

import com.google.common.net.HttpHeaders;
import com.google.inject.Inject;
import conf.Definitions;
import controllers.form.LoginForm;
import controllers.secure.AccountsAuth;
import controllers.secure.NoLogin;
import models.accounts.User;
import models.accounts.UserStatus;
import play.Logger;
import play.cache.CacheApi;
import play.data.Form;
import play.i18n.Messages;
import play.libs.Json;
import play.mvc.*;
import redis.clients.jedis.exceptions.JedisConnectionException;
import services.GtSession;
import services.ResultJson;
import services.debug.DebugCreateGameData;
import services.user.GtUser;
import util.LoggerUtil;

import java.util.UUID;

public class Login extends Controller {
    @Inject
    CacheApi cacheApi;

    /**
     * クラス名
     */
    final private static String className = "Login";

    /**
     * ログイン画面
     */
    public Result index() {

        // method name
        final String methodName = "index";

        if (!DebugCreateGameData.getCreated()) {
            DebugCreateGameData.firstCreate();
        }
        if (!DebugCreateGameData.getUserCreated()) {
            DebugCreateGameData.firstUserCreate();
        }

        Http.Cookie cookie = request().cookie(Definitions.SESSION_KEY);
        LoggerUtil.info(className, methodName, LoggerUtil.Type.START, "ログイン開始");

        //cookieがない場合ログイン画面へ
        if (cookie == null) {
            LoggerUtil.info(className, methodName, LoggerUtil.Type.ACTION, "クッキー無し", "ログイン画面へ");
            return Results.ok(views.html.login_top.render());
        }


        Object cache = null;
        try {
            cache = cacheApi.get(cookie.value());
        } catch (JedisConnectionException e) {
            cacheApi.remove(cookie.value());
            response().discardCookie(Definitions.SESSION_KEY);
        }

        //キャッシュがない場合もログイン画面へ
        if (cache == null) {
            LoggerUtil.info(className, methodName, LoggerUtil.Type.ACTION, "cash");
            response().discardCookie(Definitions.SESSION_KEY);
            return Results.ok(views.html.login_top.render());
        }

        //キャッシュがあった場合
        if (cache instanceof GtSession) {
            LoggerUtil.info(className, methodName, LoggerUtil.Type.ACTION, "ゲームリストを表示");
            GtSession gtSession = (GtSession) cache;

            switch (gtSession.getUserStatus()) {
                case REGISTERED:
                    GtUser rtUser = new GtUser(gtSession);
                    return Results.redirect("/game");
                case PRE_REGISTRATION:
                    return Results.ok("メールを確認してください。");
                default:
                    cacheApi.remove(cookie.value());
                    response().discardCookie(Definitions.SESSION_KEY);

                    User user = User.getFind().byId(gtSession.getUserId());
                    if (user != null) {
                        user.setUserStatus(UserStatus.PRE_REGISTRATION);
                        user.update();
                    }
                    return Results.redirect("/");
            }
        } else {
            cacheApi.remove(cookie.value());
            response().discardCookie(Definitions.SESSION_KEY);

            return Results.redirect("/");
        }
    }

    /**
     * ログイン画面（認証済み）
     */
    @Security.Authenticated(AccountsAuth.class)
    public Result indexLogin() {

        LoggerUtil.info("Login", "indexLogin", LoggerUtil.Type.ACTION, "ログイン画面（認証済み）");
        GtSession GtSession = (GtSession) ctx().args.get(Definitions.SESSION_KEY);

        GtUser gtUser = new GtUser(GtSession);

        return Results.ok(views.html.translation_game_list.render(gtUser));
    }


    /**
     * ログイン処理
     */
    @Security.Authenticated(NoLogin.class)
    public Result doLogin() {
        // method name
        final String methodName = "doLogin";

        // ユーザエージェント
        String userAgent = request().getHeader(HttpHeaders.USER_AGENT);

        Form<LoginForm> form = Form.form(LoginForm.class).bindFromRequest();

        if (form.hasErrors()) {
            LoggerUtil.info(className, methodName, LoggerUtil.Type.ACTION, "ログイン処理のエラー");
            return Results.ok("ユーザーIDかパスワードが違います"); // TODO:ログイン失敗ページ
        }
        LoginForm lf = form.get();

        try {
            // ログイン処理を実行。
            GtUser gtUser = new GtUser(lf.mail, lf.pass, request());

            // セッションを設定する。
            AccountsAuth.registerLoginSession(ctx(), UUID.randomUUID().toString(), gtUser.getSession());

            Logger.debug("ログインに成功しました。 email : " + lf.mail + " , password : " + lf.pass);

            ResultJson resultJson = new ResultJson(
                    OK,
                    Messages.get(lang(), "/game") // TODO Messages
            );

            return Results.ok(resultJson.toJson());

        } catch (Exception e) {
            return Results.badRequest(Json.toJson(e)); // TODO:ログイン失敗ページ
        } catch (Error e) {
            e.printStackTrace();
            return Results.internalServerError();
        }
    }


    public Result logout() {
        AccountsAuth.unregisterLoginSession(ctx());
        return Results.redirect("/");
    }

}
