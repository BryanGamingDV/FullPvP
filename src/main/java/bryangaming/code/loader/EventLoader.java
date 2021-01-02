package bryangaming.code.loader;

import bryangaming.code.listeners.ChatListener;
import bryangaming.code.listeners.FightListener;
import bryangaming.code.listeners.JoinListener;
import bryangaming.code.manager.ConfigManager;
import bryangaming.code.service.PluginService;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.PluginManager;

public class EventLoader {

    private final PluginService pluginService;

    public EventLoader(PluginService pluginService){
        this.pluginService = pluginService;

        setup();
        pluginService.getPlugin().getLogger().info("Events loaded!");
    }

    public void setup(){
        PluginManager pluginManager = pluginService.getPlugin().getServer().getPluginManager();
        pluginManager.registerEvents(new JoinListener(pluginService), pluginService.getPlugin());
        pluginManager.registerEvents(new FightListener(pluginService), pluginService.getPlugin());
        pluginManager.registerEvents(new ChatListener(pluginService), pluginService.getPlugin());
    }

}
