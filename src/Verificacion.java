
package AlgoritmosOrdenamiento;

import java.util.Arrays;
import java.util.Random;
import java.util.function.Consumer;

public class Verificacion {

    private static final int VALOR_MAXIMO = 999_999;

    private static final int[] TAMANOS = {
            0, 1, 2, 10, 1000, 5000
    };

    private static final String[] TIPOS = {
            "aleatorio", "ascendente", "descendente", "duplicados"
    };

    private static int[] generarAleatorio(int n, Random azar) {
        int[] arreglo = new int[n];

        for (int i = 0; i < n; i++) {
            arreglo[i] = azar.nextInt(VALOR_MAXIMO + 1);
        }

        return arreglo;
    }

    private static int[] generarAscendente(int n, Random azar) {
        int[] arreglo = generarAleatorio(n, azar);

        Arrays.sort(arreglo);

        return arreglo;
    }

    private static int[] generarDescendente(int n, Random azar) {
        int[] arreglo = generarAscendente(n, azar);

        for (int i = 0; i < n / 2; i++) {
            int temporal = arreglo[i];
            arreglo[i] = arreglo[n - 1 - i];
            arreglo[n - 1 - i] = temporal;
        }

        return arreglo;
    }

    private static int[] generarDuplicados(int n, Random azar) {
        int[] arreglo = new int[n];

        for (int i = 0; i < n; i++) {
            arreglo[i] = azar.nextInt(11);
        }

        return arreglo;
    }

    private static int[] generarEntrada(int tipo, int n, Random azar) {
        switch (tipo) {
            case 0:
                return generarAleatorio(n, azar);
            case 1:
                return generarAscendente(n, azar);
            case 2:
                return generarDescendente(n, azar);
            default:
                return generarDuplicados(n, azar);
        }
    }

    private static boolean verificar(
            Consumer<int[]> algoritmo,
            int[] entrada
    ) {
        int[] copiaAlgoritmo = entrada.clone();
        int[] copiaReferencia = entrada.clone();

        algoritmo.accept(copiaAlgoritmo);

        Arrays.sort(copiaReferencia);

        return Arrays.equals(copiaAlgoritmo, copiaReferencia);
    }

    private static int probar(
            String nombre,
            Consumer<int[]> algoritmo
    ) {
        Random azar = new Random(42);

        int superadas = 0;

        System.out.println("=== " + nombre + " ===");

        for (int n : TAMANOS) {

            StringBuilder linea = new StringBuilder(
                    String.format("n=%-5d", n)
            );

            for (int tipo = 0; tipo < TIPOS.length; tipo++) {

                int[] entrada = generarEntrada(tipo, n, azar);

                boolean correcto = verificar(algoritmo, entrada);

                if (correcto) {
                    superadas++;
                }

                linea.append(" | ")
                        .append(TIPOS[tipo])
                        .append(": ")
                        .append(correcto ? "OK" : "FALLO");
            }

            System.out.println(linea);
        }

        int total = TAMANOS.length * TIPOS.length;

        System.out.println(
                "Resultado: " + superadas + " de " + total
                        + " pruebas superadas\n"
        );

        return superadas;
    }

    public static void ejecutar() {

        String[] nombres = {
                "Selection", "Bubble", "Insertion", "Shell", "Merge"
        };

        int[] superadas = new int[nombres.length];

        System.out.println("VERIFICACION CONTRA Arrays.sort\n");

        superadas[0] = probar("Selection", SelectionSort::sort);
        superadas[1] = probar("Bubble", BubbleSort::sort);
        superadas[2] = probar("Insertion", InsertionSort::sort);
        superadas[3] = probar("Shell", ShellSort::sort);
        superadas[4] = probar("Merge", MergeSort::sort);

        int totalPorAlgoritmo = TAMANOS.length * TIPOS.length;

        int sumaSuperadas = 0;

        System.out.println("=== RESUMEN ===");

        for (int i = 0; i < nombres.length; i++) {

            sumaSuperadas += superadas[i];

            System.out.println(
                    String.format(
                            "%-10s %2d / %d  %s",
                            nombres[i],
                            superadas[i],
                            totalPorAlgoritmo,
                            superadas[i] == totalPorAlgoritmo
                                    ? "TODAS LAS PRUEBAS PASARON"
                                    : "HAY FALLOS"
                    )
            );
        }

        System.out.println(
                "\nTotal general: " + sumaSuperadas + " de "
                        + (totalPorAlgoritmo * nombres.length)
                        + " pruebas superadas."
        );
    }
}
