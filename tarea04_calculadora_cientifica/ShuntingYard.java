package tarea04_calculadora_cientifica;

import java.util.*;
import tarea04_calculadora_cientifica.Tokenizador.Token;
import tarea04_calculadora_cientifica.Tokenizador.TipoToken;

public class ShuntingYard {

    private static final Map<String, Integer> PRECEDENCIA = new HashMap<>();
    private static final Map<String, String> ASOCIATIVIDAD = new HashMap<>();

    static {
        PRECEDENCIA.put("+", 2);
        PRECEDENCIA.put("-", 2);
        PRECEDENCIA.put("X", 3);
        PRECEDENCIA.put("/", 3);
        PRECEDENCIA.put("^", 4); 

        ASOCIATIVIDAD.put("+", "Izquierda");
        ASOCIATIVIDAD.put("-", "Izquierda");
        ASOCIATIVIDAD.put("X", "Izquierda");
        ASOCIATIVIDAD.put("/", "Izquierda");
        ASOCIATIVIDAD.put("^", "Derecha"); 
    }

    private static int getPrecedencia(Token token) {
        return PRECEDENCIA.getOrDefault(token.valor, 0);
    }

    private static boolean esAsociativoIzquierda(Token token) {
        return ASOCIATIVIDAD.getOrDefault(token.valor, "Izquierda").equals("Izquierda");
    }

    public static List<Token> convertirAPostfijo(List<Token> tokensInfijos) {
        List<Token> salida = new ArrayList<>(); 
        Stack<Token> operadores = new Stack<>(); 

        for (Token token : tokensInfijos) {
            switch (token.tipo) {
                case NUMBER:
                case CONSTANT:
                    salida.add(token);
                    break;

                case FUNCTION:
                    operadores.push(token);
                    break;

                case OPERATOR:
                    while (!operadores.isEmpty() && operadores.peek().tipo == TipoToken.OPERATOR &&
                           ((esAsociativoIzquierda(token) && getPrecedencia(token) <= getPrecedencia(operadores.peek())) ||
                            (!esAsociativoIzquierda(token) && getPrecedencia(token) < getPrecedencia(operadores.peek())))) {
                        salida.add(operadores.pop());
                    }
                    operadores.push(token);
                    break;

                case LPAREN:
                    operadores.push(token);
                    break;

                case RPAREN:
                    while (!operadores.isEmpty() && operadores.peek().tipo != TipoToken.LPAREN) {
                        salida.add(operadores.pop());
                    }
                    if (!operadores.isEmpty()) {
                        operadores.pop(); 
                    }
                    
                    if (!operadores.isEmpty() && operadores.peek().tipo == TipoToken.FUNCTION) {
                        salida.add(operadores.pop());
                    }
                    break;
                
                default:
                    break; 
            }
        }
        while (!operadores.isEmpty()) {
            salida.add(operadores.pop());
        }

        return salida;
    }
}