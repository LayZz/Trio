package de.mojamgames.trio.game.scheduler;

import de.mojamgames.trio.Trio;
import de.mojamgames.trio.game.Game;
import de.mojamgames.trio.game.GameUser;
import de.mojamgames.trio.utils.Title;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

public class LobbyScheduler implements Runnable {
    private BukkitTask task = null;
    private Game g = null;
    private Integer counter = 0, realCounter = 61;

    public LobbyScheduler(Game g) {
        this.g = g;
        this.task = Bukkit.getScheduler().runTaskTimer(Trio.getInstance(), this, 0L, 20L);
        g.changeGameStatus(Game.Status.LOBBY);
    }

    @Override
    public void run() {
        if(g.getGameUsers().size() >= 2) {
            realCounter--;
            if(realCounter == 60 || realCounter == 45 || realCounter == 30 || realCounter == 15 || realCounter >= 10) {
                if(realCounter != 0) {
                    g.broadcast(Trio.getPrefix() + "§bDas Spiel startet in §6§o" + realCounter + " SSekunde(n)§b!");
                } else {
                    for(GameUser user : g.getGameUsers()) {
                        Title.sendTitle(user.getPlayer(), "§bDas Spiel startet §6§ojetzt§b!", 0, 2, 0);
                    }
                    try {
                        Thread.sleep(4000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    new GameScheduler(this.g);
                    this.task.cancel();
                }
            }
        } else {
            counter++;
            if(counter == 45) {
                g.broadcast(Trio.getPrefix() + "§cEs werden §6§o" + g.MIN_PLAYER + "Spieler §czum starten benötigt! §6" + g.getGameUsers().size() + " §c§l/ §c" + g.MIN_PLAYER);
            }
        }
    }
}
