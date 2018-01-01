package services;

import com.avaje.ebean.Ebean;
import javassist.NotFoundException;
import models.accounts.User;
import models.accounts.UserPrivileges;
import models.accounts.UserStatus;
import services.user.form.GtSystemManagerUserForm;
import services.user.views.ViewUser;

import java.util.ArrayList;
import java.util.List;

import static util.StringUtil.generatePasswordHash;

public class GtSystemManager {

    final static private String className = "GtSystemManager";

    /**
     * コンストラクタ
     */
    public GtSystemManager(){
    }

    /**
     * userの情報を全部返す。
     * @return
     */
    public List<ViewUser> list(){

        List<User> list = User.getFind().where()
                .findList();

        List<ViewUser> viewList = new ArrayList<>();
        for (User user : list) {

            viewList.add(new ViewUser(user));
        }
        return viewList;

    }

    /**
     * userを削除する。
     * @return
     */
    public void delete(String id) throws NotFoundException {
        Ebean.beginTransaction();
        try {
            User user=User.getFind().where()
                    .eq("publicId",id)
                    .findUnique();
            if (user == null)
                throw new NotFoundException("Delete:対象のUserデータが見つかりませんでした。");
            user.delete();
            user.save();
            user.refresh();

            Ebean.commitTransaction();
        } catch (Exception e) {
            throw e;
        } finally {
            Ebean.endTransaction();
        }
    }

    /**
     * Userを保存する
     */
    public void save(GtSystemManagerUserForm form) throws Exception {
        Ebean.beginTransaction();
        User user;
        try {
            if(form.getPublicId()==null){
               user=new User();
            }else{
                user=User.getFind().where()
                        .eq("publicId",form.getPublicId())
                        .findUnique();
                if (user == null) throw new NotFoundException("Delete:対象のUserデータが見つかりませんでした。");
            }
            user.setUserPrivileges(UserPrivileges.getPrivilages(form.getUserPrivirage()));
            user.setBirthDay(form.getBirthDay());
            user.setMobileNumber(form.getMobileNumber());
            user.setMail(form.getMail());
            user.setUserName(form.getUserName());
            user.setUserStatus(UserStatus.REGISTERED);
            user.setEncPassword(generatePasswordHash(form.getPass()));
            user.save();
            user.refresh();

            Ebean.commitTransaction();
        } catch (Exception e) {
            throw e;
        } finally {
            Ebean.endTransaction();
        }
    }
}
