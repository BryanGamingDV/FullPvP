package bryangaming.code.methods;

import bryangaming.code.data.PlayerData;
import bryangaming.code.manager.CacheManager;
import bryangaming.code.manager.ConfigManager;
import bryangaming.code.service.PluginService;
import bryangaming.code.utils.PathManager;
import fr.minuskube.netherboard.Netherboard;
import fr.minuskube.netherboard.bukkit.BPlayerBoard;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import java.util.List;

public class ScoreboardMethod {

    private PluginService pluginService;

    private static ConfigManager config;
    private static CacheManager cacheManager;

    public ScoreboardMethod(PluginService pluginService) {
        this.pluginService = pluginService;
        config = pluginService.getFiles().getConfig();
        cacheManager = pluginService.getCache();
    }

    public static void setScoreboard(Player player) {
        BPlayerBoard playerBoard = Netherboard.instance().getBoard(player);

        List<String> scoreboardLines = config.getStringList("scoreboard.lines");
        PathManager.replacePvPLines(player, scoreboardLines);

        playerBoard.setName(PathManager.setColor(config.getString("scoreboard.title")));

        int lineid = 0;

        for (int id = 16; id > 16 - scoreboardLines.size(); id--){
            String scoreboardLine = scoreboardLines.get(lineid);

            if (scoreboardLine.isEmpty()){
                if (lineid < 10) {
                    scoreboardLine = "&" + lineid;
                }else {
                    scoreboardLine = "&0&" + lineid / 2;
                }
            }

            playerBoard.set(PathManager.setColor(scoreboardLine), id);
            lineid++;
        }

    }

    public static void reloadScoreboard(Player player){
        unsetScoreboard(player);
        setScoreboard(player);
    }

    public static void unsetScoreboard(Player player){
        BPlayerBoard playerBoard = Netherboard.instance().getBoard(player);
        playerBoard.clear();
    }

}
