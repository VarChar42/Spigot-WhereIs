package me.varchar42.whereis.bungee;

import com.mojang.nbt.CompoundTag;
import com.mojang.nbt.IntTag;
import com.mojang.nbt.ListTag;
import com.mojang.nbt.NbtIo;

import java.io.*;

public class LookupEntry implements Serializable {

    private String name;
    private String path;

    public LookupEntry(String name, String path) {
        this.name = name;
        this.path = path;
    }

    public LookupEntry() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPosInfo(String playerUUID) {
        try {

            File playdataFile = new File(String.format("%s%s.dat", path, playerUUID));
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
            return (String.format("%s[%s, %s, %s] in dimension %s", "[bWhereIs]", pos.get(0), pos.get(1), pos.get(2), dimname));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
