package models.contents;

import com.avaje.ebean.Model;
import models.GtModel;
import models.TypeInterface;
import org.apache.commons.lang3.RandomStringUtils;
import play.i18n.Lang;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(
        uniqueConstraints = @UniqueConstraint(
                columnNames = {
                        "name",
                        "game_id"
                }
        )
)
public class YmlFile extends GtModel {

    /**
     * 言語のステータス
     */
    public enum Language implements TypeInterface {

        ENGLISH(1, "english");

        Integer id;
        String message;

        Language(Integer id, String message) {
            this.id = id;
            this.message = message;
        }

        @Override
        public Integer getId() {
            return this.id;
        }

        @Override
        public String getMessage(Lang lang, String... values) {
            return this.message;
        }

        public String getMessage() {
            return this.message;
        }

    }

    /**
     * 公開ID（文字列）
     */
    @NotNull
    @Column(unique = true)
    private String publicId;

    /**
     * Fileの名前
     */
    @NotNull
    @Column(length = 256)
    private String name;

    /**
     * Fileの概要
     */
    @Column(length = 256)
    private String summary;

    /**
     * Game_id
     */
    @NotNull
    @JoinColumn(name = "game_id")
    @ManyToOne
    private Game game;

    /**
     * 原文の言語
     */
    @NotNull
    private Integer lang;

    /**
     * Documentのリレーション
     */
    @OneToMany
    private List<Document> documentList = new ArrayList<>();

    @PrePersist
    public void init() {
        super.initModel();

        // 公開IDの設定
        if (publicId == null) {
            String id;
            while (publicId == null) {
                id = RandomStringUtils.randomAlphabetic(10);
                if (YmlFile.getFind().where().eq("publicId", publicId).findCount() == 0) {
                    publicId = id;
                }
            }
        }
    }

    public String getPublicId() {
        return publicId;
    }

    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Language getLang() {
        switch (lang) {
            case 1:
                return Language.ENGLISH;
            default:
                return null;
        }


    }

    public void setLang(Integer lang) {
        this.lang = lang;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public List<Document> getDocumentList() {
        return documentList;
    }

    public void setDocumentList(List<Document> documentList) {
        this.documentList = documentList;
    }

    /**
     * EbeanのFinder
     */
    private final static Model.Find<Long, YmlFile> find = new Model.Find<Long, YmlFile>() {
    };

    /**
     * YmlFileのFinderを取得
     */
    public static Find<Long, YmlFile> getFind() {
        return find;
    }

}
