package models.accounts;

import models.TypeInterface;
import play.i18n.Lang;
import play.i18n.Messages;

public enum UserPrivileges implements TypeInterface {

    /**
     * システム管理者ユーザ
     * システム全体に対するスーパユーザー
     */
    SYSTEM_MANAGER(2000, "sper"),

    /**
     * ユーザ管理者ユーザ
     * ユーザーを管理して権限を与えることができる
     * 基本的にすべての編集ができるユーザー
     */
    ADMIN_MANAGER(20, "kannri"),

    /**
     * 一般ユーザ
     * 編集しかできない
     */
    NORMAL(10, "normal"),

    /**
     * ゲストユーザ
     * ログインしていないユーザー（１つしか存在しない）
     * 基本的に翻訳の編集のみ
     */
    GUEST(0, "guest");

    private Integer id;
    private String messageKey;

    UserPrivileges(Integer id, String messageKey) {
        this.id = id;
        this.messageKey = messageKey;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public String getMessage(Lang lang, String... values) {
        return Messages.get(lang, this.messageKey, (Object[]) values);
    }

    public static UserPrivileges getPrivilages(Integer id) {
        switch (id) {
            case 2000:
                return SYSTEM_MANAGER;
            case 20:
                return ADMIN_MANAGER;
            case 10:
                return NORMAL;
            case 0:
                return GUEST;
            default:
                return NORMAL;
        }

    }
}
