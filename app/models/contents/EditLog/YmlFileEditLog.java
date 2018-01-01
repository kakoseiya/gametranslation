package models.contents.EditLog;

import models.GtModel;
import models.contents.Document;
import models.contents.YmlFile;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity
public class YmlFileEditLog extends GtModel {

    /**
     * YmlFile_id
     */
    @NotNull
    @ManyToOne
    private YmlFile ymlFile;

    /**
     * Log
     */
    @NotNull
    @Column(columnDefinition = "text")
    private String log;

    /**
     * EbeanのFinder
     */
    private final static Find<Long, YmlFileEditLog> find = new Find<Long, YmlFileEditLog>() {
    };

    /**
     * EditLogのFinderを取得
     */
    public static Find<Long, YmlFileEditLog> getFind() {
        return find;
    }

    public YmlFile getYmlFile() {
        return ymlFile;
    }

    public void setYmlFile(YmlFile ymlFile) {
        this.ymlFile = ymlFile;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }
}
