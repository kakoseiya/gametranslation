package services.user;

import com.avaje.ebean.Ebean;
import javassist.NotFoundException;
import services.user.state.GameState;
import services.user.state.game.GameAdmin;
import services.user.views.ViewGame;

import java.util.List;

public class GtUserGame implements GameState {

    private GameState getGameState() {
        return new GameAdmin();
    }

    @Override
    public List<ViewGame> listGame() {
        return getGameState().listGame();
    }

    @Override
    public ViewGame getGame(String id) throws NotFoundException {
        return getGameState().getGame(id);
    }

    @Override
    public void deleteGame(String id) throws NotFoundException {

        Ebean.beginTransaction();
        try {
            getGameState().deleteGame(id);
            Ebean.commitTransaction();
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw e;
        } finally {
            Ebean.endTransaction();
        }
    }

    @Override
    public void saveGame(PostGame form) throws NotFoundException {
        Ebean.beginTransaction();
        try {
            getGameState().saveGame(form);
            Ebean.commitTransaction();
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw e;
        } finally {
            Ebean.endTransaction();
        }
    }


}
