package AlgoritmosOrdenamiento;

public class InsertionSort {

    public static void sort(int[] arr) {

        // Recorre el arreglo desde el segundo elemento
        for (int i = 1; i < arr.length; i++) {

            // Guarda el elemento que se va a insertar
            int valorActual = arr[i];

            // Comienza comparando con el elemento anterior
            int j = i - 1;

            // Desplaza hacia la derecha los elementos mayores
            while (j >= 0 && arr[j] > valorActual) {
                arr[j + 1] = arr[j];
                j--;
            }

            // Inserta el elemento en su posición correcta
            arr[j + 1] = valorActual;
        }
    }
}