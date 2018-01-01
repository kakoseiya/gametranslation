package models.contents;


import models.GtModel;
import org.apache.commons.lang3.RandomStringUtils;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"name", "version"}))
public class Game extends GtModel {

    /**
     * 公開ID（文字列）
     */
    @NotNull
    @Column(unique = true)
    private String publicId;

    /**
     * Gameの名前
     */
    @NotNull
    @Column(length = 256)
    private String name;

    /**
     * Version
     */
    @NotNull
    private String version;

    /**
     * Gameの概要
     */
    @Column(columnDefinition = "text")
    private String overview;

    /**
     * YmlFileのリレーション
     */
    @OneToMany
    private List<YmlFile> ymlFileList = new ArrayList<>();

    @PrePersist
    public void init() {
        super.initModel();

        // 公開IDの設定
        if (publicId == null) {
            String id;
            while (publicId == null) {
                id = RandomStringUtils.randomAlphabetic(10);
                if (Game.getFind().where().eq("publicId", id).findCount() == 0) {
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

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public List<YmlFile> getYmlFileList() {
        return ymlFileList;
    }

    public void setYmlFileList(List<YmlFile> ymlFileList) {
        this.ymlFileList = ymlFileList;
    }

    /**
     * EbeanのFinder
     */
    private final static Find<Long, Game> find = new Find<Long, Game>() {
    };

    /**
     * GameのFinderを取得
     */
    public static Find<Long, Game> getFind() {
        return find;
    }


}
