package de.mojamgames.trio.game;

import de.mojamgames.trio.Trio;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class GameUser {
    private Player p;
    private Integer rightCombinations = 0;
    private byte woolByte;

    public GameUser(Object obj) {
        if(obj instanceof UUID) {
            this.p = Bukkit.getPlayer((UUID) obj);
        } else if(obj instanceof Player) {
            this.p = (Player) obj;
        } else if(obj instanceof String) {
            this.p = Bukkit.getPlayer((String) obj);
        }
    }

    public Player getPlayer() {
        return this.p;
    }

    public void addRightCombination(Integer rightCombinations) {
        this.rightCombinations++;
    }

    public Integer getRightCombinations() {
        return rightCombinations;
    }

    public void save(Boolean won) {
        Integer combinations = 0, wongames = 0, points = 0, gamesPlayed = 0;
        ResultSet rs = Trio.getMySQL().query("SELECT * FROM `trio_users` WHERE `uuid`='" + this.p.getUniqueId());
        try {
            if(rs.next()) {
                combinations = rs.getInt("rightCombinations");
                wongames = rs.getInt("wonGames");
                points = rs.getInt("points");
                gamesPlayed = rs.getInt("gamesPlayed");
            } else {
                points += getRightCombinations() * Game.getPointsPerRightCombination();
                if(won) {
                    points += Game.getPointsPerWin();
                    Trio.getMySQL().update("INSERT INTO `trio_users` (`uuid`, `wonGames`, `rightCombinations`, `points`, `gamesPlayed`) VALUES ('" + this.p.getUniqueId() + "', '1', '" + getRightCombinations() + "', '" + points + "', '" + gamesPlayed + "');");
                } else {
                    Trio.getMySQL().update("INSERT INTO `trio_users` (`uuid`, `wonGames`, `rightCombinations`, `points`, `gamesPlayed`) VALUES ('" + this.p.getUniqueId() + "', '0', '" + getRightCombinations() + "', '" + points + "', '1');");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        points += getRightCombinations() * Game.getPointsPerRightCombination();
        if(won) {
            points += Game.getPointsPerWin();
            Trio.getMySQL().update("UPDATE `trio_users` SET `wonGames`='" + wongames + 1 + ", `rightCombinations`='" + combinations + getRightCombinations() + "', `gamesPlayed`='" + gamesPlayed + 1 + "', `points`='" + points + "' WHERE `uuid`='" + this.p.getUniqueId() + "';");
        } else {
            Trio.getMySQL().update("UPDATE `trio_users` SET `rightCombinations`='" + combinations + getRightCombinations() + "', `gamesPlayed`='" + gamesPlayed + 1 + "', `points`='" + points + "' WHERE `uuid`='" + this.p.getUniqueId() + "';");
        }
    }


}
