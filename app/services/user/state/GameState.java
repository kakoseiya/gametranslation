package services.user.state;

import javassist.NotFoundException;
import services.GtFormAbstract;
import services.user.views.ViewGame;

import java.util.List;
import java.util.Map;

public interface GameState {


    class PostGame extends GtFormAbstract {
        private String publicId;
        private String title;
        private String version;

        public Map<String, List<play.data.validation.ValidationError>> validate() {
            if (title == null || trimSpace(title).length() == 0) {
                addValidationError("title", ""); // TODO : 何かエラーメッセージMessagesで
            }
            if (version == null || trimSpace(version).length() == 0) {
                addValidationError("version", ""); // TODO : 何かエラーメッセージMessagesで
            }

            return validationErrorMap;

        }

        public String getPublicId() {
            return publicId;
        }

        public void setPublicId(String id) {
            this.publicId = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }
    }


    /**
     * Gameのリストを取得する
     */
    List<ViewGame> listGame();

    /**
     * Gameのデータを取得する
     */
    ViewGame getGame(String id) throws NotFoundException;

    /**
     * Gameのデータの削除フラグを立てる
     */
    void deleteGame(String id) throws NotFoundException;

    /**
     *Gameのデータを保存する
     */
    void saveGame(PostGame form) throws NotFoundException;


}
