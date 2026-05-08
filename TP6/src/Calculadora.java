import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Calculadora extends JFrame implements ActionListener {

    private JTextField pantalla;
    private JPanel panelbotones;
    private boolean nuevaOperacion = true;
    private double numeroAnterior = 0;
    private String operador = "";

    // --- 1. EL CONSTRUCTOR ventana lo que se ve
    public Calculadora() {
        setTitle("Calculadora");
        setSize(300, 300);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        pantalla = new JTextField("0");
        pantalla.setEditable(false);
        pantalla.setHorizontalAlignment(JTextField.RIGHT);
        add(pantalla, BorderLayout.NORTH);

        panelbotones = new JPanel();
        panelbotones.setLayout(new GridLayout(4, 5, 2, 2));

        String[] numeros = {
                "7", "8", "9", "/", "CE",
                "4", "5", "6", "*", "C",
                "1", "2", "3", "-", "",
                "0", "", ".", "+", "="
        };

        for (String texto : numeros) {
            JButton boton = new JButton(texto);

            if (!texto.equals("")) {
                boton.addActionListener(this);
            }

            panelbotones.add(boton);
        }

        add(panelbotones, BorderLayout.CENTER);
        setVisible(true);
    }



    @Override
    public void actionPerformed (ActionEvent evt) { // CORRECCIÓN: 'evt' en minúscula
        String botonApretado = evt.getActionCommand();

        if ("0123456789.".contains(botonApretado)) {
            if (nuevaOperacion) {
                pantalla.setText(botonApretado);
                nuevaOperacion = false;
            } else {
                pantalla.setText(pantalla.getText() + botonApretado);
            }
        }
        else if ("+-*/".contains(botonApretado)) {
            numeroAnterior = Double.parseDouble(pantalla.getText());
            operador = botonApretado;
            nuevaOperacion = true;
        }
        else if (botonApretado.equals("=")) {
            double numeroActual = Double.parseDouble(pantalla.getText());
            double resultado = 0;

            switch (operador) {
                case "+":
                    resultado = numeroAnterior + numeroActual;
                    break;
                case "-":
                    resultado = numeroAnterior - numeroActual;
                    break;
                case "*":
                    resultado = numeroAnterior * numeroActual;
                    break;
                case "/":
                    if (numeroActual != 0) resultado = numeroAnterior / numeroActual;
                    else {
                        pantalla.setText("Error");
                        return;
                    }
                    break;
            }

            if (!pantalla.getText().equals("Error")) {
                pantalla.setText(String.valueOf(resultado));
            }
            nuevaOperacion = true;
        }
        else if (botonApretado.equals("C") || botonApretado.equals("CE")) {
            pantalla.setText("0");
            numeroAnterior = 0;
            operador = "";
            nuevaOperacion = true;
        }
    }


    public static void main (String[] args){
        new Calculadora();
    }
}