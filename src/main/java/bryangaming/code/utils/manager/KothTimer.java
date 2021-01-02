package bryangaming.code.utils.manager;

import bryangaming.code.FullPvP;
import bryangaming.code.data.KothData;
import bryangaming.code.data.PlayerData;
import bryangaming.code.manager.CacheManager;
import bryangaming.code.service.PluginService;
import com.google.common.xml.XmlEscapers;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class KothTimer {

    private static final HashMap<UUID, BukkitRunnable> bukkitRunnable = new HashMap<>();
    private static int time = getTime();

    private PluginService pluginService;
    private static FullPvP plugin;
    private static CacheManager cacheManager;

    public KothTimer(PluginService pluginService) {
        this.pluginService = pluginService;
        plugin = pluginService.getPlugin();
        cacheManager = pluginService.getCache();

    }

    public static void addKoth(UUID uuid) {
        if (bukkitRunnable.size() >= 1) {
            for (UUID online : bukkitRunnable.keySet()) {
                removeCooldown(online);
            }
        }

        addCooldown(uuid);
        bukkitRunnable.get(uuid).runTaskLater(plugin, 20L);
    }

    public static boolean wasInKoth(UUID uuid){
        return bukkitRunnable.get(uuid) != null;
    }

    public static void removeCooldown(UUID uuid){
        bukkitRunnable.remove(uuid);
    }

    private static void addCooldown(UUID uuid, String arena, int id){
        time = getTime();
        KothData kothData = cacheManager.getArena().get(arena).getKoth(id);
        Player player = Bukkit.getPlayer(uuid);

        bukkitRunnable.put(uuid, new BukkitRunnable() {
            @Override
            public void run() {
                if (time == 0){

                    for (ItemStack itemStack : kothData.getRewards()){
                        if (player.getInventory().getContents().length < 36){

                            cancel();
                        }
                        player.getInventory().addItem(itemStack);
                    }
                    cancel();

                }
                time--;
            }
        });
    }


    private static int getTime(){
        return 500;
    }
}
