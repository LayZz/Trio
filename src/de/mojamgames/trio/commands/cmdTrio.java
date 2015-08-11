package de.mojamgames.trio.commands;

import de.mojamgames.trio.Trio;
import de.mojamgames.trio.objectives.GameField;
import de.mojamgames.trio.utils.Title;
import de.mojamgames.trio.utils.chatmessage.ChatMessage;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

public class cmdTrio implements CommandExecutor {
    private static List<Player> playerList = new ArrayList<>();
    private BukkitTask task;

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String lable, String[] args) {
        if(cs instanceof Player) {
            Player p = (Player) cs;
            if(args.length == 0) {
                return true;
            } else if(args.length == 1) {
                if(args[0].equalsIgnoreCase("create")) {
                    if(args[0].equalsIgnoreCase("finish")) {
                        if (playerList.contains(p)) {
                            playerList.remove(p);
                            task.cancel();
                        }
                    } else if(args[0].equalsIgnoreCase("setspawn")) {
                        if(playerList.contains(p)) {
                            Location loc = p.getLocation();
                            Title.sendSubTitle(p, "§bSpawnpunkt gesetzt! (X - " + loc.getBlockX() + " | Y - " + loc.getBlockY() + " | Z - " + loc.getBlockZ() + ")", 1, 3, 1);
                            Trio.getMySQL().update("INSERT INTO `trio_spawns` (`x`, `y`, `z`, `yaw`, `pitch`, `world`) VALUES ('" + loc.getX() + "', '" + loc.getY() + "', '" + loc.getZ() + "', '" + loc.getYaw() + "', '" + loc.getPitch() + "', '" + loc.getWorld().getName() + "');");
                        }
                    } else {
                        p.sendMessage("§cFehler: §7§oDu darfst diesen Befehl nicht ausführen!");
                        return true;
                    }
                }
            } else if(args.length == 2) {
                if(args[0].equalsIgnoreCase("create")) {
                    playerList.add(p);
                    task = Bukkit.getScheduler().runTaskTimer(Trio.getInstance(), () -> {
                        for(int i = 0; i < 50; i++) {
                            p.sendMessage("");
                        }
                        new ChatMessage("§a§ltest").command("/time set 0").send(p);
                        new ChatMessage("§a§n§lSpawnpoint setzen").tooltip("§7§oSetzt einen Spawnpoint.").command("/trio setspawn").then().text("       ").then().text("§b§n§lSpielfeld setzen").tooltip("§7§oSetzt das Spielfeld").command("/trio setfield " + args[1]).send(p);
                        p.sendMessage("");
                        p.sendMessage("");
                        p.sendMessage("");
                        p.sendMessage("");
                    }, 0L, 20*8L);
                } else if(args[0].equalsIgnoreCase("setfield")) {
                    if (playerList.contains(p)) {
                        new GameField(p.getLocation(), args[1]);
                        Title.sendSubTitle(p, "§bSpielfeld gesetzt!");
                        return true;
                    }
                }
            }
        } else {
            cs.sendMessage("§cFehler: §7§oDieser Befehl kann nur von Spielern ausgeführt werden!");
            return true;
        }
        return true;
    }

}
