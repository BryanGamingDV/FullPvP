package bryangaming.code.utils;

import bryangaming.code.data.PlayerData;
import bryangaming.code.loader.ConfigLoader;
import bryangaming.code.manager.CacheManager;
import bryangaming.code.manager.ConfigManager;
import bryangaming.code.methods.RankupMethod;
import bryangaming.code.service.PluginService;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;

public class PathManager {

    private final PluginService pluginService;
    private static CacheManager cacheManager;

    public PathManager(PluginService pluginService) {
        this.pluginService = pluginService;
         cacheManager = pluginService.getCache();
    }

    public static String setStringLimit(String[] stringList, int except){

        StringBuilder stringBuilder = new StringBuilder();
        int id = 0;

        for (String string : stringList){
            stringBuilder.append(string).append(" ,");

            if (id + except >= stringList.length - 1){
                stringBuilder.append(string);
                break;
            }
            id++;
        }
        return stringBuilder.toString();
    }

    public String replaceVariables(String path) {
        ConfigManager config = pluginService.getFiles().getConfig();
        return path
                .replace(config.getString("config.p-variable"), config.getString("config.prefix"))
                .replace(config.getString("config.e-variable"), config.getString("config.error"))
                .replace("%newline%", "\n");
    }

    public static String setColor(String path){
        return ChatColor.translateAlternateColorCodes('&', path);
    }


    public static void replacePvPLines(Player player, List<String> line){
        PlayerData playerData = cacheManager.getPlayerData().get(player.getUniqueId());

        line.replaceAll(text -> text

                // Player variables
                .replace("%playername%", player.getName())
                .replace("%arena%", playerData.getArenaName())
                .replace("%rank%", playerData.getRankup())
                .replace("%nextrank%", RankupMethod.getNextRank(player))

                // Server variables
                .replace("%online%", String.valueOf(Bukkit.getServer().getOnlinePlayers().size()))

                // Stats variables
                .replace("%kills%", String.valueOf(playerData.getKills()))
                .replace("%killstreak%", String.valueOf(playerData.getKillStreak()))
                .replace("%maxkillstreak%", String.valueOf(playerData.getMaxKillStreak()))
                .replace("%deaths%", String.valueOf(playerData.getDeaths()))
                .replace("%coins%", String.valueOf(playerData.getCoins())));

    }


    public static void replaceStatsLines(Player player, List<String> line){
        PlayerData playerData = cacheManager.getPlayerData().get(player.getUniqueId());

        line.replaceAll(text -> text

                // Player variables
                .replace("%playername%", player.getName())
                .replace("%rank%", playerData.getRankup())

                // Stats variables
                .replace("%kills%", String.valueOf(playerData.getKills()))
                .replace("%maxkillstreak%", String.valueOf(playerData.getMaxKillStreak()))
                .replace("%deaths%", String.valueOf(playerData.getDeaths()))
                .replace("%coins%", String.valueOf(playerData.getCoins())));

    }
}
