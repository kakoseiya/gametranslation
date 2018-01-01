package util;

import org.apache.commons.codec.digest.DigestUtils;

public final class StringUtil {

    private static final String SECRET_KEY = "gametranslation";

    private static final String hashSecureKeyString = "gametranslation";

    public static String generatePasswordHash(String password){
        LoggerUtil.info("StringUtil","hash", LoggerUtil.Type.ACTION,password);
        return DigestUtils.sha256Hex(password + hashSecureKeyString);
    }

    /**
     * 電子メールの構文か調べる
     *
     * @param emailString 電子メールアドレス文字列
     * @return true：正しい構文　false：間違っている構文
     */
    public static boolean isEmailSyntax(String emailString) {
        String syntax = "^[a-zA-Z0-9!#$%&'_`/=~\\*\\+\\-\\?\\^\\{\\|\\}]+(\\.[a-zA-Z0-9!#$%&'_`/=~\\*\\+\\-\\?\\^\\{\\|\\}]+)*+(.*)@[a-zA-Z0-9][a-zA-Z0-9\\-]*(\\.[a-zA-Z0-9\\-]+)+$";
        return emailString.matches(syntax);
    }
}
