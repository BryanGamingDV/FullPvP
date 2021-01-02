package bryangaming.code.service.vault;

import bryangaming.code.manager.ConfigManager;
import bryangaming.code.service.PluginService;
import bryangaming.code.utils.PluginConditions;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultManager {

    private PluginService pluginService;
    private static Economy econ = null;

    private static ConfigManager config;

    public VaultManager(PluginService pluginService){
        this.pluginService = pluginService;
        config = pluginService.getFiles().getConfig();
        loadPlugins();
    }

    public void loadPlugins(){

        if (config.getString("arena.economy").equalsIgnoreCase("vault")) {
            if (!Bukkit.getPluginManager().isPluginEnabled("Vault")){
                setupEconomy();
                Bukkit.getLogger().info("Economy set: Vault");
            }else{
                Bukkit.getLogger().info("Economy set: Vault");
                Bukkit.getLogger().info("Error: Vault is not enabled!");
            }
        }else{
            Bukkit.getLogger().info("Economy set: Plugin");
        }

    }

    private void setupEconomy(){
        RegisteredServiceProvider<Economy> rsp = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);

        if (rsp == null) {
            Bukkit.getLogger().info("Error! Economy wasn't loaded.");
        }

        econ = rsp.getProvider();

    }

    public static Economy getEconomy(){
        return econ;
    }
}
