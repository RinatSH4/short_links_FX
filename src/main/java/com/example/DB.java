package com.example;
import java.sql.*;

public class DB {
    private final String HOST = "localhost";
    private final String PORT = "8889";
    private final String LOGIN = "root";
    private final String PASS = "root";
    private final String DB_NAME = "graduation";

    Connection dbConn = null;

    private Connection getDbConnection() throws ClassNotFoundException, SQLException {
        String connStr = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DB_NAME;
        Class.forName("com.mysql.cj.jdbc.Driver");

        dbConn = DriverManager.getConnection(connStr, LOGIN, PASS);
        return dbConn;
    }

    public void isConnected() throws ClassNotFoundException, SQLException {
        dbConn = getDbConnection();
        System.out.println(dbConn.isValid(1000));
    }

    //добавить новую запись
    public void addLinks(String link, String shortLink) {
        String sql = "INSERT INTO `short_links` (`link`, `short_link`) VALUES (?, ?)";

        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(sql);
            prSt.setString(1, link);
            prSt.setString(2, shortLink);
            prSt.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    //проверим, есть ли уже такая сокращенная ссыль
    public boolean isExistsShortLink(String shortLink) {
        String sql = "SELECT id FROM short_links WHERE short_link = ?";

        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(sql);
            prSt.setString(1, shortLink);

            ResultSet res = prSt.executeQuery();
            return res.next();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    //Получить все
    public ResultSet getAllShortLinks() throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM short_links";
        Statement statement = getDbConnection().createStatement();
        return statement.executeQuery(sql);
    }

    //удаляем запись по id
    public void deleteLink(int id) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM short_links WHERE id = ?";
        try (PreparedStatement statement = getDbConnection().prepareStatement(sql)) {
            statement.setInt(1, id);
            int rowsAffected = statement.executeUpdate();
        }
    }

}
