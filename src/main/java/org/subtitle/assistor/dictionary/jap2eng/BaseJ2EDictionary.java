package org.subtitle.assistor.dictionary.jap2eng;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import org.subtitle.assistor.dictionary.WordDictionary;

import java.util.*;
import java.util.stream.Collectors;



public abstract class BaseJ2EDictionary implements WordDictionary {
    protected List<String> columnOrder;

    public BaseJ2EDictionary(){
        this.columnOrder = List.of("kanji", "kana", "english", "pos");
    }

    private static int wordRemainderLength(String w1, String w2) {
        Set l1 = new HashSet(Arrays.asList(w1.toCharArray()));
        Set l2 = new HashSet(Arrays.asList(w2.toCharArray()));

        if (l1.size() == 0) {
            return l2.size();
        } else {
            l2.removeAll(l1);
            return l2.size();
        }
    }

    public static List<Map<String, String>> sortJapaneseSearchResult(String word, List<Map<String, String>> result){
        return result.stream().sorted(
                (left, right) -> Math.min(
                        wordRemainderLength(word, left.get("kanji")),
                        wordRemainderLength(word, left.get("kana"))
                ) - Math.min(
                        wordRemainderLength(word, right.get("kanji")),
                        wordRemainderLength(word, right.get("kana"))
                )
        ).collect(Collectors.toList());
    }

    public static List<Map<String, String>> sortEnglishSearchResult(String word, List<Map<String, String>> result){
        return result.stream().sorted(
                (left, right) ->
                        wordRemainderLength(word, left.get("english")) - wordRemainderLength(word, right.get("english"))
        ).collect(Collectors.toList());
    }

    @Override
    public List<String> getColumns() {
        return this.columnOrder;
    }
}
