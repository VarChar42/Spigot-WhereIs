package me.varchar42.whereis.bungee;

import net.md_5.bungee.api.plugin.Plugin;

import java.io.*;
import java.util.ArrayList;

public class WhereIsBungeePlugin extends Plugin {

    public ArrayList<LookupEntry> entries;
    private File dataFile;

    @Override
    public void onEnable() {
        super.onEnable();

        this.getProxy().registerChannel("whereis:setup");
        getProxy().getPluginManager().registerListener(this,
                new EventListener(this));

        getLogger().info("OK");

        dataFile = new File(String.format("%s%sbWhereIs%sservers.conf", getProxy().getPluginsFolder().getAbsolutePath(), File.separator, File.separator));
        dataFile.getParentFile().mkdirs();


        if (dataFile.exists()) {
            readServers();
        } else {
            entries = new ArrayList<>();
            saveServers();
        }

        getProxy().getPluginManager().registerCommand(this, new WhereIsBungeeCommand(this));

    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    public void addServer(String name, String path) {
        for (LookupEntry entry : entries) {
            if (entry.getPath().equals(path)) return;
        }
        entries.add(new LookupEntry(name, path));
        saveServers();


    }

    private void saveServers() {
        try {
            if (!dataFile.exists()) dataFile.createNewFile();
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(dataFile));
            for (LookupEntry entry : entries) {
                bufferedWriter.write(String.format("%s=%s\n", entry.getName(), entry.getPath()));
            }
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void readServers() {
        try {
            if (entries == null) entries = new ArrayList<>();
            entries.clear();
            BufferedReader reader = new BufferedReader(new FileReader(dataFile));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split("=");
                if (data.length != 2) continue;
                entries.add(new LookupEntry(data[0], data[1]));
            }
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
