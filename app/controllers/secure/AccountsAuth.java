package controllers.secure;

import com.google.common.net.HttpHeaders;
import conf.Definitions;
import eu.bitwalker.useragentutils.UserAgent;
import models.accounts.UserStatus;
import play.Logger;
import play.cache.Cache;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;
import services.GtSession;
import services.user.GtUser;

import java.util.UUID;

public class AccountsAuth extends Security.Authenticator {
    final static private String className = "AccountsAuth";


    /**
     * コントローラへアクセスされたときに、認証されているか確認する
     *
     * @param context コンテキスト
     * @return ログインされているアクセスであればユーザーIDを返す、未ログインであればnull
     */
    @Override
    public String getUsername(Http.Context context) {
        final String methodName = "getUsername";

        String mail,
                pass,
                userAgent;

        // ユーザーエージェントの設定
        userAgent = context.request().getHeader(HttpHeaders.USER_AGENT);

        // クッキーがあるかどうか
        Http.Cookie cookie = context.request().cookie(Definitions.SESSION_KEY);


        if (null == cookie) {
            // クッキーがない場合の処理
            // ユーザー名、パスワードを引数にする
            mail = context.request().getQueryString(Definitions.QUERY_LOGIN_USER);
            pass = context.request().getQueryString(Definitions.QUERY_LOGIN_PASS);

            try {
                if (mail != null && mail.length() > 0 && pass != null && pass.length() > 0) {
                    // ログインする（ログなし）
                    GtSession gtSession = GtSession.login(mail, pass, userAgent);


                    // ランダムなセッションキーを用意します。
                    String sessionKey = UUID.randomUUID().toString();

                    if (gtSession.getUserStatus().equals(UserStatus.REGISTERED)) {

                        context.args.put(Definitions.SESSION_KEY, gtSession);

                        return sessionKey;
                    }
                }

                return null;

            } catch (Error e) {
                Logger.warn(e.getMessage());
            }

            return null;
        } else {

            // クッキーがある場合の処理
            try {
                Object cache = Cache.get(cookie.value());
                if (cache != null && cache instanceof GtSession) {
                    GtSession gtSession = (GtSession) cache;

                    if (!gtSession.getUserAgent().equals(userAgent)) {

                        UserAgent ua1, ua2;
                        ua1 = UserAgent.parseUserAgentString(userAgent);
                        ua2 = UserAgent.parseUserAgentString(gtSession.getUserAgent());
                        if (ua1.getBrowser() != ua2.getBrowser()) {

                            // キャッシュから削除
                            AccountsAuth.unregisterLoginSession(context);
                            return null;
                        }
                    }
                    gtSession.setRequest(context.request());

                    // コンテキストに追加
                    context.args.put(Definitions.SESSION_KEY, gtSession);
                    registerLoginSession(context, cookie.value(), gtSession);

                    if (gtSession.getUserStatus().equals(UserStatus.REGISTERED)) {
                        return cookie.value();
                    }
                    return null;
                } else {
                    Cache.remove(cookie.value());
                    Logger.error("ユーザのセッションキャッシュのオブジェクト型に異常がありました。");
                    return null;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    /**
     * ログイン情報を更新する
     *
     * @param context   コンテキスト
     * @param userToken ログインに紐付くトークン
     * @param userInfo  ユーザー情報クラス
     */
    public static void registerLoginSession(Http.Context context, String userToken, GtSession userInfo) {
        int timeOut = Definitions.SESSION_TIME;
        userInfo.setUserAgent(context.request().getHeader(HttpHeaders.USER_AGENT));

        // アプリケーションキャッシュの有効期限を今から60*timeOut秒にセットす
        Cache.set(userToken, userInfo, 60 * timeOut);
        context.response().setCookie(Definitions.SESSION_KEY, userToken, 60 * 60 * 24 * 30);

//        context.session().touch(Definitions.SESSION_KEY, userToken);
    }


    /**
     * ログアウトさせる
     *
     * @param context コンテキスト
     */
    public static void unregisterLoginSession(Http.Context context) {
        try {
            Http.Cookie cookie = context.request().cookie(Definitions.SESSION_KEY);

//            String sessionKey = context.session().get(Definitions.SESSION_KEY);
            if (cookie == null) return;
            // アプリケーションキャッシュからログイン状態を削除する
            Cache.remove(cookie.value());
            // ログインクッキーを削除させる
//            context.session().remove(Definitions.SESSION_KEY);
            context.response().discardCookie(Definitions.SESSION_KEY);
        } catch (Exception e) {
        }
    }

    /**
     * ログインしていないときのアクセスに対する動作。
     * 管理モードではログイン画面、公開モードでは404 Not Found
     *
     * @param context コンテキスト
     * @return 振り分けられたResult
     */
    @Override
    public Result onUnauthorized(Http.Context context) {

        if (context.args.containsKey(Definitions.SESSION_KEY)) {
            Object cache = context.args.get(Definitions.SESSION_KEY);
            if (cache != null && cache instanceof GtUser) {
                GtUser cacheUser = (GtUser) cache;

            } else {
                Logger.error("ユーザのセッションキャッシュのオブジェクト型に異常がありました。");
            }
        }

        unregisterLoginSession(context);
        return null;
    }


}
