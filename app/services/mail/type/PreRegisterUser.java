package services.mail.type;

import conf.Definitions;
import models.accounts.PreRegist;
import play.Play;
import play.i18n.Lang;
import play.i18n.Messages;
import play.libs.Akka;
import play.libs.mailer.Email;
import play.libs.mailer.MailerClient;
import scala.concurrent.duration.Duration;
import services.mail.GtMailerType;
import services.mail.MailUser;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class PreRegisterUser implements GtMailerType {

    /**
     * メールタイトル
     */
    final static private String titleMessageKey =  "mail.title.preRegister";

    /**
     * 送信者
     */
    final static private String from = "system.mail.from.support.name";


    /**
     * メールクライアント
     */
    private MailerClient mailerClient;

    /**
     * 招待先
     */
    private PreRegist preRegist;

    /**
     * コンストラクタ
     */
    public PreRegisterUser(PreRegist preRegist) {
        this.mailerClient = Play.application().injector().instanceOf(MailerClient.class);
        this.preRegist = preRegist;
    }

    @Override
    public MailUser getFrom() {
        return new MailUser(
                Messages.get(from),
                Definitions.NO_REPLY_MAIL
        );
    }

    @Override
    public String getTitle() {
        return Definitions.MAIL_TITLE_PREFIX + Messages.get(titleMessageKey);
    }

    @Override
    public String getHtml() {
        return null;
    }

    @Override
    public String getText() {
        return views.html.mailTemplate.mail_txt_preRegister
                .render(
                        this.preRegist.getHash()
                )
                .body().trim();
    }

    @Override
    public List<MailUser> getToList() {
        return null;
    }

    @Override
    public void send() {

        Akka.system().scheduler().scheduleOnce(
                Duration.create(10, TimeUnit.MILLISECONDS),
                () -> {

                    Email email = new Email();

                    email.addTo(preRegist.getEmail());
                    email.setFrom(getFrom().toString());
                    email.setSubject(getTitle());
                    email.setBodyText(getText());
                    email.addBcc(Definitions.NO_REPLY_MAIL);

                    mailerClient.send(email);
                },
                Akka.system().dispatcher()
        );
    }


}
