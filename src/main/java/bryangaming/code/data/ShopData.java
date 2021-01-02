package bryangaming.code.data;

import bryangaming.code.manager.CacheManager;
import bryangaming.code.manager.ConfigManager;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import team.unnamed.gui.abstraction.item.ItemClickable;
import team.unnamed.gui.core.gui.GUIBuilder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ShopData {

    private CacheManager cache;
    private ConfigManager shops;
    private final String shopid;


    public ShopData(CacheManager cache, ConfigManager shops, String shopid){
        this.shopid = shopid;
        this.shops = shops;
        this.cache = cache;
    }

    public Inventory getShop(){
        GUIBuilder guiBuilder = GUIBuilder.builder(shopid, getIntData("rows"));
        for (String items : getItems()){
            if (isItemSet(items)){
                continue;
            }

            int id = Integer.parseInt(items);
            ItemStack itemStack = createItemStack(id, "item");

            String title = shops.getString("config.title")
                    .replace("item", getItemData(id, "material"));

            List<String> shopslore = shops.getStringList("config.lore");
            shopslore.forEach(text -> text
                    .replace("%costs%", getItemData(id, "costs")));

            setItemMeta(itemStack, title, shopslore);

            guiBuilder.addItem(ItemClickable.builder(id)
                    .setItemStack(itemStack)
                    .setAction(event -> {
                        giveItem(event.getWhoClicked(), id, itemStack);
                        return true;
                    })
                    .build());
        }

        for (String items : getChangePage()){
            int id = Integer.parseInt(items);

            ItemStack itemStack = createItemStack(id, "change-page");
            setItemMeta(itemStack, getChangeData(id, "title"), getListChangeData(id, "lore"));

            guiBuilder.addItem(ItemClickable.builder(id)
                    .setItemStack(itemStack)
                    .setAction(event -> {
                        changePage(event.getWhoClicked(), id);
                        return true;
                    })
                    .build());
        }

        int id = Integer.parseInt(getClose("id"));

        ItemStack itemStack = createItemStack(id, "close");
        setItemMeta(itemStack, getClose("title"), getLoreClose("lore"));

        guiBuilder.addItem(ItemClickable.builder(id)
                .setItemStack(itemStack)
                .setAction(event -> {
                    closePage(event.getWhoClicked());
                    return true;
                })
                .build());

        return guiBuilder.build();
    }
    public ItemStack createItemStack(int id, String type){
        Material material;
        int amount;

        if (type.equalsIgnoreCase("item")){
            material = Material.getMaterial(getItemData(id, "material"));
            amount = Integer.parseInt(getItemData(id, "amount"));

        }else if (type.equalsIgnoreCase("change-page")){
            material = Material.getMaterial(getChangeData(id, "material"));
            amount = Integer.parseInt(getChangeData(id, "amount"));

        }else{
            material = Material.getMaterial(getClose("material"));
            amount = Integer.parseInt(getClose("amount"));
        }

        return new ItemStack(material, amount);
    }

    public void setItemMeta(ItemStack itemStack, String title, List<String> lore){
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName(title);
        itemMeta.setLore(lore);

        itemStack.setItemMeta(itemMeta);
    }

    public boolean isItemSet(String id) {
        Set<String> changepage = new HashSet<>(shops.getConfigurationSection("shops." + shopid + ".change-page").getKeys(false));

        if (changepage.contains(id)){
            return true;
        }

        if (shops.getString("shops" + shopid + ".change-page").equalsIgnoreCase(id)){
            return true;
        }

        return false;
    }

    public Integer getIntData(String data){
        return shops.getInt("shops. " + shopid + "." + data);
    }

    public boolean isMain(){
        return shops.getBoolean("shops." + shopid + ".main");
    }

    public void giveItem(HumanEntity player, int id, ItemStack itemStack){
        PlayerData playerData = cache.getPlayerData().get(player.getUniqueId());

        int moneycosts = Integer.parseInt(getItemData(id, "costs"));

        if (playerData.getCoins() < moneycosts){
            player.closeInventory();
            return;
        }

        playerData.removeCoins(moneycosts);
        player.getInventory().addItem(itemStack);
    }

    public void changePage(HumanEntity player, int id){
        ShopData shopData = cache.getShops().get(id);

        if (shopData == null) {
            player.closeInventory();
            return;
        }

        player.closeInventory();
        player.openInventory(shopData.getShop());
    }

    public void closePage(HumanEntity player){
        player.closeInventory();
    }

    public Set<String> getItems(){
        return shops.getConfigurationSection("shops." + shopid + ".ids").getKeys(false);
    }


    public Set<String> getChangePage(){
        return shops.getConfigurationSection("shops." + shopid + ".change-modes").getKeys(false);
    }


    public String getItemData(int id, String data){
        return shops.getString("shops. " + shopid + ".ids." + id + "." + data);
    }

    public String getChangeData(int id, String data) {
        return shops.getString("shops." + shopid + ".change-modes." + id + "." + data);
    }

    public List<String> getListChangeData(int id, String data){
        return shops.getStringList("shops." + shopid + ".change-modes." + id + "." + data);
    }

    public String getClose(String data){
        return shops.getString("shops." + shopid + ".close." + data);
    }

    public List<String> getLoreClose(String data){
        return shops.getStringList("shops." + shopid + ".close." + data);
    }
}
