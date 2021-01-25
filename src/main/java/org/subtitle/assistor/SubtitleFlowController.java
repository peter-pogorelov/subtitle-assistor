package org.subtitle.assistor;

import java.io.IOException;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import org.subtitle.assistor.udp.UDPSubscriber;

public class SubtitleFlowController implements UDPSubscriber {
    @FXML private ListView subtitleList;

    @FXML public void onMouseClicked(MouseEvent arg0) throws IOException {
        String selectedItem = (String) subtitleList.getSelectionModel().getSelectedItem();
        if(selectedItem != null && !selectedItem.isEmpty())
            App.openInvestigationWindow(selectedItem);
    }

    @Override
    public void onCommandReceived(String command) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                ObservableList items = subtitleList.getItems();
                items.add(0, command);
            }
        });
    }
}
