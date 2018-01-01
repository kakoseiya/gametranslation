package models;

import play.Logger;
import play.i18n.Lang;

/**
 * 種類の列挙型用インターフェース
 *
 * @author Kako Seiya
 * @version 1.0
 * @since 1.0
 */
public interface TypeInterface {

    /**
     * IDを取得します
     * @return ID番号
     */
    Integer getId();

    /**
     * Messagesからメッセージ文字列を取得します。
     * @return メッセージ文字列
     */
    String getMessage(Lang lang, String... values);

    static <E extends TypeInterface> E getType(Class<E> enumClass, int id){
        for(E e : enumClass.getEnumConstants()){
            if(e.getId().equals(id)){
                return e;
            }
        }
        Logger.error(enumClass.getName()+ " のfindで値が見つかりませんでした。" + enumClass.getName() + " を確認してください。：" + id);
        throw new IllegalArgumentException();
    }


}
