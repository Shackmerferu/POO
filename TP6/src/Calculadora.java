import java.awt.*;
import java.awt.event.*;

public class Calculadora extends Frame implements ActionListener, KeyListener {
    private TextField pantalla;
    private Panel panelb;
    private double resultado = 0;
    private String operador = ""; 

    public Calculadora() {
        
        setTitle("Calculadora Toni");
        setSize(600, 500);
        
        pantalla = new TextField("0"); 
        pantalla.setEditable(false);
        pantalla.addKeyListener(this); 
        
      
        panelb = new Panel(new GridLayout(4, 4, 5, 5));
        String[] botones = {"7","8","9","/",
                            "4","5","6","*",
                            "1","2","3","-",
                            "0","C","=","+"}; 
        
        for(int i=0; i<botones.length; i++){
            Button b = new Button(botones[i]);
            b.addActionListener(this); 
            b.addKeyListener(this);     
            panelb.add(b);
        }
        
       
        this.setLayout(new BorderLayout());
        this.add(pantalla, BorderLayout.NORTH);
        this.add(panelb, BorderLayout.CENTER);
        
        
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        String valor = e.getActionCommand(); 

        if (valor.matches("[0-9]")) {
           
            if (pantalla.getText().equals("0") || pantalla.getText().equals(String.valueOf(resultado))) {
                pantalla.setText(valor);
            } else {
                pantalla.setText(pantalla.getText() + valor);
            }
        } 
        else if (valor.equals("C")) {
            pantalla.setText("0");
            resultado = 0;
            operador = "";
        } 
        else {
            try {
                double numeroActual = Double.parseDouble(pantalla.getText());

                if (!operador.isEmpty()) {
                    if (operador.equals("+")) {
                        resultado += numeroActual;}
                    else if (operador.equals("-")) {
                        resultado -= numeroActual;}
                    else if (operador.equals("*")) {
                        resultado *= numeroActual;}
                    else if (operador.equals("/")) {
                        if (numeroActual != 0) resultado /= numeroActual;
                        else {
                            pantalla.setText("Error");
                            operador = "";
                            return;
                        }
                    }
                } else {
                    resultado = numeroActual;
                }

                pantalla.setText(String.valueOf(resultado));
                operador = valor.equals("=") ? "" : valor;
                
            } catch (NumberFormatException ex) {
               
                pantalla.setText("0");
            }
        }
    }
    
   
    public void keyPressed(KeyEvent e) {
         char c = e.getKeyChar();
       
       if (Character.isDigit(c) || "+-*/=C".contains(String.valueOf(c).toUpperCase())) {
            actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, String.valueOf(c).toUpperCase()));
        } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
          actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "="));
        } else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
            actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "C"));
        }
    }

    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {} 

    public static void main(String[] args) {
        new Calculadora();
    }
}




