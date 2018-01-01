package services.user;

import com.avaje.ebean.Ebean;
import models.accounts.PreRegist;
import models.accounts.User;
import models.accounts.UserPrivileges;
import models.accounts.UserStatus;
import play.Logger;
import play.mvc.Http;
import services.GtSession;
import services.mail.GtMailer;
import services.mail.type.PreRegisterUser;
import services.mail.type.PreRegisterUserDone;
import services.user.form.GtMyPageUserAdminForm;
import services.user.form.GtUserSignUpForm;
import services.user.state.UserState;
import services.user.views.ViewUser;
import util.LoggerUtil;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import static models.accounts.User.userExist;
import static util.StringUtil.generatePasswordHash;


public class GtUser implements UserState {

    /**
     * ユーザのレコードID
     */
    private Long userId;

    /**
     * ユーザー
     */
    private User user;

    /**
     * クラス名
     */
    final private static String className = "GtUser";

    /**
     * コンストラクタ
     */
    private GtUser() {
    }

    /**
     * セッション
     */
    protected GtSession session;

    public GtUser(GtSession session) {
        this.session = session;
        this.userId = session.getUserId();
    }

    public GtUser(User user) {
        this.session = GtSession.get(user);
        this.userId = user.getId();
    }

    public GtSession getSession() {
        return session;
    }

    public Long getUserId() {
        return userId;
    }

    /**
     * メールアドレスとパスワード平文からログインする
     *
     * @param email メールアドレス
     * @param pass  パスワード
     */
    public GtUser(String email, String pass, Http.Request request) {
        this.session = GtSession.loginWithLog(email, pass, request);
        this.userId = this.session.getUserId();
    }

    /**
     * ユーザの仮登録をします。
     *
     * @param form    仮登録用のフォーム
     * @param request 仮登録を実行したクライアント情報
     */
    public static String preRegister(GtUserSignUpForm form, Http.Request request) throws Exception {
        final String methodName = "preRegister";

        String mail, pass, userName, mobileNumber;
        LocalDate birthDay;
        mail = form.getMail();
        pass = form.getPass();
        userName = form.getUserName();
        mobileNumber = form.getMobileNumber();
        birthDay = form.getBirthDay();

        if (userExist(mail)) {
            throw new Exception();
        }

        try {
            Ebean.beginTransaction();

            PreRegist preRegist = PreRegist.getFind()
                    .where()
                    .eq("email", mail)
                    .eq("deleted", false)
                    .eq("expired", false)
                    .findUnique();

            if (preRegist != null) {
                preRegist.update();
            } else {
                // 仮登録レコードを作成します。
                preRegist = new PreRegist();
                preRegist.setEmail(mail);
                preRegist.setEncPassword(generatePasswordHash(pass));
                preRegist.setUserInformation(request);
                preRegist.setBirthDay(birthDay);
                preRegist.setUserName(userName);
                preRegist.setMobileNumber(mobileNumber);
                preRegist.save();
                preRegist.refresh();
            }

            GtMailer gtMailer = new GtMailer(new PreRegisterUser(preRegist));
            gtMailer.send();

            Ebean.commitTransaction();
            return preRegist.getHash();

        } catch (Exception e) {
            Ebean.rollbackTransaction();

        } finally {
            Ebean.endTransaction();
        }
        return methodName;
    }

    /**
     * 仮登録の情報を本登録にします。
     *
     * @param hash    仮登録のメール確認用ハッシュ
     * @param request 操作するクライアント情報
     */
    public static void confirmPreRegister(String hash, Http.Request request) {
        String methodName = "confirmPreRegister";

        try {
            Ebean.beginTransaction();
            PreRegist preRegist = PreRegist.getFind()
                    .where()
                    .eq("hash", hash)
                    .eq("deleted", false)
                    .eq("expired", false)
                    .findUnique();

            if (preRegist == null) {
                throw new Error();
            }

            /* ユーザのメールアドレスとして登録されていないか確認します。 */
            if (userExist(preRegist.getEmail())) {
                preRegist.setExpired(true);
                Ebean.commitTransaction();
                throw new Error();
            }

            Date now = new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 48);

            if (preRegist.getModifiedDate().after(now)) {
                preRegist.setExpired(true);
                preRegist.update();
                Ebean.commitTransaction();

                throw new Error();

            } else {

                // 確認完了
                GtUser gtUser = GtUser.generate(
                        preRegist.getEmail(),
                        preRegist.getEncPassword(),
                        preRegist.getUserName(),
                        preRegist.getBirthDay(),
                        preRegist.getMobileNumber(),
                        null
                );

                User user = gtUser.getUser();
                preRegist.setExpired(true);

                Ebean.commitTransaction();

                GtMailer gtMailer = new GtMailer(new PreRegisterUserDone(user, preRegist.getEmail()));
                gtMailer.send();

            }

        } catch (Exception e) {
            e.printStackTrace();
            throw e;

        } finally {
            Ebean.endTransaction();
        }
    }

    /**
     * ユーザ情報を新規登録します。
     *
     * @param email        新規登録するメールアドレス
     * @param encPass      ログイン用のハッシュ化されたパスワード
     * @param userName     ユーザー名
     * @param birthDay     誕生日
     * @param mobileNumber 携帯番号
     * @param privileges   作成するユーザのシステムにおけるユーザ権限
     * @return 新規作成された GtUser オブジェクト
     */
    public static GtUser generate(
            String email, String encPass, String userName, LocalDate birthDay, String mobileNumber, UserPrivileges privileges) {
        GtUser gtUser = new GtUser();
        gtUser.create(email, encPass, userName, birthDay, mobileNumber, privileges);

        return gtUser;

    }

    private void create(
            String email, String encPass, String userName, LocalDate birthDay, String mobileNumber, UserPrivileges privileges) {
        String methodName = "create";

        /* メールアドレスが使われていないか確認する。*/
        if (userExist(email)) {
            // 登録されているので例外
            throw new Error();
        }
        try {
            /* ユーザを作る。 */
            User user = new User();
            if (privileges != null) {
                user.setUserPrivileges(privileges);
            } else {
                user.setUserPrivileges(UserPrivileges.NORMAL);
            }
            user.setEncPassword(encPass);
            user.setUserStatus(UserStatus.REGISTERED);
            user.setUserName(userName);
            user.setMail(email);
            user.setBirthDay(birthDay);
            user.setMobileNumber(mobileNumber);
            user.save();
            user.refresh();

            this.session = GtSession.get(user);


        } catch (Exception e) {
            throw new Error();
        } finally {
        }
    }

    /**
     * ユーザーを取得する
     *
     * @return User
     */
    public User getUser() {
        return User.getFind().byId(this.session.getUserId());
    }


    @Override
    public ViewUser getAccountInformation() throws Exception {

        return new ViewUser(getUser());
    }

    @Override
    public void changePassword(String pass) {
        if (!checkPassword(pass)) {
            User user = getUser();
            user.setEncPassword(generatePasswordHash(pass));
            user.save();
            user.refresh();
        }
    }

    @Override
    public boolean checkPassword(String password) {
        return getUser().getEncPassword().matches(generatePasswordHash(password));
    }

    @Override
    public List<ViewUser> getLoginLog(String lang) throws Exception {
        return null;
    }

    @Override
    public void changeMobile(String mobileNumber) {
        if (!getUser().getUserName().matches(mobileNumber)) {
            User user = getUser();
            user.setMobileNumber(mobileNumber);
            user.save();
            user.refresh();
        }
    }

    @Override
    public void changeName(String name) {
        if (!getUser().getUserName().matches(name)) {
            User user = getUser();
            user.setUserName(name);
            user.save();
            user.refresh();
        }
    }

    @Override
    public void changeEmail(String email) {
        if (!userExist(email) && !getUser().getMail().matches(email)) {
            User user = getUser();
            user.setMail(email);
            user.save();
            user.refresh();
        }
    }

    @Override
    public void changeInfo(GtMyPageUserAdminForm form) {
        String methodName = "changeInfo";
        try {
            Ebean.beginTransaction();
            changeEmail(form.getMail());
            changeName(form.getUserName());
            if (!(form.getPass() == null)) changePassword(form.getPass());
            changeMobile(form.getMobileNumber());
            Ebean.commitTransaction();
        } catch (Exception e) {
            LoggerUtil.error(className, methodName, LoggerUtil.Type.ERROR, "changeInfo", e);
        } finally {
            Ebean.endTransaction();
        }
    }

    @Override
    public void licenseAgree() throws Exception {

    }

    @Override
    public void delete() throws Exception {

    }
}
