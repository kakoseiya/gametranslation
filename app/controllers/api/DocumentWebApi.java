package controllers.api;

import controllers.secure.AccountsAuth;
import play.Logger;
import play.data.Form;
import play.data.FormFactory;
import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import play.mvc.Security;
import services.GtSession;
import services.ResultJson;
import services.user.GtUser;
import services.user.GtUserDocument;
import services.user.state.DocumentState;
import util.LoggerUtil;

import javax.inject.Inject;

import static conf.Definitions.SESSION_KEY;

public class DocumentWebApi extends Controller {
    @Inject
    FormFactory formFactory;

    final static private String className = "DocumentWebApi";

    @Security.Authenticated(AccountsAuth.class)
    public Result page(String id){
        final String methodName="page_document";

        GtSession user = (GtSession) ctx().args.get(SESSION_KEY);
        GtUser gtUser = new GtUser(user);
        LoggerUtil.debug(className,methodName, LoggerUtil.Type.ACTION,"DocumentPage");

        return Results.ok(views.html.translation_game_document.render(gtUser,id));

    }

    /**
     * Documentの一覧を取得します。
     *
     * @return ResultJson
     */
    @Security.Authenticated(AccountsAuth.class)
    public Result list(String id) {
        final String methodName = "list_document";

        try {
            GtUserDocument gtUserDocument = new GtUserDocument();

            ResultJson resultJson = new ResultJson(
                    OK,
                    gtUserDocument.listDocument(id)
            );

            return Results.ok(resultJson.toJson());
        } catch (Exception e) {
            System.out.print("error:");
            System.out.println(e);
            return Results.badRequest("NG");
        }

    }

    /**
     * Documentの情報を取得します。
     *
     * @param publicId 公開鍵
     * @return ResultJson
     */
    @Security.Authenticated(AccountsAuth.class)
    public Result get(String publicId) {
        final String methodName = "get";

        try {
            GtUserDocument gtUserDocument = new GtUserDocument();

            ResultJson resultJson = new ResultJson(
                    OK,
                    gtUserDocument.getDocument(publicId)
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
            GtUserDocument gtUserDocument = new GtUserDocument();

            gtUserDocument.deleteDocument(publicId);

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
            GtUserDocument gtUserDocument = new GtUserDocument();

            Form<DocumentState.PostDocument> form = formFactory.form(DocumentState.PostDocument.class).bindFromRequest();

            DocumentState.PostDocument postDocument = form.get();
            gtUserDocument.saveDocument(postDocument);

            ResultJson resultJson = new ResultJson(
                    OK,
                    Messages.get(lang(), "Documentが登録されました。") // TODO Messages
            );

            return Results.ok(resultJson.toJson());
        } catch (Exception e) {
            LoggerUtil.error(className,methodName, LoggerUtil.Type.ERROR,"error:",e);
            return Results.badRequest();
        }
    }
}
