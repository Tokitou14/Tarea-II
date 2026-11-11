package AlgoritmosOrdenamiento;

/** Radix LSD decimal: counting sort estable en cada digito. Solo no negativos. */
public class RadixSort {
    public static void sort(int[] arr) {
        if (arr.length < 2) return;

        int maximo = 0;
        for (int valor : arr) {
            if (valor < 0) throw new IllegalArgumentException("Radix solo acepta enteros >= 0");
            if (valor > maximo) maximo = valor;
        }

        int[] auxiliar = new int[arr.length];
        // La base 10 es fija; una pasada por cada digito decimal del maximo.
        for (long divisor = 1; maximo / divisor > 0; divisor *= 10) {
            int[] conteos = new int[10];
            for (int valor : arr) conteos[(int) ((valor / divisor) % 10)]++;
            // Conteos acumulados para hallar el destino de cada valor.
            for (int d = 1; d < 10; d++) conteos[d] += conteos[d - 1];
            // Retroceso para mantener estable el orden de valores con igual digito.
            for (int i = arr.length - 1; i >= 0; i--) {
                int digito = (int) ((arr[i] / divisor) % 10);
                auxiliar[--conteos[digito]] = arr[i];
            }
            System.arraycopy(auxiliar, 0, arr, 0, arr.length);
        }
    }
}
