package de.mojamgames.trio.objectives;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GameField {

    private Integer siteLength = 109;
    public static List<GameField> gameFields = new ArrayList<>();
    public List<NumberField> numberFields = new ArrayList<>();
    private String name;
    private Location firstBlock;
    private Integer maxX;
    private Integer maxY;
    private Integer maxZ;
    private Integer minX;
    private Integer minY;
    private Integer minZ;

    public GameField(Location startLoc, String name) {
        this.name = name;
        this.firstBlock = new Location(startLoc.getWorld(), startLoc.getX() + 1, startLoc.getY(), startLoc.getZ());
        createNewGameField(startLoc);
        gameFields.add(this);
        this.minY = firstBlock.getBlockY();
        this.maxY = firstBlock.getBlockY() + siteLength - 1;
        this.minX = firstBlock.getBlockX();
        this.maxX = firstBlock.getBlockX() + siteLength - 1;
        this.minZ = firstBlock.getBlockZ();
        this.maxX = firstBlock.getBlockZ() + 1;
    }


    public void createNewGameField(Location startLoc) {
        /** DOUBLE GRID */
        for(int u = 0; u < 2; u++) {
            Location loc = new Location(startLoc.getWorld(), startLoc.getX() + 1, startLoc.getY(), startLoc.getZ() + u);
            for (int i = 0; i < 10; i++) {
                Location loc1 = new Location(loc.getWorld(), loc.getX() + 12 * i, loc.getY(), loc.getZ());
                for (int o = 0; o < siteLength; o++) {
                    Location finalLoc = new Location(loc1.getWorld(), loc1.getX(), loc1.getY() + o, loc1.getZ());
                    finalLoc.getBlock().setType(Material.WOOL);
                    finalLoc.getBlock().setData((byte) 15);


                }
                Location loc2 = new Location(loc.getWorld(), loc.getX(), loc.getY() + 12 * i, loc.getZ());
                for (int o = 0; o < siteLength; o++) {
                    Location finalLoc = new Location(loc2.getWorld(), loc2.getX() + o, loc2.getY(), loc2.getZ());
                    finalLoc.getBlock().setType(Material.WOOL);
                    finalLoc.getBlock().setData((byte) 15);
                }
            }
        }

        /** BACKGROUND WALL */
        final Integer[] counter = {-1};
        Bukkit.getScheduler().runTaskTimer(Bukkit.getPluginManager().getPlugin("Trio"), () -> {
            if(counter[0] < siteLength - 1) {
                counter[0]++;
                Location loc = new Location(startLoc.getWorld(), startLoc.getX() + 1, startLoc.getY() + counter[0], startLoc.getZ());
                for(int i = 0; i < siteLength - 1; i++) {
                    Location finalLoc = new Location(loc.getWorld(), loc.getX() + i, loc.getY(), loc.getZ());
                    if(finalLoc.getBlock().getType() == Material.AIR) {
                        finalLoc.getBlock().setType(Material.WOOL);
                    }
                }
            }
            Bukkit.getScheduler().cancelTasks(Bukkit.getPluginManager().getPlugin("Trio"));
        }, 4L, 5L);
    }

    public void setNewNumbers() {
        Location loc = new Location(firstBlock.getWorld(), firstBlock.getX()+1, firstBlock.getY()+11, firstBlock.getZ()+1);
        HashMap<BlockNumber, Integer> bnMap = new HashMap<>();
        for(int i = 0; i < 9; i++) {
            for (int o = 0; o < 9; o++) {
                Location finalLoc = new Location(loc.getWorld(), loc.getX() + 12 * o, loc.getY() + 12 * i, loc.getZ());
                Boolean b = false;
                BlockNumber bn;
                while (!b) {
                    bn = BlockNumber.getRandomBlockNumber();
                    if (bnMap.containsKey(bn)) {
                        if (bnMap.get(bn) < 9) {
                            numberFields.add(new NumberField(this, bn, finalLoc));
                            Integer now = bnMap.get(bn);
                            bnMap.put(bn, now + 1);
                            b = true;
                        } else {
                            b = false;
                        }
                    } else {
                        numberFields.add(new NumberField(this, bn, finalLoc));
                        bnMap.put(bn, 1);
                        b = true;
                    }
                }
            }
        }
    }

    public String getName() {
        return this.name;
    }

    public Location getFirstBlock() {
        return this.firstBlock;
    }

    public static GameField select(Location loc) {
        for(GameField gf : gameFields) {
            if(gf.getMaxX() >= loc.getX() && gf.getMinX() <= loc.getX()) {
                if(gf.getMaxY() >= loc.getY() && gf.getMinY() <= loc.getY()) {
                    if(gf.getMaxZ() >= loc.getZ() && gf.getMinZ() <= loc.getZ()) {
                        return gf;
                    }
                }
            }
        }
        return null;
    }

    public static GameField select(String name) {
        for(GameField gf : gameFields) {
            if(gf.getName().equalsIgnoreCase(name)){
                return gf;
            }
        }
        return null;
    }


    public Integer getMaxX() {
        return maxX;
    }

    public Integer getMaxY() {
        return maxY;
    }

    public Integer getMaxZ() {
        return maxZ;
    }

    public Integer getMinX() {
        return minX;
    }

    public Integer getMinY() {
        return minY;
    }

    public Integer getMinZ() {
        return minZ;
    }


    public List<NumberField> getNumberFields() {
        return numberFields;
    }
}

