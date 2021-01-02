package bryangaming.code.methods;

import bryangaming.code.service.PluginService;
import bryangaming.code.utils.PathManager;

import org.bukkit.command.CommandSender;

public class SenderManager {

    public PluginService pluginService;
    static PathManager pathManager;

    public SenderManager(PluginService pluginService){
        this.pluginService = pluginService;
        pathManager = pluginService.getUtils().getPathManager();
    }

    public static void sendMessage(CommandSender sender, String message){

        message = pathManager.replaceVariables(message);
        message = PathManager.setColor(message);

        sender.sendMessage(message);
    }
}
