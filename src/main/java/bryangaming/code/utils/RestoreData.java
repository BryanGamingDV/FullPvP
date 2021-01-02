package bryangaming.code.utils;

import bryangaming.code.data.ArenaData;
import bryangaming.code.data.PlayerData;
import bryangaming.code.loader.ConfigLoader;
import bryangaming.code.manager.CacheManager;
import bryangaming.code.manager.ConfigManager;
import bryangaming.code.methods.ScoreboardMethod;
import bryangaming.code.service.PluginService;
import bryangaming.code.utils.serializable.LocationSerializable;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Locale;
import java.util.UUID;

public class RestoreData {

    private PluginService pluginService;
    private static CacheManager cacheManager;
    private static ConfigLoader configLoader;

    public RestoreData(PluginService pluginService){
        this.pluginService = pluginService;
        cacheManager = pluginService.getCache();
        configLoader = pluginService.getFiles();
    }

    public static void restoreData(String data) {
        switch (data) {
            case "arenas":
                ConfigManager arenaConfig = configLoader.getArenas();

                for (String arenaKey : arenaConfig.getConfigurationSection("arenas").getKeys(false)) {

                    cacheManager.getArena().put(arenaKey, new ArenaData(configLoader.getArenas(), arenaKey, true));
                    ArenaData arenaData = cacheManager.getArena().get(arenaKey);

                    String arenaString = arenaConfig.getString("arenas." + arenaKey + ".location");

                    if (arenaString != null) {
                        arenaData.setLobby(LocationSerializable.fromString(arenaString));
                    }

                    if (arenaConfig.getConfigurationSection("arenas." + arenaKey + ".kits") != null) {
                        for (String itemKey : arenaConfig.getConfigurationSection("arenas." + arenaKey + ".kits").getKeys(false)) {
                            arenaData.getKits().createFullItem(Material.valueOf(arenaConfig.getString("arenas." + arenaKey + ".kits." + itemKey + ".id")), Integer.parseInt(itemKey));
                        }
                    }
                }
                break;

            case "players":
                ConfigManager playersConfig = configLoader.getPlayers();

                for (String uuidKey : playersConfig.getConfigurationSection("players").getKeys(false)) {
                    UUID uuid = UUID.fromString(uuidKey);

                    cacheManager.getPlayerData().put(uuid, new PlayerData(configLoader.getPlayers(), uuid, true));
                    PlayerData playerData = cacheManager.getPlayerData().get(uuid);

                    playerData.setKills(Integer.parseInt(playersConfig.getString("players." + uuidKey + ".kills")));
                    playerData.setDeaths(Integer.parseInt(playersConfig.getString("players." + uuidKey + ".deaths")));
                    playerData.setCoins(Integer.parseInt(playersConfig.getString("players." + uuidKey + ".coins")));
                }
            case "config":
                for (Player player : Bukkit.getServer().getOnlinePlayers()){
                    ScoreboardMethod.reloadScoreboard(player);
                }
        }
    }

    public static boolean isDataSet(String data){
        switch (data) {
            case "arenas":
                return configLoader.getArenas().getConfigurationSection("arenas") != null;

            case "players":
                return configLoader.getPlayers().getConfigurationSection("players") != null;

            case "config":
                return configLoader.getConfig().getConfigurationSection("config") != null;
        }
        return false;
    }
}
