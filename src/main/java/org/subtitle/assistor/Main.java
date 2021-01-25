package org.subtitle.assistor;

import javafx.application.Application;
import org.subtitle.assistor.sql.SQLiteConnectionPool;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args){
        try {
            String fileRunningDirectory = App.getRunningDirectory();

            App.sqlController = new SQLiteConnectionPool();
            App.sqlController.addNewConnection("jmdict",
                    fileRunningDirectory + "/jmdict.db");

            Application.launch(App.class);
        } catch (SQLException e) {
            System.out.println("Error occured during connection to the database.");
        }
    }
}
