package AlgoritmosOrdenamiento;

public class MergeSort {

    public static void sort(int[] arr) {

        // Crea el arreglo auxiliar para realizar las mezclas
        int[] auxiliar = new int[arr.length];

        // Inicia el ordenamiento recursivo
        mergeSort(arr, auxiliar, 0, arr.length - 1);
    }

    private static void mergeSort(int[] arr, int[] auxiliar, int inicio, int fin) {

        // Caso base: un arreglo de un elemento ya está ordenado
        if (inicio >= fin) {
            return;
        }

        // Calcula el punto medio del arreglo
        int medio = inicio + (fin - inicio) / 2;

        // Ordena recursivamente la mitad izquierda
        mergeSort(arr, auxiliar, inicio, medio);

        // Ordena recursivamente la mitad derecha
        mergeSort(arr, auxiliar, medio + 1, fin);

        // Mezcla ambas mitades ordenadas
        merge(arr, auxiliar, inicio, medio, fin);
    }

    private static void merge(int[] arr, int[] auxiliar, int inicio, int medio, int fin) {

        // Índices para recorrer ambas mitades
        int indiceIzquierdo = inicio;
        int indiceDerecho = medio + 1;
        int indiceAuxiliar = inicio;

        // Compara y copia el menor elemento de ambas mitades
        while (indiceIzquierdo <= medio && indiceDerecho <= fin) {

            if (arr[indiceIzquierdo] <= arr[indiceDerecho]) {
                auxiliar[indiceAuxiliar] = arr[indiceIzquierdo];
                indiceIzquierdo++;
            } else {
                auxiliar[indiceAuxiliar] = arr[indiceDerecho];
                indiceDerecho++;
            }

            indiceAuxiliar++;
        }

        // Copia los elementos restantes de la mitad izquierda
        while (indiceIzquierdo <= medio) {
            auxiliar[indiceAuxiliar] = arr[indiceIzquierdo];
            indiceIzquierdo++;
            indiceAuxiliar++;
        }

        // Copia los elementos restantes de la mitad derecha
        while (indiceDerecho <= fin) {
            auxiliar[indiceAuxiliar] = arr[indiceDerecho];
            indiceDerecho++;
            indiceAuxiliar++;
        }

        // Copia los elementos mezclados al arreglo original
        for (int i = inicio; i <= fin; i++) {
            arr[i] = auxiliar[i];
        }
    }
}