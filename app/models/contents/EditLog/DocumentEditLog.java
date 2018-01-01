package models.contents.EditLog;

import models.GtModel;
import models.contents.Document;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity
public class DocumentEditLog extends GtModel {

    /**
     * Document_id
     */
    @NotNull
    @ManyToOne
    private Document document;

    /**
     * Log
     */
    @NotNull
    @Column(columnDefinition = "text")
    private String log;

    /**
     * EbeanのFinder
     */
    private final static Find<Long, DocumentEditLog> find = new Find<Long, DocumentEditLog>() {
    };

    /**
     * EditLogのFinderを取得
     */
    public static Find<Long, DocumentEditLog> getFind() {
        return find;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

}
