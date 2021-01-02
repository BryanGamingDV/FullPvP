package bryangaming.code.methods.commands;

import bryangaming.code.data.ArenaData;
import bryangaming.code.data.PlayerData;
import bryangaming.code.manager.ConfigManager;
import bryangaming.code.methods.ScoreboardMethod;
import bryangaming.code.methods.SenderManager;
import bryangaming.code.methods.events.BroadcastMethod;
import bryangaming.code.service.PluginService;
import bryangaming.code.utils.serializable.ItemsSerializable;
import bryangaming.code.utils.serializable.UtilsSerializable;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

public class QuitMethod {

    private PluginService pluginService;

    private static HashMap<UUID, PlayerData> playerStats;
    private static ConfigManager messages;

    public QuitMethod(PluginService pluginService) {
        this.pluginService = pluginService;
        playerStats = pluginService.getCache().getPlayerData();

        messages = pluginService.getFiles().getMessages();
    }

    public static void quitGame(UUID uuid) {

        Player player = Bukkit.getPlayer(uuid);

        PlayerData playerData = playerStats.get(uuid);

        if (!(playerData.isPlaying())){
            SenderManager.sendMessage(player.getPlayer(), messages.getString("player.not-playing"));
            return;
        }

        SenderManager.sendMessage(player.getPlayer(), messages.getString("player.quit")
                    .replace("%arena%", playerData.getArenaName()));

        playerData.setArenaName(null);
        player.teleport(playerData.getLastLocation());

        PlayerInventory inventory = player.getInventory();
        ItemsSerializable.clearItems(player);

        int id = 0;
        for (ItemStack item : playerData.getLastInventory()){
            if (item == null){
                id++;
                inventory.setItem(id, new ItemStack(Material.AIR));
                continue;
            }

            inventory.setItem(id, item);
            id++;
        }

        ItemsSerializable.setArmors(player, playerData.getLastArmor());
        ScoreboardMethod.unsetScoreboard(player);

    }
}
