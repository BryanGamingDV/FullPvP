package bryangaming.code.methods.commands;

import bryangaming.code.data.ArenaData;
import bryangaming.code.data.KitsData;
import bryangaming.code.data.PlayerData;
import bryangaming.code.manager.ConfigManager;
import bryangaming.code.methods.ScoreboardMethod;
import bryangaming.code.methods.SenderManager;
import bryangaming.code.service.PluginService;
import bryangaming.code.utils.serializable.ItemsSerializable;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.UUID;

public class
JoinMethod {

    private PluginService pluginService;

    private static HashMap<String, ArenaData> arenaStats;
    private static HashMap<UUID, PlayerData> playerStats;
    private static ConfigManager messages;

    public JoinMethod(PluginService pluginService){
        this.pluginService = pluginService;
        arenaStats = pluginService.getCache().getArena();
        playerStats = pluginService.getCache().getPlayerData();

        messages = pluginService.getFiles().getMessages();
    }

    public static void joinGame(UUID uuid, String arena) {

        Player player = Bukkit.getPlayer(uuid);

        ArenaData arenaData = arenaStats.get(arena);
        PlayerData playerData = playerStats.get(uuid);

        if (playerData.isPlaying()){
            SenderManager.sendMessage(player.getPlayer(), messages.getString("player.already-playing")
                        .replace("%arena%", arena));
            return;
        }

        if (arenaData == null){
            SenderManager.sendMessage(player.getPlayer(), messages.getString("error.arena.no-exists")
                    .replace("%arena%", arena));
            return;
        }

        KitsData kitsData = arenaData.getKits();

        if (!(arenaData.isLobbySet())){
            SenderManager.sendMessage(player.getPlayer(), messages.getString("error.arena.lobby-no-set")
                        .replace("%arena%", arena));
            return;
        }

        playerData.setArenaName(arena);

        playerData.setLastLocation(player.getLocation());
        player.teleport(arenaData.getLobbyLocation());

        ScoreboardMethod.setScoreboard(player);

        playerData.setLastInventory(ItemsSerializable.getContents(player));
        playerData.setLastArmor(ItemsSerializable.getArmorContents(player));

        ItemsSerializable.clearItems(player);

        for (int id = 0; id < kitsData.getKitSize(); id++){
            player.getInventory().addItem(kitsData.getItem(id + 1));
        }

        SenderManager.sendMessage(player.getPlayer(), messages.getString("player.join")
                    .replace("%arena%", arena));

    }
}
