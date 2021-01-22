package org.subtitle.assistor.tokenize;

import java.util.List;
import java.util.stream.Collectors;

import org.atilika.kuromoji.Tokenizer;

public class JapaneseTokenizer extends BaseTokenizer {
    Tokenizer tokenizer;

    public JapaneseTokenizer(){
        this.tokenizer = Tokenizer.builder().mode(Tokenizer.Mode.NORMAL).build();
    }

    @Override
    public List<LanguageToken> tokenize(String sentence) {
        return tokenizer.tokenize(sentence).stream()
                .map(token -> new LanguageToken(token.getBaseForm(), token.getSurfaceForm()))
                .collect(Collectors.toList());
    }
}
