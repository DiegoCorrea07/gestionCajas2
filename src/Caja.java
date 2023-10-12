import java.util.List;
import java.util.Stack;
import javax.swing.JTextArea; // Añade la importación necesaria

public class Caja {
    private String codigo;
    private String empresa;

    public Caja(String codigo, String empresa) {
        this.codigo = codigo;
        this.empresa = empresa;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    @Override
    public String toString() {
        return "Caja --> || Código: " + codigo + " | Empresa: " + empresa + " ||";
    }

    public static void agregarCaja(Stack<Caja> pila, Caja nuevaCaja, JTextArea datosField) { // Agrega el argumento JTextArea
        if (pila.size() < 6) {
            pila.push(nuevaCaja);
        } else {
            datosField.append("No hay espacio en la pila seleccionada. Seleccione otra.\n");
        }
    }

    public static Caja buscarCaja(List<Stack<Caja>> pilas, String codigo) {
        for (Stack<Caja> pila : pilas) {
            for (Caja caja : pila) {
                if (caja.getCodigo().equals(codigo)) {
                    return caja;
                }
            }
        }
        return null;
    }

    public static void sacarCaja(List<Stack<Caja>> pilas, String codigo, JTextArea datosField) {
        boolean cajaEncontrada = false;

        for (Stack<Caja> pila : pilas) {
            Stack<Caja> pilaAuxiliar = new Stack<>();
            while (!pila.isEmpty()) {
                Caja caja = pila.pop();
                if (caja.getCodigo().equals(codigo)) {
                    cajaEncontrada = true;
                } else {
                    pilaAuxiliar.push(caja);
                }
            }
            while (!pilaAuxiliar.isEmpty()) {
                pila.push(pilaAuxiliar.pop());
            }
        }

        if (!cajaEncontrada) {
            datosField.append("Caja no encontrada.\n");
        }
    }

    public static void imprimirPilas(List<Stack<Caja>> pilas, JTextArea datosField) { // Agrega el argumento JTextArea
        for (int i = 0; i < pilas.size(); i++) {
            Stack<Caja> pila = pilas.get(i);
            datosField.append("Pila " + i + ": [\n");

            for (Caja caja : pila) {
                datosField.append("  " + caja.toString() + "\n");
            }

            datosField.append("]\n");
        }
    }
}
