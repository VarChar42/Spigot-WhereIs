package me.varchar42.whereis.bungee;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;


public class EventListener implements Listener {

    private WhereIsBungeePlugin plugin;

    public EventListener(WhereIsBungeePlugin plugin) {
        this.plugin = plugin;
        plugin.getLogger().info("Setup Listeners");

    }

    @EventHandler
    public void onPluginMessage(PluginMessageEvent e) {

        ByteArrayDataInput in = ByteStreams.newDataInput(e.getData());
        if (e.getTag().equals("whereis:setup")) {
            if (in.readUTF().equals("setupData")) {

                String name = in.readUTF();
                String path = in.readUTF();
                plugin.addServer(name, path);
                plugin.getLogger().info(String.format("Added new server: %s : %s", name, path));
            }
        }

    }

}
