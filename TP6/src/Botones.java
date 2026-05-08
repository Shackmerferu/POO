import javax.swing.*;      // 1. Importamos Swing (Nota la 'x' en javax)
import java.awt.*;         // 2. Seguimos usando AWT para Layouts y Colores
import java.awt.event.*;

 public class Botones extends Panel implements ActionListener {

    Button b1,b2,b3;
    TextField texto1,texto2;
    TextArea areatexto;
    public Botones() {
        b1 = new Button( "Boton B1" );
        b2 = new Button( "Boton B2" );
        b3 = new Button( "Boton B3" );


        texto1=new TextField(30);
        texto2=new TextField(30);
        areatexto=new TextArea(5,30);

        this.add( b1 );
        this.add(texto1);
        this.add( b2 );
        this.add(texto2);
        this.add( b3 );
        this.add(areatexto);

        b1.addActionListener(this);
        b2.addActionListener(this);
        b3.addActionListener(this);
    }

    public static void main(String[] args) {
        //Crea una nueva ventana
        Frame f = new Frame("Botones y texto ");

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
        f.show();
    }

    public void actionPerformed (ActionEvent evt) {
        if(evt.getActionCommand()==b1.getActionCommand())
           texto1.setText( "Se ha pulsado el boton B1" );
        if(evt.getActionCommand()==b2.getActionCommand())
            texto2.setText( "Se ha pulsado el boton B2" );
        if(evt.getActionCommand()==b3.getActionCommand())
            areatexto.append( "Se ha pulsado el boton B3\n" );

    }
}

