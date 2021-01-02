package bryangaming.code.utils.serializable;

import bryangaming.code.methods.events.BroadcastMethod;
import org.bukkit.Bukkit;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemsSerializable {

    public static List<ItemStack> getContents(Player player){
        PlayerInventory inventory = player.getInventory();
        return new ArrayList<>(Arrays.asList(inventory.getContents()));
    }

    public static List<ItemStack> getArmorContents(Player player){
        PlayerInventory inventory = player.getInventory();
        return new ArrayList<>(Arrays.asList(inventory.getArmorContents()));
    }

    public static void setArmors(Player player, List<ItemStack> armors){
        PlayerInventory inventory = player.getInventory();
        inventory.setBoots(armors.get(0));
        inventory.setLeggings(armors.get(1));
        inventory.setChestplate(armors.get(2));
        inventory.setHelmet(armors.get(3));
    }

    public static void clearItems(Player player){
        PlayerInventory inventory = player.getInventory();

        inventory.clear();
        inventory.setHelmet(null);
        inventory.setChestplate(null);
        inventory.setLeggings(null);
        inventory.setBoots(null);
    }
}
