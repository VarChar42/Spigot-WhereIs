package me.varchar42.whereis.commands;


import me.varchar42.nbt.CompoundTag;
import me.varchar42.nbt.IntTag;
import me.varchar42.nbt.ListTag;
import me.varchar42.nbt.NbtIo;
import me.varchar42.whereis.WhereIsPlugin;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WhereIs implements CommandExecutor, TabCompleter {

    private WhereIsPlugin plugin;

    public WhereIs(WhereIsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        try {
            Player player;
            if (sender instanceof Player) {
                player = (Player) sender;
                if (!player.hasPermission("whereis.use")) {
                    sender.sendMessage(WhereIsPlugin.PREFIX + "You do not have permissions to perform this command!");
                    return true;
                }
            }
            if (args.length != 1) return false;
            @SuppressWarnings("deprecation") OfflinePlayer oPlayer = sender.getServer().getOfflinePlayer(args[0]);

            if (!oPlayer.hasPlayedBefore()) {
                sender.sendMessage(WhereIsPlugin.PREFIX + "Player was never online");
            } else if (oPlayer.isOnline()) {
                Location loc = oPlayer.getPlayer().getLocation();
                sender.sendMessage(String.format("%s[%s, %s, %s] in world \"%s\"", WhereIsPlugin.PREFIX, loc.getX(), loc.getY(), loc.getZ(), loc.getWorld().getName()));
            } else {

                sender.sendMessage(WhereIsPlugin.PREFIX + "Loading playerdata.");

                File playerDataFile = new File(String.format("%s%s%s.dat", sender.getServer().getWorldContainer().getAbsoluteFile(), plugin.getPlayerdataPath(), oPlayer.getUniqueId()));
                FileInputStream inputStream = new FileInputStream(playerDataFile);

                CompoundTag playerData = NbtIo.readCompressed(inputStream);

                ListTag<IntTag> playerPosList = (ListTag<IntTag>) playerData.getList("Pos");
                String dim = playerData.getString("Dimension");


                sender.sendMessage(String.format("%s[%s, %s, %s] in dimension %s", WhereIsPlugin.PREFIX, playerPosList.get(0), playerPosList.get(1), playerPosList.get(2), dim));
                return true;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {

        if (sender.hasPermission("whereis.use")) {
            List<String> suggestion = new ArrayList<>();

            if (args.length == 1) {
                for (OfflinePlayer offlinePlayer : sender.getServer().getOfflinePlayers()) {
                    String name = offlinePlayer.getName();
                    if (name == null) continue;
                    if (name.toLowerCase().startsWith(args[0].toLowerCase())) {
                        suggestion.add(name);
                    }
                }
            }
            return suggestion;
        } else {
            return new ArrayList<>(0);
        }
    }
}
