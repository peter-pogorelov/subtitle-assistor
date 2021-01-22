package org.subtitle.assistor.sql;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class SQLiteConnectionsPool {
    Map<String, SQLiteConnection> openConnections;

    public SQLiteConnectionsPool(){
        openConnections = new HashMap<String, SQLiteConnection>();
    }

    public void addNewConnection(String name, String pathToDatabase) throws SQLException {
        this.openConnections.put(name, new SQLiteConnection(pathToDatabase));
        this.openConnections.get(name).openConnection();
    }

    public SQLiteConnection getConnection(String name){
        return this.openConnections.get(name);
    }

    public void closeAllConnections(){
        for(SQLiteConnection connection : this.openConnections.values()){
            connection.closeConnection();
        }
    }
}
