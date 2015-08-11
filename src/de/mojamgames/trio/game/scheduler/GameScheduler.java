package de.mojamgames.trio.game.scheduler;

import de.mojamgames.trio.Trio;
import de.mojamgames.trio.game.Game;
import de.mojamgames.trio.game.GameUser;
import de.mojamgames.trio.utils.Title;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.util.Random;

public class GameScheduler implements Runnable {
    static BukkitTask task = null;
    Integer rounds = 0;
    Integer MAX_ROUNDS = 0;
    Integer roundCounter = 1;
    Game g = null;

    public GameScheduler(Game g) {
        task = Bukkit.getScheduler().runTaskTimer(Trio.getInstance(), this, 20L, 20L);
        MAX_ROUNDS = g.getGameUsers().size() * 2;
        g.changeGameStatus(Game.Status.IN_GAME);
        this.g = g;
    }

    @Override
    public void run() {
        roundCounter--;
        if((roundCounter == 30) || (roundCounter >= 10 && roundCounter <= 0)) {
            g.broadcast(Trio.getPrefix() + "§bDie Runde endet in §6§o" + roundCounter + " Sekunde(n)§b!");
        } else if(roundCounter == 0) {
            if(rounds < MAX_ROUNDS) {
                roundCounter = g.MAX_ROUND_TIME + 1;
                rounds++;
                for (GameUser user : g.getGameUsers()) {
                    Title.sendTitle(user.getPlayer(), "§bRunde §6§l" + rounds, 1, 2, 1);
                }
                Bukkit.getScheduler().runTaskLater(Trio.getInstance(), () -> {
                    Random rdm = new Random();
                    Integer i = rdm.nextInt(91);
                    while(i <= 0 && i >= 90) {
                        i = rdm.nextInt(91);
                    }
                    for (GameUser user : g.getGameUsers()) {
                        Title.sendTitle(user.getPlayer(), "§bGesuchte Zahl: §6§l" + i, 0, 2, 0);
                    }
                    Bukkit.getScheduler().runTaskLater(Trio.getInstance(), () -> g.getGameField().setNewNumbers(), 30L);
                }, 30L);
            } else {
                for(GameUser user : g.getGameUsers()) {
                    Title.sendTitle(user.getPlayer(), "§b§lDas Spiel ist zu Ende!", 1, 3, 1);
                }

                if(g.getWinners().size() == 1) {
                    Bukkit.getScheduler().runTaskLater(Trio.getInstance(), () -> {
                        for(GameUser user : g.getGameUsers()) {
                            Title.sendTitles(user.getPlayer(), "§b§lDer Spieler §6§o§l" + g.getWinners().get(0).getPlayer().getName() + " §b hat gewonnen!", "§b§oEr hatte §6§o§l" + g.getWinners().get(0).getRightCombinations() + " §b§orichtige Kombinationen!", 2, 4, 2);
                        }
                    }, 130L);
                } else {
                    String winners = "";
                    for(GameUser user : g.getWinners()) {
                        winners += user.getPlayer().getName() + ", ";
                    }
                    winners = StringUtils.removeEnd(winners, ", ");
                    for(GameUser user : g.getGameUsers()) {
                        Title.sendTitles(user.getPlayer(), "§6§o§l" + winners + " §b haben gewonnen!", "§b§osie haben alle §6§o§l" + g.getWinners().get(0).getRightCombinations() + " §b§orichtige Kombinationen!", 2, 4, 2);
                    }
                }

                g.restart();
            }
        }
    }

    public static BukkitTask getTask() {
        return task;
    }
}
