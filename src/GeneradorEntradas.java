package AlgoritmosOrdenamiento;

import java.util.Arrays;
import java.util.Random;

/** Genera reproduciblemente EXACTAMENTE la misma entrada por tipo y n. */
public class GeneradorEntradas {
    public static final String[] TIPOS = {"aleatorio", "ascendente", "descendente", "duplicados"};
    public static int[] generar(int tipo, int n) {
        return generarConMaximo(tipo, n, 999_999);
    }
    public static int[] generarConMaximo(int tipo, int n, int maximo) {
        Random azar = new Random(42L + 31L * n + 131L * tipo + 17L * maximo);
        int[] arreglo = new int[n];
        int limite = tipo == 3 ? 11 : maximo + 1;
        for (int i = 0; i < n; i++) arreglo[i] = azar.nextInt(limite);
        if (tipo == 1 || tipo == 2) {
            // Solo crea el arreglo de entrada; no se incluye en los tiempos.
            Arrays.sort(arreglo);
            if (tipo == 2) {
                for (int i = 0; i < n / 2; i++) {
                    int temporal = arreglo[i];
                    arreglo[i] = arreglo[n - i - 1];
                    arreglo[n - i - 1] = temporal;
                }
            }
        }
        return arreglo;
    }
}
