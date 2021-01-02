package bryangaming.code.listeners;

import bryangaming.code.data.ArenaData;
import bryangaming.code.data.PlayerData;
import bryangaming.code.manager.ConfigManager;
import bryangaming.code.methods.RankupMethod;
import bryangaming.code.methods.events.BroadcastMethod;
import bryangaming.code.service.PluginService;
import org.bukkit.Bukkit;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.UUID;

public class FightListener implements Listener{

    private final PluginService pluginService;

    public FightListener(PluginService pluginService){
        this.pluginService = pluginService;
    }

    @EventHandler
    public void onKill(PlayerDeathEvent event) {

        Player player = event.getEntity();
        UUID victimUUID = player.getUniqueId();

        PlayerData victimData = pluginService.getCache().getPlayerData().get(victimUUID);

        if (!victimData.isPlaying()) {
            return;
        }

        ArenaData arenaData = pluginService.getCache().getArena().get(victimData.getArenaName());
        ConfigManager config = pluginService.getFiles().getConfig();

        arenaData.respawnAutomatic(player);
        event.setDeathMessage(null);

        victimData.addDeath();
        victimData.addCoins(config.getInt("arena.coins.victim"));

        if (victimData.getCoins() + config.getInt("arena.coins.victim") > 0){
            return;
        }

        if (victimData.getKillStreak() > 0){
            victimData.setKillStreak(0);
        }

        System.out.println("Event cause" + player.getLastDamageCause().getCause().name());

        if (player.getLastDamageCause().getCause() != DamageCause.ENTITY_ATTACK && player.getLastDamageCause().getCause() != DamageCause.PROJECTILE) {
            BroadcastMethod.sendDeath(victimData.getArenaName(), victimUUID, player.getLastDamageCause().getCause());
            return;
        }

        if (player.getKiller() == null){
            return;
        }

        UUID killerUUID = player.getKiller().getUniqueId();
        PlayerData killerData = pluginService.getCache().getPlayerData().get(killerUUID);

        killerData.addKill();
        killerData.addCoins(config.getInt("arena.coins.attacker"));

        if (RankupMethod.hasRequeriments(player)) {
            RankupMethod.rankupPlayer(player);
        }
        killerData.addKillStreak();

        for (String killStreakKey : config.getConfigurationSection("arena.coins.killstreak").getKeys(false)){
            int data = config.getInt("arena.coins.killstreak." + killStreakKey, -1);
            if (data < 0){
                continue;
            }

            killerData.addCoins(data);
        }

        if (killerData.getMaxKillStreak() < killerData.getKillStreak()){
            killerData.setMaxKillStreak(killerData.getKillStreak());
        }

        BroadcastMethod.sendDeath(victimData.getArenaName(), killerUUID, victimUUID, player.getLastDamageCause().getCause());
    }

    @EventHandler
    public void onMobKill(EntityDamageByEntityEvent event){

        System.out.println("Killer: " + event.getDamager().getType().name());
        System.out.println("Victim: " + event.getEntity().getType().name());

        if (event.getDamager() instanceof Player){
            PlayerData victimData = pluginService.getCache().getPlayerData().get(event.getEntity().getUniqueId());
            victimData.addAssist(event.getDamager().getUniqueId());
            return;
        }

        if (!(event.getEntity() instanceof Player)){
            return;
        }

        Player player = (Player) event.getEntity();
        UUID victimUUID = player.getUniqueId();

        PlayerData victimData = pluginService.getCache().getPlayerData().get(victimUUID);

        if (!victimData.isPlaying()){
            return;
        }

        if (event.getCause() != DamageCause.ENTITY_ATTACK && event.getCause() != DamageCause.PROJECTILE){
            return;
        }

        if (!(player.getHealth() <= event.getDamage())){
            return;
        }

        if (event.getCause() == DamageCause.PROJECTILE){
            Arrow arrow = (Arrow) event.getDamager();

            if (arrow.getShooter() instanceof Player){
                Player playerShooter = (Player) arrow.getShooter();
                BroadcastMethod.sendDeath(victimData.getArenaName(), playerShooter.getUniqueId(), victimUUID, player.getLastDamageCause().getCause());
                return;
            }

            Entity entityShooter = (Entity) arrow.getShooter();
            BroadcastMethod.sendDeath(victimData.getArenaName(), entityShooter.getType(), victimUUID, player.getLastDamageCause().getCause());
            victimData.getAssists().clear();
            return;
        }
        BroadcastMethod.sendDeath(victimData.getArenaName(), event.getDamager().getType(), victimUUID, player.getLastDamageCause().getCause());
        victimData.getAssists().clear();
    }

}
