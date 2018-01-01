package controllers.api;

import conf.Definitions;
import controllers.secure.AccountsAuth;
import javassist.bytecode.stackmap.TypeData;
import play.data.Form;
import play.data.FormFactory;
import play.i18n.Messages;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import play.mvc.Security;
import services.GtSession;
import services.ResultJson;
import services.user.GtUser;
import services.user.GtUserGame;
import services.user.state.GameState;
import util.LoggerUtil;

import javax.inject.Inject;

/**
 * GameデータのWebApi
 */
public class GameWebApi extends Controller {

    @Inject
    FormFactory formFactory;

    final static private String className = "GameWebApi";

    @Security.Authenticated(AccountsAuth.class)
    public Result page() {
        final String methodName = "translation_game_document";

        LoggerUtil.debug(className, methodName, LoggerUtil.Type.DONE, ctx());
        GtSession GtSession = (GtSession) ctx().args.get(Definitions.SESSION_KEY);
        GtUser gtUser = new GtUser(GtSession);

        return Results.ok(views.html.translation_game_list.render(gtUser));
    }

    /**
     * Game一覧を取得します。
     *
     * @return ResultJson
     */
    @Security.Authenticated(AccountsAuth.class)
    public Result list() {
        final String methodName = "translation_game_list";

        try {
            GtUserGame gtUserGame = new GtUserGame();

            ResultJson resultJson = new ResultJson(
                    OK,
                    gtUserGame.listGame()
            );

            return Results.ok(resultJson.toJson());
        } catch (Exception e) {
            return Results.badRequest();
        }

    }

    /**
     * Gameの情報を取得します。
     *
     * @param publicId 公開鍵
     * @return ResultJson
     */
    @Security.Authenticated(AccountsAuth.class)
    public Result get(String publicId) {
        final String methodName = "get";

        try {
            GtUserGame gtUserGame = new GtUserGame();

            ResultJson resultJson = new ResultJson(
                    OK,
                    gtUserGame.getGame(publicId)
            );

            return Results.ok(resultJson.toJson());
        } catch (Exception e) {
            return Results.badRequest();
        }
    }

    /**
     * Gameを削除する。
     *
     * @param publicId 公開鍵
     * @return ResultJson
     */
    @Security.Authenticated(AccountsAuth.class)
    public Result delete(String publicId) {
        final String methodName = "delete";
        try {
            GtUserGame gtUserGame = new GtUserGame();
            gtUserGame.deleteGame(publicId);

            ResultJson resultJson = new ResultJson(
                    OK,
                    gtUserGame.listGame()
            );

            return Results.ok(resultJson.toJson());
        } catch (Exception e) {
            return Results.badRequest();
        }
    }

    /**
     * Game一覧を取得します。
     *
     * @return ResultJson
     */
    @Security.Authenticated(AccountsAuth.class)
    public Result post() {
        final String methodName = "post";
        try {
            GtUserGame gtUserGame = new GtUserGame();

            Form<GameState.PostGame> form = formFactory.form(GameState.PostGame.class).bindFromRequest();
            if (form.hasErrors()) {
                LoggerUtil.error(className,methodName, LoggerUtil.Type.ERROR,"Post エラー");

                return Results.badRequest(Json.toJson(form.errors()));
            }

            GameState.PostGame postGame = form.get();
            gtUserGame.saveGame(postGame);

            ResultJson resultJson = new ResultJson(
                    OK,
                    Messages.get(lang(), "Gameが登録されました。") // TODO Messages
            );

            return Results.ok(resultJson.toJson());
        } catch (Exception e) {
            return Results.badRequest(Json.toJson(e));
        }
    }

}
