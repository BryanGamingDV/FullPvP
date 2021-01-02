package bryangaming.code;


import bryangaming.code.service.PluginService;
import bryangaming.code.utils.RestoreData;
import org.bukkit.plugin.java.JavaPlugin;

public class FullPvP extends JavaPlugin{

    private RestoreData restoreData;
    
    public void onEnable() {
        PluginService pluginService = new PluginService(this);
        restoreData = new RestoreData(pluginService);
        registerData();
    }

    public void registerData(){
        if (RestoreData.isDataSet("arenas")){
            RestoreData.restoreData("arenas");
        }

        if (RestoreData.isDataSet("players")){
            RestoreData.restoreData("players");
        }

        if (RestoreData.isDataSet("config")){
            RestoreData.restoreData("config");
        }
    }
}
