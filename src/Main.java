package AlgoritmosOrdenamiento;

public class Main {
    public static void main(String[] args) throws Exception {
        if (args.length == 0 || args[0].equalsIgnoreCase("verificar")) {
            Verificacion.ejecutar();
        } else if (args[0].equalsIgnoreCase("benchmark")) {
            Benchmark.ejecutar(args.length >= 2 ? args[1] : "resultados/mediciones.csv");
        } else {
            System.out.println("Uso: Main [verificar | benchmark [archivo.csv]]");
        }
    }
}
