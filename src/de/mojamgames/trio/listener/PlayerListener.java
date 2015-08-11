package de.mojamgames.trio.listener;

import de.mojamgames.trio.game.Game;
import de.mojamgames.trio.objectives.GameField;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Set;

public class PlayerListener implements Listener {

    @EventHandler
    public void onPlayerClick(PlayerInteractEvent e) {
        Game g = Game.isInGame(e.getPlayer());
        if(g != null) {
            if (g.getGameStatus() == Game.Status.IN_GAME) {
                Block b = e.getPlayer().getTargetBlock((Set<Material>) null, 105);
                Location loc = b.getLocation();
                for (GameField gf : GameField.gameFields) {
                    e.getPlayer().sendMessage(gf.getName());
                    gf.getNumberFields().stream().filter(nf -> nf.maxX >= loc.getX() && nf.minX <= loc.getX()).filter(nf -> nf.maxY >= loc.getY() && nf.minY <= loc.getY()).filter(nf -> nf.maxZ >= loc.getZ() && nf.minZ <= loc.getZ()).forEach(nf -> {
                        for (Location bl : nf.getBlocks()) {

                        }
                    });
                }
            }
        }

        if(e.getClickedBlock() instanceof Sign) {
            Sign sign = (Sign) e.getClickedBlock();
            if(sign.getLine(0).equalsIgnoreCase("[Trio]")) {

            }
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {

    }


}
