import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GeneradorCurp {

    private static final Map<String, String> ENTIDAD_MAP = new HashMap<>();
    
    static {
        ENTIDAD_MAP.put("AGU : Aguascalientes", "AS");
        ENTIDAD_MAP.put("BCN : Baja California", "BC");
        ENTIDAD_MAP.put("BCS : Baja California Sur", "BS");
        ENTIDAD_MAP.put("CAM : Campeche", "CC");
        ENTIDAD_MAP.put("CHP : Chiapas", "CS");
        ENTIDAD_MAP.put("CHH : Chihuahua", "CH");
        ENTIDAD_MAP.put("COA : Coahuila", "CL");
        ENTIDAD_MAP.put("COL : Colima", "CM");
        ENTIDAD_MAP.put("CMX : Ciudad de México", "DF");
        ENTIDAD_MAP.put("DUR : Durango", "DG");
        ENTIDAD_MAP.put("GUA : Guanajuato", "GT");
        ENTIDAD_MAP.put("GRO : Guerrero", "GR");
        ENTIDAD_MAP.put("HID : Hidalgo", "HG");
        ENTIDAD_MAP.put("JAL : Jalisco", "JC");
        ENTIDAD_MAP.put("MEX : México", "MC");
        ENTIDAD_MAP.put("MIC : Michoacán", "MN");
        ENTIDAD_MAP.put("MOR : Morelos", "MS");
        ENTIDAD_MAP.put("NAY : Nayarit", "NT");
        ENTIDAD_MAP.put("NLE : Nuevo León", "NL");
        ENTIDAD_MAP.put("OAX : Oaxaca", "OC");
        ENTIDAD_MAP.put("PUE : Puebla", "PL");
        ENTIDAD_MAP.put("QUE : Querétaro", "QT");
        ENTIDAD_MAP.put("ROO : Quintana Roo", "QR");
        ENTIDAD_MAP.put("SLP : San Luis Potosí", "SP");
        ENTIDAD_MAP.put("SIN : Sinaloa", "SL");
        ENTIDAD_MAP.put("SON : Sonora", "SR");
        ENTIDAD_MAP.put("TAB : Tabasco", "TC");
        ENTIDAD_MAP.put("TAM : Tamaulipas", "TS");
        ENTIDAD_MAP.put("TLA : Tlaxcala", "TL");
        ENTIDAD_MAP.put("VER : Veracruz", "VZ");
        ENTIDAD_MAP.put("YUC : Yucatán", "YN");
        ENTIDAD_MAP.put("ZAC : Zacatecas", "ZS");
    }

    public static String generarParteCURP(String nombre, String apPaterno, String apMaterno, Date fechaNac, String genero, String entidad) {
        String p1 = obtenerVocalInterna(apPaterno);
        String p2 = (apMaterno == null || apMaterno.isEmpty()) ? "X" : apMaterno.substring(0, 1);
        String p3 = obtenerNombreValido(nombre);

        SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
        String fecha = sdf.format(fechaNac);
        
        String gen = genero.startsWith("M") ? "M" : "H";
        String ent = ENTIDAD_MAP.getOrDefault(entidad, "NE");
        
        String c1 = obtenerConsonanteInterna(apPaterno);
        String c2 = (apMaterno == null || apMaterno.isEmpty()) ? "X" : obtenerConsonanteInterna(apMaterno);
        String c3 = obtenerConsonanteInterna(p3);

        return apPaterno.substring(0, 1) + p1 + p2 + p3.substring(0, 1) + fecha + gen + ent + c1 + c2 + c3;
    }

    public static String generarParteRFC(String nombre, String apPaterno, String apMaterno, Date fechaNac) {
        String p1 = obtenerVocalInterna(apPaterno);
        String p2 = (apMaterno == null || apMaterno.isEmpty()) ? "X" : apMaterno.substring(0, 1);
        String p3 = obtenerNombreValido(nombre);

        SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
        String fecha = sdf.format(fechaNac);

        return apPaterno.substring(0, 1) + p1 + p2 + p3.substring(0, 1) + fecha;
    }

    private static String obtenerNombreValido(String nombre) {
        String[] nombres = nombre.split(" ");
        if (nombres.length > 1 && (nombres[0].equals("JOSE") || nombres[0].equals("MARIA"))) {
            return nombres[1];
        }
        return nombres[0];
    }

    private static String obtenerVocalInterna(String texto) {
        Pattern pattern = Pattern.compile("[AEIOU]");
        Matcher matcher = pattern.matcher(texto.substring(1));
        if (matcher.find()) {
            return matcher.group();
        }
        return "X";
    }

    private static String obtenerConsonanteInterna(String texto) {
        Pattern pattern = Pattern.compile("[BCDFGHJKLMNPQRSTVWXYZ]");
        Matcher matcher = pattern.matcher(texto.substring(1));
        if (matcher.find()) {
            return matcher.group();
        }
        return "X";
    }
}