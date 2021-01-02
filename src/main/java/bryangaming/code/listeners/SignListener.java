package bryangaming.code.listeners;

import bryangaming.code.data.ArenaData;
import bryangaming.code.data.PlayerData;
import bryangaming.code.data.ShopData;
import bryangaming.code.manager.ConfigManager;
import bryangaming.code.methods.SenderManager;
import bryangaming.code.methods.commands.JoinMethod;
import bryangaming.code.methods.commands.QuitMethod;
import bryangaming.code.service.PluginService;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.net.http.WebSocket;
import java.util.List;

public class SignListener implements Listener{

    private final PluginService pluginService;

    public SignListener(PluginService pluginService){
        this.pluginService = pluginService;
    }

    @EventHandler
    public void onSign(SignChangeEvent sign){

        ConfigManager messages = pluginService.getFiles().getMessages();
        Player player = sign.getPlayer();

        if (sign.getLine(0).equalsIgnoreCase("[Shop]")){
            sign.setLine(0, getSignLine("shop"));
            return;
        }

        if (sign.getLine(0).equalsIgnoreCase("[Buy]")){
            setSignVault(sign, "buy");
            return;
        }
        if (sign.getLine(0).equalsIgnoreCase("[Sell]")){
            setSignVault(sign, "sell");
            return;
        }

        if (sign.getLine(0).equalsIgnoreCase("[Join]")){
            if (sign.getLine(1) == null){
                SenderManager.sendMessage(player, messages.getString("error.signs.join.nline-2"));
                return;
            }
            sign.setLine(0, getSignLine("join"));
        }

        if (sign.getLine(0).equalsIgnoreCase("[Quit]")){
            if (sign.getLine(1) == null){
                SenderManager.sendMessage(player, messages.getString("error.signs.quit.nline-2"));
                return;
            }
            sign.setLine(0, getSignLine("quit"));
        }

    }
    @EventHandler
    public void onEvent(PlayerInteractEvent event){

        Player player = event.getPlayer();
        PlayerData playerData = pluginService.getCache().getPlayerData().get(player.getUniqueId());

        Block block = event.getClickedBlock();
        BlockState state = block.getState();

        if (!(block instanceof Sign)){
            return;
        }
        Sign sign = (Sign) state;

        if (sign.getLine(0).equalsIgnoreCase(getSignLine("buy"))) {
            if (playerData.getCoins() < Integer.parseInt(sign.getLine(1))){
                return;
            }

            playerData.removeCoins(Integer.parseInt(sign.getLine(1)));
            player.getInventory().addItem(new ItemStack(Material.getMaterial(sign.getLine(3)),Integer.parseInt(sign.getLine(2))));

        }

        if (sign.getLine(0).equalsIgnoreCase(getSignLine("sell"))) {
            if (!(player.getInventory().getItemInMainHand().getType().name().equalsIgnoreCase(sign.getLine(3)))){
                return;
            }

            playerData.addCoins(Integer.parseInt(sign.getLine(1)));

            playerData.removeCoins(Integer.parseInt(sign.getLine(1)));
            player.getInventory().addItem(new ItemStack(Material.getMaterial(sign.getLine(3)),Integer.parseInt(sign.getLine(2))));

        }
        if (sign.getLine(0).equalsIgnoreCase(getSignLine("join"))) {
            ArenaData arenaData = pluginService.getCache().getArena().get(sign.getLine(1));

            if (arenaData == null){
                return;
            }

            JoinMethod.joinGame(player.getUniqueId(), arenaData.getArenaName());

        }
        if (sign.getLine(0).equalsIgnoreCase(getSignLine("quit"))) {

            QuitMethod.quitGame(player.getUniqueId());
        }
        if (sign.getLine(0).equalsIgnoreCase(getSignLine("shop"))) {
            ShopData shopData;

            if (sign.getLine(1).trim().isEmpty()) {
                for (ShopData shopKey : pluginService.getCache().getShops().values()) {
                    if (shopKey.isMain()) {
                        shopData = shopKey;
                    }
                }
                shopData = null;
            }else {
                shopData = pluginService.getCache().getShops().get(Integer.parseInt(sign.getLine(1)));
            }

            if (shopData == null){
                return;
            }

            player.openInventory(shopData.getShop());
        }

    }

    public String getSignLine(String data){
        ConfigManager config = pluginService.getFiles().getConfig();
        return config.getString("signs." + data);
    }

    public void setSignVault(SignChangeEvent sign, String data){

        Player player = sign.getPlayer();
        ConfigManager messages = pluginService.getFiles().getMessages();

        if (sign.getLine(1) == null){
            sign.setLine(0, "&c[Error]");
            SenderManager.sendMessage(player, messages.getString("error.signs." + data + ".nline-1.didnt-put"));
            return;
        }

        if (Integer.parseInt(sign.getLine(1)) < 0){
            sign.setLine(0, "&c[Error]");
            SenderManager.sendMessage(player, messages.getString("error.signs." + data + ".nline-1.negative"));
            return;
        }

        if (sign.getLine(2) == null){
            sign.setLine(0, "&c[Error]");
            SenderManager.sendMessage(player, messages.getString("error.signs." + data + ".nline-2.didnt-put"));
            return;
        }
        if (Integer.parseInt(sign.getLine(2)) < 0){
            sign.setLine(0, "&c[Error]");
            SenderManager.sendMessage(player, messages.getString("error.signs." + data + ".nline-2.negative"));
            return;
        }

        if (sign.getLine(3) == null){
            sign.setLine(0, "&c[Error]");
            SenderManager.sendMessage(player, messages.getString("error.signs." + data + ".nline-3.didnt-put"));
            return;
        }

        if (Material.getMaterial(sign.getLine(3)) == null){
            sign.setLine(0, "&c[Error]");
            SenderManager.sendMessage(player, messages.getString("error.signs." + data + ".nline-3.no-exists"));
            return;
        }

        sign.setLine(0, getSignLine(data));
    }
}
