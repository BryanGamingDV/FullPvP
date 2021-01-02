package bryangaming.code.data;

import bryangaming.code.manager.ConfigManager;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;


public class KothData {

    private Location mainPos1 = null;
    private Location mainPos2 = null;

    private List<ItemStack> inventory = null;

    private final ConfigManager configManager;
    private final String arenaName;
    private final int id;

    public KothData(ConfigManager configManager, String arenaName, int id){
        this.configManager = configManager;
        this.arenaName = arenaName;
        this.id = id;
    }


    public void setLocation(Location pos1, Location pos2){
        mainPos1 = pos1;
        mainPos2 = pos2;

        configManager.set("arenas." + arenaName + ".koths." + id + ".pos1", mainPos1);
        configManager.set("arenas." + arenaName + ".koths." + id + ".pos2", mainPos2);
        configManager.save();
    }

    public String getPos1(){
        return mainPos1.getX() + ", " + mainPos1.getY() + ", " + mainPos1.getZ();
    }

    public String getPos2(){
        return mainPos2.getX() + ", " + mainPos2.getY() + ", " + mainPos2.getZ();
    }

    public void addRewards(ItemStack itemStack){
        inventory.add(itemStack);
    }

    public void removeRewards(int id){
        inventory.remove(id);
    }

    public List<ItemStack> getRewards(){
        return inventory;
    }
    public boolean isLocated(Location location){
        return mainPos1.getX() <= location.getX() && mainPos2.getX() >= location.getX()
                && mainPos1.getY() <= location.getY() && mainPos2.getY() >= location.getY()
                && mainPos1.getZ() <= location.getZ() && mainPos2.getY() >= location.getZ();
    }

}
