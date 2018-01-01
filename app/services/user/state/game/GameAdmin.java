package services.user.state.game;

import javassist.NotFoundException;
import models.contents.Game;
import services.user.state.GameState;
import services.user.views.ViewGame;

import java.util.ArrayList;
import java.util.List;

public class GameAdmin implements GameState {

    /**
     * コンストラクタ
     */
    public GameAdmin() {

    }

    @Override
    public List<ViewGame> listGame() {
        List<Game> list = Game.getFind().where()
                .eq("deleted", false)
                .findList();

        List<ViewGame> viewList = new ArrayList<>();
        for (Game game : list) {
            viewList.add(new ViewGame(game));
        }

        return viewList;
    }

    /**
     * Gameのデータを取得する
     *
     * @param id 指定するPublicID
     * @return Game
     */
    @Override
    public ViewGame getGame(String id) throws NotFoundException {
        Game game = Game.getFind().where()
                .eq("publicId", id)
                .eq("deleted", false)
                .findUnique();

        if (game == null)
            throw new NotFoundException("対象のスニペットが見つかりませんでした。");

        return new ViewGame(game);
    }

    @Override
    public void deleteGame(String id) throws NotFoundException {
        Game game = Game.getFind().where()
                .eq("publicId", id)
                .eq("deleted", false)
                .findUnique();

        if (game == null)
            throw new NotFoundException("対象のGameデータが見つかりませんでした。");

        game.setDeleted(true);
        game.save();
    }

    @Override
    public void saveGame(PostGame form) throws NotFoundException {

        Game game;
        if (form.getPublicId() == null) {
            game = new Game();
        } else {
            game = Game.getFind().where()
                    .eq("publicId", form.getPublicId())
                    .eq("deleted", false)
                    .findUnique();
            if (game == null)
                throw new NotFoundException("Save:対象のGameデータが見つかりませんでした。"+form.getPublicId());
        }
        game.setName(form.getTitle());
        game.setVersion(form.getVersion());
        game.save();
    }
}
