package services.user.views;

import com.fasterxml.jackson.annotation.JsonInclude;
import models.contents.Game;

/**
 * Gameの表示用クラス
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ViewGame {

    /**
     * 公開ID（文字列）
     */
    private String publicId;

    /**
     * ゲーム名
     */
    private String name;
    /**
     * 概要
     */
    private String overview;

    /**
     * Version
     */
    private String version;

    public ViewGame(Game game) {
        this.publicId = game.getPublicId();
        this.name = game.getName();
        this.version = game.getVersion();
        this.overview = game.getOverview();
    }

    public String getPublicId() {
        return publicId;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public String getOverview() {
        return overview;
    }
}
