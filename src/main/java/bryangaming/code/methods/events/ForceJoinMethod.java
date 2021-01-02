package bryangaming.code.methods.events;

import bryangaming.code.data.ArenaData;
import bryangaming.code.manager.ConfigManager;
import bryangaming.code.methods.commands.JoinMethod;
import bryangaming.code.service.PluginService;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class ForceJoinMethod {

    private PluginService service;

    private static ConfigManager config;
    private static HashMap<String, ArenaData>  arenaData;

    public ForceJoinMethod(PluginService pluginService){
        this.service = pluginService;
        config = pluginService.getFiles().getConfig();
        arenaData = pluginService.getCache().getArena();
    }

    public static void forceJoin(UUID uuid){

        String arenaName = config.getString("arena.force-join");

        if (arenaName == null || arenaName.equalsIgnoreCase("none")){
            return;
        }

        if (arenaData.get(arenaName) == null){
            return;
        }

        JoinMethod.joinGame(uuid, arenaName);
    }
}
