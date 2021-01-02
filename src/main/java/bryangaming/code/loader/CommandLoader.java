package bryangaming.code.loader;


import bryangaming.code.FullPvP;
import bryangaming.code.commands.MainCommand;
import bryangaming.code.service.PluginService;

public class CommandLoader {

    private final PluginService service;

    public CommandLoader(PluginService pluginService){
        this.service = pluginService;

        setup();
        pluginService.getPlugin().getLogger().info("Commands loaded!");
    }

    public void setup(){
        FullPvP plugin = service.getPlugin();
        plugin.getCommand("FullPvP").setExecutor(new MainCommand(service));
    }
}
