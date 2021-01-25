package org.subtitle.assistor;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.sql.SQLException;

import javafx.stage.WindowEvent;
import org.subtitle.assistor.dictionary.jap2eng.JMDict;
import org.subtitle.assistor.sql.SQLiteConnectionPool;
import org.subtitle.assistor.tokenize.JapaneseTokenizer;
import org.subtitle.assistor.udp.UDPServer;

/**
 * JavaFX App
 */

public class App extends Application {

    private static Scene scene;
    private static UDPServer controller;

    private static Float recentXPosition = null;
    private static Float recentYPosition = null;

    public static SQLiteConnectionPool sqlController;

    @Override
    public void start(Stage stage) throws IOException {
        App.openSubtitleFlowWindow(stage);
    }

    protected static String getRunningDirectory(){
        String path = App.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        try {
            return new File(URLDecoder.decode(path, "UTF-8")).getAbsoluteFile().getParent();
        } catch (UnsupportedEncodingException e) {
            System.out.println("Gathering path error: " + e.toString());
        }

        return path;
    }

    protected static void openSubtitleFlowWindow(Stage stage) throws IOException {
        URL styleFolder = App.class.getResource("/stylesheet.css");
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("subtitleflow.fxml"));
        Parent root = fxmlLoader.load();
        scene = new Scene(root, 640, 480);
        controller = new UDPServer(5005);

        scene.getStylesheets().add(styleFolder.toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Subtitle Analyzer");
        stage.show();

        stage.setOnCloseRequest(t -> {
            System.out.println("Abort.");
            controller.pause();
            App.sqlController.closeAllConnections();
            Platform.exit();
            System.exit(0);
        });

        controller.addSubscriber(fxmlLoader.getController());
        controller.start();
    }

    protected static void openInvestigationWindow(String subtitle) throws IOException {
        URL styleFolder = App.class.getResource("/stylesheet.css");
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("subanalyze.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = new Stage();
        Scene scene = new Scene(root, 450, 450);
        scene.getStylesheets().add(styleFolder.toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Analysis");

        // move window to previous position
        if(App.recentXPosition != null && App.recentYPosition != null) {
            stage.setX(App.recentXPosition);
            stage.setY(App.recentYPosition);
        }

        stage.show();

        stage.xProperty().addListener((obs, oldVal, newVal) -> App.recentXPosition = newVal.floatValue());
        stage.yProperty().addListener((obs, oldVal, newVal) -> App.recentYPosition = newVal.floatValue());

        ((SubtitleAnalyzeController)fxmlLoader.getController()).init(
                subtitle, new JapaneseTokenizer(), new JMDict(sqlController.getConnection("jmdict"))
        );
    }

    /*
    public static void main(String[] args) throws SQLException {
        App.sqlController = new SQLiteConnectionPool();
        App.sqlController.addNewConnection("jmdict",
                "/Users/ppogorelov/Python/PycharmProjects/mpv-japanese-sub-assistent/database/test.db");

        System.out.println(App.getRunningDirectory());
        App.launch();
    }
     */
}