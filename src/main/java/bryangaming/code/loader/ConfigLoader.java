package bryangaming.code.loader;

import bryangaming.code.service.PluginService;
import bryangaming.code.manager.ConfigManager;

import java.util.HashMap;

public class ConfigLoader {

    private final PluginService service;

    private ConfigManager config;
    private ConfigManager messages;
    private ConfigManager arena;
    private ConfigManager players;
    private ConfigManager shops;

    public ConfigLoader(PluginService pluginService){
        this.service = pluginService;

        setup();
        pluginService.getPlugin().getLogger().info("Config loaded!");
    }

    public void setup(){
        HashMap<String, ConfigManager> configData = service.getCache().getConfigData();

        config = setConfiguration("config.yml");
        configData.put("config.yml", config);

        messages = setConfiguration("messages.yml");
        configData.put("messages.yml", messages);

        arena = setConfiguration("arenas.yml");
        configData.put("arenas.yml", arena);

        players = setConfiguration("players.yml");
        configData.put("players.yml", players);

        shops = setConfiguration("shops.yml");
        configData.put("shops.yml", shops);
    }

    public ConfigManager setConfiguration(String file){
        return new ConfigManager(service.getPlugin(), file);
    }

    public ConfigManager getConfig() {
        return config;
    }

    public ConfigManager getArenas(){
        return arena;
    }

    public ConfigManager getPlayers(){
        return players;
    }

    public ConfigManager getShops(){
        return players;
    }


    public ConfigManager getMessages() {
        return messages;
    }
}
