package org.subtitle.assistor.dictionary;

import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;

import java.util.*;

public interface WordDictionary {
    public List<String> getColumns();

    public abstract List<Map<String, String>> fromBaseLanguage(String word);
    public abstract List<Map<String, String>> fromRefLanguage(String word);
}
