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

public class CreateMethod {

    private PluginService pluginService;

    private static HashMap<String, ArenaData> arenaData;
    private static ConfigManager messages;
    private static ConfigManager arena;

    public CreateMethod(PluginService pluginService) {
        this.pluginService = pluginService;
        arenaData = pluginService.getCache().getArena();
        messages = pluginService.getFiles().getMessages();
        arena = pluginService.getFiles().getArenas();
    }

    public static void createArena(UUID uuid, String name){
        Player player = Bukkit.getPlayer(uuid);

        if (arenaData.get(name) != null){
            SenderManager.sendMessage(player.getPlayer(), messages.getString("error.arena.already-created")
                    .replace("%arena%", name));
            return;
        }

        arenaData.put(name, new ArenaData(arena, name));
        SenderManager.sendMessage(player.getPlayer(), messages.getString("arena.created")
                .replace("%arena%", name));

    }
}