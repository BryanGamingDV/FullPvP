package bryangaming.code.commands.shop;

import bryangaming.code.data.ShopData;
import bryangaming.code.loader.ConfigLoader;
import bryangaming.code.manager.CacheManager;
import bryangaming.code.methods.SenderManager;
import bryangaming.code.service.PluginService;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class ShopEvent {

    private final PluginService service;
    private final ConfigLoader configLoader;

    public ShopEvent(PluginService service) {
        this.service = service;
        this.configLoader = service.getFiles();
    }

    public boolean onShop(CommandSender sender) {
        HashMap<Integer, ShopData> shops = service.getCache().getShops();

        if (!(sender instanceof Player)) {
            service.getPlugin().getLogger().info(service.getFiles().getMessages().getString("error.messages.no-console"));
            return true;
        }

        Player player = (Player) sender;

        for (String shopid : service.getFiles().getShops().getConfigurationSection("shops").getKeys(false)){
            ShopData shop = shops.get(Integer.parseInt(shopid));

            if (!shop.isMain()){
                continue;
            }

            player.openInventory(shop.getShop());
            return true;
        }

        return true;
    }
}