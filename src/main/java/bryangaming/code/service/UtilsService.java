package bryangaming.code.service;


import bryangaming.code.methods.OnlineProtocol;
import bryangaming.code.service.vault.VaultManager;
import bryangaming.code.utils.PathManager;
import bryangaming.code.utils.PluginConditions;
import bryangaming.code.utils.ReloadData;
import bryangaming.code.utils.manager.ShopGui;

public class UtilsService {

    private final PluginService pluginService;

    private ReloadData reloadData;

    private PathManager pathManager;
    private OnlineProtocol onlineProtocol;
    private PluginConditions pluginConditions;

    private VaultManager vaultManager;

    private ShopGui shopGui;

    public UtilsService(PluginService pluginService){
        this.pluginService = pluginService;
        setup();
    }

    public void setup(){
        reloadData = new ReloadData(pluginService);

        pathManager = new PathManager(pluginService);
        onlineProtocol = new OnlineProtocol(pluginService);
        pluginConditions = new PluginConditions(pluginService);

        vaultManager = new VaultManager(pluginService);

        shopGui = new ShopGui(pluginService);
    }

    public ReloadData getReloadData() {
        return reloadData;
    }

    public PathManager getPathManager(){
        return pathManager;
    }

    public OnlineProtocol getOnlineProtocol() {
        return onlineProtocol;
    }

    public PluginConditions getPluginConditions() {
        return pluginConditions;
    }

    public VaultManager getVault(){
        return vaultManager;
    }

    public ShopGui getShopGui() {
        return shopGui;
    }
}
