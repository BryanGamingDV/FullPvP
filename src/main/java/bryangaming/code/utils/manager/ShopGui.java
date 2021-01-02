package bryangaming.code.utils.manager;

import bryangaming.code.data.ShopData;
import bryangaming.code.loader.ConfigLoader;
import bryangaming.code.manager.CacheManager;
import bryangaming.code.manager.ConfigManager;
import bryangaming.code.service.PluginService;
import bryangaming.code.service.UtilsService;
import bryangaming.code.utils.serializable.UtilsSerializable;
import org.bukkit.entity.Player;
import team.unnamed.gui.core.gui.GUIBuilder;

import java.util.ArrayList;
import java.util.Set;

public class ShopGui {

    private PluginService pluginService;
    private static ConfigManager shops;
    private static CacheManager cacheManager;

    public ShopGui(PluginService pluginService){
        this.pluginService = pluginService;
        shops = pluginService.getFiles().getShops();
        cacheManager = pluginService.getCache();
        reloadShops();
    }

    private static void reloadShops(){
        for (String shopid : getShops()){
            cacheManager.getShops().put(Integer.parseInt(shopid), new ShopData(cacheManager, shops, shopid));
        }
    }

    private static Set<String> getShops(){
        return shops.getConfigurationSection("shops.").getKeys(false);
    }
}
