package bryangaming.code.methods;

import bryangaming.code.manager.ConfigManager;
import bryangaming.code.methods.SenderManager;
import bryangaming.code.service.PluginService;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.swing.plaf.synth.Region;

public class OnlineProtocol {

    private PluginService pluginService;
    private static ConfigManager messages;

    public OnlineProtocol(PluginService pluginService){
        this.pluginService = pluginService;
        messages = pluginService.getFiles().getMessages();
    }

    public static void setup(){
        for (Player player : Bukkit.getServer().getOnlinePlayers()){
            SenderManager.sendMessage(player, messages.getString("server.on-reload"));
        }
    }
}
