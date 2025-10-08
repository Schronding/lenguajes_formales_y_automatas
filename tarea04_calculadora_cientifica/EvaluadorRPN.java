package tarea04_calculadora_cientifica;

import java.util.List;
import java.util.Stack;
import tarea04_calculadora_cientifica.Tokenizador.Token;

public class EvaluadorRPN {

    private static double factorial(double n) {
        if (n < 0 || n != Math.floor(n)) {
            throw new IllegalArgumentException("Factorial solo para enteros no negativos.");
        }
        if (n == 0) return 1;
        double resultado = 1;
        for (int i = 1; i <= n; i++) {
            resultado *= i;
        }
        return resultado;
    }

    public static double evaluar(List<Token> tokensRPN) {
        Stack<Double> pila = new Stack<>();

        for (Token token : tokensRPN) {
            switch (token.tipo) {
                case NUMBER:
                    pila.push(Double.parseDouble(token.valor));
                    break;
                
                case CONSTANT:
                    if (token.valor.equals("e")) {
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
                            pila.push(Math.exp(argumento)); 
                            break;
                        case "sqrt":
                            pila.push(Math.sqrt(argumento));
                            break;
                        case "sqr": // x²
                            pila.push(Math.pow(argumento, 2));
                            break;
                        case "cube": // x³
                            pila.push(Math.pow(argumento, 3));
                            break;
                        case "inv": // x⁻¹
                            pila.push(1 / argumento);
                            break;
                        case "fact":
                            pila.push(factorial(argumento));
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