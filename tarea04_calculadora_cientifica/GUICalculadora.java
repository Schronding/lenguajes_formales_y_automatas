package tarea04_calculadora_cientifica;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUICalculadora extends JFrame implements ActionListener {

    private JTextField pantalla;
    private String[] etiquetasBotones = {
        "SHIFT", "ON",
        "x!", "x⁻¹", "x³", "√", "x²", "xʸ",
        "Log", "Ln", "sin", "cos", "tan", "(", ")",
        "7", "8", "9", "Del", "AC",
        "4", "5", "6", "X", "+",
        "1", "2", "3", "-",
        "0", ".", "Exp", "="
    };

    public GUICalculadora() {
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
        }

        add(panelBotones, BorderLayout.CENTER);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String comando = e.getActionCommand();
        String textoActual = pantalla.getText();

        switch (comando) {
            case "=":
                break;
            case "AC":
                pantalla.setText("");
                break;
            case "Del":
                if (!textoActual.isEmpty()) {
                    pantalla.setText(textoActual.substring(0, textoActual.length() - 1));
                }
                break;
            default:
                pantalla.setText(textoActual + comando);
                break;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GUICalculadora());
    }
}