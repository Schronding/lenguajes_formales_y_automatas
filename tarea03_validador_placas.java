import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Pattern;

public class tarea03_validador_placas {

    String regexAutoParticular = "^U[KLMNP][A-HJ-NPR-Z]-\\d{3}-[A-HJ-NPR-Z]$";
    String regexMotocicleta = "^U[KLMNP][A-HJ-NPR-Z]\\d{2}$";
    String regexTaxi = "^A-\\d{4}-TQ$";
    String regexPublico = "^S[S-Y]-(?!0000)\\d{4}-[E-HJ-NPR-Z]$";

    private JFrame frame;
    private JTextField textFieldPlaca;
    private JButton botonValidar;
    private JLabel labelResultado;

    public tarea03_validador_placas() {
        frame = new JFrame("Validador de Placas de Querétaro");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(450, 200);
        frame.setLayout(null); 

        JLabel labelInstruccion = new JLabel("Introduce la placa a validar:");
        labelInstruccion.setBounds(20, 20, 200, 25);
        frame.add(labelInstruccion);

        textFieldPlaca = new JTextField();
        textFieldPlaca.setBounds(200, 20, 150, 25);
        frame.add(textFieldPlaca);

        botonValidar = new JButton("Validar");
        botonValidar.setBounds(150, 60, 100, 30);
        frame.add(botonValidar);
        
        labelResultado = new JLabel("Esperando placa...");
        labelResultado.setBounds(20, 110, 400, 25);
        labelResultado.setHorizontalAlignment(SwingConstants.CENTER);
        frame.add(labelResultado);

        botonValidar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                validarPlaca();
            }
        });
        
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void validarPlaca() {
        String placaIngresada = textFieldPlaca.getText().toUpperCase().trim();
        
        if (Pattern.matches(regexAutoParticular, placaIngresada)) {
            labelResultado.setText("Placa VÁLIDA de Automóvil Particular.");
            labelResultado.setForeground(new Color(0, 128, 0)); 
        } else if (Pattern.matches(regexMotocicleta, placaIngresada)) {
            labelResultado.setText("Placa VÁLIDA de Motocicleta.");
            labelResultado.setForeground(new Color(0, 128, 0)); 
        } else if (Pattern.matches(regexTaxi, placaIngresada)) {
            labelResultado.setText("Placa VÁLIDA de Taxi.");
            labelResultado.setForeground(new Color(0, 128, 0)); 
        } else if (Pattern.matches(regexPublico, placaIngresada)) {
            labelResultado.setText("Placa VÁLIDA de Transporte Público.");
            labelResultado.setForeground(new Color(0, 128, 0)); 
        } else {
            labelResultado.setText("'" + placaIngresada + "' NO ES VÁLIDA o no corresponde a un formato conocido.");
            labelResultado.setForeground(Color.RED);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new tarea03_validador_placas();
            }
        });
    }
}