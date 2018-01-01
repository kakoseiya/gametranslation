package services;

import models.accounts.User;
import models.accounts.UserPrivileges;
import models.accounts.UserStatus;
import play.mvc.Http;

import static util.StringUtil.generatePasswordHash;

public class GtSession {

    /**
     * ユーザID
     */
    private Long userId;

    /**
     * ユーザーステータス
     */
    private UserStatus userStatus;

    /**
     * ユーザ権限
     */
    private UserPrivileges userPrivileges;

    /**
     * ユーザー名
     */
    private String userName;

    /**
     * ユーザエージェント
     */
    private String userAgent;

    /**
     * ユーザのリクエスト情報
     */
    private transient Http.Request request;

    /**
     * ユーザのアクセスIP
     */
    private String remoteIp;

    private GtSession() {
    }

    /**
     * ユーザー情報から初期化
     *
     * @param user ユーザー情報
     */
    private GtSession(User user) {
        this.setUser(user);
    }


    public static GtSession loginWithLog(String email, String pass, Http.Request request) {
        try {
            // アクセス元のユーザーエージェント
            String userAgent = request.getHeader(Http.HeaderNames.USER_AGENT);

            GtSession session = GtSession.login(email, pass, userAgent);

            try {
                // アクセス元のIPアドレスを設定。
                String[] ip = request.getHeader(Http.HeaderNames.X_FORWARDED_FOR).split(",");
                session.setRemoteIp(ip[0]);

            } catch (Exception e) {
                session.setRemoteIp("");
            }

            session.request = request;
            return session;

        } catch (Exception e) {
            return null;
        }
    }

    /**
     * ログイン状態のセッションを作成する
     *
     * @param email     　ログインするユーザーのメールアドレス
     * @param pass      　ログインするユーザーのパスワード
     * @param userAgent 　ログインするユーザーのクライアントリクエスト情報
     * @return セッション情報
     */
    public static GtSession login(String email, String pass, String userAgent) {
        String encPass = generatePasswordHash(pass);
        return loginEncPass(email, encPass, userAgent);
    }

    /**
     * ログイン状態のセッションを作成する
     *
     * @param email     　ログインするユーザーのメールアドレス
     * @param encPass   　ログインするユーザーのパスワード（データベース格納のハッシュ値）
     * @param userAgent 　ログインするユーザーのクライアントリクエスト情報
     * @return セッション情報
     */
    public static GtSession loginEncPass(String email, String encPass, String userAgent) {

        User user = User.getFind().where()
                .eq("encPassword", encPass)
                .eq("mail", email)
                .eq("deleted", false)
                .findUnique();

        GtSession gtSession = new GtSession(user);
        gtSession.userAgent = userAgent;

        return gtSession;
    }

    public UserPrivileges getUserPrivileges() {
        return userPrivileges;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public Http.Request getRequest() {
        return request;
    }

    public Long getUserId() {
        return userId;
    }

    public UserStatus getUserStatus() {
        return userStatus;
    }

    public String getRemoteIp() {
        return remoteIp;
    }

    public static GtSession get(User user) {
        return new GtSession(user);
    }

    public void setUser(User user) {
        this.userStatus = user.getUserStatus();
        this.userId = user.getId();
        this.userName = user.getUserName();
        this.userPrivileges = user.getUserPrivileges();
        this.remoteIp = "";
        this.userAgent = "";
    }

    public void setUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }

    public void setUserPrivileges(UserPrivileges userPrivileges) {
        this.userPrivileges = userPrivileges;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public void setRequest(Http.Request request) {
        this.request = request;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setRemoteIp(String remoteIp) {
        this.remoteIp = remoteIp;
    }
}
