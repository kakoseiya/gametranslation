package services.user;

import com.avaje.ebean.Ebean;
import javassist.NotFoundException;
import services.user.state.DocumentState;
import services.user.state.document.DocumentAdmin;
import services.user.views.ViewDocument;

import java.util.List;

public class GtUserDocument implements DocumentState {

    private DocumentState getDocumentState(){
        return new DocumentAdmin();
    }


    @Override
    public List<ViewDocument> listDocument(String id) {
        return getDocumentState().listDocument(id);
    }

    @Override
    public ViewDocument getDocument(String id) throws NotFoundException {
        return getDocumentState().getDocument(id);
    }

    @Override
    public void deleteDocument(String id) throws NotFoundException {
        Ebean.beginTransaction();
        try{
            getDocumentState().deleteDocument(id);
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
    public void saveDocument(PostDocument form) throws NotFoundException {
        Ebean.beginTransaction();
        try{
            getDocumentState().saveDocument(form);
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
