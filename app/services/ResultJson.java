package services;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import net.arnx.jsonic.JSONHint;
import play.libs.Json;


/**
 * フロント用返却JSON型
 *
 * @author  KAKO Seiya
 * @version 1.0
 * @since   1.0
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResultJson {

    /**
     * ステータスコード
     */
    protected Integer code;

    /**
     * メッセージ
     */
    protected String  message;

    /**
     * データ
     */
    protected Object data;

    /**
     * データの総数
     */
    protected Integer total;


    /**
     * コンストラクタ
     * @param code    ステータスコード
     * @param message メッセージ
     */
    public ResultJson(int code, String message){
        this.code    = code;
        this.message = message;
    }

    public ResultJson(int code, Object data){
        this.code = code;
        this.data = data;
    }

    public ResultJson(int code, Object data, Integer total){
        this.code = code;
        this.data = data;
        this.total = total;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }

    @JSONHint(ignore = true)
    @JsonIgnore
    public JsonNode toJson(){

        return Json.toJson(this);
    }

    public Integer getTotal() {
        return total;
    }
}
