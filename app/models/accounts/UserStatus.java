package models.accounts;

import models.TypeInterface;
import play.i18n.Lang;
import play.i18n.Messages;

public enum UserStatus implements TypeInterface {
    /**
     *仮登録
     */
    PRE_REGISTRATION(100,"karitouroku"),

    /**
     * 登録
     */
    REGISTERED(200,"touroku");



    private Integer id;
    private String messageKey;

    UserStatus(Integer id, String messageKey){
        this.id = id;
        this.messageKey = messageKey;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public String getMessage(Lang lang, String... values){
        return Messages.get(lang, this.messageKey, (Object[]) values);
    }
}
