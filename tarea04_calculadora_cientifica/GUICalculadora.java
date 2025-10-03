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
    private Map<String, JButton> botonesModificables = new HashMap<>();
    private Map<String, String> mapaShift = new HashMap<>();
    private String[] etiquetasBotones = {
        "SHIFT", "ON",
        "x!", "x⁻¹", "^", "√", "x²", "xʸ",
        "Log", "Ln", "sin", "cos", "tan", "(", ")",
        "7", "8", "9", "Del", "AC",
        "4", "5", "6", "X", "/",
        "1", "2", "3", "+", "-",
        "0", ".", "Exp", "="
    };

    public GUICalculadora() {
        mapaShift.put("sin", "asin");
        mapaShift.put("cos", "acos");
        mapaShift.put("tan", "atan");
        mapaShift.put("Log", "10^");
        mapaShift.put("Ln", "e^");
        mapaShift.put("x²", "∛"); 
        mapaShift.put("^", "ʸ√x");

        setTitle("Calculadora Científica");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        pantalla = new JTextField();
        pantalla.setFont(new Font("Arial", Font.PLAIN, 24));
        pantalla.setHorizontalAlignment(JTextField.RIGHT);
        pantalla.setEditable(false);
        add(pantalla, BorderLayout.NORTH);

        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new GridLayout(0, 5, 5, 5));
        
        for (String etiqueta : etiquetasBotones) {
            JButton boton = new JButton(etiqueta);
            boton.setFont(new Font("Arial", Font.BOLD, 16));
            boton.addActionListener(this);
            panelBotones.add(boton);

            if (mapaShift.containsKey(etiqueta)) {
                botonesModificables.put(etiqueta, boton);
            }
        }
        add(panelBotones, BorderLayout.CENTER);
        setLocationRelativeTo(null);
        setVisible(true);
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
            System.out.println("Tokens Pre-procesados: " + tokens); 

            List<Token> tokensRPN = ShuntingYard.convertirAPostfijo(tokens);

            double resultado = EvaluadorRPN.evaluar(tokensRPN);

            pantalla.setText(String.valueOf(resultado));
            esResultado = true;

        } catch (Exception ex) {
            pantalla.setText("Error");
            esResultado = true;
        }
    }
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
                if (esResultado || textoActual.equals("Error")) {
                    pantalla.setText(""); 
                    esResultado = false;  
                }
                if(comando.equals("10^") || comando.equals("e^")){
                    pantalla.setText(pantalla.getText() + (comando.equals("10^") ? "10^" : "Exp("));
                } else {
                    pantalla.setText(pantalla.getText() + comando);
                }
                break;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GUICalculadora());
    }
}