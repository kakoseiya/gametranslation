package models;


import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.WhenModified;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * GameTranslation
 *
 * @author KAKO Seiya
 * @version 1.0
 * @since 1.0
 */
@MappedSuperclass
public class GtModel extends Model {

    /**
     * 通し番号
     */
    @Id
    protected Long id;

    /**
     * 削除されたかどうか
     */
    @NotNull
    protected Boolean deleted;

    /**
     * 登録日時
     */
    @CreatedTimestamp
    protected Date createdDate;

    /**
     * 更新日時
     */
    @WhenModified
    protected Date modifiedDate;

    /**
     * IDを取得します。
     * @return ID
     */
    public Long getId() {
        return id;
    }

    /**
     * IDを設定します。
     * @param id 設定するID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 登録日時を取得します。
     * @return 登録日時
     */
    public Date getCreatedDate() {
        return createdDate;
    }

    /**
     * 登録日時を設定します。
     * @param createdDate 設定する登録日時
     */
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    /**
     * 更新日時を取得します。
     * @return 登録日時
     */
    public Date getModifiedDate() {
        return modifiedDate;
    }

    /**
     * 更新日時を設定します。
     * @param modifiedDate 設定する更新日時
     */
    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    /**
     * 削除されているかどうか
     * @return 削除されていたら true
     */
    public boolean isDeleted() {
        return (deleted == null) ? false : deleted;
    }

    /**
     * 削除フラグを設定します。
     * @param deleted 設定するフラグ
     */
    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    /**
     * 初期化メソッド
     */
    protected void initModel(){
        deleted = (deleted == null) ? false : deleted;
    }
}
