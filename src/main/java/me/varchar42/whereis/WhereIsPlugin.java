package me.varchar42.whereis;

import me.varchar42.whereis.commands.Otp;
import me.varchar42.whereis.commands.WhereIs;
import org.bstats.bukkit.Metrics;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class WhereIsPlugin extends JavaPlugin {

    public static final String PREFIX = ChatColor.BLUE + "[WhereIs] " + ChatColor.RESET;
    private Metrics metrics;

    @Override
    public void onEnable() {
        super.onEnable();


        metrics = new Metrics(this, 6252);
        if (!metrics.isEnabled()) getServer().getConsoleSender().sendMessage(PREFIX +"Disabled bStats!");
        else {
            getServer().getConsoleSender().sendMessage(PREFIX + "bStats is enabled!");
        }


        getServer().getMessenger().registerOutgoingPluginChannel(this, "whereis:setup");

        Otp otpHandler = new Otp();
        getCommand("otp").setExecutor(otpHandler);
        getCommand("otp").setTabCompleter(otpHandler);

        WhereIs wiHandler = new WhereIs(this);
        getCommand("whereis").setExecutor(wiHandler);
        getCommand("whereis").setTabCompleter(wiHandler);

    }


    public String getPlayerdataPath() {
        String worldname = getServer().getWorlds().get(0).getName();
        return String.format("#%s#playerdata#", worldname).replace("#", File.separator);
    }


}
