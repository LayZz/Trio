package de.mojamgames.trio.listener;

import de.mojamgames.trio.Trio;
import de.mojamgames.trio.game.Game;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class SignListener implements Listener {

    @EventHandler
    public void onSign(SignChangeEvent e) {
        if(e.getLine(0).equalsIgnoreCase("trio")) {
            String gf = e.getLine(1);
            final Game[] g = {null};
            Game.runningGames.entrySet().stream().filter(entry -> entry.getValue().getName().equalsIgnoreCase(e.getLine(1))).forEach(entry -> g[0] = entry.getKey());
            if(g[0] != null) {
                e.setLine(0, "§c§l[§6§lTRIO§c§l]");
                e.setLine(1, "§a§o" + Trio.firstCharToUpper(gf));
                e.setLine(2, "");
                e.setLine(3, "§b§n" + g[0].getGameStatus().name().toUpperCase());
                g[0].setJoinSign((Sign) e.getBlock());
            } else {
                e.setLine(0, "§4§nFEHLER");
                e.setLine(1, "");
                e.setLine(2, "§c§oDieses GameField");
                e.setLine(3, "§c§oexistiert nicht!");
            }
        }
    }

}
