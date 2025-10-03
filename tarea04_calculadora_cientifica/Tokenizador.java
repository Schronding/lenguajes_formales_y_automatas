package tarea04_calculadora_cientifica;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tokenizador {

    public static enum TipoToken {
        NUMBER, OPERATOR, FUNCTION, LPAREN, RPAREN, CONSTANT, FACTORIAL
    }
    public static class Token {
        public final TipoToken tipo;
        public final String valor;
        public Token(TipoToken tipo, String valor) { this.tipo = tipo; this.valor = valor; }
        @Override
        public String toString() { return "Token{" + "tipo=" + tipo + ", valor='" + valor + '\'' + '}'; }
    }

    public static List<Token> tokenizar(String expresion) {
        List<Token> tokens = new ArrayList<>();
        String patronTokens = "(?<NUMBER>[0-9]*\\.?[0-9]+)|(?<FUNCTION>sin|cos|tan|Log|Ln|asin|acos|atan)|(?<OPERATOR>[+\\-X/\\^])|(?<FACTORIAL>!)|(?<LPAREN>\\()|(?<RPAREN>\\))|(?<CONSTANT>Exp)";
        Pattern pattern = Pattern.compile(patronTokens);
        Matcher matcher = pattern.matcher(expresion);
        while (matcher.find()) {
            if (matcher.group("NUMBER") != null) tokens.add(new Token(TipoToken.NUMBER, matcher.group("NUMBER")));
            else if (matcher.group("FUNCTION") != null) tokens.add(new Token(TipoToken.FUNCTION, matcher.group("FUNCTION")));
            else if (matcher.group("OPERATOR") != null) tokens.add(new Token(TipoToken.OPERATOR, matcher.group("OPERATOR")));
            else if (matcher.group("FACTORIAL") != null) tokens.add(new Token(TipoToken.FACTORIAL, matcher.group("FACTORIAL")));
            else if (matcher.group("LPAREN") != null) tokens.add(new Token(TipoToken.LPAREN, matcher.group("LPAREN")));
            else if (matcher.group("RPAREN") != null) tokens.add(new Token(TipoToken.RPAREN, matcher.group("RPAREN")));
            else if (matcher.group("CONSTANT") != null) tokens.add(new Token(TipoToken.CONSTANT, matcher.group("CONSTANT")));
        }
        return tokens;
    }


    public static List<Token> preProcesarTokens(List<Token> tokens) {
        tokens = procesarNegativosUnarios(tokens);
        tokens = procesarMultiplicacionImplicita(tokens);
        return tokens;
    }

 
    private static List<Token> procesarNegativosUnarios(List<Token> tokens) {
        List<Token> tokensProcesados = new ArrayList<>();
        for (int i = 0; i < tokens.size(); i++) {
            Token actual = tokens.get(i);
            if (actual.tipo == TipoToken.OPERATOR && actual.valor.equals("-")) {
                if (i == 0 || tokens.get(i - 1).tipo == TipoToken.OPERATOR || tokens.get(i - 1).tipo == TipoToken.LPAREN) {
                    tokensProcesados.add(new Token(TipoToken.NUMBER, "0"));
                }
            }
            tokensProcesados.add(actual);
        }
        return tokensProcesados;
    }


    private static List<Token> procesarMultiplicacionImplicita(List<Token> tokens) {
        List<Token> tokensProcesados = new ArrayList<>();
        for (int i = 0; i < tokens.size(); i++) {
            tokensProcesados.add(tokens.get(i));
            if (i < tokens.size() - 1) {
                Token actual = tokens.get(i);
                Token siguiente = tokens.get(i + 1);
                if ((actual.tipo == TipoToken.NUMBER || actual.tipo == TipoToken.RPAREN) &&
                    (siguiente.tipo == TipoToken.LPAREN || siguiente.tipo == TipoToken.FUNCTION)) {
                    tokensProcesados.add(new Token(TipoToken.OPERATOR, "X"));
                }
            }
        }
        return tokensProcesados;
    }
}