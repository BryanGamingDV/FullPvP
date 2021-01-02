package bryangaming.code.utils;

import bryangaming.code.data.ArenaData;
import bryangaming.code.data.PlayerData;
import bryangaming.code.loader.ConfigLoader;
import bryangaming.code.manager.CacheManager;
import bryangaming.code.manager.ConfigManager;
import bryangaming.code.methods.RankupMethod;
import bryangaming.code.service.PluginService;
import bryangaming.code.utils.serializable.LocationSerializable;
import org.bukkit.Material;

import java.io.ObjectInputFilter;
import java.util.HashMap;
import java.util.UUID;

public class ReloadData {

    private PluginService pluginService;
    private static CacheManager cacheManager;
    private static ConfigLoader configLoader;

    public ReloadData(PluginService pluginService){
        this.pluginService = pluginService;
        cacheManager = pluginService.getCache();
        configLoader = pluginService.getFiles();
    }

    public static void reloadData(String config){

        ConfigManager arenaConfig = configLoader.getArenas();
        ConfigManager playersConfig = configLoader.getPlayers();

        switch (config){
            case "arenas":

                for (String arenaKey : arenaConfig.getConfigurationSection("arenas").getKeys(false)) {

                    ArenaData arenaData = cacheManager.getArena().get(arenaKey);
                    String arenaString = arenaConfig.getString("arenas." + arenaKey + ".location");

                    if (arenaString != null) {
                        arenaData.setLobby(LocationSerializable.fromString(arenaString));
                    }

                    if (arenaConfig.getConfigurationSection("arenas." + arenaKey + ".kits") != null) {
                        arenaData.getKits().getData().clear();
                        for (String itemKey : arenaConfig.getConfigurationSection("arenas." + arenaKey + ".kits").getKeys(false)) {
                            arenaData.getKits().createFullItem(Material.valueOf(arenaConfig.getString("arenas." + arenaKey + ".kits." + itemKey + ".id")), Integer.parseInt(itemKey));
                        }
                    }
                }

            case "players":
                HashMap<UUID, PlayerData> playerData = cacheManager.getPlayerData();

                if (playerData.keySet().size() > 10){
                    return;
                }

                for (UUID uuid : playerData.keySet()){
                    PlayerData player = playerData.get(uuid);

                    if (playersConfig.getString("players." + uuid + ".name") == null || playersConfig.getString("players." + uuid + ".name").equalsIgnoreCase(player.getName())){
                        playersConfig.set("players." + uuid + ".name", player.getName());
                    }

                    player.setKills(Integer.parseInt(playersConfig.getString("players." + uuid + ".kills")));
                    player.setDeaths(Integer.parseInt(playersConfig.getString("players." + uuid + ".deaths")));
                    player.setCoins(Integer.parseInt(playersConfig.getString("players." + uuid + ".coins")));

                }
            case "config":
                RankupMethod.reloadCache();
        }
    }

}
