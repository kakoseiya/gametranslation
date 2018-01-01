package conf;

import play.Play;

import java.util.regex.Pattern;

public class Definitions {

    /**
     * Login用のセッション名
     */
    final static public String QUERY_LOGIN_USER = "email";
    final static public String QUERY_LOGIN_PASS = "password";

    /**
     * クッキーにおけるセッション名
     */
    final static public String SESSION_KEY = Play.application().configuration().getString("app.sessionName", "GT_ACCOUNTS");

    /**
     * セッション有効時間
     */
    final static public int SESSION_TIME = Play.application().configuration().getInt("app.sessionTimeOut", 3000);

    /**
     * パスワードの構文パターン
     */
    final static public Pattern PASSWORD_PATTERN = Pattern.compile("^[A-Za-z0-9\\-.+@_~]{6,30}$");

    /**
     * システムメールアドレス（返信不可）
     */
    final static public String NO_REPLY_MAIL = Play.application().configuration().getString("app.mail.no_reply", "no-reply@harudays.com");

    /**
     * アプリケーションの動作URL
     */
    final static public String USER_MANAGEMENT_SYSTEM_BASE_URL = Play.application().configuration().getString(
            "app.system.baseUrl",
            "http://localhost:9000"
    );

    /**
     * メール件名のプレフィックス
     */
    final static public String MAIL_TITLE_PREFIX = Play.application().configuration().getString("mail.prefix", "");
}
