package AlgoritmosOrdenamiento;

/** Quicksort clasico con pivote fijo: ultimo elemento (Lomuto). */
public class QuickSort {
    public static void sort(int[] arr) {
        quickSort(arr, 0, arr.length - 1);
    }

    private static void quickSort(int[] arr, int inicio, int fin) {
        if (inicio >= fin) return;
        // El pivote se elige SIEMPRE al final; no se aleatoriza ni se busca la mediana.
        int posicionPivote = particionar(arr, inicio, fin);
        // La recursividad desbalanceada puede generar StackOverflowError.
        quickSort(arr, inicio, posicionPivote - 1);
        quickSort(arr, posicionPivote + 1, fin);
    }

    private static int particionar(int[] arr, int inicio, int fin) {
        int pivote = arr[fin];
        int fronteraMenores = inicio - 1;
        // Todos los valores <= pivote se colocan a la izquierda.
        for (int j = inicio; j < fin; j++) {
            if (arr[j] <= pivote) {
                fronteraMenores++;
                intercambiar(arr, fronteraMenores, j);
            }
        }
        intercambiar(arr, fronteraMenores + 1, fin);
        return fronteraMenores + 1;
    }

    private static void intercambiar(int[] arr, int a, int b) {
        int temporal = arr[a];
        arr[a] = arr[b];
        arr[b] = temporal;
    }
}
