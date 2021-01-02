package bryangaming.code.utils;

import bryangaming.code.manager.ConfigManager;
import bryangaming.code.service.PluginService;
import org.bukkit.Bukkit;

public class PluginConditions {

    private PluginService pluginService;
    private static ConfigManager config;

    public PluginConditions(PluginService pluginService){
        this.pluginService = pluginService;
        config = pluginService.getFiles().getConfig();
    }


    public static boolean IsVaultEnabled(){

        if (!config.getString("arena.economy").equalsIgnoreCase("Vault")){
            return false;
        }

        if (!Bukkit.getPluginManager().isPluginEnabled("Vault")){
            return false;
        }

        return true;
    }
}
