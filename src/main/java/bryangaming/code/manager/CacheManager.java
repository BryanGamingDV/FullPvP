package bryangaming.code.manager;

import bryangaming.code.data.ArenaData;
import bryangaming.code.data.PlayerData;
import bryangaming.code.data.ShopData;
import bryangaming.code.service.PluginService;

import java.util.HashMap;
import java.util.UUID;

public class CacheManager {

    private PluginService pluginService;

    private final HashMap<String, ArenaData> arenaData = new HashMap<>();
    private final HashMap<String, ConfigManager> configData = new HashMap<>();
    private final HashMap<UUID, PlayerData> playerData = new HashMap<>();
    private final HashMap<Integer, ShopData> shopData = new HashMap<>();

    public CacheManager(PluginService pluginService){
        this.pluginService = pluginService;
    }

    public HashMap<String, ArenaData> getArena(){
        return arenaData;
    }

    public HashMap<String, ConfigManager> getConfigData() {
        return configData;
    }

    public HashMap<UUID, PlayerData> getPlayerData() {
        return playerData;
    }

    public HashMap<Integer, ShopData> getShops() {
        return shopData;
    }
}
