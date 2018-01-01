package services.mail;

import play.i18n.Lang;

import java.util.List;

public class GtMailer implements GtMailerType {

    /**
     * 送信タイプ
     */
    protected GtMailerType type;

    /**
     * コンストラクタ
     *
     * @param type 動作タイプ
     */
    public GtMailer(GtMailerType type) {
        this.type = type;
    }

    @Override
    public MailUser getFrom() {
        return type.getFrom();
    }

    @Override
    public String getTitle() {
        return type.getTitle();
    }

    @Override
    public String getHtml() {
        return type.getHtml();
    }

    @Override
    public String getText() {
        return type.getText();
    }

    @Override
    public List<MailUser> getToList() {
        return type.getToList();
    }

    @Override
    public void send() {
        type.send();
    }

}
