package services;

import com.fasterxml.jackson.annotation.JsonIgnore;
import play.data.validation.ValidationError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GtFormAbstract {

    /**
     * Validation Errors
     */
    @JsonIgnore
    protected Map<String, List<ValidationError>> validationErrorMap = null;

    /**
     * Error add
     */
    protected void addValidationError(String formElement, String errorMessageKey){
        if(validationErrorMap == null) validationErrorMap = new HashMap<>();
        ValidationError error = new ValidationError(formElement,errorMessageKey, new ArrayList<>());
        List<ValidationError> errorList = new ArrayList<>();
        errorList.add(error);
        validationErrorMap.put(formElement,errorList);
    }

    /**
     * 全角スペースもトリム
     * @param org 元の文字列
     * @return トリムされた文字列
     */
    @JsonIgnore
    protected static String trimSpace(String org){
        char[] value = org.toCharArray();
        int len = value.length;
        int st = 0;
        char[] val = value;

        while ((st < len) && (val[st] <= ' ' || val[st] == '　')) {
            st++;
        }
        while ((st < len) && (val[len - 1] <= ' ' || val[len - 1] == '　')) {
            len--;
        }

        return ((st>0) || (len<value.length)) ? org.substring(st,len):org;
    }
}
