package bryangaming.code.listeners;

import bryangaming.code.data.ArenaData;
import bryangaming.code.data.KothData;
import bryangaming.code.data.PlayerData;
import bryangaming.code.data.RegenChestData;
import bryangaming.code.service.PluginService;
import bryangaming.code.utils.manager.KothTimer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.UUID;

public class onKothListener implements Listener {

    private final PluginService pluginService;

    public onKothListener(PluginService pluginService){
        this.pluginService = pluginService;
    }

    @EventHandler
    public void onKoth(PlayerMoveEvent event) {

        UUID uuid = event.getPlayer().getUniqueId();
        PlayerData playerData = pluginService.getCache().getPlayerData().get(uuid);

        if (!playerData.isPlaying()) {
            return;
        }

        ArenaData arenaData = pluginService.getCache().getArena().get(playerData.getArenaName());

        for (KothData kothData : arenaData.getKothManager().values()) {
            if (!kothData.isLocated(event.getPlayer().getLocation())) {
                if (KothTimer.wasInKoth(uuid)){
                    KothTimer.removeCooldown(uuid);
                }
                return;
            }
            KothTimer.addKoth(uuid);

        }



    }
}
