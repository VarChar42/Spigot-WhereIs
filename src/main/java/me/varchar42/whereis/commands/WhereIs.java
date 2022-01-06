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
            Player player = null;
            if (sender instanceof Player) {
                player = (Player) sender;
                if (!player.hasPermission("whereis.use")) {
                    sender.sendMessage(WhereIsPlugin.PREFIX + "You do not have permissions to perform this command!");
                    return true;
                }
            }
            if (args.length < 1) return false;

                if (args.length != 1) return false;
                @SuppressWarnings("deprecation") OfflinePlayer oPlayer = sender.getServer().getOfflinePlayer(args[0]);

                if (!oPlayer.hasPlayedBefore()) {
                    sender.sendMessage(WhereIsPlugin.PREFIX + "Player was never online");
                } else if (oPlayer.isOnline()) {
                    Location loc = oPlayer.getPlayer().getLocation();
                    sender.sendMessage(String.format("%s[%s, %s, %s] in world \"%s\"", WhereIsPlugin.PREFIX, loc.getX(), loc.getY(), loc.getZ(), loc.getWorld().getName()));
                } else {
                    sender.sendMessage(WhereIsPlugin.PREFIX + "Loading playerdata.");
                    File playdataFile = new File(String.format("%s%s%s.dat", sender.getServer().getWorldContainer().getAbsoluteFile(), plugin.getPlayerdataPath(), oPlayer.getUniqueId()));
                    FileInputStream inputStream = new FileInputStream(playdataFile);
                    CompoundTag playerdata = NbtIo.readCompressed(inputStream);
                    ListTag<IntTag> pos = (ListTag<IntTag>) playerdata.getList("Pos");
                    String dim = playerdata.getString("Dimension");



                    sender.sendMessage(String.format("%s[%s, %s, %s] in dimension %s", WhereIsPlugin.PREFIX, pos.get(0), pos.get(1), pos.get(2), dim));
                    return true;
                }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {

        if (sender.isOp()) {
            OfflinePlayer[] of = sender.getServer().getOfflinePlayers();

            List<String> offlineNames = new ArrayList<String>();
            List<String> suggestion = new ArrayList<String>();

            if (args.length == 1) {
                for (int x = 0; x < of.length; x++) {
                    offlineNames.add(of[x].getName());
                }
                for (String guess : offlineNames) {
                    if (guess.toLowerCase().startsWith(args[0].toLowerCase()))
                        suggestion.add(guess);
                }

            }

            return suggestion;

        } else {
            List<String> nothing = new ArrayList<String>();
            return nothing;

        }
    }
}
