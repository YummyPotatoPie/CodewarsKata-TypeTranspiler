package TypeTranspiler;

import java.util.Hashtable;
import java.lang.StringBuilder;

public class Tokenizer {

    private final String input;

    private int position = 0;

    private final Hashtable<String, WordToken> keywordTable;

    public Tokenizer(String input) throws IllegalArgumentException {
        if (input == null) throw new IllegalArgumentException();
        this.input = input;
        keywordTable = new Hashtable<>();
        reserveKeyword(new WordToken("in"));
        reserveKeyword(new WordToken("out"));
    }

    private void reserveKeyword(WordToken token) {
        this.keywordTable.put(token.getLexeme(), token);
    }

    private boolean isNotEof() {
        return input.length() != position;
    }

    private void SkipWhiteSpaces() {
        while (isNotEof() && Character.isWhitespace(input.charAt(position))) {
            position++;
        }
    }

    private WordToken readIdentifier() {
        StringBuilder buffer = new StringBuilder();

        while (isNotEof() && (Character.isLetterOrDigit(input.charAt(position)) || input.charAt(position) == '_')) {
            buffer.append(input.charAt(position));
            position++;
        }

        WordToken token = this.keywordTable.get(buffer.toString());
        if (token != null) return token;
        return new WordToken(buffer.toString());
    }

    private Token readLambdaOp() throws IllegalArgumentException {
        position++;
        if (isNotEof() && input.charAt(position) == '>') {
            position++;
            return new Token(Attribute.LAMBDAOP);
        }
        throw new IllegalArgumentException();
    }

    public Token nextToken() {
        SkipWhiteSpaces();
        if (!isNotEof()) return null;
        if (Character.isLetter(input.charAt(position)) || input.charAt(position) == '_') return readIdentifier();
        if (input.charAt(position) == '-') return readLambdaOp();
        if (isNotEof()) {
            char symbol = input.charAt(position);
            position++;
            return new Token(symbol);
        }
        return null;
    }
}
