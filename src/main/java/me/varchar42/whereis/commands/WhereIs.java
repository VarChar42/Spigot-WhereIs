package me.varchar42.whereis.commands;


import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.mojang.nbt.CompoundTag;
import com.mojang.nbt.IntTag;
import com.mojang.nbt.ListTag;
import com.mojang.nbt.NbtIo;
import me.varchar42.whereis.WhereIsPlugin;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class WhereIs implements CommandExecutor {

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
            if (args[0].equalsIgnoreCase("find")) {
                if (args.length != 2) return false;
                @SuppressWarnings("deprecation") OfflinePlayer oPlayer = sender.getServer().getOfflinePlayer(args[1]);

                if (!oPlayer.hasPlayedBefore()) {
                    sender.sendMessage(WhereIsPlugin.PREFIX + "Player was never online");
                } else if (oPlayer.isOnline()) {
                    Location loc = oPlayer.getPlayer().getLocation();
                    sender.sendMessage(String.format("%s[%s, %s, %s] in world %s", WhereIsPlugin.PREFIX, loc.getX(), loc.getY(), loc.getZ(), loc.getWorld().getName()));
                } else {
                    sender.sendMessage(WhereIsPlugin.PREFIX + "Loading playerdata.");
                    File playdataFile = new File(String.format("%s%s%s.dat", sender.getServer().getWorldContainer().getAbsoluteFile(), plugin.getPlayerdataPath(), oPlayer.getUniqueId()));
                    FileInputStream inputStream = new FileInputStream(playdataFile);
                    CompoundTag playerdata = NbtIo.readCompressed(inputStream);
                    ListTag<IntTag> pos = (ListTag<IntTag>) playerdata.getList("Pos");
                    int dim = playerdata.getInt("Dimension");
                    String dimname = "" + dim;
                    switch (dim) {
                        case -1:
                            dimname = "Nether";
                            break;
                        case 0:
                            dimname = "Overworld";
                            break;
                        case 1:
                            dimname = "TheEnd";
                            break;
                    }


                    sender.sendMessage(String.format("%s[%s, %s, %s] in dimension %s", WhereIsPlugin.PREFIX, pos.get(0), pos.get(1), pos.get(2), dimname));
                    return true;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

}
