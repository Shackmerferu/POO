import java.awt.*;
import java.awt.event.*;

public class Botones extends Panel implements ActionListener {

    Button b1,b2,b3;
    TextField t1, t2;
    TextArea at;
    public Botones() {
        b1 = new Button( "Boton B1" );
        b2 = new Button( "Boton B2" );
        b3 = new Button( "Boton B3" );
        this.add( b1 );
        this.add( b2 );
        this.add( b3 );
       
        t1= new TextField(20);
        t2= new TextField(20);
        at= new TextArea(10,30);  
        this.add(t1);
        this.add(t2);
        this.add(at);
        b1.addActionListener(this);
        b2.addActionListener(this);
        b3.addActionListener(this);
        }

    public static void main(String[] args) {
        //Crea una nueva ventana
        Frame f = new Frame("Botones Nuevo");

        WindowListener l=new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                        System.exit(0);
                };
        };
        f.addWindowListener(l);

        //Crea una instancia de Botones

        Botones b = new Botones();
        //Agrega el objeto para que se muestre por la ventana.
        f.add("Center", b);
        //Redimensiona la ventana a su tama�o natural
        f.pack();
        f.setVisible(true);
    }

    public void actionPerformed (ActionEvent evt) {
      
        if(evt.getActionCommand()==b1.getActionCommand())
           t1.setText("Se ha pulsado el boton B1" );
        if(evt.getActionCommand()==b2.getActionCommand())
           t2.setText("Se ha pulsado el boton B2" );
        if(evt.getActionCommand()==b3.getActionCommand())
          at.append( "Se ha pulsado el boton B3" );
    }
}