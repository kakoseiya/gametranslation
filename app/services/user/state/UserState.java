package services.user.state;

import services.user.form.GtMyPageUserAdminForm;
import services.user.views.ViewUser;

import java.util.List;
import java.util.Set;

/**
 * ユーザーの基本操作を定義する
 */
public interface UserState {

    /**
     * ユーザ情報を取得します。
     *
     * @return ユーザ情報
     */
    ViewUser getAccountInformation() throws Exception;

    /**
     * パスワードを変更します。
     *
     * @param pass 変更するパスワード（平文）
     */
    void changePassword(String pass);


    /**
     * パスワードが現在設定されているものかどうかを確認します。
     *
     * @param password 確認するパスワード
     * @return 同じパスワードであれば true
     */
    boolean checkPassword(String password);


    /**
     * ログインのログを取得します。（システム設定のリミット件数）
     *
     * @param lang ログの取得言語
     * @return ログリスト
     */
    List<ViewUser> getLoginLog(String lang) throws Exception;


    /**
     * ユーザ氏名を変更します。
     *
     * @param name 設定するユーザ名
     */
    void changeName(String name);

    /**
     * メールアドレスを変更します。
     *
     * @param email 設定するメールアドレス
     */
    void changeEmail(String email);

    /**
     * 電話番号を変更します。
     *
     * @param mobileNumber 変更する電話番号
     */
    void changeMobile(String mobileNumber);

    /**
     * ユーザー情報を変更します。
     *
     * @param form フォーム
     */
    void changeInfo(GtMyPageUserAdminForm form);

//    /**
//     * アバター画像を追加します。
//     *
//     * ユーザ自身、または操作権限（システム管理者等）を持つユーザによってのみ実行されます。
//     *
//     * @param file    追加する画像データ。
//     */
//    void postAvatar(File file) throws IOException, RtUserIllegalActionException, RtIllegalActionException;
//
//    /**
//     * アバター画像を削除します。
//     *
//     * ユーザ自身、または操作権限（システム管理者等）を持つユーザによってのみ実行されます。
//     */
//    void deleteAvatar() throws IOException, RtUserIllegalActionException;

    /**
     * ライセンスに同意します。
     * <p>
     * ユーザ自身、または操作権限（システム管理者等）を持つユーザによってのみ実行されます。
     */
    void licenseAgree() throws Exception;

    /**
     * 退会手続きをします。
     */
    void delete() throws Exception;
}
