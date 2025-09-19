import java.util.Scanner;
import java.util.regex.Pattern;

public class tarea03_validador_placas {

    public static void main(String[] args) {

        String regexAutoParticular = "^U[KLMNP][A-HJ-NPR-Z]-\\d{3}-[A-HJ-NPR-Z]$";
        // ver si puedo agregar API REPUVE
        // Hacer grafico el programa
        String regexMotocicleta = "^U[KLMNP][A-HJ-NPR-Z]\\d{2}$";
        // Rango: UKA01 - UPZ99
        String regexTaxi = "^A-d{4}-TQ$";
        // A-9999-TQ
        String regexPublico = "^S[S-Y]-(?!0000)\\d{4}-[E-HJ-NPR-Z]$";
        // Rango de transporte publico: SS-0001-E - SY-9999-Z
        Scanner scanner = new Scanner(System.in);

        System.out.print("Por favor, introduce la placa a validar: ");

        String placaIngresada = scanner.nextLine().toUpperCase();

        if (Pattern.matches(regexAutoParticular, placaIngresada)) {
            System.out.println("La placa '" + placaIngresada + "' es VÁLIDA y corresponde a un Automóvil Particular.");
        } else if (Pattern.matches(regexMotocicleta, placaIngresada)) {
            System.out.println("La placa '" + placaIngresada + "' es VÁLIDA y corresponde a una Motocicleta.");
        } else if (Pattern.matches(regexTaxi, placaIngresada)) {
            System.out.println("La placa '" + placaIngresada + "' es VÁLIDA y corresponde a un Taxi.");
        }
          else if (Pattern.matches(regexPublico, placaIngresada)) {
            System.out.println("La placa '" + placaIngresada + "' es VÁLIDA y corresponde a un Transporte Publico.");
        } else {
            System.out.println("La placa '" + placaIngresada + "' NO ES VÁLIDA según los formatos conocidos de Querétaro.");
        }
        
        scanner.close();
    }
}