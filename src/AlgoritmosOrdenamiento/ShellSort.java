package AlgoritmosOrdenamiento;

public class ShellSort {

    public static void sort(int[] arr) {

        // Comienza con un salto igual a la mitad del tamaño del arreglo
        for (int salto = arr.length / 2; salto > 0; salto /= 2) {

            // Recorre el arreglo utilizando el salto actual
            for (int i = salto; i < arr.length; i++) {

                // Guarda el elemento que se va a insertar
                int valorActual = arr[i];

                // Comienza comparando con el elemento anterior según el salto
                int j = i;

                // Desplaza los elementos mayores hacia la derecha según el salto
                while (j >= salto && arr[j - salto] > valorActual) {
                    arr[j] = arr[j - salto];
                    j -= salto;
                }

                // Inserta el elemento en su posición correcta
                arr[j] = valorActual;
            }
        }
    }
}