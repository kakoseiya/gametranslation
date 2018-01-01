package controllers.secure;

import conf.Definitions;
import play.Logger;
import play.cache.Cache;
import play.mvc.Http;
import play.mvc.Result;
import services.GtSession;

public class NoLogin extends AccountsAuth{
    /**
     * コントローラへアクセスされたときに、認証されているか確認する
     *
     * @param context コンテキスト
     * @return ログインしていなければ正常。ログイン済みであればnull
     */
    @Override
    public String getUsername(Http.Context context) {

        Http.Cookie cookie = context.request().cookie(Definitions.SESSION_KEY);

        if(cookie == null)
            return "NoLogin";

        Object cache = Cache.get(cookie.value());

        if(cache != null && cache instanceof GtSession){
            return null;
        } else{
            Logger.error("ユーザのセッションキャッシュのオブジェクト型に異常がありました。");
        }

        AccountsAuth.unregisterLoginSession(context);
        return "NoLogin";

    }


    /**
     * ログインしているときの処理。
     *
     * @param context コンテキスト
     * @return 振り分けられたResult
     */
    @Override
    public Result onUnauthorized(Http.Context context) {

        return redirect(controllers.routes.Login.index());

    }


}
