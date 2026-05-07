
package tp6;

import java.awt.Button;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class Botones extends Panel implements ActionListener {

    Button b1,b2,b3;

    TextField tf1, tf2;
    TextArea ta;

    public Botones() {

        b1 = new Button( "Boton B1" );
        b2 = new Button( "Boton B2" );
        b3 = new Button( "Boton B3" );

        tf1 = new TextField(20);
        tf2 = new TextField(20);

        ta = new TextArea(5,30);

        this.add( b1 );
        this.add( tf1 );

        this.add( b2 );
        this.add( tf2 );

        this.add( b3 );
        this.add( ta );

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

        //Redimensiona la ventana a su tamaño natural
        f.pack();

        f.show();
    }

    public void actionPerformed (ActionEvent evt) {

        if(evt.getActionCommand()==b1.getActionCommand())
            tf1.setText( "Se ha pulsado el boton B1" );

        if(evt.getActionCommand()==b2.getActionCommand())
            tf2.setText( "Se ha pulsado el boton B2" );

        if(evt.getActionCommand()==b3.getActionCommand())
            ta.append( "Se ha pulsado el boton B3\n" );

            // probar diferencia:
            // ta.setText("Se ha pulsado el boton B3");
    }
}

