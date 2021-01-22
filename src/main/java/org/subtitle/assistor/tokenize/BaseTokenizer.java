package org.subtitle.assistor.tokenize;

import javafx.util.Pair;

import java.util.List;

public abstract class BaseTokenizer {
    public abstract List<LanguageToken> tokenize(String sentence);

    public static Pair<Integer, Integer> getWordWindowFromCharacterIndex(int charIndex, List<LanguageToken> tokens){
        int cumLen = 0;
        for(LanguageToken token: tokens){
            if (cumLen < charIndex && charIndex <= cumLen + token.surfaceForm.length()) {
                return new Pair<>(cumLen, cumLen + token.surfaceForm.length());
            }
            cumLen += token.surfaceForm.length();
        }

        return null;
    }
}
