package bryangaming.code.data;

import bryangaming.code.manager.ConfigManager;
import bryangaming.code.utils.serializable.LocationSerializable;
import bryangaming.code.utils.serializable.UtilsSerializable;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ArenaData{

    private final ConfigManager arena;
    private final String arenaname;

    private final Map<Integer, KitsData> kitData = new HashMap<>();
    private final Map<Integer, KothData> kothData = new HashMap<>();
    private final Map<Integer, RegenChestData> regenChestData = new HashMap<>();

    private Location arenaLocation = null;

    public ArenaData(ConfigManager arena, String arenaname){
        this.arenaname = arenaname;
        this.arena = arena;

        arena.set("arenas." + arenaname + ".name", arenaname);
        arena.set("arenas." + arenaname + ".kits-automaticaly", false);
        kitData.put(0, new KitsData(arena, arenaname));
        setUp();
        arena.reload();
    }

    public ArenaData(ConfigManager arena, String arenaname, Boolean restoredata){
        this.arenaname = arenaname;
        this.arena = arena;

        if (restoredata) {
            kitData.put(0, new KitsData(arena, arenaname));
            return;
        }

        arena.reload();
    }

    public void setUp(){
        setDefaultItem(1, Material.IRON_HELMET);
        setDefaultItem(2, Material.IRON_CHESTPLATE);
        setDefaultItem(3, Material.IRON_LEGGINGS);
        setDefaultItem(4, Material.IRON_BOOTS);
    }


    public void setDefaultItem(int id, Material material){
        getKits().createItem(material, id);
        getKits().setItemFlags(id, "Default title", Arrays.asList("Default lore"), Arrays.asList("Protection,1"));
    }

    public KitsData getKits(){
        return kitData.get(0);
    }

    public void addRegenChest(Location loc) {
        int id = regenChestData.size() + 1;
        regenChestData.put(id, new RegenChestData(arena, arenaname, id));
        regenChestData.get(id).setLocation(loc);
    }

    public void removeRegenChest(){
        regenChestData.remove(regenChestData.size() + 1, new RegenChestData(arena, arenaname, regenChestData.size() + 1));
    }

    public void removeRegenChest(Integer id){
        regenChestData.remove(id);
    }

    public RegenChestData getRegenChest(int id){
        return regenChestData.get(id);
    }

    public Map<Integer, RegenChestData> getRegenChestManager(){
        return regenChestData;
    }

    public void createKoth(){
        int id = kothData.size() + 1;
        kothData.put(id, new KothData(arena, arenaname, id));
    }

    public KothData getKoth(Integer id){
        return kothData.get(id);
    }

    public Map<Integer, KothData> getKothManager(){
        return kothData;
    }

    public void deleteKoth(int id){
        kothData.remove(id);
    }

    public void deleteKoth(){
        kothData.remove(kothData.size() - 1);
    }

    public void setLobby(Location location){

        arenaLocation = location;

        arena.set("arenas." + arenaname + ".location", LocationSerializable.toString(arenaLocation));
        arena.save();
    }
    public void respawnAutomatic(Player player){
        player.spigot().respawn();

        UtilsSerializable.waitTeleport(player, arenaLocation, 1);
        UtilsSerializable.clearItems(player, 1);

    }
    public boolean isLobbySet(){
        return arenaLocation != null;
    }

    public Location getLobbyLocation(){
        return arenaLocation;
    }

    public String getArenaName(){
        return arenaname;
    }
}
