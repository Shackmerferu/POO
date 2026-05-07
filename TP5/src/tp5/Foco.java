package tp5;

abstract class Foco {

    public abstract void encender();
    //la subclase focoled no puede sobreescribir al metodo de encender porque es la ultima version del metodo disponible , si focoled necesitase utilizar el metodo debe invocarlo desde su implementacion llamando a su clase padre Foco
    
}
