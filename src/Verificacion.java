package AlgoritmosOrdenamiento;

import java.util.Arrays;
import java.util.function.Consumer;

public class Verificacion {
    private static final int[] TAMANOS = {0, 1, 2, 10, 1000, 5000};
    public static final String[] NOMBRES = {
        "Selection", "Bubble", "Insertion", "Shell", "Merge", "Quick", "Radix"
    };
    @SuppressWarnings("unchecked")
    public static final Consumer<int[]>[] ALGORITMOS = new Consumer[] {
        (Consumer<int[]>) SelectionSort::sort, (Consumer<int[]>) BubbleSort::sort,
        (Consumer<int[]>) InsertionSort::sort, (Consumer<int[]>) ShellSort::sort,
        (Consumer<int[]>) MergeSort::sort, (Consumer<int[]>) QuickSort::sort,
        (Consumer<int[]>) RadixSort::sort
    };
    public static void ejecutar() {
        int total = 0, superadas = 0;
        System.out.println("VERIFICACION CONTRA Arrays.sort");
        for (int a = 0; a < NOMBRES.length; a++) {
            int ok = 0;
            for (int n : TAMANOS) {
                for (int tipo = 0; tipo < GeneradorEntradas.TIPOS.length; tipo++) {
                    int[] entrada = GeneradorEntradas.generar(tipo, n);
                    int[] esperado = entrada.clone();
                    int[] obtenido = entrada.clone();
                    Arrays.sort(esperado);
                    ALGORITMOS[a].accept(obtenido);
                    if (Arrays.equals(esperado, obtenido)) ok++;
                    else System.err.printf("FALLO: %s n=%d tipo=%s%n", NOMBRES[a], n, GeneradorEntradas.TIPOS[tipo]);
                    total++;
                }
            }
            superadas += ok;
            System.out.printf("%-10s %2d/%d pruebas%n", NOMBRES[a], ok,
                    TAMANOS.length * GeneradorEntradas.TIPOS.length);
        }
        // Incluye casos de 9 digitos de la prueba especial de Radix.
        int[] especiales = {0, 999_999_999, 11, 9_999_999, 123_456_789, 1_000_000};
        int[] esperado = especiales.clone(), obtenido = especiales.clone();
        Arrays.sort(esperado);
        RadixSort.sort(obtenido);
        boolean ok = Arrays.equals(esperado, obtenido);
        superadas += ok ? 1 : 0;
        total++;
        System.out.printf("Radix 9 digitos: %s%n", ok ? "OK" : "FALLO");
        System.out.printf("TOTAL: %d/%d pruebas superadas%n", superadas, total);
        if (superadas != total) throw new AssertionError("Se encontraron errores en el ordenamiento");
    }
}
