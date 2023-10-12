import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class AlmacenApp extends JFrame {
    private List<Stack<Caja>> pilas;
    private JComboBox<Integer> pilaSelector;
    private JTextField codigoField;
    private JTextField empresaField;
    private JTextArea datosField;
    private JPanel panel;
    private JButton ingresarButton;
    private JButton buscarButton;
    private JButton sacarButton;
    private JButton imprimirButton;
    private JButton salirButton;

    public AlmacenApp() {
        pilas = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            pilas.add(new Stack<>());
        }

        pilaSelector = new JComboBox<>();
        for (int i = 0; i < pilas.size(); i++) {
            pilaSelector.addItem(i);
        }

        codigoField = new JTextField(10);
        empresaField = new JTextField(10);
        datosField = new JTextArea(15, 40);
        datosField.setEditable(false);
        JScrollPane scroll = new JScrollPane(datosField);

        panel = new JPanel();
        panel.setLayout(new FlowLayout());

        ingresarButton = new JButton("Ingresar Caja");
        buscarButton = new JButton("Buscar Caja");
        sacarButton = new JButton("Sacar Caja");
        imprimirButton = new JButton("Imprimir Pilas");
        salirButton = new JButton("Salir");

        panel.add(new JLabel("Seleccionar Pila: "));
        panel.add(pilaSelector);
        panel.add(new JLabel("Código de Empresa: "));
        panel.add(codigoField);
        panel.add(new JLabel("Nombre de Empresa: "));
        panel.add(empresaField);
        panel.add(ingresarButton);
        panel.add(buscarButton);
        panel.add(sacarButton);
        panel.add(imprimirButton);
        panel.add(salirButton);

        Container c = getContentPane();
        c.setLayout(new BorderLayout());
        c.add(panel, BorderLayout.NORTH);
        c.add(scroll, BorderLayout.CENTER);

        ingresarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String codigo = codigoField.getText().trim();
                String empresa = empresaField.getText().trim();
                int pilaIndex = pilaSelector.getSelectedIndex();

                if (codigo.isEmpty() || empresa.isEmpty()) {
                    datosField.append("Debe ingresar el Código de Empresa y el Nombre de Empresa.\n");
                } else if (!codigo.matches("\\d+")) {
                    datosField.append("El Código de Empresa debe contener solo números.\n");
                } else if (!empresa.matches("[a-zA-Z ]+")) {
                    datosField.append("El Nombre de Empresa debe contener solo letras.\n");
                } else if (pilas.get(pilaIndex).size() >= 6) {
                    datosField.append("No hay espacio en la pila seleccionada. Seleccione otra.\n");
                } else {
                    // El código y el nombre de la empresa son válidos, y hay espacio en la pila, procede con la operación.
                    Caja nuevaCaja = new Caja(codigo, empresa);
                    Caja.agregarCaja(pilas.get(pilaIndex), nuevaCaja, datosField);
                    codigoField.setText("");
                    empresaField.setText("");
                    datosField.append("Caja agregada a Pila " + pilaIndex + ": Código: " + codigo + ", Empresa: " + empresa + "\n");
                    datosField.repaint();
                }
            }
        });


        buscarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String codigo = codigoField.getText().trim();

                if (codigo.isEmpty()) {
                    datosField.append("Debe ingresar el Código de Empresa para buscar una caja.\n");
                } else {
                    int pilaIndex = -1; // Variable para almacenar el índice de la pila
                    int cajaPosition = -1; // Variable para almacenar la posición de la caja

                    for (int i = 0; i < pilas.size(); i++) {
                        Stack<Caja> pila = pilas.get(i);
                        for (int j = 0; j < pila.size(); j++) {
                            Caja caja = pila.get(j);
                            if (caja.getCodigo().equals(codigo)) {
                                pilaIndex = i;
                                cajaPosition = j;
                                break; // Rompe el bucle cuando se encuentra la caja
                            }
                        }
                        if (pilaIndex != -1) {
                            break; // Rompe el bucle externo cuando se encuentra la caja
                        }
                    }

                    if (pilaIndex != -1) {
                        datosField.append("Caja encontrada en Pila " + pilaIndex + ", Posición " + cajaPosition + "\n");
                    } else {
                        datosField.append("Caja no encontrada.\n");
                    }
                }
            }
        });


        sacarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String codigo = codigoField.getText().trim();

                if (codigo.isEmpty()) {
                    datosField.append("Debe ingresar el Código de Empresa para sacar una caja.\n");
                } else {
                    boolean cajaEncontrada = false;
                    int pilaIndex = -1; // Variable para almacenar el índice de la pila

                    for (int i = 0; i < pilas.size(); i++) {
                        Stack<Caja> pila = pilas.get(i);
                        Stack<Caja> pilaAuxiliar = new Stack<>();

                        while (!pila.isEmpty()) {
                            Caja caja = pila.pop();
                            if (caja.getCodigo().equals(codigo)) {
                                cajaEncontrada = true;
                                pilaIndex = i;
                            } else {
                                pilaAuxiliar.push(caja);
                            }
                        }

                        while (!pilaAuxiliar.isEmpty()) {
                            pila.push(pilaAuxiliar.pop());
                        }
                    }

                    if (cajaEncontrada) {
                        datosField.append("Caja retirada de Pila " + pilaIndex + "\n");
                    } else {
                        datosField.append("Caja no encontrada.\n");
                    }
                }
            }
        });


        imprimirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Caja.imprimirPilas(pilas, datosField);
            }
        });

        salirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                AlmacenApp almacenApp = new AlmacenApp();
                almacenApp.setTitle("Almacén de Cajas");
                almacenApp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                almacenApp.setSize(1200, 600);
                almacenApp.setVisible(true);
            }
        });
    }
}
