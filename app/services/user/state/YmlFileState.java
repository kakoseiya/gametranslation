package services.user.state;

import javassist.NotFoundException;
import play.data.validation.ValidationError;
import services.GtFormAbstract;
import services.user.views.ViewYmlFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.List;
import java.util.Map;

public interface YmlFileState {

    class PostYmlFile extends GtFormAbstract {
        private String id;
        private String fileName;
        private String gamePublicId;
        private String summary;

        public Map<String, List<ValidationError>> validate() {
            if (fileName == null || trimSpace(fileName).length() == 0) {
                addValidationError("fileName", ""); // TODO : 何かエラーメッセージMessagesで
            }
            return validationErrorMap;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public String getGamePublicId() {
            return gamePublicId;
        }

        public void setGamePublicId(String gamePublicId) {
            this.gamePublicId = gamePublicId;
        }

        public String getSummary() {
            return summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }
    }


    /**
     * YmlFileのリストを取得する
     */
    List<ViewYmlFile> listYmlFile(String id);

    /**
     * YmlFileのデータを取得する
     */
    ViewYmlFile getYmlFile(String id) throws NotFoundException;

    /**
     * YmlFileのデータの削除フラグを立てる
     */
    void deleteYmlFile(String id) throws NotFoundException;

    /**
     * YmlFileのデータを保存する
     */
    void saveYmlFile(PostYmlFile form) throws NotFoundException;

    /**
     * YmlFileのFileデータをドキュメント（Document）に直す
     */
    void toDocument(File file, String fileName, String gamePublicId) throws NotFoundException;


}
