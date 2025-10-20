import javax.swing.*;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class ValidadorFormulario {

    private static final Map<String, String> REGEX_MAP = new LinkedHashMap<>();

    static {
        REGEX_MAP.put("Nombre(s)", "^[A-ZÁÉÍÓÚÑ ]{1,20}$");
        REGEX_MAP.put("Apellido Paterno", "^[A-ZÁÉÍÓÚÑ ]{1,15}$");
        REGEX_MAP.put("Apellido Materno", "^[A-ZÁÉÍÓÚÑ ]{1,15}$");
        REGEX_MAP.put("Fecha de nacimiento", "^(?:0?[1-9]|[12]\\d|3[01])([-/])(?:0?[1-9]|1[0-2])\\1(?:(?:\\d{2})?\\d{2})$");
        REGEX_MAP.put("Nacionalidad", "^[A-Z]{2}$");
        REGEX_MAP.put("RFC", "^[A-Z0-9]{13}$"); 
        REGEX_MAP.put("CURP", "^[A-Z0-9]{18}$"); 
        REGEX_MAP.put("Calle", "^[A-Z0-9 ]+$");
        REGEX_MAP.put("Número Ext.", "^\\d+$");
        REGEX_MAP.put("Colonia", "^[A-Z0-9 ]+$");
        REGEX_MAP.put("Ciudad", "^[A-Z ]+$");
        REGEX_MAP.put("Estado", "^[A-Z ]+$");
        REGEX_MAP.put("Código Postal", "^\\d{5}$");
        REGEX_MAP.put("Teléfono", "^(\\d{10}|\\d{2}\\s\\d{4}\\s\\d{4}|\\d{3}\\s\\d{3}\\s\\d{4})$");
        REGEX_MAP.put("Correo electrónico", "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.(com|org|mx|edu|gob)$");
        REGEX_MAP.put("Contraseña", "^(?=.*[a-z])(?=.*[A-Z])(?=(?:.*\\d){2})(?=.*[\\W_]).{8,}$");        
        REGEX_MAP.put("Tarjeta bancaria", "^(\\d{16}|\\d{4}\\s\\d{4}\\s\\d{4}\\s\\d{4})$");
    }

    public static Map<String, String> validar(Map<String, JComponent> campos, Map<String, String> placeholders) {
        Map<String, String> errores = new LinkedHashMap<>();

        String[] camposIgnoreCase = {"Nombre(s)", "Apellido Paterno", "Apellido Materno", "Calle", "Colonia", "Ciudad", "Estado", "Correo electrónico", "RFC", "CURP", "Nacionalidad"};

        for (Map.Entry<String, JComponent> campoEntry : campos.entrySet()) {
            String nombreCampo = campoEntry.getKey();
            JComponent componente = campoEntry.getValue();

            if (componente instanceof JTextField) {
                JTextField textField = (JTextField) componente;
                String valor = textField.getText();
                String placeholder = placeholders.getOrDefault(nombreCampo, "");

                if (valor.equals(placeholder)) {
                    errores.put(nombreCampo, "El campo '" + nombreCampo + "' no puede estar vacío.");
                    continue; 
                }

                String regex = REGEX_MAP.get(nombreCampo);
                if (regex != null) {
                    boolean esValido;
                    if (java.util.Arrays.asList(camposIgnoreCase).contains(nombreCampo)) {
                        esValido = Pattern.compile(regex, Pattern.CASE_INSENSITIVE).matcher(valor).matches();
                    } else {
                        esValido = Pattern.matches(regex, valor);
                    }
                    if (!esValido) {
                        errores.put(nombreCampo, "El formato de '" + nombreCampo + "' es incorrecto.");
                    }
                }
            } else if (componente instanceof JComboBox) {
                JComboBox<?> comboBox = (JComboBox<?>) componente;
                if (comboBox.getSelectedIndex() == 0) {
                    errores.put(nombreCampo, "Debes seleccionar una opción para '" + nombreCampo + "'.");
                }
            }
        }
        return errores;
    } 

}