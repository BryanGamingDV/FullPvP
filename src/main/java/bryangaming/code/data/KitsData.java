package bryangaming.code.data;

import bryangaming.code.group.EnchantmentGroup;
import bryangaming.code.manager.ConfigManager;
import bryangaming.code.methods.events.BroadcastMethod;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.event.server.BroadcastMessageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class KitsData{

    private final ConfigManager configManager;
    private final String arenaName;

    private final Map<Integer, ItemStack> items = new HashMap<>();

    public KitsData(ConfigManager configManager, String arenaName){
        this.configManager = configManager;
        this.arenaName = arenaName;
    }


    public void createItem(Material material, int id){
        items.put(id, new ItemStack(material));

        if (configManager.getString("arenas." + arenaName + ".kits." + id + ".id") == null){
            configManager.set("arenas." + arenaName + ".kits." + id + ".id", material.name());
        }
    }

    public void setItemFlags(int id, String name, List<String> lore, List<String> enchantment){

        configManager.set("arenas." + arenaName + ".kits." + id + ".name", name);
      configManager.set("arenas." + arenaName + ".kits." + id + ".lore", lore);
      configManager.set("arenas." + arenaName + ".kits." + id + ".enchantment", enchantment);
      configManager.save();

      ItemMeta itemMeta = items.get(id).getItemMeta();

      itemMeta.setDisplayName(name);
      itemMeta.setLore(lore);

      for (String enchant : enchantment){
          Enchantment enchantname = EnchantmentGroup.getEnchantment(enchant.split(",")[0].toLowerCase());
          itemMeta.addEnchant(enchantname, Integer.parseInt(enchant.split(",")[1]), true);
      }

      items.get(id).setItemMeta(itemMeta);

    }

    public void createFullItem(Material material, int id){
        items.put(id, new ItemStack(material));

        ItemMeta itemMeta = items.get(id).getItemMeta();

        itemMeta.setDisplayName(configManager.getString("arenas." + arenaName + ".kits." + id + ".name"));
        itemMeta.setLore(configManager.getStringList("arenas." + arenaName + ".kits." + id + ".lore"));

        for (String enchant : configManager.getStringList("arenas." + arenaName + ".kits." + id + ".enchantment")){
            Enchantment enchantname = EnchantmentGroup.getEnchantment(enchant.split(",")[0].toLowerCase());
            itemMeta.addEnchant(enchantname, Integer.parseInt(enchant.split(",")[1]), true);
        }

        items.get(id).setItemMeta(itemMeta);
    }

    public Map<Integer, ItemStack> getData(){
        return items;
    }

    public int getKitSize(){
        return items.size();
    }

    public ItemStack getItem(int id){
        return items.get(id);
    }
}
