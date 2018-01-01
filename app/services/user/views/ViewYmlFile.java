package services.user.views;

import com.fasterxml.jackson.annotation.JsonInclude;
import models.contents.YmlFile;
import services.user.GtUserDocument;

import java.math.BigDecimal;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ViewYmlFile {

    /**
     * 公開ID（文字列）
     */
    private String publicId;

    /**
     * Fileの名前
     */
    private String name;

    /**
     * Game_id
     */
    private ViewGame game;

    /**
     * Fileの概要
     */
    private String summary;

    /**
     * 翻訳完了の割合
     */
    private BigDecimal rate;

    /**
     * 言語
     */
    private String lang;

    /**
     * Documentリスト
     */
    private  List<ViewDocument> documents;

    public ViewYmlFile(YmlFile ymlFile){
        GtUserDocument gtUserDocument =new GtUserDocument();

        this.publicId=ymlFile.getPublicId();
        this.name=ymlFile.getName();
        this.summary=ymlFile.getSummary();
        this.game=new ViewGame(ymlFile.getGame());
        this.documents= gtUserDocument.listDocument(ymlFile.getPublicId());
        this.lang=ymlFile.getLang().getMessage();

        float S=0,trans=0;
        for(int i=0;i<this.documents.size();i++){
            S++;
            if(this.documents.get(i).getStatus()>30){
                trans++;
            }
        }
        trans=trans/S*100;
        BigDecimal bd=new BigDecimal(trans);
        this.rate=bd.setScale(1,BigDecimal.ROUND_HALF_UP);

    }

    public String getPublicId() {
        return publicId;
    }

    public String getName() {
        return name;
    }

    public ViewGame getGame() {
        return game;
    }

    public List<ViewDocument> getDocuments() {
        return documents;
    }

    public String getSummary() {
        return summary;
    }

    public String getLang() {
        return lang;
    }

    public BigDecimal getRate() {
        return rate;
    }

}
