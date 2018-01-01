package services.debug;

import models.accounts.User;
import models.contents.Document;
import models.contents.YmlFile;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import services.ResultJson;
import services.user.GtUserDocument;
import services.user.views.ViewDocument;

import java.util.ArrayList;
import java.util.List;

public class DebugUser {

    public List<DebugViewUser> listUser() {
        List<User> list = User.getFind().where()
                .eq("deleted", false)
                .findList();

        List<DebugViewUser> viewList = new ArrayList<>();
        for (User user : list) {
            viewList.add(new DebugViewUser(user));
        }
        return viewList;
    }
}
