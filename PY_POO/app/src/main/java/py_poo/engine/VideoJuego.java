package py_poo.engine;

import java.util.List;

import py_poo.entities.ObjetoGrafico;

import py_poo.engine.EstadoJuego;

public abstract class VideoJuego {
    protected String Nombre;
    protected boolean Activo;
    protected EstadoJuego estado;
    protected List<Integer> Puntuacion;
    protected Nivel NivelActual;
    protected List<ObjetoGrafico> Entidades;
    private int ResX;
    private int ResY;
    protected boolean Fullscreen;
    private List<Jugador> Jugador;
    private String Resultado;

    protected void iniciar(){

    }

    protected void actualizar(){
        if(!Activo){
            return;
        }
        switch (estado) {
            case MENU:
                crearPartida();
                estado= EstadoJuego.JUGANDO;
                break;
            case JUGANDO:
                cargarNivel();
                break;
            case PAUSA:
                pausa();
                break;
            case GAME_OVER:
                
        }
    }

    protected void finalizar(){}
      
    protected void pausa(){}

    protected void crearPartida(){}

    protected void reiniciar(){}
    
    public void cargarNivel(){}

    public void getResultado(){}

    public void getGanador(){}

    public void getPerdedor(){}

    public List<Integer> getpuntaje(){return Puntuacion;}

    public void iniciapuntaje(){}

    public void sumarPunto(int Puntaje){}

    public void resetPuntaje(){}

}
