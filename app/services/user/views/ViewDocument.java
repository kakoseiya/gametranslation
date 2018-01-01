package services.user.views;

import com.fasterxml.jackson.annotation.JsonInclude;
import models.contents.Document;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ViewDocument {

    /**
     * 公開ID（文字列）
     */
    private String publicId;

    /**
     * Key Word
     */
    private String key;

    /**
     * 原文
     */
    private String origText;

    /**
     * 確定した翻訳
     */
    private String transText;

    /**
     * 翻訳（未確定の翻訳）
     */
    private String transGoogleText;

    /**
     * 翻訳済みかどうかのステータス
     */
    private Integer status;

    /**
     * YmlFile_publicId
     */
    private String ymlFileId;

    /**
     * View ゲーム
     */
    private ViewGame game;

    /**
     * 備考
     */
    private String remarks;

    public ViewDocument(Document document) {
        this.publicId = document.getPublicId();
        this.key = document.getKey();
        this.origText = document.getOrigText();
        this.transText = document.getTransText();
        this.transGoogleText = document.getTransGoogleText();
        this.status = document.getStatus();
        this.remarks=document.getRemarks();
        this.ymlFileId=document.getYmlFile().getPublicId();
        this.game=new ViewGame(document.getYmlFile().getGame());
    }

    public String getPublicId() {
        return publicId;
    }

    public String getKey() {
        return key;
    }

    public String getOrigText() {
        return origText;
    }

    public String getTransText() {
        return transText;
    }

    public String getTransGoogleText() {
        return transGoogleText;
    }

    public Integer getStatus() {
        return status;
    }

    public String getRemarks() {
        return remarks;
    }

    public String getYmlFileId() {
        return ymlFileId;
    }

    public ViewGame getGame() {
        return game;
    }
}
