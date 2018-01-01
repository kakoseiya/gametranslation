package services.user;

import com.avaje.ebean.Ebean;
import javassist.NotFoundException;
import services.user.state.YmlFileState;
import services.user.state.yml.YmlFileAdmin;
import services.user.views.ViewYmlFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.List;

public class GtUserYmlFile implements YmlFileState{


    private YmlFileState getYmlFileState(){
        return new YmlFileAdmin();
    }


    @Override
    public List<ViewYmlFile> listYmlFile(String id) {
        return getYmlFileState().listYmlFile(id);
    }

    @Override
    public ViewYmlFile getYmlFile(String id) throws NotFoundException {
        return getYmlFileState().getYmlFile(id);
    }

    @Override
    public void deleteYmlFile(String id) throws NotFoundException {
        Ebean.beginTransaction();
        try{
            getYmlFileState().deleteYmlFile(id);
            Ebean.commitTransaction();
        }catch (NotFoundException e){
            throw e;
        }catch (Exception e){
            throw e;
        }finally {
            Ebean.endTransaction();
        }
    }

    @Override
    public void saveYmlFile(PostYmlFile form) throws NotFoundException {
        Ebean.beginTransaction();
        try{
            getYmlFileState().saveYmlFile(form);
            Ebean.commitTransaction();
        }catch (NotFoundException e){
            throw e;
        }catch (Exception e){
            throw e;
        }finally {
            Ebean.endTransaction();
        }
    }

    @Override
    public void toDocument(File file,String fileName,String gamePublicId) throws NotFoundException {
        Ebean.beginTransaction();
        try{
            getYmlFileState().toDocument(file,fileName,gamePublicId);
            Ebean.commitTransaction();
        }catch (NotFoundException e){
            throw e;
        }catch (Exception e){
            throw e;
        }finally {
            Ebean.endTransaction();
        }
    }

}
