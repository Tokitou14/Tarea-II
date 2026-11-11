package AlgoritmosOrdenamiento;

import java.io.File;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Locale;
import java.util.function.Consumer;

/** Solo nanoTime alrededor de sort(); generacion, copia y verificacion quedan fuera. */
public class Benchmark {
    private static final int REPETICIONES = 5;
    private static final int BASE = 1000;
    private static final int MAX_CUADRATICOS = 16000;
    private static final int MAX_GRANDES = 1024000;

    private static void registro(PrintWriter csv, String experimento, String nombre,
            String tipo, int n, int digitos, int repeticion, Long ns, String estado) {
        csv.printf(Locale.ROOT, "%s,%s,%s,%d,%d,%d,%s,%s%n", experimento, nombre, tipo,
                n, digitos, repeticion, ns == null ? "" : ns.toString(), estado);
    }

    private static boolean medirCaso(PrintWriter csv, String experimento, String nombre,
            Consumer<int[]> ordenamiento, String tipo, int[] entrada, int digitos) {
        int[] referencia = entrada.clone();
        Arrays.sort(referencia); // Solo para validar, FUERA de tiempo medido.
        // Primera ejecucion para calentamiento del JIT: se descarta.
        for (int rep = 0; rep <= REPETICIONES; rep++) {
            int[] copia = entrada.clone(); // FUERA de tiempo medido.
            long inicio = System.nanoTime();
            try {
                ordenamiento.accept(copia);
            } catch (StackOverflowError error) {
                registro(csv, experimento, nombre, tipo, entrada.length, digitos, rep,
                        null, "STACK_OVERFLOW");
                csv.flush();
                System.out.printf("STACK_OVERFLOW: %-10s %-11s n=%d; se detiene esta serie.%n",
                        nombre, tipo, entrada.length);
                return false;
            }
            long transcurrido = System.nanoTime() - inicio;
            if (!Arrays.equals(copia, referencia)) {
                registro(csv, experimento, nombre, tipo, entrada.length, digitos, rep,
                        null, "ERROR_ORDENAMIENTO");
                throw new AssertionError("Resultado invalido: " + nombre + " " + tipo);
            }
            if (rep > 0) registro(csv, experimento, nombre, tipo, entrada.length,
                    digitos, rep, transcurrido, "OK");
        }
        csv.flush();
        System.out.printf("OK: %-10s %-11s n=%d (%d repeticiones)%n", nombre, tipo,
                entrada.length, REPETICIONES);
        return true;
    }

    public static void ejecutar(String ruta) throws Exception {
        File destino = new File(ruta);
        if (destino.getParentFile() != null) destino.getParentFile().mkdirs();
        try (PrintWriter csv = new PrintWriter(destino, StandardCharsets.UTF_8.name())) {
            csv.println("experimento,algoritmo,tipo_entrada,n,digitos,repeticion,tiempo_ns,estado");
            for (int a = 0; a < Verificacion.NOMBRES.length; a++) {
                String nombre = Verificacion.NOMBRES[a];
                Consumer<int[]> sort = Verificacion.ALGORITMOS[a];
                int limite = a <= 2 ? MAX_CUADRATICOS : MAX_GRANDES;
                for (int tipo = 0; tipo < GeneradorEntradas.TIPOS.length; tipo++) {
                    for (int n = BASE; n <= limite; n *= 2) {
                        // Al generar por semilla/tipo/n, todos los algoritmos reciben lo mismo.
                        int[] entrada = GeneradorEntradas.generar(tipo, n);
                        if (!medirCaso(csv, "principal", nombre, sort,
                                GeneradorEntradas.TIPOS[tipo], entrada, 0)) break;
                    }
                }
            }
            // Prueba especial de Radix: n fijo y variacion de d (3, 6, 9).
            int n = 100_000;
            for (int digitos : new int[]{3, 6, 9}) {
                int maximo = (int) Math.pow(10, digitos) - 1;
                int[] entrada = GeneradorEntradas.generarConMaximo(0, n, maximo);
                medirCaso(csv, "radix_digitos", "Radix", RadixSort::sort,
                        "aleatorio", entrada, digitos);
            }
        }
        System.out.println("Archivo CSV escrito en: " + destino.getAbsolutePath());
    }
}
