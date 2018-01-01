package services.user.state.document;

import javassist.NotFoundException;
import models.contents.Document;
import models.contents.YmlFile;
import services.user.state.DocumentState;
import services.user.views.ViewDocument;
import util.LoggerUtil;


import java.util.ArrayList;
import java.util.List;

public class DocumentAdmin implements DocumentState {
    final static private String className = "DocumentAdmin";

    /**
     * コンストラクタ
     */
    public DocumentAdmin() {

    }

    @Override
    public List<ViewDocument> listDocument(String id) {
        YmlFile ymlFile = YmlFile.getFind().where()
                .eq("publicId", id)
                .eq("deleted", false)
                .findUnique();

        List<Document> list = Document.getFind().where()
                .eq("deleted", false)
                .eq("ymlfile_id", ymlFile.getId())
                .findList();

        List<ViewDocument> viewList = new ArrayList<>();
        for (Document document : list) {
            viewList.add(new ViewDocument(document));
        }
        return viewList;
    }

    @Override
    public ViewDocument getDocument(String id) throws NotFoundException {
        Document document = Document.getFind().where()
                .eq("publicId", id)
                .eq("deleted", false)
                .findUnique();

        if (document == null)
            throw new NotFoundException("対象のスニペットが見つかりませんでした。");

        return new ViewDocument(document);
    }

    @Override
    public void deleteDocument(String id) throws NotFoundException {
        Document document = Document.getFind().where()
                .eq("publicId", id)
                .eq("deleted", false)
                .findUnique();

        if (document == null)
            throw new NotFoundException("対象のDocumentデータが見つかりませんでした。");

        document.setDeleted(true);
        document.save();
    }

    @Override
    public void saveDocument(DocumentState.PostDocument form) throws NotFoundException {
        final String methodName = "save_document";
        Document document;
        YmlFile ymlFile;
        if (form.getPublicId() == null) {
            document = new Document();
            ymlFile = YmlFile.getFind().where()
                    .eq("publicId", form.getYmlFileId())
                    .eq("deleted", false)
                    .findUnique();
            document.setYmlFile(ymlFile);
            LoggerUtil.debug(className, methodName, LoggerUtil.Type.ACTION, document);
        } else {
            document = Document.getFind().where()
                    .eq("publicId", form.getPublicId())
                    .eq("deleted", false)
                    .findUnique();
            if (document == null)
                throw new NotFoundException("対象のGameデータが見つかりませんでした。");
        }
        if (Integer.parseInt(form.getStatus()) == 50) {
            document.setStatus(Document.Status.TRANSLATED.getId());
        } else {
            document.setStatus(Document.Status.IMPERFECT.getId());
        }
        document.setKey(form.getKey());
        document.setOrigText(form.getOrigText());
        document.setTransGoogleText(form.getTransGoogleText());
        document.setTransText(form.getTransText());
        document.setRemarks(form.getRemarks());
        document.save();
        document.refresh();

    }
}
