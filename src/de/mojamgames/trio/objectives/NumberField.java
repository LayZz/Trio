package de.mojamgames.trio.objectives;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

public class NumberField {
    public Integer maxX, maxY, maxZ, minX, minY, minZ;
    private GameField gf;

    public NumberField(GameField gf, BlockNumber bn, Location loc) {
        this.gf = gf;
        this.maxX = loc.getBlockX() + 10;
        this.minX = loc.getBlockX();
        this.maxY = loc.getBlockY();
        this.minY = loc.getBlockY()-10;
        this.maxZ = loc.getBlockZ();
        this.minZ = loc.getBlockZ()-1;

        Location newLoc = loc.add(3, -1, 0);
        for(int i = 0; i < 9; i++) {
            for(int o = 0; o < 5; o++) {
                if(bn.getFormat()[i*5+o].equalsIgnoreCase("X")) {
                    Block b = new Location(newLoc.getWorld(), newLoc.getBlockX()+o, newLoc.getBlockY()-i, newLoc.getBlockZ()).getBlock();
                    b.setType(Material.WOOL);
                    b.setData((byte) 15);
                }
            }
        }
    }

    public List<Location> getBlocks() {
        List<Location> list = new ArrayList<>();

        for(int i = 0; i < 11; i++) {
            for(int o = 0; o < 11; o++) {
                list.add(new Location(gf.getFirstBlock().getWorld(), minX+o, maxY-i, minZ));
            }
        }

        return list;
    }

}
