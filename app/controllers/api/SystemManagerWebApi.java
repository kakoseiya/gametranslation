package controllers.api;

import conf.Definitions;
import controllers.secure.AccountsAuth;
import controllers.secure.SystemManagerAuth;
import javassist.NotFoundException;
import play.data.Form;
import play.data.FormFactory;
import play.i18n.Messages;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import play.mvc.Security;
import services.GtSession;
import services.GtSystemManager;
import services.ResultJson;
import services.user.GtUser;
import services.user.GtUserGame;
import services.user.form.GtSystemManagerUserForm;
import services.user.state.GameState;

import javax.inject.Inject;

@Security.Authenticated(SystemManagerAuth.class)
public class SystemManagerWebApi extends Controller {
    @Inject
    FormFactory formFactory;

    final static private String className = "UserWebApi";

    /**
     * 登録してあるユーザー全体を表示する
     */
    public Result list() {
        final String methodName = "get";

        GtSystemManager gtSystemManager = new GtSystemManager();

        try {
            ResultJson resultJson = new ResultJson(
                    OK,
                    gtSystemManager.list()
            );

            return Results.ok(resultJson.toJson());
        } catch (Exception e) {
            return Results.badRequest();
        }
    }

    /**
     * 　システム管理者用ページを開く
     */
    public Result page() {
        final String methodName = "page";
        GtSession GtSession = (GtSession) ctx().args.get(Definitions.SESSION_KEY);
        GtUser gtUser = new GtUser(GtSession);

        return Results.ok(views.html.administrator_user_list.render(gtUser));
    }

    /**
     * ユーザーを削除する
     */
    public Result delete(String id) {
        final String methodName = "page";
        GtSystemManager gtSystemManager = new GtSystemManager();
        try {
            gtSystemManager.delete(id);
            ResultJson resultJson = new ResultJson(
                    OK,
                    gtSystemManager.list()
            );
            return Results.ok(resultJson.toJson());
        } catch (NotFoundException e) {
            e.printStackTrace();
            return Results.badRequest();
        }
    }

    public Result post(){
        final String methodName = "post";
        try {
            GtSystemManager gtSystemManager = new GtSystemManager();

            Form<GtSystemManagerUserForm> form = formFactory.form(GtSystemManagerUserForm.class).bindFromRequest();
            if (form.hasErrors()) {
                System.out.println("post Error:");

                return Results.badRequest(Json.toJson(form.errors()));
            }

            GtSystemManagerUserForm postSystemUser = form.get();
            gtSystemManager.save(postSystemUser);

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
