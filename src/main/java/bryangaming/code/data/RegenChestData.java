package bryangaming.code.data;

import bryangaming.code.manager.ConfigManager;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;


public class RegenChestData {

    private final List<ItemStack> itemStack = new ArrayList<>();
    private Location location = null;

    private final ConfigManager configManager;
    private final String arenaName;
    private final int id;

    public RegenChestData(ConfigManager configManager, String arenaName, int id){
        this.configManager = configManager;
        this.arenaName = arenaName;
        this.id = id;
    }

    public void setLocation(Location loc){
        location = loc;
    }

    public boolean isLocated(Location loc){
        return loc == location;
    }
    public String getLocation(){
        return location.getX() + ", " + location.getY() + ", " + location.getZ();
    }

    public void addItem(ItemStack item){
        itemStack.add(item);
    }

    public void removeItem(){
        itemStack.remove(itemStack.size() -1);
    }

    public List<ItemStack> getItemStack(){
        return itemStack;
    }

}
