package TypeTranspiler;

public class Main {

    public static void main(String[] args) {
        TypeTranspiler transpiler = new TypeTranspiler(new Tokenizer("A     "));

        String parsed = transpiler.transpile();
        System.out.println(parsed);
    }

}
