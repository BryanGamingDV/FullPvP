package bryangaming.code.methods.events;

import bryangaming.code.data.PlayerData;
import bryangaming.code.group.CauseGroup;
import bryangaming.code.manager.CacheManager;
import bryangaming.code.manager.ConfigManager;
import bryangaming.code.methods.FormatStatic;
import bryangaming.code.methods.SenderManager;
import bryangaming.code.service.PluginService;
import bryangaming.code.utils.PathManager;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import java.nio.file.Path;
import java.util.Locale;
import java.util.UUID;

public class BroadcastMethod {

    private PluginService pluginService;

    private static CacheManager cacheManager;
    private static ConfigManager messages;
    private static ConfigManager config;

    public BroadcastMethod(PluginService pluginService){
        this.pluginService = pluginService;
        cacheManager = pluginService.getCache();
        messages = pluginService.getFiles().getMessages();
        config = pluginService.getFiles().getConfig();
    }

    public static void sendChat(UUID playeruuid, String message){

        Player player = Bukkit.getPlayer(playeruuid);
        PlayerData playerData = cacheManager.getPlayerData().get(playeruuid);

        for (Player online : Bukkit.getServer().getOnlinePlayers()) {
            PlayerData onlineData = cacheManager.getPlayerData().get(online.getUniqueId());
            if (!onlineData.isPlaying()){
                continue;
            }

            if (!onlineData.getArenaName().equalsIgnoreCase(playerData.getArenaName())){
                continue;
            }

            SenderManager.sendMessage(online, messages.getString("arena.chat-format")
                    .replace("%player%", player.getName())
                    .replace("%message%", PathManager.setColor(message))
                    .replace("%rank%", playerData.getRankup()));

        }

    }

    public static void sendDeath(String arena, EntityType entity, UUID victimuuid, DamageCause cause){

        Player victim = Bukkit.getPlayer(victimuuid);
        String entityname = entity.name().toLowerCase();

        for (Player player : Bukkit.getServer().getOnlinePlayers()){

            PlayerData playerData = cacheManager.getPlayerData().get(player.getUniqueId());
            if (!playerData.isPlaying()){
                continue;
            }

            if (!playerData.getArenaName().equalsIgnoreCase(arena)){
                continue;
            }


            SenderManager.sendMessage(player, formatMessage(cause, "global")
                    .replace("%killer%", StringUtils.capitalize(entityname))
                    .replace("%victim%", victim.getName()));
        }

        SenderManager.sendMessage(victim, formatMessage(cause, "victim")
                .replace("%killer%", StringUtils.capitalize(entityname)));


    }

    public static void sendDeath(String arena, UUID killeruuid, UUID victimuuid, DamageCause cause){

        Player killer = Bukkit.getPlayer(killeruuid);
        Player victim = Bukkit.getPlayer(victimuuid);

        for (Player player : Bukkit.getServer().getOnlinePlayers()){
            PlayerData playerData = cacheManager.getPlayerData().get(player.getUniqueId());
            if (!playerData.isPlaying()){
                continue;
            }

            if (!playerData.getAssists().isEmpty()){
                for (UUID uuid : playerData.getAssists()){
                    PlayerData assistData = cacheManager.getPlayerData().get(player.getUniqueId());

                    assistData.addCoins(config.getInt("arena.coins.assist"));
                    Player assistPlayer = Bukkit.getPlayer(uuid);
                    if (assistPlayer != null){
                        SenderManager.sendMessage(assistPlayer, messages.getString("deaths.assist")
                                    .replace("%victim%", victim.getName()));
                    }
                }
                playerData.getAssists().clear();
            }

            if (!playerData.getArenaName().equalsIgnoreCase(arena)){
                continue;
            }


            SenderManager.sendMessage(player, formatMessage(cause, "global")
                .replace("%killer%", killer.getName())
                .replace("%victim%", victim.getName()));
    }

        SenderManager.sendMessage(killer, formatMessage(cause, "victim")
                    .replace("%victim%", victim.getName()));

        SenderManager.sendMessage(victim, formatMessage(cause, "killer")
                    .replace("%killer%", killer.getName()));
    }

    public static void sendDeath(String arena, UUID victimuuid, DamageCause cause){

        Player victim = Bukkit.getPlayer(victimuuid);

        for (Player player : Bukkit.getServer().getOnlinePlayers()){
            PlayerData playerData = cacheManager.getPlayerData().get(player.getUniqueId());

            if (!playerData.isPlaying()){
                continue;
            }

            if (!playerData.getArenaName().equalsIgnoreCase(arena)){
                continue;
            }

            SenderManager.sendMessage(player, formatMessage(cause, "global")
                        .replace("%victim%", victim.getName()));
        }

        SenderManager.sendMessage(victim, formatMessage(cause, "victim")
                .replace("%victim%", victim.getName()));
    }


    private static String formatMessage(DamageCause cause, String mode){
        if (CauseGroup.getCause(cause) == null){
            return FormatStatic.setColor(messages.getString("deaths." + mode + ".other"));
        }
        return FormatStatic.setColor(messages.getString("deaths." + mode + "." + CauseGroup.getCause(cause)));
    }
}
