package services.user.state;

import javassist.NotFoundException;
import play.data.validation.ValidationError;
import services.GtFormAbstract;
import services.user.views.ViewDocument;

import java.util.List;
import java.util.Map;

public interface DocumentState {

    class PostDocument extends GtFormAbstract {
        private String publicId;
        private String ymlFileId;
        private String key;
        private String origText;
        private String transGoogleText;
        private String transText;
        private String remarks;
        private String status;

        public Map<String, List<ValidationError>> validate() {
            if (ymlFileId == null || trimSpace(ymlFileId).length() == 0) {
                addValidationError("ymlFileId", ""); // TODO : 何かエラーメッセージMessagesで
            } else if (key == null || trimSpace(key).length() == 0) {
                addValidationError("key", ""); // TODO : 何かエラーメッセージMessagesで
            } else if (origText == null || trimSpace(origText).length() == 0) {
                addValidationError("origText", ""); // TODO : 何かエラーメッセージMessagesで
            } else if (transGoogleText == null || trimSpace(transGoogleText).length() == 0) {
                addValidationError("transGoogleText", ""); // TODO : 何かエラーメッセージMessagesで
            } else if (transText == null || trimSpace(transText).length() == 0) {
                addValidationError("transText", ""); // TODO : 何かエラーメッセージMessagesで
            } else if (remarks == null || trimSpace(remarks).length() == 0) {
                addValidationError("remarks", ""); // TODO : 何かエラーメッセージMessagesで
            }
            return validationErrorMap;
        }

        public String getPublicId() {
            return publicId;
        }

        public void setPublicId(String publicId) {
            this.publicId = publicId;
        }

        public String getYmlFileId() {
            return ymlFileId;
        }

        public void setYmlFileId(String ymlFileId) {
            this.ymlFileId = ymlFileId;
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

        public String getTransGoogleText() {
            return transGoogleText;
        }

        public void setTransGoogleText(String transGoogleText) {
            this.transGoogleText = transGoogleText;
        }

        public String getTransText() {
            return transText;
        }

        public void setTransText(String transText) {
            this.transText = transText;
        }

        public String getRemarks() {
            return remarks;
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }


    /**
     * YmlFileのリストを取得する
     */
    List<ViewDocument> listDocument(String id);

    /**
     * YmlFileのデータを取得する
     */
    ViewDocument getDocument(String id) throws NotFoundException;

    /**
     * YmlFileのデータの削除フラグを立てる
     */
    void deleteDocument(String id) throws NotFoundException;

    /**
     * YmlFileのデータを保存する
     */
    void saveDocument(PostDocument form) throws NotFoundException;

}
