package bryangaming.code.listeners;

import bryangaming.code.data.PlayerData;
import bryangaming.code.methods.events.BroadcastMethod;
import bryangaming.code.service.PluginService;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener{

    private final PluginService pluginService;

    public ChatListener(PluginService pluginService){
        this.pluginService = pluginService;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event){
        PlayerData playerData = pluginService.getCache().getPlayerData().get(event.getPlayer().getUniqueId());

        if (!playerData.isPlaying()){
            return;
        }

        event.setCancelled(true);
        BroadcastMethod.sendChat(event.getPlayer().getUniqueId(), event.getMessage());
    }
}
