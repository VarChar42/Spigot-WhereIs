package me.varchar42.whereis.commands;

import me.varchar42.nbt.CompoundTag;
import me.varchar42.nbt.DoubleTag;
import me.varchar42.nbt.ListTag;
import me.varchar42.nbt.NbtIo;
import me.varchar42.whereis.WhereIsPlugin;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Otp implements CommandExecutor, TabCompleter {

    public String playerDataPath;

    public Otp(WhereIsPlugin main) {
        String mainWorldName = main.getServer().getWorlds().get(0).getName();
        playerDataPath = String.format("%s#%s#playerdata#",
                main.getServer().getWorldContainer().getAbsoluteFile(),
                mainWorldName).replace("#", File.separator);


    }

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

            String playerName = args[0];
            double x = Double.parseDouble(args[1]);
            double y = Double.parseDouble(args[2]);
            double z = Double.parseDouble(args[3]);

            OfflinePlayer[] players;

            if (playerName.equals("all")) {
                players = sender.getServer().getOfflinePlayers();
            } else {
                players = new OfflinePlayer[] {sender.getServer().getOfflinePlayer(playerName)};
            }

            for (OfflinePlayer player : players) {
                try {
                    if (!player.hasPlayedBefore()) {
                        sender.sendMessage(WhereIsPlugin.PREFIX + "Player " + player.getName() + " was never online.");
                    } else if (player.isOnline()) {
                        if (players.length != 1) continue;
                        sender.sendMessage(WhereIsPlugin.PREFIX + "Player " + player.getName() + " is online use /tp instead.");
                    } else {
                        teleportOfflinePlayer(player, x, y, z);
                        sender.sendMessage(WhereIsPlugin.PREFIX + "Position of " + player.getName() + " updated.");
                    }
                } catch (IOException e) {
                    sender.sendMessage(WhereIsPlugin.PREFIX + "Error while updating " + player.getName());
                    e.printStackTrace();
                }
            }
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public void teleportOfflinePlayer(OfflinePlayer player, double x, double y, double z) throws IOException {
        File playDataFile = new File(String.format("%s%s.dat", playerDataPath, player.getUniqueId()));

        FileInputStream inputStream = new FileInputStream(playDataFile);
        CompoundTag playerData = NbtIo.readCompressed(inputStream);
        inputStream.close();
        ListTag<DoubleTag> pos = new ListTag<>("Pos");
        pos.add(new DoubleTag("x", x));
        pos.add(new DoubleTag("y", y));
        pos.add(new DoubleTag("z", z));
        playerData.putList(pos);
        FileOutputStream outputStream = new FileOutputStream(playDataFile);
        NbtIo.writeCompressed(playerData, outputStream);
        outputStream.close();
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command arg1, String arg2, String[] args) {

        if (sender.isOp() && args.length == 1) {

            OfflinePlayer[] offlinePlayers = sender.getServer().getOfflinePlayers();
            List<String> suggestions = new ArrayList<>();

            for (OfflinePlayer offlinePlayer : offlinePlayers) {
                String name = offlinePlayer.getName();
                if (name == null) continue;
                if (name.toLowerCase().startsWith(args[0].toLowerCase()))
                    suggestions.add(name);
            }

            return suggestions;
        }

        return null;
    }
}
