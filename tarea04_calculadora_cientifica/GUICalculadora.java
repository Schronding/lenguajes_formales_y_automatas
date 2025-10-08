package tarea04_calculadora_cientifica;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import tarea04_calculadora_cientifica.Tokenizador.Token;

public class GUICalculadora extends JFrame implements ActionListener {

    private JTextField pantalla;
    private boolean esResultado = false;
    private boolean shiftActivo = false;
    private double ultimaRespuesta = 0.0;

    private Map<String, JButton> botonesModificables = new HashMap<>();
    private Map<String, String> mapaShift = new HashMap<>();
    private Map<String, String> mapaFunciones = new HashMap<>();

    public GUICalculadora() {
        mapaShift.put("sin", "asin");
        mapaShift.put("cos", "acos");
        mapaShift.put("tan", "atan");
        mapaShift.put("Log", "10^");
        mapaShift.put("Ln", "exp");
        mapaShift.put("x²", "√");
        mapaShift.put("x³", "∛");
        
        mapaFunciones.put("x!", "fact(");
        mapaFunciones.put("x⁻¹", "inv(");
        mapaFunciones.put("x³", "cube(");
        mapaFunciones.put("x²", "sqr(");
        mapaFunciones.put("√", "sqrt(");
        mapaFunciones.put("sin", "sin(");
        mapaFunciones.put("cos", "cos(");
        mapaFunciones.put("tan", "tan(");
        mapaFunciones.put("Log", "Log(");
        mapaFunciones.put("Ln", "Ln(");
        mapaFunciones.put("asin", "asin(");
        mapaFunciones.put("acos", "acos(");
        mapaFunciones.put("atan", "atan(");
        mapaFunciones.put("exp", "exp(");
        mapaFunciones.put("∛", "cbrt(");

        setTitle("Calculadora Científica");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        pantalla = new JTextField();
        pantalla.setFont(new Font("Arial", Font.PLAIN, 24));
        pantalla.setHorizontalAlignment(JTextField.RIGHT);
        pantalla.setEditable(false);
        add(pantalla, BorderLayout.NORTH);

        JPanel panelPrincipalBotones = new JPanel(new BorderLayout());
        
        String[] etiquetasSuperiores = {
            "SHIFT", "Ans", "x!", "x⁻¹", "x³",
            "x²", "^", "Log", "Ln", "sin",
            "cos", "tan", "(", ")", ""
        };
        
        JPanel panelSuperior = new JPanel(new GridLayout(3, 5, 5, 5));
        for (String etiqueta : etiquetasSuperiores) {
            crearBotonOEspacio(etiqueta, panelSuperior);
        }
        
        String[] etiquetasInferiores = {
            "7", "8", "9", "Del", "AC",
            "4", "5", "6", "X", "/",
            "1", "2", "3", "+", "-",
            "0", ".", "Exp", "="
        };
        
        JPanel panelInferior = new JPanel(new GridLayout(4, 5, 5, 5));
        for (String etiqueta : etiquetasInferiores) {
            crearBotonOEspacio(etiqueta, panelInferior);
        }
        
        panelPrincipalBotones.add(panelSuperior, BorderLayout.NORTH);
        panelPrincipalBotones.add(panelInferior, BorderLayout.CENTER);

        add(panelPrincipalBotones, BorderLayout.CENTER);
        
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    private void crearBotonOEspacio(String etiqueta, JPanel panel) {
        if (etiqueta.isEmpty()) {
            panel.add(new JLabel(""));
        } else {
            JButton boton = new JButton(etiqueta);
            boton.setFont(new Font("Arial", Font.BOLD, 16));
            boton.addActionListener(this);
            panel.add(boton);
            if (mapaShift.containsKey(etiqueta)) {
                botonesModificables.put(etiqueta, boton);
            }
        }
    }

    private void actualizarBotonesShift() {
        for (Map.Entry<String, String> entry : mapaShift.entrySet()) {
            String original = entry.getKey();
            String conShift = entry.getValue();
            JButton boton = botonesModificables.get(original);
            if (boton != null) {
                boton.setText(shiftActivo ? conShift : original);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String comando = e.getActionCommand();

        if (esResultado && !esOperadorContinuo(comando) && !comando.equals("SHIFT")) {
            pantalla.setText("");
            esResultado = false;
        }
        if (pantalla.getText().equals("Error")) {
            pantalla.setText("");
        }
        
        String textoActual = pantalla.getText();

        switch (comando) {
            case "SHIFT":
                shiftActivo = !shiftActivo;
                actualizarBotonesShift();
                break;
            case "=":
                if (!textoActual.isEmpty()) {
                    try {
                        List<Token> tokensCrudos = Tokenizador.tokenizar(textoActual);
                        List<Token> tokens = Tokenizador.preProcesarTokens(tokensCrudos);
                        List<Token> tokensRPN = ShuntingYard.convertirAPostfijo(tokens);
                        double resultado = EvaluadorRPN.evaluar(tokensRPN);
                        ultimaRespuesta = resultado;
                        pantalla.setText(String.format("%." + 10 + "g", resultado));
                        esResultado = true;
                    } catch (Exception ex) {
                        pantalla.setText("Error");
                        esResultado = true;
                    }
                }
                break;
            case "Ans":
                pantalla.setText(textoActual + String.format("%." + 10 + "g", ultimaRespuesta));
                break;
            case "Exp":
                pantalla.setText(textoActual + "e");
                break;
            case "AC":
                pantalla.setText("");
                esResultado = false;
                break;
            case "Del":
                if (!textoActual.isEmpty()) {
                    pantalla.setText(textoActual.substring(0, textoActual.length() - 1));
                }
                break;
            default:
                if (mapaFunciones.containsKey(comando)) {
                    pantalla.setText(textoActual + mapaFunciones.get(comando));
                } else {
                    pantalla.setText(textoActual + comando);
                }
                break;
        }
    }

    private boolean esOperadorContinuo(String comando) {
        String[] operadores = {"+", "-", "X", "/", "^"};
        for (String op : operadores) {
            if (op.equals(comando)) return true;
        }
        return false;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GUICalculadora());
    }
}