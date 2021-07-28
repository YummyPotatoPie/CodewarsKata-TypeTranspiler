package TypeTranspiler;

public class TypeTranspiler {

    private final Tokenizer tokenizer;

    private Token currentToken;

    public TypeTranspiler(Tokenizer tokenizer) {
        this.tokenizer = tokenizer;
        this.currentToken = tokenizer.nextToken();
    }

    private void match(int tag) throws UnsupportedOperationException, IllegalArgumentException {
        if (currentToken == null) throw new UnsupportedOperationException();

        if (currentToken.getTag() == tag) {
            currentToken = tokenizer.nextToken();
            return;
        }
        throw new IllegalArgumentException();
    }

    public String transpile() {
        String transpiled = type();
        if (currentToken != null) return null;
        return transpiled;
    }

    private String type() {
        if (currentToken.getTag() == '(') return functionType();
        else return userType();
    }

    private String functionType() {
        StringBuilder functionType = new StringBuilder();

        match('(');
        functionType.append(parameters());
        match(')');
        match(Attribute.LAMBDAOP);
        functionType.append(type()).append('>');
        return functionType.toString();
    }

    private String parameters() {
        if (currentToken.getTag() == ')') return "Function0" + "<";

        int paramsCount = 1;
        StringBuilder params = new StringBuilder();
        params.append(type());
        if (currentToken != null && currentToken.getTag() == ',') {
            while (currentToken != null && currentToken.getTag() == ',') {
                match(',');
                paramsCount++;
                params.append(',').append(type());
            }
        }
        return "Function" + paramsCount + "<" + params.toString() + ",";
    }

    private String userType() {
        if (currentToken.getTag() != Attribute.IDENTIFIER) {
            throw new IllegalArgumentException();
        }

        StringBuilder userType = new StringBuilder();

        userType.append(simpleUserType());
        if (currentToken != null && currentToken.getTag() == '.') {
            match('.');
            userType.append('.').append(userType());
        }
        return userType.toString();
    }

    private String simpleUserType() {
        StringBuilder simpleUserType = new StringBuilder();

        simpleUserType.append(name());
        if (currentToken != null && currentToken.getTag() == '<') {
            match('<');
            simpleUserType.append('<').append(typeParams()).append('>');
            match('>');
        }
        return simpleUserType.toString();
    }

    private String name() {
        if (currentToken.getTag() == Attribute.IDENTIFIER) {
            String name = ((WordToken)currentToken).getLexeme();
            match(Attribute.IDENTIFIER);
            return name.equals("Int") ? "Integer" : name.equals("Unit") ? "Void" : name;
        }
        throw new IllegalArgumentException();
    }

    private String typeParams() {
        StringBuilder params = new StringBuilder();
        params.append(typeParam());

        if (currentToken != null && currentToken.getTag() == ',') {
            while (currentToken != null && currentToken.getTag() == ',') {
                match(',');
                params.append(',').append(typeParam());
            }
        }
        return params.toString();
    }

    private String typeParam() {
        if (currentToken.getTag() == '*') {
            match('*');
            return "?";
        }

        if (currentToken.getTag() == Attribute.IDENTIFIER) {
            String word = ((WordToken)currentToken).getLexeme();

            if (word.equals("in") || word.equals("out")) {
                match(Attribute.IDENTIFIER);
                if (currentToken.getTag() != Attribute.IDENTIFIER) {
                    return word;
                }
                return word.equals("in") ? "? super " + type() : "? extends " + type();
            }
        }
        return type();
    }

}
