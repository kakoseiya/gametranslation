package controllers.secure;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.net.HttpHeaders;
import conf.Definitions;
import eu.bitwalker.useragentutils.UserAgent;
import javassist.NotFoundException;
import models.accounts.UserPrivileges;
import play.Logger;
import play.cache.Cache;
import play.mvc.Http;
import play.mvc.Result;
import services.GtSession;
import util.LoggerUtil;

import java.util.UUID;

/**
 * SystemManeager（システムの管理者用）認証
 */
public class SystemManagerAuth extends AccountsAuth {
    final static private String className = "SystemManagerAuth";

    /**
     * コントローラへアクセスされたときに、認証されているか確認する
     *
     * @param context コンテキスト
     * @return ログインされているアクセスであればユーザーIDを返す、未ログインであればnull
     */
    @Override
    public String getUsername(Http.Context context) {
        final String methodName = "getUserName";

        String mail, pass, userAgent;
        Http.Cookie cookie;

        cookie = context.request().cookie(Definitions.SESSION_KEY);
        userAgent = context.request().getHeader(HttpHeaders.USER_AGENT);

        if (null == cookie) {
            // クッキーがない場合の処理
            // ユーザー名、パスワード、グループ（オプション）を引数にしたことを想定
            mail = context.request().getQueryString(Definitions.QUERY_LOGIN_USER);
            pass = context.request().getQueryString(Definitions.QUERY_LOGIN_PASS);

            if (mail != null && mail.length() > 0 && pass != null && pass.length() > 0) {

                try {
                    GtSession gtSession = GtSession.login(mail, pass, userAgent);

                    String sessionKey = UUID.randomUUID().toString();

                    if (!gtSession.getUserPrivileges().equals(UserPrivileges.SYSTEM_MANAGER)) {
                        return null;
                    }

                    context.args.put(Definitions.SESSION_KEY, gtSession);
                    return sessionKey;

                } catch (Error e) {
                    Logger.warn(e.getMessage());
                }
            }
            return null;
        } else {

            Object cache = Cache.get(cookie.value());
            if (cache != null && cache instanceof GtSession) {
                GtSession gtSession = (GtSession) cache;

                if (!UserPrivileges.SYSTEM_MANAGER.equals(gtSession.getUserPrivileges())) {
                    return null;
                }

                if (!gtSession.getUserAgent().equals(userAgent)) {

                    UserAgent ua1, ua2;
                    ua1 = UserAgent.parseUserAgentString(userAgent);
                    ua2 = UserAgent.parseUserAgentString(gtSession.getUserAgent());

                    if (ua1.getBrowser() != ua2.getBrowser()) {

                        LoggerUtil.debug(
                                className,
                                methodName,
                                LoggerUtil.Type.ACTION,
                                "不正なアクセス",
                                userAgent
                        );

                        // キャッシュから削除
                        AccountsAuth.unregisterLoginSession(context);
                        return null;
                    }
                }
                gtSession.setRequest(context.request());

                context.args.put(Definitions.SESSION_KEY, gtSession);
                registerLoginSession(context, cookie.value(), gtSession);

                return cookie.value();
            } else {
                Cache.remove(cookie.value());
                Logger.error("ユーザのセッションキャッシュのオブジェクト型に異常がありました。");
                return null;
            }
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

        return notFound();
    }
}
