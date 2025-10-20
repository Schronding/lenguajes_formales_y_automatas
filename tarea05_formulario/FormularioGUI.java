import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.LinkedHashMap;
import java.util.Map;

public class FormularioGUI extends JFrame implements ActionListener {

    private final Map<String, JComponent> campos = new LinkedHashMap<>();
    private final Map<String, String> placeholders = new LinkedHashMap<>();
    private JPasswordField campoContrasena;

    public FormularioGUI() {
        setTitle("Formulario de Datos Generales");
        setSize(500, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        placeholders.put("Nombre(s)", "Ej: ANA SOFIA");
        placeholders.put("Fecha de nacimiento", "dd/mm/aaaa");
        placeholders.put("Nacionalidad", "Ej: MX");
        placeholders.put("RFC", "AAAA010101BBB");
        placeholders.put("CURP", "AAAA010101HXXNNN01");
        placeholders.put("Calle", "AV. REVOLUCION");
        placeholders.put("Número Ext.", "123");
        placeholders.put("Código Postal", "76230");
        placeholders.put("Teléfono", "XXX XXX XXXX");
        placeholders.put("Correo electrónico", "usuario@dominio.com");
        placeholders.put("Contraseña", "Mínimo 8 caracteres");
        placeholders.put("Tarjeta bancaria", "XXXX XXXX XXXX XXXX");

        JPanel panelCampos = new JPanel(new GridLayout(0, 2, 10, 10));
        panelCampos.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] etiquetas = {
            "Nombre(s)", "Apellido Paterno", "Apellido Materno", "Fecha de nacimiento",
            "Género", "Nacionalidad", "Estado civil", "RFC", "CURP",
            "Calle", "Número Ext.", "Colonia", "Ciudad", "Estado", "Código Postal",
            "Teléfono", "Correo electrónico", "Contraseña", "Tarjeta bancaria"
        };
        
        String[] opcionesGenero = {"", "MASCULINO", "FEMENINO"};
        String[] opcionesEstadoCivil = {"", "SOLTERO(A)", "CASADO(A)", "DIVORCIADO(A)", "VIUDO(A)", "UNION LIBRE"};

        for (String etiqueta : etiquetas) {
             if (etiqueta.equals("Contraseña")) {
                JPanel panelEtiqueta = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
                panelEtiqueta.add(new JLabel("Contraseña:"));
                JButton infoButton = new JButton("?");
                infoButton.setMargin(new Insets(0, 2, 0, 2));
                infoButton.addActionListener(e -> mostrarRequisitosContrasena());
                panelEtiqueta.add(infoButton);
                panelCampos.add(panelEtiqueta);

                JPanel panelCampo = new JPanel(new BorderLayout(5, 0));
                campoContrasena = new JPasswordField();
                campos.put(etiqueta, campoContrasena);
                addPlaceholderLogic(campoContrasena, placeholders.get("Contraseña"));
                panelCampo.add(campoContrasena, BorderLayout.CENTER);

                JCheckBox checkMostrar = new JCheckBox("Mostrar");
                checkMostrar.addActionListener(e -> toggleVisibilidadContrasena(checkMostrar.isSelected()));
                panelCampo.add(checkMostrar, BorderLayout.EAST);
                panelCampos.add(panelCampo);
            } else {
                panelCampos.add(new JLabel(etiqueta + ":"));
                if (etiqueta.equals("Género")) {
                    JComboBox<String> comboBox = new JComboBox<>(opcionesGenero);
                    campos.put(etiqueta, comboBox);
                    panelCampos.add(comboBox);
                } else if (etiqueta.equals("Estado civil")) {
                    JComboBox<String> comboBox = new JComboBox<>(opcionesEstadoCivil);
                    campos.put(etiqueta, comboBox);
                    panelCampos.add(comboBox);
                } else {
                    JTextField textField = new JTextField();
                    campos.put(etiqueta, textField);
                    addPlaceholderLogic(textField, placeholders.getOrDefault(etiqueta, ""));
                    panelCampos.add(textField);
                }
            }
        }

        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton botonValidar = new JButton("Validar Formulario");
        botonValidar.addActionListener(this);
        panelBoton.add(botonValidar);

        add(new JScrollPane(panelCampos), BorderLayout.CENTER);
        add(panelBoton, BorderLayout.SOUTH);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void addPlaceholderLogic(JTextField textField, String placeholder) {
        if (placeholder == null || placeholder.isEmpty()) return;

        textField.setText(placeholder);
        textField.setForeground(Color.GRAY);

        if (textField instanceof JPasswordField) {
            ((JPasswordField) textField).setEchoChar('•');
        }

        textField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textField.getForeground() == Color.GRAY) {
                    textField.setText("");
                    textField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                String currentText = (textField instanceof JPasswordField)
                    ? new String(((JPasswordField) textField).getPassword())
                    : textField.getText();

                if (currentText.isEmpty()) {
                    textField.setForeground(Color.GRAY);
                    textField.setText(placeholder);
                }
            }
        });
    }

    private void mostrarRequisitosContrasena() {
        JOptionPane.showMessageDialog(this,
            "La contraseña debe cumplir con lo siguiente:\n\n" +
            "- Mínimo 8 caracteres de longitud\n" +
            "- Mínimo 1 letra minúscula\n" +
            "- Mínimo 1 letra mayúscula\n" +
            "- Mínimo 2 números\n" +
            "- Mínimo 1 símbolo especial (ej. -, _, *, !)",
            "Requisitos de Contraseña",
            JOptionPane.INFORMATION_MESSAGE);
    }

    private void toggleVisibilidadContrasena(boolean mostrar) {
        if (campoContrasena.getForeground() == Color.BLACK) {
            campoContrasena.setEchoChar(mostrar ? (char) 0 : '•');
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Map<String, String> errores = ValidadorFormulario.validar(this.campos, this.placeholders);
        if (errores.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "¡Formulario validado exitosamente!", "Validación Correcta",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            StringBuilder mensajeError = new StringBuilder("Por favor, corrige los siguientes campos:\n\n");
            errores.values().forEach(error -> mensajeError.append("- ").append(error).append("\n"));
            JOptionPane.showMessageDialog(this,
                    mensajeError.toString(), "Errores de Validación",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FormularioGUI());
    }
}