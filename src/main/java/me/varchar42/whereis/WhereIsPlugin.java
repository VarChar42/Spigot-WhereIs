package me.varchar42.whereis;

import me.varchar42.whereis.commands.Otp;
import me.varchar42.whereis.commands.WhereIs;
import me.varchar42.whereis.utils.Metrics;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class WhereIsPlugin extends JavaPlugin {

    public static final String PREFIX = ChatColor.BLUE + "[WhereIs] " + ChatColor.RESET;
    private Metrics metrics;

    @Override
    public void onEnable() {
        super.onEnable();


        metrics = new Metrics(this);
        if (!metrics.isEnabled()) getServer().getConsoleSender().sendMessage("Disabled bStats!");
        else {
            getServer().getConsoleSender().sendMessage("bStats is enabled!");
        }


        getServer().getMessenger().registerOutgoingPluginChannel(this, "whereis:setup");

        getCommand("whereis").setExecutor(new WhereIs(this));
        getCommand("otp").setExecutor(new Otp());

    }


    public String getPlayerdataPath() {
        String worldname = getServer().getWorlds().get(0).getName();
        return String.format("#%s#playerdata#", worldname).replace("#", File.separator);
    }


}
