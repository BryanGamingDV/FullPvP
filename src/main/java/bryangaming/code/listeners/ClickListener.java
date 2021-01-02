package bryangaming.code.listeners;

import bryangaming.code.data.PlayerData;
import bryangaming.code.methods.mode.EditMode;
import bryangaming.code.service.PluginService;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashMap;
import java.util.Map;

public class ClickListener implements Listener{


    private PluginService pluginService;

    public ClickListener(PluginService pluginService){
        this.pluginService = pluginService;
    }

    @EventHandler
    public void onClick(PlayerInteractEvent event){
        PlayerData playerData = pluginService.getCache().getPlayerData().get(event.getPlayer().getUniqueId());
        if (!playerData.isEditMode()){
            return;
        }
        Map<String, Location> posList = playerData.getPosList();

        if (event.getClickedBlock() == null){
            return;
        }

        if (event.getAction() == Action.LEFT_CLICK_BLOCK){
            posList.put("pos1", event.getClickedBlock().getLocation());
            EditMode.sendEditMode(event.getPlayer().getUniqueId());
            return;
        }

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK){
            posList.put("pos2", event.getClickedBlock().getLocation());
        }

    }
}
