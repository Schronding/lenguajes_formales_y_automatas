package tarea04_calculadora_cientifica;

import java.util.List;
import java.util.Stack;
import tarea04_calculadora_cientifica.Tokenizador.Token;

public class EvaluadorRPN {

    public static double evaluar(List<Token> tokensRPN) {
        Stack<Double> pila = new Stack<>();

        for (Token token : tokensRPN) {
            switch (token.tipo) {
                case NUMBER:
                    pila.push(Double.parseDouble(token.valor));
                    break;
                
                case CONSTANT:
                    if (token.valor.equals("Exp")) {
                        pila.push(Math.E);
                    }
                    break;

                case OPERATOR:
                    double b = pila.pop();
                    double a = pila.pop();
                    
                    switch (token.valor) {
                        case "+":
                            pila.push(a + b);
                            break;
                        case "-":
                            pila.push(a - b);
                            break;
                        case "X":
                            pila.push(a * b);
                            break;
                        case "/":
                            pila.push(a / b);
                            break;
                        case "^":
                            pila.push(Math.pow(a, b));
                            break;
                    }
                    break;
                
                case FUNCTION:
                    double argumento = pila.pop();
                    switch (token.valor) {
                        case "exp":
                            pila.push(Math.exp(argumento)); // Math.exp(x) es e^x
                            break;
                        case "√":
                            pila.push(Math.sqrt(argumento));
                            break;
                        case "sin":
                            pila.push(Math.sin(Math.toRadians(argumento)));
                            break;
                        case "cos":
                            pila.push(Math.cos(Math.toRadians(argumento)));
                            break;
                        case "tan":
                            pila.push(Math.tan(Math.toRadians(argumento)));
                            break;
                        case "Log": 
                            pila.push(Math.log10(argumento));
                            break;
                        case "Ln": 
                            pila.push(Math.log(argumento));
                            break;
                        case "asin":
                            pila.push(Math.toDegrees(Math.asin(argumento)));
                            break;
                        case "acos":
                            pila.push(Math.toDegrees(Math.acos(argumento)));
                            break;
                        case "atan":
                            pila.push(Math.toDegrees(Math.atan(argumento)));
                            break;
                    }
                    break;
                
                default:
                    break;
            }
        }
        return pila.pop();
    }
}