package me.varchar42.whereis.bungee;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class WhereIsBungeeCommand extends Command {

    private WhereIsBungeePlugin plugin;

    public WhereIsBungeeCommand(WhereIsBungeePlugin plugin) {
        super("bWhereIs", "whereis.use");
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {


        ProxiedPlayer player = plugin.getProxy().getPlayer(args[0]);

        if (player == null) {
            commandSender.sendMessage(new TextComponent("Not found"));
            return;

        }

        for (LookupEntry entry : plugin.entries) {
            commandSender.sendMessage(new TextComponent(entry.getName()));
            commandSender.sendMessage(new TextComponent(player.getUniqueId().toString()));

            commandSender.sendMessage(new TextComponent(entry.getPosInfo(player.getUniqueId().toString())));

        }

        commandSender.sendMessage(new TextComponent("Finished"));

    }
}
