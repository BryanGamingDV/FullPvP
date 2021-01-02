package bryangaming.code.service;

import bryangaming.code.FullPvP;

import bryangaming.code.loader.CommandLoader;
import bryangaming.code.loader.ConfigLoader;
import bryangaming.code.loader.EventLoader;
import bryangaming.code.manager.CacheManager;
import bryangaming.code.utils.serializable.UtilsSerializable;

public class PluginService {

    private final FullPvP plugin;

    private CacheManager cacheService;

    private ConfigLoader configLoader;
    private CommandLoader commandLoader;
    private EventLoader eventLoader;

    private UtilsService utilsService;
    private MethodsService methodsService;
    private GroupServices groupServices;
    private SerializableServices serializableServices;

    public PluginService(FullPvP plugin){
        this.plugin = plugin;
        plugin.getLogger().info("Loading plugin..");
        setup();
    }

    public void setup(){
        cacheService = new CacheManager(this);

        configLoader = new ConfigLoader(this);
        commandLoader = new CommandLoader(this);
        eventLoader = new EventLoader(this);

        utilsService = new UtilsService(this);
        methodsService = new MethodsService(this);
        groupServices = new GroupServices(this);
        serializableServices = new SerializableServices(this);
    }

    public MethodsService getMethodsService(){
        return methodsService;
    }

    public EventLoader getEvents() {
        return eventLoader;
    }

    public CommandLoader getCommands() {
        return commandLoader;
    }

    public ConfigLoader getFiles() {
        return configLoader;
    }

    public CacheManager getCache(){
        return cacheService;
    }
    public UtilsService getUtils() {
        return utilsService;
    }

    public GroupServices getGroupServices(){
        return groupServices;
    }

    public SerializableServices getSerializable() {
        return serializableServices;
    }

    public FullPvP getPlugin() {
        return plugin;
    }
}
