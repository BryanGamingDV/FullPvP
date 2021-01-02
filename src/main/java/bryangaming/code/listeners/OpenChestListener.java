package bryangaming.code.listeners;

import bryangaming.code.data.ArenaData;
import bryangaming.code.data.PlayerData;
import bryangaming.code.data.RegenChestData;
import bryangaming.code.service.PluginService;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class OpenChestListener implements Listener {


    private final PluginService pluginService;

    public OpenChestListener(PluginService pluginService) {
        this.pluginService = pluginService;
    }

    @EventHandler
    public void onOpenChest(PlayerInteractEvent event) {

        PlayerData playerData = pluginService.getCache().getPlayerData().get(event.getPlayer().getUniqueId());

        if (!playerData.isPlaying()){
            return;
        }

        if (!(event.getClickedBlock().getType() == Material.CHEST)){
            return;
        }

        ArenaData arenaData = pluginService.getCache().getArena().get(playerData.getArenaName());
        Chest chest = (Chest) event.getClickedBlock().getState();

        for (RegenChestData regenChestData : arenaData.getRegenChestManager().values()){

            if (regenChestData.isLocated(chest.getLocation())){
                for (ItemStack itemList : regenChestData.getItemStack()) {
                    if (itemList == null){
                        chest.getBlockInventory().addItem(new ItemStack(Material.AIR, 0));
                    }
                    chest.getBlockInventory().addItem(itemList);
                }
                break;
            }
        }



    }
}