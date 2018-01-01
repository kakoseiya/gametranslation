package services.debug;

import models.accounts.User;
import models.accounts.UserPrivileges;
import models.accounts.UserStatus;
import models.contents.Document;
import models.contents.Game;
import models.contents.YmlFile;
import util.LoggerUtil;

import java.time.LocalDate;
import java.util.List;

import static util.StringUtil.generatePasswordHash;

public class DebugCreateGameData {

    static public Boolean getCreated() {
        int count = Game.getFind().where()
                .eq("deleted", false)
                .findCount();
        return count > 0;
    }

    static public Boolean getUserCreated() {
        int count = User.getFind().where()
                .eq("deleted", false)
                .eq("userPrivileges", UserPrivileges.GUEST.getId())
                .findCount();
        return count > 0;
    }

    static public void firstUserCreate() {
        User user;
        user = new User();
        user.setBirthDay(LocalDate.of(1990, 1, 1));
        user.setMail("kakoseiya@gmail.com");
        user.setEncPassword(generatePasswordHash("guest04140414"));
        user.setMobileNumber("09050016639");
        user.setUserName("GUEST");
        user.setUserPrivileges(UserPrivileges.GUEST);
        user.setUserStatus(UserStatus.REGISTERED);
        user.save();
        user.refresh();
    }

    static public void firstCreate() {
        Game game;
        YmlFile ymlFile;
        Document document;

        for (int i = 0; i < 30; i++) {
            game = new Game();
            game.setName("Test Game" + (i + 1));
            game.setVersion("1.1." + i);
            game.setOverview("# Markdown CSS\n" +
                    "\n" +
                    "This is a simple style with normalized defaults, it should work on anything from a desktop, to a phone.\n" +
                    "\n" +
                    "## How to use\n" +
                    "\n" +
                    "Add this to the top of your markdown file\n" +
                    "```\n" +
                    "<link href=\"https://raw.github.com/kottkrig/Markdown-CSS/master/markdown.css\" rel=\"stylesheet\"></link>\n" +
                    "```\n" +
                    "\n" +
                    "\n" +
                    "## Credits\n" +
                    "- [Clownfart](https://github.com/clownfart/Markdown-CSS) - For the original version\n" +
                    "- [HTML5 Boilerplate](h5bp.com) - For normalize.css and media queries.\n" +
                    "- [Markdown.css](http://kevinburke.bitbucket.org/markdowncss) - For inspiration.\n" +
                    "- Various other web resources for inspiration.");
            game.save();
            for (int j = 0; j < 30; j++) {
                ymlFile = new YmlFile();
                ymlFile.setGame(game);
                ymlFile.setName("Test_l_english_" + (i + 1) + "_" + (j + 1));
                ymlFile.setSummary("test test" + (i + 1) + "_" + (j + 1));
                ymlFile.setLang(YmlFile.Language.ENGLISH.getId());
                ymlFile.save();
                for (int k = 0; k < 30; k++) {
                    document = new Document();
                    document.setYmlFile(ymlFile);
                    document.setKey("KEY_WORD:" + (i + 1) + "_" + (j + 1) + "_" + (k + 1));
                    document.setOrigText("Sample Orig:" + (i + 1) + "_" + (j + 1) + "_" + (k + 1));
                    document.setTransText("例文:" + (i + 1) + "_" + (j + 1) + "_" + (k + 1));
                    document.setTransGoogleText("確定例文" + (i + 1) + "_" + (j + 1) + "_" + (k + 1));
                    document.setRemarks("備考:" + (i + 1) + "_" + (j + 1) + "_" + (k + 1));
                    if (k < 15) {
                        document.setStatus(Document.Status.TRANSLATED.getId());
                    } else {
                        document.setStatus(Document.Status.IMPERFECT.getId());
                    }
                    document.save();
                }
            }
        }

        String pass;
        pass = "s414s414";
        User user=new User();
        user.setBirthDay(LocalDate.of(1994, 4, 14));
        user.setMail("kakoseiya.work@gmail.com");
        user.setEncPassword(generatePasswordHash(pass));
        user.setMobileNumber("09050016639");
        user.setUserName("kakoseiya");
        user.setUserPrivileges(UserPrivileges.SYSTEM_MANAGER);
        user.setUserStatus(UserStatus.REGISTERED);
        user.save();
        user.refresh();
        LoggerUtil.info("Debug", "create", LoggerUtil.Type.ACTION, "userCreate");
        LoggerUtil.info("Debug", "create", LoggerUtil.Type.ACTION, user.getEncPassword());
    }
}
