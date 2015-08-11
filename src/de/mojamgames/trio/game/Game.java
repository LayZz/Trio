package de.mojamgames.trio.game;

import de.mojamgames.trio.Trio;
import de.mojamgames.trio.game.scheduler.GameScheduler;
import de.mojamgames.trio.game.scheduler.LobbyScheduler;
import de.mojamgames.trio.objectives.GameField;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

import java.util.*;

public class Game {
    public static HashMap<Game, GameField> runningGames = new HashMap<>();
    private List<Location> spawnList = new ArrayList<>();
    private GameField gf;
    private List<GameUser> gameUsers = new ArrayList<>();
    public Integer MAX_PLAYER = 14;
    public Integer MIN_PLAYER = 2;
    public Integer MAX_ROUND_TIME = 120; //secs
    private Status gameStatus = Status.LOBBY;
    private Sign joinSign;

    public enum Status {
        IN_GAME,
        LOBBY,
        RESTARTING,
    }

    public Game(GameField gf) {
        this.gf = gf;
        runningGames.put(this, this.gf);

        new LobbyScheduler(this);
    }

    public GameField getGameField() {
        return this.gf;
    }

    public void changeGameStatus(Status gameStatus) {
        this.gameStatus = gameStatus;
        this.joinSign.setLine(3, "§b§n" + this.gameStatus.name().toUpperCase());
    }

    public Status getGameStatus() {
        return gameStatus;
    }

    public Boolean join(GameUser user) {
        if(!gameUsers.contains(user) && gameUsers.size() < MAX_PLAYER) {
            if(gameStatus == Status.LOBBY) {
                Random r = new Random();
                Integer i = r.nextInt(getSpawns().size());
                user.getPlayer().teleport(getSpawns().get(i));
                gameUsers.add(user);
            }
            user.getPlayer().sendMessage("§cDu kannst den Server momentan nicht joinen!");
            return false;
        }
        user.getPlayer().sendMessage("§cFehler: §7§oDu bist bereits online!");
        return false;
    }

    public void setJoinSign(Sign sign) {
        this.joinSign = sign;
    }

    public Sign getJoinSign() {
        return this.joinSign;
    }

    public void leave(GameUser user) {}

    public static Integer getPointsPerRightCombination() {
        return Trio.getInstance().getConfig().getInt("Points.rightCombination");
    }

    public static Integer getPointsPerWin() {
        return Trio.getInstance().getConfig().getInt("Points.Win");
    }

    public void addSpawn(Location loc) {
        if(!spawnList.contains(loc)) {
            spawnList.add(loc);
        }
    }

    public List<GameUser> getGameUsers() {
        return this.gameUsers;
    }

    public List<Location> getSpawns() {
        return this.spawnList;
    }

    public void restart() {
        changeGameStatus(Status.RESTARTING);
        GameScheduler.getTask().cancel();
        for(GameUser user : getGameUsers()) {
            user.save(getWinners().contains(user));
        }
    }

    public void broadcast(String... msg) {
        for(GameUser user : this.getGameUsers()) {
            user.getPlayer().sendMessage(msg);
        }
    }

    public List<GameUser> getWinners() {
        List<GameUser> list = new ArrayList<>();
        for(GameUser user : getGameUsers()) {
            if(!list.isEmpty()) {
                if (user.getRightCombinations() > list.get(0).getRightCombinations()) {
                    list.clear();
                    list.add(user);
                    continue;
                } else if(Objects.equals(user.getRightCombinations(), list.get(0).getRightCombinations())) {
                    list.add(user);
                    continue;
                }
            }
            list.add(user);
        }
        return list;
    }

    public static Game isInGame(Player p) {
        for(Game g : runningGames.keySet()) {
            for(GameUser user : g.getGameUsers()) {
                if(user.getPlayer() == p) {
                    return g;
                }
            }
        }
        return null;
    }

    public static void loadGames() {

    }
}
