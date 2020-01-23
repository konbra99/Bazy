package com.company;

import java.sql.*;

public class Polaczenie {
    private Statement statement;

    public Polaczenie() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/Biblioteka?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
        String uzytkownik = "krazi225";
        String haslo = "krazior225";
        Connection connection = DriverManager.getConnection(url, uzytkownik, haslo);
        statement = connection.createStatement();
    }

    public ResultSet zapytanie(String query) throws SQLException {
        return statement.executeQuery(query);
    }

    public int wstawianie(String query) throws SQLException {
        return statement.executeUpdate(query);
    }
}
