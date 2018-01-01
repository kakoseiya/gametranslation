package services.mail;

import play.i18n.Lang;

import java.util.List;

public interface GtMailerType {

    /**
     * 送信者を取得します。
     *
     * @return メール送信者
     */
    MailUser getFrom();

    /**
     * 件名
     */
    String getTitle();

    /**
     * メールHTMLボディーを取得します。
     *
     * @return メール本文（HTML）
     */
    String getHtml();

    /**
     * メールテキストを取得します。
     *
     * @return メール本文
     */
    String getText();

    /**
     * 宛先リストを取得します。
     *
     * @return 宛先リスト
     */
    List<MailUser> getToList();

    /**
     * メールを送信します。
     */
    void send();
}
