package services.user.state.yml;

import javassist.NotFoundException;
import models.contents.Document;
import models.contents.Game;
import models.contents.YmlFile;
import org.springframework.util.MimeTypeUtils;
import services.user.state.YmlFileState;
import services.user.views.ViewYmlFile;
import util.LoggerUtil;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.beans.Encoder;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static models.contents.YmlFile.Language.*;

public class YmlFileAdmin implements YmlFileState {

    final static private String className = "YmlFileAdmin";

    /**
     * コンストラクタ
     */
    public YmlFileAdmin() {

    }

    @Override
    public List<ViewYmlFile> listYmlFile(String id) {
        Game game = Game.getFind().where()
                .eq("publicId", id)
                .eq("deleted", false)
                .findUnique();

        List<YmlFile> list = YmlFile.getFind().where()
                .eq("deleted", false)
                .eq("game_id", game.getId())
                .findList();

        List<ViewYmlFile> viewList = new ArrayList<>();
        for (YmlFile ymlFile : list) {

            viewList.add(new ViewYmlFile(ymlFile));
        }
        return viewList;
    }

    @Override
    public ViewYmlFile getYmlFile(String id) throws NotFoundException {
        YmlFile ymlFile = YmlFile.getFind().where()
                .eq("publicId", id)
                .eq("deleted", false)
                .findUnique();

        if (ymlFile == null)
            throw new NotFoundException("対象のスニペットが見つかりませんでした。");


        return new ViewYmlFile(ymlFile);
    }

    @Override
    public void deleteYmlFile(String id) throws NotFoundException {
        YmlFile ymlFile = YmlFile.getFind().where()
                .eq("publicId", id)
                .eq("deleted", false)
                .findUnique();

        if (ymlFile == null)
            throw new NotFoundException("対象のGameデータが見つかりませんでした。");

        ymlFile.setDeleted(true);
        ymlFile.save();
    }

    @Override
    public void saveYmlFile(PostYmlFile form) throws NotFoundException {
        YmlFile ymlFile;
        if (form.getId() == null) {
            ymlFile = new YmlFile();
        } else {
            ymlFile = YmlFile.getFind().where()
                    .eq("publicId", form.getId())
                    .eq("deleted", false)
                    .findUnique();
            if (ymlFile == null)
                throw new NotFoundException("対象のGameデータが見つかりませんでした。");
        }
        ymlFile.setName(form.getFileName());
        ymlFile.save();
    }

    @Override
    public void toDocument(File file, String fileName, String gamePublicId) throws NotFoundException {
        final String methodName = "toDocument";

        List<String> stringList = new ArrayList<>();
        BufferedReader br = null;
        try {
            FileReader filereader = new FileReader(file);
            br = new BufferedReader(filereader);
            String str;
            while ((str = br.readLine()) != null) {
                stringList.add(str);
            }
            LoggerUtil.debug(className, methodName, LoggerUtil.Type.ACTION, stringList);
            Game game = Game.getFind().where()
                    .eq("publicId", gamePublicId)
                    .eq("deleted", false)
                    .findUnique();

            String[] strArray = new String[stringList.size()];
            stringList.toArray(strArray);
            YmlFile ymlFile = new YmlFile();
            ymlFile.setName(fileName);
            ymlFile.setGame(game);
            ymlFile.setLang(ENGLISH.getId());
            ymlFile.save();
            ymlFile.refresh();
            for (int i = 1; i < strArray.length; i++) {

                String[] doubleSplit = strArray[i].split("[\"]+", -1);
                String[] strSplit = doubleSplit[0].split(" ");
                Document document = new Document();
                String key;

                if (strSplit.length > 1) {
                    StringBuffer sBuff = new StringBuffer();
                    for (int j = 1; j < strSplit.length - 1; j++) {
                        sBuff.append(strSplit[j]);
                        sBuff.append(" ");
                    }
                    sBuff.append(strSplit[strSplit.length - 1]);
                    key = sBuff.toString();
                } else {
                    key = strSplit[1];
                }

                document.setYmlFile(ymlFile);
                document.setKey(key);
                document.setOrigText(doubleSplit[1]);
                document.setStatus(Document.Status.IMPERFECT.getId());
                document.save();
                document.refresh();
                LoggerUtil.info(className, methodName, LoggerUtil.Type.ACTION, "i:", i);
            }


            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) br.close();
            } catch (IOException e) {
                LoggerUtil.error(className, methodName, LoggerUtil.Type.ERROR, e);
            }
        }


    }

}

