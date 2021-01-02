package bryangaming.code.listeners;

import bryangaming.code.data.PlayerData;
import bryangaming.code.manager.ConfigManager;
import bryangaming.code.methods.events.ForceJoinMethod;
import bryangaming.code.service.PluginService;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.HashMap;
import java.util.UUID;

public class JoinListener implements Listener{

    private final PluginService pluginService;

    public JoinListener(PluginService pluginService){
        this.pluginService = pluginService;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event){

        ConfigManager config = pluginService.getFiles().getConfig();
        UUID playeruuid = event.getPlayer().getUniqueId();


        HashMap<UUID, PlayerData> playerData = pluginService.getCache().getPlayerData();

        if (playerData.get(playeruuid) == null) {
            playerData.put(playeruuid, new PlayerData(pluginService.getFiles(), playeruuid));
        }else{
            playerData.get(playeruuid).setOnlinePlayer(event.getPlayer());
        }

        if (config.getBoolean("minigame.force-join")){
            ForceJoinMethod.forceJoin(event.getPlayer().getUniqueId());
        }
    }
}
