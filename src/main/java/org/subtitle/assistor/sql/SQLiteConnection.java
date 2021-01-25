package org.subtitle.assistor.sql;

import java.sql.*;

public class SQLiteConnection implements SQLConnection {
    String pathToDatabase;
    Connection dbConnection;
    Statement stmt;

    public SQLiteConnection(String pathToDatabase)
    {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.out.println("Error. Unable to initialize database driver.");
        }

        this.pathToDatabase = pathToDatabase;
    }

    public void openConnection() throws SQLException {
        String url = "jdbc:sqlite:" + this.pathToDatabase;
        // create a connection to the database
        this.dbConnection = DriverManager.getConnection(url);
        this.stmt = this.dbConnection.createStatement();
        System.out.println("Connection to SQLite has been established.");
    }

    public void closeConnection() {
        try {
            if (this.dbConnection != null) {
                this.stmt.close();
                this.dbConnection.close();
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public Connection getConnection(){
        return this.dbConnection;
    }

    public Statement getStatement() {
        return this.stmt;
    }

    @Override
    public ResultSet getQueryResult(String query) throws SQLException{
        ResultSet result = this.stmt.executeQuery(query);

        return result;
    }
}