package org.subtitle.assistor;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;

import org.subtitle.assistor.dictionary.jap2eng.JMDict;
import org.subtitle.assistor.sql.SQLiteConnectionsPool;
import org.subtitle.assistor.tokenize.JapaneseTokenizer;
import org.subtitle.assistor.udp.UDPController;

/**
 * JavaFX App
 */

public class App extends Application {

    private static Scene scene;
    private static UDPController controller;
    private static SQLiteConnectionsPool sqlController;

    @Override
    public void start(Stage stage) throws IOException {
        App.openSubtitleFlowWindow(stage);
    }

    protected static void openSubtitleFlowWindow(Stage stage) throws IOException {
        URL styleFolder = App.class.getResource("/stylesheet.css");
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("subtitleflow.fxml"));
        Parent root = fxmlLoader.load();
        scene = new Scene(root, 640, 480);
        controller = new UDPController(5005);

        scene.getStylesheets().add(styleFolder.toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Subtitle Analyzer");
        stage.show();

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
        stage.show();


        ((SubtitleAnalyzeController)fxmlLoader.getController()).init(
                subtitle, new JapaneseTokenizer(), new JMDict(sqlController.getConnection("jmdict"))
        );
    }

    public static void main(String[] args) throws SQLException {
        sqlController = new SQLiteConnectionsPool();
        sqlController.addNewConnection("jmdict",
                "/Users/ppogorelov/Python/PycharmProjects/mpv-japanese-sub-assistent/database/test.db");

        launch();
    }

}