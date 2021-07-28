package TypeTranspiler;

public class WordToken extends Token {

    private String lexeme;

    public WordToken(String lexeme) {
        super(Attribute.IDENTIFIER);
        this.lexeme = lexeme;
    }

    public String getLexeme() {
        return this.lexeme;
    }

}
