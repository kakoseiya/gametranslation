package models.contents;

import models.GtModel;
import models.TypeInterface;
import models.contents.EditLog.DocumentEditLog;
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
                        "key",
                        "ymlfile_id"
                }
        )
)
public class Document extends GtModel {

    /**
     * レコードの状態
     */
    public enum Status implements TypeInterface {
        /**
         * 翻訳完了状態
         */
        TRANSLATED(50, "translated"),

        /**
         * 翻訳未完了状態
         */
        IMPERFECT(0, "imperfect");

        Integer id;
        String message;

        Status(Integer id, String message) {
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
    }


    /**
     * 公開ID（文字列）
     */
    @NotNull
    @Column(unique = true)
    private String publicId;


    /**
     * YmlFile_id
     */
    @NotNull
    @JoinColumn(name = "ymlfile_id")
    @ManyToOne
    private YmlFile ymlFile;

    /**
     * Key
     */
    @NotNull
    private String key;

    /**
     * 原文
     */
    @NotNull
    @Column(columnDefinition = "text")
    private String origText;

    /**
     * 翻訳
     */
    @Column(columnDefinition = "text")
    private String transText;

    /**
     * 翻訳（Googleの翻訳）
     */
    @Column(columnDefinition = "text")
    private String transGoogleText;

    /**
     * 備考
     */
    @Column(columnDefinition = "text")
    private String remarks;

    /**
     * 翻訳済みかどうかのステータス
     */
    @NotNull
    private Integer status;

    /**
     * 編集中かどうか
     * 誰かが編集している間はtrueを返す
     */
    @NotNull
    private boolean editing;

    /**
     * EditLogのリレーション
     */
    @OneToMany
    private List<DocumentEditLog> editLogList = new ArrayList<>();

    @PrePersist
    public void init() {
        super.initModel();

        // 公開IDの設定
        if (publicId == null) {
            String id;
            while (publicId == null) {
                id = RandomStringUtils.randomAlphabetic(10);
                if (Document.getFind().where().eq("publicId", id).findCount() == 0) {
                    publicId = id;
                }
            }
        }
        editing = false;
    }

    public String getPublicId() {
        return publicId;
    }

    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }

    public YmlFile getYmlFile() {
        return ymlFile;
    }

    public void setYmlFile(YmlFile ymlFile) {
        this.ymlFile = ymlFile;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getOrigText() {
        return origText;
    }

    public void setOrigText(String origText) {
        this.origText = origText;
    }

    public String getTransText() {
        return transText;
    }

    public void setTransText(String transText) {
        this.transText = transText;
    }

    public String getTransGoogleText() {
        return transGoogleText;
    }

    public void setTransGoogleText(String transGoogleText) {
        this.transGoogleText = transGoogleText;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<DocumentEditLog> getEditLogList() {
        return editLogList;
    }

    public void setEditLogList(List<DocumentEditLog> editLogList) {
        this.editLogList = editLogList;
    }

    public boolean isEditing() {
        return editing;
    }

    public void setEditing(boolean editing) {
        this.editing = editing;
    }

    /**
     * EbeanのFinder
     */
    private final static Find<Long, Document> find = new Find<Long, Document>() {
    };

    /**
     * DocumentのFinderを取得
     */
    public static Find<Long, Document> getFind() {
        return find;
    }

}
