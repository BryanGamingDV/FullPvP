package bryangaming.code.utils.serializable;

import bryangaming.code.FullPvP;
import bryangaming.code.service.PluginService;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import team.unnamed.gui.abstraction.item.ItemClickable;
import team.unnamed.gui.core.gui.GUIBuilder;

public class UtilsSerializable {

    private PluginService pluginService;
    private static FullPvP fullPvP;

    public UtilsSerializable(PluginService pluginService) {
        this.pluginService = pluginService;
        fullPvP = pluginService.getPlugin();
    }

    public static void waitTeleport(Player player, Location location, Integer seconds) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(fullPvP, new Runnable() {
            @Override
            public void run() {
                player.teleport(location);
            }
        }, seconds * 10);

    }

    public static void clearItems(Player player, Integer seconds) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(fullPvP, new Runnable() {
            @Override
            public void run() {
                ItemsSerializable.clearItems(player);
            }
        }, seconds * 10);

    }


}
