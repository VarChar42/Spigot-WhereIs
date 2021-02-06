package me.varchar42.whereis.commands;

import com.mojang.nbt.CompoundTag;
import com.mojang.nbt.DoubleTag;
import com.mojang.nbt.ListTag;
import com.mojang.nbt.NbtIo;
import me.varchar42.whereis.WhereIsPlugin;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Otp implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        try {

            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (!player.isOp()) {
                    sender.sendMessage(WhereIsPlugin.PREFIX + "You do not have permissions to perform this command!");
                    return true;
                }
            }
            if (args.length != 4) return false;

            @SuppressWarnings("deprecation") OfflinePlayer player = sender.getServer().getOfflinePlayer(args[0]);

            if (!player.hasPlayedBefore()) {
                sender.sendMessage(WhereIsPlugin.PREFIX + "Player was never online");
            } else if (player.isOnline()) {
                sender.sendMessage(WhereIsPlugin.PREFIX + "Player is online use /tp instead");
            } else {
                String worldname = sender.getServer().getWorlds().get(0).getName();
                String path = String.format("#%s#playerdata#", worldname).replace("#", File.separator);


                File playdataFile = new File(String.format("%s%s%s.dat", sender.getServer().getWorldContainer().getAbsoluteFile(), path, player.getUniqueId()));


                FileInputStream inputStream = new FileInputStream(playdataFile);
                CompoundTag playerdata = NbtIo.readCompressed(inputStream);
                inputStream.close();


                ListTag<DoubleTag> pos = new ListTag<>("Pos");
                pos.add(new DoubleTag("x", Double.parseDouble(args[1])));
                pos.add(new DoubleTag("y", Double.parseDouble(args[2])));
                pos.add(new DoubleTag("z", Double.parseDouble(args[3])));
                playerdata.putList(pos);
                FileOutputStream outputStream = new FileOutputStream(playdataFile);
                NbtIo.writeCompressed(playerdata, outputStream);
                sender.sendMessage(WhereIsPlugin.PREFIX + "Player position changed.");

                outputStream.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}
