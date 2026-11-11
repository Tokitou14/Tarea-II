package AlgoritmosOrdenamiento;

public class BubbleSort {

    public static void sort(int[] arr) {

        // Recorre el arreglo completo en cada pasada
        for (int i = 0; i < arr.length - 1; i++) {

            // Compara todos los pares de elementos adyacentes
            for (int j = 0; j < arr.length - 1; j++) {

                // Intercambia si los elementos están en orden incorrecto
                if (arr[j] > arr[j + 1]) {
                    int temporal = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temporal;
                }
            }
        }
    }
}