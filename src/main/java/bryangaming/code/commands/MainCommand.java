package bryangaming.code.commands;

import bryangaming.code.commands.shop.ShopEvent;
import bryangaming.code.data.ArenaData;
import bryangaming.code.data.KothData;
import bryangaming.code.data.RegenChestData;
import bryangaming.code.loader.ConfigLoader;
import bryangaming.code.methods.SenderManager;

import bryangaming.code.methods.commands.CreateMethod;
import bryangaming.code.methods.commands.DeleteMethod;
import bryangaming.code.methods.commands.JoinMethod;
import bryangaming.code.methods.commands.QuitMethod;
import bryangaming.code.methods.mode.EditMode;
import bryangaming.code.service.PluginService;
import bryangaming.code.manager.ConfigManager;

import bryangaming.code.utils.PathManager;
import bryangaming.code.utils.serializable.LocationSerializable;
import com.google.common.base.Supplier;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


import java.util.*;
import java.util.logging.ErrorManager;

public class MainCommand implements CommandExecutor {

    private final PluginService service;
    private final ConfigLoader configLoader;
    private final ShopEvent shopEvent;

    public MainCommand(PluginService service) {
        this.service = service;
        this.configLoader = service.getFiles();
        shopEvent = new ShopEvent(service);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        ConfigManager messages = configLoader.getMessages();

        String argsText = String.join(", ", args);
        String subArgsText = PathManager.setStringLimit(args, 1);

        if (!(sender instanceof Player)) {
            service.getPlugin().getLogger().info(messages.getString("error.messages.no-console"));
            return true;
        }

        if (command.getName().equalsIgnoreCase("shops")){
            shopEvent.onShop(sender);
            return true;
        }

        Player player = (Player) sender;

        if (args.length < 1) {
            messages.getStringList("command.help").forEach(text -> SenderManager.sendMessage(player, text));
            return true;
        }

        if (args[0].equalsIgnoreCase("help")) {
            messages.getStringList("command.help").forEach(text -> SenderManager.sendMessage(player, text));
            return true;
        }
        if (args[0].equalsIgnoreCase("stats")){
            List<String> statsList = messages.getStringList("command.stats"); ;

            if (args.length < 2) {
                PathManager.replaceStatsLines(player, statsList);

            } else {
                Player playerStats = Bukkit.getPlayer(args[1]);

                if (playerStats == null) {
                    SenderManager.sendMessage(player, messages.getString("error.message.offline")
                            .replace("%player%", args[1]));
                    return true;
                }
                PathManager.replaceStatsLines(playerStats, statsList);
            }

            statsList.forEach(text -> SenderManager.sendMessage(player, text));
            return true;

        }
        if (args[0].equalsIgnoreCase("join")) {
            if (args.length < 2) {
                SenderManager.sendMessage(player, messages.getString("error.message.unknown-arg")
                        .replace("%usage%", "/" + command.getName() + " " + argsText + " [<arena>]"));
                return true;
            }

            JoinMethod.joinGame(player.getUniqueId(), args[1]);
            return true;
        }

        if (args[0].equalsIgnoreCase("quit")) {
            QuitMethod.quitGame(player.getUniqueId());
            return true;
        }

        if (args[0].equalsIgnoreCase("create")) {
            if (args.length < 2) {
                SenderManager.sendMessage(player, messages.getString("error.message.unknown-arg")
                        .replace("%usage%", "/" + command.getName() + " " + args[0] + " [<arena>]"));
                return true;
            }

            CreateMethod.createArena(player.getUniqueId(), args[1]);
            return true;
        }

        if (args[0].equalsIgnoreCase("delete")) {
            if (args.length < 2) {
                SenderManager.sendMessage(player, messages.getString("error.message.unknown-arg")
                        .replace("%usage%", "/" + command.getName() + " " + args[0] + " [<arena>]"));
                return true;
            }

            DeleteMethod.deleteArena(player.getUniqueId(), args[1]);
            return true;
        }

        if (args[0].equalsIgnoreCase("arena")) {
            if (args.length < 2) {
                SenderManager.sendMessage(player, messages.getString("error.message.unknown-arg")
                        .replace("%usage%", "/" + command.getName() + " " + args[0] + " [<arena>]"));
                return true;
            }
            ArenaData arenaData = service.getCache().getArena().get(args[1]);

            if (arenaData == null) {
                SenderManager.sendMessage(player, messages.getString("error.arena.no-exists")
                        .replace("%arena%", args[1]));
                return true;
            }

            if (args.length < 3) {
                messages.getStringList("command.arena")
                        .forEach(text -> SenderManager.sendMessage(player, text.replace("%arena%", args[1])));
                return true;
            }

            if (args[2].equalsIgnoreCase("help")){
                messages.getStringList("command.arena")
                        .forEach(text -> SenderManager.sendMessage(player, text.replace("%arena%", args[1])));
                return true;
            }

            if (args[2].equalsIgnoreCase("setlobby")) {

                Location location = player.getLocation();

                arenaData.setLobby(location);
                SenderManager.sendMessage(player.getPlayer(), messages.getString("arena.lobby-created")
                            .replace("%location", LocationSerializable.toString(location)));
                return true;
            }
            if (args[2].equalsIgnoreCase("regenchest")){

                if (args.length < 4){
                    messages.getStringList("messages.regenchest")
                            .forEach(text -> SenderManager.sendMessage(player, text
                                    .replace("%arena%", args[1])));
                    return true;
                }

                if (args[3].equalsIgnoreCase("help")){
                    messages.getStringList("messages.regenchest")
                            .forEach(text -> SenderManager.sendMessage(player, text
                                    .replace("%arena%", args[1])));
                    return true;
                }
                if (args[3].equalsIgnoreCase("create")){
                    Location location = player.getTargetBlock(null, 5).getLocation();
                    arenaData.addRegenChest(location);
                    return true;
                }
                if (args[3].equalsIgnoreCase("delete")){
                    if (args.length < 5){
                        arenaData.removeRegenChest();
                        return true;
                    }

                    arenaData.removeRegenChest(Integer.parseInt(args[4]));
                }

                if (args[3].equalsIgnoreCase("list")){
                    if (arenaData.getKothManager().keySet().size() < 1){
                        SenderManager.sendMessage(player, messages.getString("error.arena.regenchest.list-empty"));
                        return false;
                    }
                    SenderManager.sendMessage(player, messages.getString("command.list.space"));
                    SenderManager.sendMessage(player, messages.getString("command.list.regenchest.format")
                            .replace("%arena%", args[1]));

                    int id = 1;
                    for (RegenChestData regenChestData : arenaData.getRegenChestManager().values()){
                        SenderManager.sendMessage(player, "&8- &f" + id + "&8. -> " + regenChestData.getLocation());
                    }
                    SenderManager.sendMessage(player, messages.getString("command.list.space"));
                    return true;
                }

                if (args[3].equalsIgnoreCase("id")){
                    RegenChestData regenChestData = arenaData.getRegenChest(Integer.parseInt(args[4]));

                    if (regenChestData == null){
                        SenderManager.sendMessage(player, messages.getString("error.message.unknown-id")
                                .replace("%usage%", "/" + command.getName() + " " + subArgsText +  "<id>"));
                        return true;
                    }

                    if (args.length < 6){
                        messages.getStringList("messages.regenchestid")
                                .forEach(text -> SenderManager.sendMessage(player, text
                                        .replace("%arena%", args[1])
                                        .replace("%id%", args[4])));
                        return true;
                    }

                    if (args[5].equalsIgnoreCase("help")){
                        messages.getStringList("messages.regenchestid")
                                .forEach(text -> SenderManager.sendMessage(player, text
                                        .replace("%arena%", args[1])
                                        .replace("%id%", args[4])));
                        return true;
                    }

                    if (args[5].equalsIgnoreCase("setinv")){
                        for (ItemStack itemStack : player.getInventory().getContents()){
                            regenChestData.addItem(itemStack);
                        }
                        return true;
                    }
                    SenderManager.sendMessage(player, messages.getString("error.message.unknown-arg")
                            .replace("%usage%", "/" + command.getName() + " " + args[0] + " " + args[1] + " " + args[2] + " " + args[3] + " " + args[4] + " help"));
                    return true;
                }
                SenderManager.sendMessage(player, messages.getString("error.message.unknown-arg")
                        .replace("%usage%", "/" + command.getName() + " " + args[0] + " " + args[1] + " " + args[2] +  " help"));
                return true;

            }if (args[2].equalsIgnoreCase("koth")){
                if (args.length < 4){
                    messages.getStringList("messages.koth")
                            .forEach(text -> SenderManager.sendMessage(player, text
                                    .replace("%arena%", args[1])));
                    return true;
                }
                if (args[3].equalsIgnoreCase("help")){
                    messages.getStringList("messages.koth")
                            .forEach(text -> SenderManager.sendMessage(player, text
                                    .replace("%arena%", args[1])));
                    return true;
                }

                if (args[3].equalsIgnoreCase("create")){
                    arenaData.createKoth();
                    return true;
                }

                if (args[3].equalsIgnoreCase("delete")){
                    if (args.length < 5){
                        arenaData.deleteKoth();
                        return true;
                    }
                    arenaData.deleteKoth(Integer.parseInt(args[4]));
                }

                if (args[3].equalsIgnoreCase("list")){

                    if (arenaData.getKothManager().keySet().size() < 1){
                        SenderManager.sendMessage(player, messages.getString("error.arena.koth.list-empty"));
                        return false;
                    }
                    SenderManager.sendMessage(player, messages.getString("command.list.space"));
                    SenderManager.sendMessage(player, messages.getString("command.list.regenchest.format")
                            .replace("%arena%", args[1]));

                    int id = 1;
                    for (KothData kothData : arenaData.getKothManager().values()){
                        SenderManager.sendMessage(player, "&8- &f" + id + "&8. -> " + kothData.getPos1() + " &8- " + kothData.getPos2());
                    }
                    SenderManager.sendMessage(player, messages.getString("command.list.space"));
                    return true;
                }

                if (args[3].equalsIgnoreCase("id")) {

                    KothData kothData = arenaData.getKoth(Integer.parseInt(args[4]));

                    if (kothData == null) {
                        SenderManager.sendMessage(player, messages.getString("error.message.unknown-id")
                                .replace("%usage%", "/" + command.getName() + " " + subArgsText +  "<id>"));
                        return true;
                    }

                    if (args.length < 6) {
                        messages.getStringList("messages.kothid")
                                .forEach(text -> SenderManager.sendMessage(player, text
                                        .replace("%arena%", args[1])
                                        .replace("%id%", args[4])));
                        return true;
                    }
                    if (args[5].equalsIgnoreCase("help")){
                        messages.getStringList("messages.kothid")
                                .forEach(text -> SenderManager.sendMessage(player, text
                                        .replace("%arena%", args[1])
                                        .replace("%id%", args[4])));
                        return true;
                    }

                    if (args[5].equalsIgnoreCase("setlocation")) {
                        EditMode.startEditMode(player.getUniqueId(), arenaData.getArenaName(), Integer.parseInt(args[4]));
                        return true;
                    }

                    if (args[5].equalsIgnoreCase("reward")) {
                        if (args.length < 7){
                            messages.getStringList("messages.kothidrewards")
                                    .forEach(text -> SenderManager.sendMessage(player, text
                                            .replace("%arena%", args[1])
                                            .replace("%id%", args[4])));
                            return true;
                        }

                        if (args[6].equalsIgnoreCase("help")){
                            messages.getStringList("messages.kothidrewards")
                                    .forEach(text -> SenderManager.sendMessage(player, text
                                            .replace("%arena%", args[1])
                                            .replace("%id%", args[4])));
                            return true;
                        }
                        if (args[6].equalsIgnoreCase("setinv")) {
                            for (ItemStack itemStack : player.getInventory().getContents()) {
                                kothData.addRewards(itemStack);
                            }
                            return true;
                        }

                        if (args[6].equalsIgnoreCase("add")) {
                            kothData.addRewards(player.getInventory().getItemInMainHand());
                            return true;
                        }
                        if (args[6].equalsIgnoreCase("remove")) {

                            int id = Integer.parseInt(args[7]);

                            if (args.length < 9) {
                                return true;
                            }

                            if (kothData.getRewards().get(id) == null) {
                                SenderManager.sendMessage(player, messages.getString("error.message.unknown-id")
                                        .replace("%usage%", "/" + command.getName() + " " + subArgsText +  "<id>"));
                                return true;
                            }

                            kothData.removeRewards(id);
                            return true;
                        }

                        if (args[6].equalsIgnoreCase("list")) {
                            if (arenaData.getKothManager().keySet().size() < 1){
                                SenderManager.sendMessage(player, messages.getString("error.arena.rewards.list-empty"));
                                return false;
                            }
                            SenderManager.sendMessage(player, messages.getString("command.list.space"));
                            SenderManager.sendMessage(player, messages.getString("command.list.rewards.format")
                                    .replace("%arena%", args[1])
                                    .replace("%mode%", "koth")
                                    .replace("%id%", args[4]));

                            int id = 1;
                            for (ItemStack itemStack : arenaData.getKoth(Integer.parseInt(args[4])).getRewards()){
                                SenderManager.sendMessage(player, "&8- &f" + id + "&8. -> " + itemStack.getType().name());
                            }

                            SenderManager.sendMessage(player, messages.getString("command.list.space"));
                            return true;
                        }

                        SenderManager.sendMessage(player, messages.getString("error.message.unknown-arg")
                                .replace("%usage%", "/" + command.getName() + " " + argsText +  " help"));
                        return true;
                    }
                    SenderManager.sendMessage(player, messages.getString("error.message.unknown-arg")
                            .replace("%usage%", "/" + command.getName() + " " + argsText + " help"));
                    return true;
                }

                SenderManager.sendMessage(player, messages.getString("error.message.unknown-arg")
                        .replace("%usage%", "/" + command.getName() + " " + argsText + " help"));
                return true;

            }
            SenderManager.sendMessage(player, messages.getString("error.message.unknown-arg")
                    .replace("%usage%", "/" + command.getName() + " " + argsText + " help"));
            return true;
        }

        if (args[0].equalsIgnoreCase("list")){

            HashMap<String, ArenaData> arenaList = service.getCache().getArena();
            SenderManager.sendMessage(player, messages.getString("command.list-arena")
                        .replace("%size%", String.valueOf(arenaList.size())));
            SenderManager.sendMessage(player, messages.getString("command.space"));

            if (arenaList.size() >= 1) {
                for (ArenaData arenaData : arenaList.values()) {
                    SenderManager.sendMessage(player, "&8-> &f" + arenaData.getArenaName());
                }
            }else{
                SenderManager.sendMessage(player, messages.getString("error.arena.list-empty"));
            }

            SenderManager.sendMessage(player, messages.getString("command.space"));
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            HashMap<String, ConfigManager> configData = service.getCache().getConfigData();

            if (args.length < 2) {
                SenderManager.sendMessage(player, messages.getString("error.message.unknown-arg")
                        .replace("%usage%", "/" + command.getName() + " " + argsText + "-all/[file]"));
                return true;
            }

            if (args[1].equalsIgnoreCase("-all")) {
                configData.values().forEach(ConfigManager::reload);
                SenderManager.sendMessage(player, messages.getString("command.reload"));
                return true;
            }

            if (configData.get(args[1]) == null) {
                SenderManager.sendMessage(player, messages.getString("error.command.config-noexists")
                        .replace("%file%", args[1] + ".yml"));
                return true;
            }

            configData.get(args[1]).reload();
            SenderManager.sendMessage(player, messages.getString("command.reload-file")
                    .replace("%file%", StringUtils.capitalize(args[1]) + ".yml"));

            return true;
        }

        SenderManager.sendMessage(player, messages.getString("error.message.unknown-arg")
                .replace("%usage%", "/" + command.getName() + " help"));
        return true;
    }

}

