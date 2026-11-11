package AlgoritmosOrdenamiento;

public class SelectionSort {

    public static void sort(int[] arr) {

        // Recorre cada posición del arreglo, excepto la última
        for (int i = 0; i < arr.length - 1; i++) {

            // Supone que el elemento actual es el mínimo
            int indiceMinimo = i;

            // Busca el elemento mínimo en el subarreglo restante
            for (int j = i + 1; j < arr.length; j++) {

                if (arr[j] < arr[indiceMinimo]) {
                    indiceMinimo = j;
                }
            }

            // Intercambia el mínimo encontrado con la posición actual
            int temporal = arr[i];
            arr[i] = arr[indiceMinimo];
            arr[indiceMinimo] = temporal;
        }
    }
}