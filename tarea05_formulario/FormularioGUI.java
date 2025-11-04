import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.DocumentFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JFormattedTextField.AbstractFormatter;
import java.text.SimpleDateFormat;
import java.text.ParseException;

public class FormularioGUI extends JFrame implements ActionListener {

    private final Map<String, JComponent> campos = new LinkedHashMap<>();
    private final Map<String, String> placeholders = new LinkedHashMap<>();
    
    private JPasswordField campoContrasena;
    private JDatePickerImpl datePicker;
    private JComboBox<String> comboNacionalidad;
    private JComboBox<String> comboEntidad;
    private JTextField telCampo1, telCampo2, telCampo3;
    private JTextField cardCampo1, cardCampo2, cardCampo3, cardCampo4;
    private JTextField campoRFC, campoCURP;

    public FormularioGUI() {
        setTitle("Formulario de Datos Generales");
        setSize(550, 850);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        placeholders.put("Nombre(s)", "Ej: ANA SOFIA");
        placeholders.put("Calle", "AV. REVOLUCION");
        placeholders.put("Número Ext.", "123");
        placeholders.put("Colonia", "REVOLUCION");
        placeholders.put("Código Postal", "76230");
        placeholders.put("Correo electrónico", "usuario@dominio.com");
        placeholders.put("Contraseña", "Mínimo 8 caracteres");

        JPanel panelCampos = new JPanel(new GridLayout(0, 2, 10, 10));
        panelCampos.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] etiquetas = {
            "Nombre(s)", "Apellido Paterno", "Apellido Materno", "Fecha de nacimiento",
            "Género", "Nacionalidad", "Entidad Federativa", "Estado civil", "RFC", "CURP",
            "Calle", "Número Ext.", "Colonia", "Código Postal",
            "Teléfono", "Correo electrónico", "Contraseña", "Tarjeta bancaria"
        };
        
        String[] opcionesGenero = {"", "MASCULINO", "FEMENINO"};
        String[] opcionesEstadoCivil = {"", "SOLTERO(A)", "CASADO(A)", "DIVORCIADO(A)", "VIUDO(A)", "UNION LIBRE"};

        DocumentFilter filtroMayusculas = new ForzarMayusculas();

        for (String etiqueta : etiquetas) {
            JLabel label = new JLabel(etiqueta + ":");

            switch (etiqueta) {
                case "Fecha de nacimiento":
                    panelCampos.add(label);
                    crearDatePicker();
                    panelCampos.add(datePicker);
                    break;
                case "Nacionalidad":
                    panelCampos.add(label);
                    crearComboNacionalidad();
                    panelCampos.add(comboNacionalidad);
                    break;
                case "Entidad Federativa":
                    panelCampos.add(label);
                    crearComboEntidad();
                    panelCampos.add(comboEntidad);
                    break;
                case "Género":
                    panelCampos.add(label);
                    JComboBox<String> comboGen = new JComboBox<>(opcionesGenero);
                    campos.put(etiqueta, comboGen);
                    panelCampos.add(comboGen);
                    break;
                case "Estado civil":
                    panelCampos.add(label);
                    JComboBox<String> comboEC = new JComboBox<>(opcionesEstadoCivil);
                    campos.put(etiqueta, comboEC);
                    panelCampos.add(comboEC);
                    break;
                case "Teléfono":
                    panelCampos.add(label);
                    panelCampos.add(crearPanelTelefono());
                    break;
                case "Tarjeta bancaria":
                    panelCampos.add(label);
                    panelCampos.add(crearPanelTarjeta());
                    break;
                case "RFC":
                    panelCampos.add(label);
                    campoRFC = new JTextField();
                    ((AbstractDocument) campoRFC.getDocument()).setDocumentFilter(filtroMayusculas);
                    panelCampos.add(crearPanelConBoton(campoRFC, "Generar", e -> generarRFC()));
                    campos.put(etiqueta, campoRFC);
                    break;
                case "CURP":
                    panelCampos.add(label);
                    campoCURP = new JTextField();
                    ((AbstractDocument) campoCURP.getDocument()).setDocumentFilter(filtroMayusculas);
                    panelCampos.add(crearPanelConBoton(campoCURP, "Generar", e -> generarCURP()));
                    campos.put(etiqueta, campoCURP);
                    break;
                case "Contraseña":
                    panelCampos.add(crearPanelContrasenaEtiqueta());
                    panelCampos.add(crearPanelContrasena());
                    break;
                default:
                    panelCampos.add(label);
                    JTextField textField = new JTextField();
                    if (etiqueta.equals("Nombre(s)") || etiqueta.equals("Apellido Paterno") ||
                        etiqueta.equals("Apellido Materno") || etiqueta.equals("Calle") || etiqueta.equals("Colonia")) {
                        ((AbstractDocument) textField.getDocument()).setDocumentFilter(filtroMayusculas);
                    }
                    addPlaceholderLogic(textField, placeholders.getOrDefault(etiqueta, ""));
                    campos.put(etiqueta, textField);
                    panelCampos.add(textField);
                    break;
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

    private void crearDatePicker() {
        UtilDateModel model = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Hoy");
        p.put("text.month", "Mes");
        p.put("text.year", "Año");
        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
        campos.put("Fecha de nacimiento", datePicker);
    }

    private void crearComboNacionalidad() {
        Vector<String> paises = getListaPaises();
        comboNacionalidad = new JComboBox<>(paises);
        comboNacionalidad.addActionListener(e -> {
            String seleccion = (String) comboNacionalidad.getSelectedItem();
            if (seleccion != null && seleccion.startsWith("MX")) {
                comboEntidad.setEnabled(true);
            } else {
                comboEntidad.setEnabled(false);
                comboEntidad.setSelectedIndex(0);
            }
        });
        campos.put("Nacionalidad", comboNacionalidad);
    }

    private void crearComboEntidad() {
        Vector<String> entidades = getListaEntidades();
        comboEntidad = new JComboBox<>(entidades);
        comboEntidad.setEnabled(false);
        campos.put("Entidad Federativa", comboEntidad);
    }

    private JPanel crearPanelTelefono() {
        JPanel panel = new JPanel(new GridLayout(1, 3, 5, 0));
        telCampo1 = new JTextField();
        telCampo2 = new JTextField();
        telCampo3 = new JTextField();
        
        addPlaceholderLogic(telCampo1, "XXX");
        addPlaceholderLogic(telCampo2, "XXX");
        addPlaceholderLogic(telCampo3, "XXXX");
        
        addAutoTabLogic(telCampo1, telCampo2, 3);
        addAutoTabLogic(telCampo2, telCampo3, 3);

        panel.add(telCampo1);
        panel.add(telCampo2);
        panel.add(telCampo3);
        
        campos.put("Teléfono1", telCampo1);
        campos.put("Teléfono2", telCampo2);
        campos.put("Teléfono3", telCampo3);
        return panel;
    }

    private JPanel crearPanelTarjeta() {
        JPanel panel = new JPanel(new GridLayout(1, 4, 5, 0));
        cardCampo1 = new JTextField();
        cardCampo2 = new JTextField();
        cardCampo3 = new JTextField();
        cardCampo4 = new JTextField();
        
        addPlaceholderLogic(cardCampo1, "XXXX");
        addPlaceholderLogic(cardCampo2, "XXXX");
        addPlaceholderLogic(cardCampo3, "XXXX");
        addPlaceholderLogic(cardCampo4, "XXXX");

        addAutoTabLogic(cardCampo1, cardCampo2, 4);
        addAutoTabLogic(cardCampo2, cardCampo3, 4);
        addAutoTabLogic(cardCampo3, cardCampo4, 4);

        panel.add(cardCampo1);
        panel.add(cardCampo2);
        panel.add(cardCampo3);
        panel.add(cardCampo4);
        
        campos.put("Tarjeta1", cardCampo1);
        campos.put("Tarjeta2", cardCampo2);
        campos.put("Tarjeta3", cardCampo3);
        campos.put("Tarjeta4", cardCampo4);
        return panel;
    }

    private JPanel crearPanelContrasena() {
        JPanel panelCampo = new JPanel(new BorderLayout(5, 0));
        campoContrasena = new JPasswordField();
        addPlaceholderLogic(campoContrasena, placeholders.get("Contraseña"));
        panelCampo.add(campoContrasena, BorderLayout.CENTER);

        JCheckBox checkMostrar = new JCheckBox("Mostrar");
        checkMostrar.addActionListener(e -> toggleVisibilidadContrasena(checkMostrar.isSelected()));
        panelCampo.add(checkMostrar, BorderLayout.EAST);
        
        campos.put("Contraseña", campoContrasena);
        
        return panelCampo;
    }
    
    private Component crearPanelContrasenaEtiqueta(){
        JPanel panelEtiqueta = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelEtiqueta.add(new JLabel("Contraseña:"));
        JButton infoButton = new JButton("?");
        infoButton.setMargin(new Insets(0, 2, 0, 2));
        infoButton.addActionListener(e -> mostrarRequisitosContrasena());
        panelEtiqueta.add(infoButton);
        return panelEtiqueta;
    }

    private JPanel crearPanelConBoton(JTextField campo, String textoBoton, ActionListener accion) {
        JPanel panel = new JPanel(new BorderLayout(5, 0));
        panel.add(campo, BorderLayout.CENTER);
        JButton boton = new JButton(textoBoton);
        boton.addActionListener(accion);
        panel.add(boton, BorderLayout.EAST);
        return panel;
    }

    private void aplicarFiltroMayusculas(JTextField campo) {
        ((AbstractDocument) campo.getDocument()).setDocumentFilter(new ForzarMayusculas());
    }

    private Vector<String> getListaPaises() {
        Vector<String> paises = new Vector<>();
        paises.add("");
        paises.add("MX : MÉXICO");
        paises.add("DE : ALEMANIA");
        paises.add("AD : ANDORRA");
        paises.add("AR : ARGENTINA");
        paises.add("AU : AUSTRALIA");
        paises.add("AT : AUSTRIA");
        paises.add("BE : BÉLGICA");
        paises.add("BO : BOLIVIA");
        paises.add("BR : BRASIL");
        paises.add("CA : CANADÁ");
        paises.add("CL : CHILE");
        paises.add("CN : CHINA");
        paises.add("CO : COLOMBIA");
        paises.add("KR : COREA DEL SUR");
        paises.add("CR : COSTA RICA");
        paises.add("CU : CUBA");
        paises.add("DK : DINAMARCA");
        paises.add("EC : ECUADOR");
        paises.add("EG : EGIPTO");
        paises.add("SV : SALVADOR, EL");
        paises.add("ES : ESPAÑA");
        paises.add("US : ESTADOS UNIDOS DE AMÉRICA");
        paises.add("PH : FILIPINAS");
        paises.add("FI : FINLANDIA");
        paises.add("FR : FRANCIA");
        paises.add("GR : GRECIA");
        paises.add("GT : GUATEMALA");
        paises.add("HN : HONDURAS");
        paises.add("IN : INDIA");
        paises.add("ID : INDONESIA");
        paises.add("IE : IRLANDA");
        paises.add("IL : ISRAEL");
        paises.add("IT : ITALIA");
        paises.add("JM : JAMAICA");
        paises.add("JP : JAPON");
        paises.add("MA : MARRUECOS");
        paises.add("NI : NICARAGUA");
        paises.add("NO : NORUEGA");
        paises.add("NZ : NUEVA ZELANDA");
        paises.add("PA : PANAMA");
        paises.add("PY : PARAGUAY");
        paises.add("PE : PERÚ");
        paises.add("PL : POLONIA");
        paises.add("PT : PORTUGAL");
        paises.add("PR : PUERTO RICO");
        paises.add("GB : REINO UNIDO");
        paises.add("CZ : CHECA, REPÚBLICA");
        paises.add("DO : DOMICANA, REPÚBLICA");
        paises.add("RU : RUSIA");
        paises.add("ZA : SUDÁFRICA");
        paises.add("SE : SUECIA");
        paises.add("CH : SUIZA");
        paises.add("TH : TAILANDIA");
        paises.add("TR : TURQUÍA");
        paises.add("UA : UCRANIA");
        paises.add("UY : URUGUAY");
        paises.add("VE : VENEZUELA");
        return paises;
    }

    private Vector<String> getListaEntidades() {
        Vector<String> entidades = new Vector<>();
        entidades.add("");
        entidades.add("AGU : Aguascalientes");
        entidades.add("BCN : Baja California");
        entidades.add("BCS : Baja California Sur");
        entidades.add("CAM : Campeche");
        entidades.add("CHP : Chiapas");
        entidades.add("CHH : Chihuahua");
        entidades.add("COA : Coahuila");
        entidades.add("COL : Colima");
        entidades.add("CMX : Ciudad de México");
        entidades.add("DUR : Durango");
        entidades.add("GUA : Guanajuato");
        entidades.add("GRO : Guerrero");
        entidades.add("HID : Hidalgo");
        entidades.add("JAL : Jalisco");
        entidades.add("MEX : México");
        entidades.add("MIC : Michoacán");
        entidades.add("MOR : Morelos");
        entidades.add("NAY : Nayarit");
        entidades.add("NLE : Nuevo León");
        entidades.add("OAX : Oaxaca");
        entidades.add("PUE : Puebla");
        entidades.add("QUE : Querétaro");
        entidades.add("ROO : Quintana Roo");
        entidades.add("SLP : San Luis Potosí");
        entidades.add("SIN : Sinaloa");
        entidades.add("SON : Sonora");
        entidades.add("TAB : Tabasco");
        entidades.add("TAM : Tamaulipas");
        entidades.add("TLA : Tlaxcala");
        entidades.add("VER : Veracruz");
        entidades.add("YUC : Yucatán");
        entidades.add("ZAC : Zacatecas");
        return entidades;
    }

    private void addAutoTabLogic(JTextField source, JComponent target, int lengthLimit) {
        source.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) { checkLength(); }
            public void removeUpdate(DocumentEvent e) { }
            public void insertUpdate(DocumentEvent e) { checkLength(); }
            
            public void checkLength() {
                if (source.getText().length() == lengthLimit) {
                    SwingUtilities.invokeLater(() -> target.requestFocusInWindow());
                }
            }
        });
    }

    private void generarRFC() {
        try {
            String nombre = ((JTextField) campos.get("Nombre(s)")).getText();
            String apPaterno = ((JTextField) campos.get("Apellido Paterno")).getText();
            String apMaterno = ((JTextField) campos.get("Apellido Materno")).getText();
            Date fechaNac = (Date) datePicker.getModel().getValue();

            if (nombre.isEmpty() || apPaterno.isEmpty() || fechaNac == null) {
                JOptionPane.showMessageDialog(this, "Debe llenar Nombre, Apellido Paterno y Fecha de Nacimiento.", "Error de generación", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String rfc = GeneradorCurp.generarParteRFC(nombre, apPaterno, apMaterno, fechaNac);
            campoRFC.setText(rfc);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al generar RFC. Verifique los datos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void generarCURP() {
        try {
            String nombre = ((JTextField) campos.get("Nombre(s)")).getText();
            String apPaterno = ((JTextField) campos.get("Apellido Paterno")).getText();
            String apMaterno = ((JTextField) campos.get("Apellido Materno")).getText();
            Date fechaNac = (Date) datePicker.getModel().getValue();
            String genero = (String) ((JComboBox<?>) campos.get("Género")).getSelectedItem();
            String entidad = (String) ((JComboBox<?>) campos.get("Entidad Federativa")).getSelectedItem();

            if (nombre.isEmpty() || apPaterno.isEmpty() || fechaNac == null || genero.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe llenar Nombre, Apellidos, Fecha y Género.", "Error de generación", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (comboNacionalidad.getSelectedItem().toString().startsWith("MX") && entidad.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Si la nacionalidad es Mexicana, debe seleccionar una Entidad Federativa.", "Error de generación", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String curp = GeneradorCurp.generarParteCURP(nombre, apPaterno, apMaterno, fechaNac, genero, entidad);
            campoCURP.setText(curp);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al generar CURP. Verifique los datos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addPlaceholderLogic(JTextField textField, String placeholder) {
        if (placeholder == null || placeholder.isEmpty()) return;

        textField.setText(placeholder);
        textField.setForeground(Color.GRAY);

        if (textField instanceof JPasswordField) {
            ((JPasswordField) textField).setEchoChar((char) 0);
        }

        textField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textField.getForeground() == Color.GRAY) {
                    textField.setText("");
                    textField.setForeground(Color.BLACK);
                    if (textField instanceof JPasswordField) {
                        ((JPasswordField) textField).setEchoChar('•');
                    }
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
                    if (textField instanceof JPasswordField) {
                        ((JPasswordField) textField).setEchoChar((char) 0);
                    }
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
        } else {
            if (mostrar) {
                campoContrasena.setEchoChar((char) 0);
            } else {
            }
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

    public static class DateLabelFormatter extends AbstractFormatter {
        private String datePattern = "dd/MM/yyyy";
        private SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);

        @Override
        public Object stringToValue(String text) throws ParseException {
            return dateFormatter.parseObject(text);
        }
        @Override
        public String valueToString(Object value) throws ParseException {
            if (value != null) {
                Calendar cal = (Calendar) value;
                return dateFormatter.format(cal.getTime());
            }
            return "";
        }
    }
}