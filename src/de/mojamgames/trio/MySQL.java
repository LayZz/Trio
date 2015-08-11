package de.mojamgames.trio;

import java.sql.*;

public class MySQL {
    private static Connection connection;
    private static String host, user, data, pass;
    private static Integer port;

    public MySQL() {
        host = Trio.getInstance().getConfig().getString("MySQL.Host");
        user = Trio.getInstance().getConfig().getString("MySQL.User");
        pass = Trio.getInstance().getConfig().getString("MySQL.Password");
        data = Trio.getInstance().getConfig().getString("MySQL.Database");
        port = Trio.getInstance().getConfig().getInt("MySQL.Port");
    }

    public boolean closeConnection() {
        try {
            if(connected()) {
                connection.close();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public boolean openConnection() {
        if(!connected()) {
            try {
                connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + data, user, pass);
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public void update(String query) {
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet query(String query) {
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            return statement.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean connected() {
        return connection != null;
    }

}

