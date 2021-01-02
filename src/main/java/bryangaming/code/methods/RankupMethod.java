package bryangaming.code.methods;

import bryangaming.code.data.PlayerData;
import bryangaming.code.manager.CacheManager;
import bryangaming.code.manager.ConfigManager;
import bryangaming.code.service.PluginService;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.*;

public class RankupMethod {

    private PluginService pluginService;

    private static final HashMap<Integer, String> rankupCache = new HashMap<>();;
    private static ConfigManager config;
    private static ConfigManager messages;
    private static CacheManager cacheManager;

    public RankupMethod(PluginService pluginService){
        this.pluginService = pluginService;
        config = pluginService.getFiles().getConfig();
        messages = pluginService.getFiles().getMessages();
        cacheManager = pluginService.getCache();
    }

    public static void reloadCache() {
        if (!rankupCache.isEmpty()) {
            rankupCache.clear();
        }

        ArrayList<String> listRanks = new ArrayList<>(config.getConfigurationSection("rankup.ranks").getKeys(true));
        int id = 1;
        for (String ranks : listRanks) {
            rankupCache.put(id, ranks);
            id++;

        }
    }

    private static HashMap<Integer, String> getRankup(){
        return rankupCache;
    }

    public static String getFirstRank(){
        return rankupCache.get(0);
    }

    public static boolean hasRequeriments(Player player){
        PlayerData playerData = cacheManager.getPlayerData().get(player.getUniqueId());
        String nextRank = rankupCache.get(playerData.getRankupID() + 1);

        if (config.getString("rankup.mode").equalsIgnoreCase("kills")){
            if (playerData.getRankupID() == getRankup().size()){
                return false;
            }

            if (playerData.getKills() == config.getInt("rankup.ranks." + nextRank)){
                return true;
            }
            return false;
        }
        return false;
    }

    public static void rankupPlayer(Player player){
        PlayerData playerData = cacheManager.getPlayerData().get(player.getUniqueId());

        if (playerData.getRankupID() == getRankup().size()){
            return;
        }

        playerData.addRankupID();
        playerData.setRankupName(getRankup().get(playerData.getRankupID()));
        
        for (Player online : Bukkit.getServer().getOnlinePlayers()){
            PlayerData onlineData = cacheManager.getPlayerData().get(online.getUniqueId());

            if (!onlineData.getArenaName().equalsIgnoreCase(playerData.getName())){
                continue;
            }

            SenderManager.sendMessage(player, messages.getString("server.rankup")
                        .replace("%player%", player.getName()
                        .replace("%rank%", playerData.getRankup())));

        }

    }

    public static String getNextRank(Player player){
        PlayerData playerData = cacheManager.getPlayerData().get(player.getUniqueId());
        String nextRank = rankupCache.get(playerData.getRankupID() + 1);

        if (nextRank == null){
            return "&fMax rank";
        }

        return nextRank;
    }
}
