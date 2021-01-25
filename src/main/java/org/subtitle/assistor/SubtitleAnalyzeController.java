package org.subtitle.assistor;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.IndexRange;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.skin.TextAreaSkin;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.HitInfo;
import javafx.util.Pair;

import org.subtitle.assistor.dictionary.WordDictionary;
import org.subtitle.assistor.tokenize.BaseTokenizer;
import org.subtitle.assistor.tokenize.LanguageToken;

public class SubtitleAnalyzeController {
    @FXML TextArea subtitleEditField;
    @FXML TableView translationTable;

    private List<LanguageToken> tokens;
    private WordDictionary dictionary;
    private String choosenWord;


    public void init(String subtitle, BaseTokenizer tokenizer, WordDictionary dictionary){
        this.translationTable.getSelectionModel().setCellSelectionEnabled(true);

        this.dictionary = dictionary;
        if(subtitle != null && !subtitle.isEmpty() && !subtitle.isBlank()) {
            this.tokens = tokenizer.tokenize(subtitle);
            subtitleEditField.setText(subtitle);
        }
    }

    @FXML
    private void onMouseReleased(MouseEvent handler){
        String selectedText = this.subtitleEditField.getSelectedText();

        int x = -1;
        int y = -1;

        if(selectedText.equals("")) {
            TextAreaSkin skin = (TextAreaSkin) this.subtitleEditField.getSkin();
            HitInfo hit = skin.getIndex(handler.getX(), handler.getY());

            Pair<Integer, Integer> window = BaseTokenizer.getWordWindowFromCharacterIndex(hit.getInsertionIndex(), this.tokens);

            if(window != null) {
                x = window.getKey();
                y = window.getValue();

                this.subtitleEditField.selectRange(x, y);
            }
        } else {
            IndexRange rg = this.subtitleEditField.getSelection();
            x = rg.getStart();
            y = rg.getEnd();
        }

        if(x != -1 && y != -1) {
            choosenWord = this.subtitleEditField.getText().substring(x, y);

            return;
        }

        choosenWord = null;
    }

    @FXML
    private void lookupTranslation(){
        List<Map<String, String>> searchResult = this.dictionary.fromBaseLanguage(this.choosenWord);
        this.translationTable.getColumns().clear();
        this.translationTable.getColumns().addAll(
                SubtitleAnalyzeController.getTableColumnsFX(this.dictionary.getColumns())
        );

        if(searchResult != null) {
            this.translationTable.setItems(SubtitleAnalyzeController.getTableDataFX(searchResult));
        }
    }

    @FXML
    private void switchToPrimary() throws IOException {
        //App.setRoot("primary");
    }

    private static List<TableColumn> getTableColumnsFX(List<String> columnNames) {
        List<TableColumn> columnList = new LinkedList<>();

        for(String columnName : columnNames){
            TableColumn column = new TableColumn<Map, String>(columnName);

            column.setCellValueFactory(
                    cellDataFeatures ->
                            new ReadOnlyStringWrapper(
                                    (String)((TableColumn.CellDataFeatures<Map, String>)cellDataFeatures)
                                            .getValue().get(columnName)
                            )
            );

            columnList.add(column);
        }

        return columnList;
    }


    private static ObservableList getTableDataFX(List<Map<String, String>> searchResult){
        return FXCollections.observableList(searchResult);
    }
}