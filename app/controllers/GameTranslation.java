package controllers;

import com.sun.org.apache.regexp.internal.RE;
import models.contents.Document;
import models.contents.Game;
import models.contents.YmlFile;

import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;

import javax.inject.Inject;

public class GameTranslation extends Controller {


    /**
     * YmlFileのリストを取得する
     *
     * @param id 　GameのPublicId
     * @return
     */

    public Result viewDocumentList(String id) {
        return Results.ok(views.html.list_document.render(id));
    }

    public Result viewDocumentAdmin(String id) {
        return Results.ok(views.html.admin_document.render(id));
    }


    private FormFactory formFactory;

    @Inject
    public GameTranslation(FormFactory formFactory) {
        this.formFactory = formFactory;
    }


}
