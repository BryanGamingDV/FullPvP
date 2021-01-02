package bryangaming.code.data;

import bryangaming.code.loader.ConfigLoader;
import bryangaming.code.manager.ConfigManager;
import bryangaming.code.methods.RankupMethod;
import bryangaming.code.service.vault.VaultManager;
import bryangaming.code.utils.PluginConditions;
import com.sun.security.auth.login.ConfigFile;
import fr.minuskube.netherboard.Netherboard;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class PlayerData {

    private ConfigManager configManager;

    private int kills = 0;
    private final List<UUID> assistManager = new ArrayList<>();

    private int killstreak = 0;
    private int maxkillstreak = 0;

    private int deaths = 0;
    private int coins = 0;

    private String rankupName = RankupMethod.getFirstRank();
    private int rankupid = 0;

    private String arenaName = null;
    private List<ItemStack> lastInventory = new ArrayList<>();
    private List<ItemStack> lastArmor = new ArrayList<>();

    private Location lastLocation = null;

    private UUID uuid;
    private Player player;
    private String playername;

    private boolean editMode = false;
    private final Map<String, Location> posList = new HashMap<>();

    public PlayerData(ConfigLoader configManager, UUID uuid){
        this.uuid = uuid;
        this.configManager = configManager.getPlayers();

        player = Bukkit.getPlayer(uuid);
        this.playername = player.getName();
        Netherboard.instance().createBoard(player, "FullPvP Scoreboard");
        this.coins = configManager.getConfig().getInt("arena.default-coins");
        setup();
    }

    public PlayerData(ConfigManager configManager, UUID uuid, Boolean restoredata) {
        if (!restoredata) return;

        this.uuid = uuid;
        this.configManager = configManager;

        player = Bukkit.getPlayer(uuid);

        if (player == null){
            playername = null;
            return;
        }
        setOnlinePlayer(player);
    }

    public boolean isSet(){
        return playername != null;
    }

    public void setOnlinePlayer(Player player){
        playername = player.getName();
        Netherboard.instance().createBoard(player, "FullPvP Scoreboard");
        setup();

    }

    private void setup(){
        String nameString = configManager.getString("players." + uuid + ".name");
        if ((nameString == null || (!nameString.equalsIgnoreCase(playername)))){
            configManager.set("players." + uuid + ".name", playername);
        }
        configManager.set("players." + uuid + ".kills", kills);
        configManager.set("players." + uuid + ".max-killstreak", maxkillstreak);
        configManager.set("players." + uuid + ".deaths", deaths);
        configManager.set("players." + uuid + ".coins", coins);
        configManager.save();
    }

    public String getName(){
        return playername;
    }

    public int getKills() {
        return kills;
    }

    public int getKillStreak(){
        return killstreak;
    }

    public int getMaxKillStreak(){
        return maxkillstreak;
    }

    public int getDeaths() {
        return deaths;
    }

    public String getRankup(){
        return rankupName;
    }

    public int getRankupID(){
        return rankupid;
    }

    public int getCoins(){
        return coins;
    }

    public boolean isPlaying(){
        return arenaName != null;
    }
    public void setArenaName(String text){
        arenaName = text;
    }

    public String getArenaName(){
        return arenaName;
    }

    public Location getLastLocation(){
        return lastLocation;
    }

    public List<ItemStack> getLastInventory(){
        return lastInventory;
    }

    public List<ItemStack> getLastArmor(){
        return lastArmor;
    }

    public List<UUID> getAssists(){
        return assistManager;
    }

    public boolean isEditMode(){
        return editMode;
    }

    public Map<String, Location> getPosList(){
        return posList;
    }

    public void setEditMode(boolean status){
        editMode = status;
    }

    public void setLastInventory(List<ItemStack> itemStacks){
        lastInventory = itemStacks;
    }

    public void setLastArmor(List<ItemStack> armor){
        lastArmor = armor;
    }

    public void setLastLocation(Location location){
        lastLocation = location;
    }

    public void addKill(){
        kills++;
    }

    public void addKillStreak(){
        killstreak++;
    }

    public void addDeath(){
        deaths++;
    }

    public void addRankupID(){
        rankupid++;
    }

    public void setRankupName(String rank){
        rankupName = rank;
    }

    public void addAssist(UUID uuid){
        assistManager.add(uuid);
    }


    public void addCoins(int number){
        coins = coins + number;
        if (PluginConditions.IsVaultEnabled()){
            VaultManager.getEconomy().depositPlayer(player, number);
        }
    }



    public void removeCoins(int number){
        coins = coins - number;
        if (PluginConditions.IsVaultEnabled()) {
            VaultManager.getEconomy().withdrawPlayer(player, number);
        }
    }

    public Player getPlayer(){
        return player;
    }

    public void setKills(int number){
        kills = number;
    }

    public void setKillStreak(int number){
        killstreak = number;
    }

    public void setMaxKillStreak(int number){
        maxkillstreak = number;
    }

    public void setDeaths(int number){
        deaths = number;
    }

    public void setCoins(int number){
        coins = number;
    }
}
