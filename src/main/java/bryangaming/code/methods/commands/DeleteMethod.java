package bryangaming.code.methods.commands;

import bryangaming.code.data.ArenaData;
import bryangaming.code.manager.ConfigManager;
import bryangaming.code.methods.SenderManager;
import bryangaming.code.service.PluginService;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerCommandSendEvent;

import java.util.HashMap;
import java.util.UUID;

public class DeleteMethod {

    private PluginService pluginService;

    private static HashMap<String, ArenaData> arenaData;
    private static ConfigManager messages;

    public DeleteMethod(PluginService pluginService) {
        this.pluginService = pluginService;
        arenaData = pluginService.getCache().getArena();
        messages = pluginService.getFiles().getMessages();
    }

    public static void deleteArena(UUID uuid, String name){
        Player player = Bukkit.getPlayer(uuid);

        if (arenaData.get(name) == null){
            SenderManager.sendMessage(player.getPlayer(), messages.getString("error.arena.already-deleted")
                    .replace("%arena%", name));
            return;
        }

        arenaData.remove(name);
        SenderManager.sendMessage(player.getPlayer(), messages.getString("arena.deleted")
                .replace("%arena%", name));
    }
}