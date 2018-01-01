package controllers.api;

import conf.Definitions;
import controllers.secure.AccountsAuth;
import controllers.secure.NoGuestAuth;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import play.mvc.Security;
import services.GtSession;
import services.ResultJson;
import services.user.GtUser;
import services.user.form.GtMyPageUserAdminForm;
import util.LoggerUtil;

import javax.inject.Inject;

public class UserWebApi extends Controller {
    @Inject
    FormFactory formFactory;

    final static private String className = "UserWebApi";

    /**
     * Userの情報を取得します。
     *
     * @return ResultJson
     */
    @Security.Authenticated(AccountsAuth.class)
    public Result get() {
        final String methodName = "get";

        try {
            GtSession GtSession = (GtSession) ctx().args.get(Definitions.SESSION_KEY);
            GtUser gtUser = new GtUser(GtSession);

            ResultJson resultJson = new ResultJson(
                    OK,
                    gtUser.getAccountInformation()
            );

            return Results.ok(resultJson.toJson());
        } catch (Exception e) {
            return Results.badRequest();
        }
    }

    /**
     * MyPageを表示する
     *
     * @return ResultJson
     */
    @Security.Authenticated(NoGuestAuth.class)
    public Result page() {
        final String methodName = "page";

        try {
            GtSession GtSession = (GtSession) ctx().args.get(Definitions.SESSION_KEY);
            GtUser gtUser = new GtUser(GtSession);

            return Results.ok(views.html.user_mypage.render(gtUser));
        } catch (Exception e) {
            return Results.badRequest();
        }
    }

    @Security.Authenticated(NoGuestAuth.class)
    public Result post() {
        final String methodName = "changePass";

        try {
            GtSession GtSession = (GtSession) ctx().args.get(Definitions.SESSION_KEY);
            GtUser gtUser = new GtUser(GtSession);

            Form<GtMyPageUserAdminForm> form = formFactory.form(GtMyPageUserAdminForm.class).bindFromRequest();
            if (form.hasErrors()) {
                LoggerUtil.error(className,methodName, LoggerUtil.Type.ERROR,"Post エラー");

                return Results.badRequest(Json.toJson(form.errors()));
            }
            GtMyPageUserAdminForm postUser=form.get();
            gtUser.changeInfo(postUser);

            ResultJson resultJson = new ResultJson(
                    OK,
                    gtUser.getAccountInformation()
            );

            return Results.ok(resultJson.toJson());
        } catch (Exception e) {
            return Results.badRequest();
        }
    }


}
