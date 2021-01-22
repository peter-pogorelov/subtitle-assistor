package org.subtitle.assistor.dictionary.jap2eng;

import java.sql.*;
import java.util.*;

import org.subtitle.assistor.sql.SQLConnection;


public class JMDict extends BaseJ2EDictionary {
    SQLConnection dbUtil;

    public JMDict(SQLConnection dbConnectionUtil) {
        this.dbUtil = dbConnectionUtil;
    }

    private String makeJapaneseSearchQuery(String japaneseWord){
        return "SELECT * FROM warehouse WHERE kana LIKE '%" + japaneseWord +  "%' OR kanji LIKE '%" + japaneseWord + "%'";
    }

    private String makeEnglishSearchQuery(String englishWord){
        return "SELECT * FROM warehouse WHERE gloss LIKE '" + englishWord +  "'";
    }

    private List<Map<String, String>> queryDictionaryRecord(String query) throws SQLException{
        final List searchResult = new LinkedList<HashMap<String, String>>();
        final ResultSet result = this.dbUtil.getQueryResult(query);

        while(result.next()){
            searchResult.add(new HashMap<String, String>() {{
                put("kana", result.getString("kana"));
                put("kanji", result.getString("kanji"));
                put("english", result.getString("gloss"));
                put("pos", result.getString("pos"));
            }});
        }

        return searchResult;
    }

    @Override
    public List<Map<String, String>> fromBaseLanguage(String word) {
        try {
            return this.queryDictionaryRecord(this.makeJapaneseSearchQuery(word));
        } catch (SQLException e){
            return null;
        }
    }

    @Override
    public List<Map<String, String>> fromRefLanguage(String word) {
        try {
            return this.queryDictionaryRecord(this.makeEnglishSearchQuery(word));
        } catch (SQLException e) {
            return null;
        }
    }
}
