package bryangaming.code.methods.mode;

import bryangaming.code.data.PlayerData;
import bryangaming.code.manager.CacheManager;
import bryangaming.code.service.PluginService;
import bryangaming.code.utils.serializable.LocationSerializable;
import org.bukkit.Location;

import java.util.*;

public class EditMode {

    private static CacheManager cacheManager;

    private static String arena;
    private static int id;

    private PluginService pluginService;

    public EditMode(PluginService pluginService){
        this.pluginService = pluginService;
        cacheManager = pluginService.getCache();
    }

    public static void startEditMode(UUID uuid, String arenaname, int kothid) {
        arena = arenaname;
        id = kothid;
        sendEditMode(uuid);
    }

    public static void sendEditMode(UUID uuid){

        PlayerData playerData = cacheManager.getPlayerData().get(uuid);
        Map<String, Location> posList = playerData.getPosList();

        if (!playerData.isEditMode()) playerData.setEditMode(true);

        if (posList.get("pos1") == null){
            return;
        }

        if (posList.get("pos2") == null){
            return;
        }

        cacheManager.getArena().get(arena).getKoth(id).setLocation(posList.get("pos1"), posList.get("pos2"));

    }
}
