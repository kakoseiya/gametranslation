package controllers.api;

import controllers.secure.AccountsAuth;
import play.Logger;
import play.data.Form;
import play.data.FormFactory;
import play.i18n.Messages;
import play.libs.Json;
import play.mvc.*;
import services.GtSession;
import services.ResultJson;
import services.user.GtUser;
import services.user.GtUserYmlFile;
import services.user.state.YmlFileState;
import util.LoggerUtil;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import java.io.File;

import static conf.Definitions.SESSION_KEY;


public class YmlFileWebApi extends Controller {
    @Inject
    FormFactory formFactory;

    final static private String className = "YmlFileWebApi";

    @Security.Authenticated(AccountsAuth.class)
    public Result page(String id){
        final String methodName="ymlFile_page";

        GtSession user = (GtSession) ctx().args.get(SESSION_KEY);
        GtUser gtUser = new GtUser(user);

        return Results.ok(views.html.translation_game_yml.render(gtUser,id));

    }

    /**
     * YmlFile一覧を取得します。
     *
     * @return ResultJson
     */
    @Security.Authenticated(AccountsAuth.class)
    public Result list(String id) {
        final String methodName = "ymlFile_list";

        try {
            GtUserYmlFile gtUserYmlFile = new GtUserYmlFile();

            ResultJson resultJson = new ResultJson(
                    OK,
                    gtUserYmlFile.listYmlFile(id)
            );

            return Results.ok(resultJson.toJson());
        } catch (Exception e) {
            System.out.print("error:");
            System.out.println(e);
            return Results.badRequest("NG");
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
            GtUserYmlFile gtUserYmlFile = new GtUserYmlFile();

            ResultJson resultJson = new ResultJson(
                    OK,
                    gtUserYmlFile.getYmlFile(publicId)
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
            GtUserYmlFile gtUserYmlFile = new GtUserYmlFile();

            gtUserYmlFile.deleteYmlFile(publicId);

            ResultJson resultJson = new ResultJson(
                    OK,
                    Messages.get(lang(), "Fileが削除されました。")
            );

            return Results.ok(resultJson.toJson());
        } catch (Exception e) {
            return Results.badRequest();
        }
    }

    /**
     * YmlFileを保存します。
     *
     * @return ResultJson
     */
    @Security.Authenticated(AccountsAuth.class)
    public Result post() {
        final String methodName = "post";
        try {
            GtUserYmlFile gtUserYmlFile = new GtUserYmlFile();

            Form<YmlFileState.PostYmlFile> form = formFactory.form(YmlFileState.PostYmlFile.class).bindFromRequest();

            YmlFileState.PostYmlFile postYmlFile = form.get();
            gtUserYmlFile.saveYmlFile(postYmlFile);

            ResultJson resultJson = new ResultJson(
                    OK,
                    Messages.get(lang(), "Gameが登録されました。") // TODO Messages
            );

            return Results.ok(resultJson.toJson());
        } catch (Exception e) {
            return Results.badRequest();
        }
    }

    @Security.Authenticated(AccountsAuth.class)
    public Result postYmlFile(String gamePublicId){
        final String methodName = "postYmlFile";
        try {
            GtUserYmlFile gtUserYmlFile = new GtUserYmlFile();

            /* POSTデータを取得します。*/
            Http.MultipartFormData<File> body = request().body().asMultipartFormData();

            Http.MultipartFormData.FilePart<File> ymlFile = body.getFile("YmlFile");

            gtUserYmlFile.toDocument(ymlFile.getFile(),ymlFile.getFilename(),gamePublicId);

            Form<YmlFileState.PostYmlFile> form = formFactory.form(YmlFileState.PostYmlFile.class).bindFromRequest();

            YmlFileState.PostYmlFile postYmlFile = form.get();
            gtUserYmlFile.saveYmlFile(postYmlFile);


            ResultJson resultJson = new ResultJson(
                    OK,
                    Messages.get(lang(), "Gameが登録されました。") // TODO Messages
            );

            return Results.ok(resultJson.toJson());
        } catch (Exception e) {
            return Results.badRequest();
        }
    }

}
